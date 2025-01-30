package com.perf.plugin

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformInvocation
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import com.example.perflibrary.LogVisitor
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.compress.utils.IOUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

class AsmTransform extends Transform {

    @Override
    String getName() {
        return "AsmTransform"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }
    /**
     * 指定Transform的作用范围
     * @return
     */
    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    /**
     * 表示当前Transform是否支持增量编译
     * @return
     */
    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {

        long startTime = System.currentTimeMillis();
        println("-------------startTime<" + startTime + ">----------------")
        // 拿到所有的class文件
        Collection<TransformInput> inputs = transformInvocation.inputs
        TransformOutputProvider outputProvider = transformInvocation.outputProvider
        if (null != outputProvider) {
            outputProvider.deleteAll()
        }

        //遍历inputs Transform的inputs有两种类型，一种是目录，一种是jar包，要分开遍历
        inputs.each {input ->
            //遍历directoryInputs(文件夹中的class文件) directoryInputs代表着以源码方式参与项目编译的所有目录结构及其目录下的源码文件
            // 比如我们手写的类以及R.class、BuildConfig.class以及R$XXX.class等
            input.directoryInputs.each {outputDirectory ->
                //文件夹中的class文件
                handDirectoryInput(directoryInput, outputProvider)
            }
            //遍历jar包中的class文件 jarInputs代表以jar包方式参与项目编译的所有本地jar包或远程jar包
            input.jarInputs.each {jarInput ->
                //处理jar包中的class文件
                handleJarInput(jarInput, outputProvider)
            }
        }

    }

    /**
     * 遍历directoryInputs  得到对应的class  交给ASM处理
     * @param directoryInput
     * @param outputProvider
     * ClassReader  用来解析编译过的字节码文件
         ClassWriter 用来重新构建编译后的类，比如修改类名，属性，方法或者生成新类的字节码文件
         ClassVisitor 用来访问类成员信息，包括标记在类上的注解，类的构造方法，类的字段，方法，静态代码快等。
         AdviceAdapter 用来访问方法的信息，用来进行具体方法字节码操作，是 MethodVisitor 的增强实现。
     */
    private static void handDirectoryInput(DirectoryInput directoryInput, TransformOutputProvider outputProvider) {
        // 判断是否为文件夹
        if (directoryInput.file.isDirectory()) {
            //列出目录所有文件（包含子文件夹，子文件夹内文件）
            directoryInput.file.eachFileRecurse {file ->
                String name = file.name
                //TODO 需要插桩class 根据自己的需求来------------- 这里判断是否是我们自己写的Application
                if ("MyApp.class" == name) {
                    ClassReader classReader = new ClassReader(file.bytes)
                    //传入COMPUTE_MAXS  ASM会自动计算本地变量表和操作数栈
                    ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
                    //创建类访问器   并交给它去处理
                    ClassVisitor classVisitor = new LogVisitor(classWriter)
                    classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
                    byte[] code = classWriter.toByteArray()
                    FileOutputStream fos = new FileOutputStream(file.parentFile.absolutePath + File.separator + name)
                    fos.write(code)
                    fos.close()
                }
            }
            //处理完输入文件后把输出传给下一个文件
            def dest = outputProvider.getContentLocation(directoryInput.name, directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
            FileUtils.copyDirectory(directoryInput.file, dest)
        }
    }
    /**
     * 遍历jarInputs 得到对应的class 交给ASM处理
     * @param jarInput
     * @param outputProvider
     */
    private static void handleJarInput(JarInput jarInput, TransformOutputProvider outputProvider) {
        if (jarInput.file.absolutePath.endsWith(".jar")) {
            //重名名输出文件,因为可能同名,会覆盖
            def jarName = jarInput.name
            def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
            if (jarName.endsWith(".jar")) {
                jarName = jarName.substring(0, jarName.length() - 4)
            }
            JarFile jarFile = new JarFile(jarInput.file)
            Enumeration enumeration = jarFile.entries()
            File tmpFile = new File(jarInput.file.getParent() + File.separator + "classes_temp.jar")
            //避免上次的缓存被重复插入
            if (tmpFile.exists()) {
                tmpFile.delete()
            }
            JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(tmpFile))
            //用于保存
            while (enumeration.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry) enumeration.nextElement()
                String entryName = jarEntry.getName()
                ZipEntry zipEntry = new ZipEntry(entryName)
                InputStream inputStream = jarFile.getInputStream(jarEntry)
                //TODO 需要插桩class 根据自己的需求来-------------
                if ("androidx/fragment/app/FragmentActivity.class" == entryName) {
                    //class文件处理
                    println '----------- jar class  <' + entryName + '> -----------'
                    jarOutputStream.putNextEntry(zipEntry)
                    ClassReader classReader = new ClassReader(IOUtils.toByteArray(inputStream))
                    ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
                    //创建类访问器   并交给它去处理
                    ClassVisitor cv = new LogVisitor(classWriter)
                    classReader.accept(cv, ClassReader.EXPAND_FRAMES)
                    byte[] code = classWriter.toByteArray()
                    jarOutputStream.write(code)
                } else {
                    jarOutputStream.putNextEntry(zipEntry)
                    jarOutputStream.write(IOUtils.toByteArray(inputStream))
                }
                jarOutputStream.closeEntry()
            }
            //结束
            jarOutputStream.close()
            jarFile.close()
            //获取output目录
            def dest = outputProvider.getContentLocation(jarName + md5Name,
                    jarInput.contentTypes, jarInput.scopes, Format.JAR)
            FileUtils.copyFile(tmpFile, dest)
            tmpFile.delete()
        }
    }

}