package com.example.perflibrary;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class LogVisitor extends ClassVisitor {

    private String className;

    public LogVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM5, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        System.out.println("LogVisitor visit------>started: " + name);
        this.className = name;
        super.visit(version, access, name, signature, superName, interfaces);
    }

    /**
     * 定义一个方法， 返回的MethodVisitor用于生成方法相关的信息
     * @param access the method's access flags (see {@link Opcodes}). This parameter also indicates if
     *     the method is synthetic and/or deprecated.
     * @param name the method's name.
     * @param descriptor the method's descriptor (see {@link Type}).
     * @param signature the method's signature. May be {@literal null} if the method parameters,
     *     return type and exceptions do not use generic types.
     * @param exceptions the internal names of the method's exception classes (see {@link
     *     Type#getInternalName()}). May be {@literal null}.
     * @return
     */
    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, descriptor, signature, exceptions);
//        com.example.myapplication.asm.app_cost
        if ("com/example/myapplication/asm/app_cost/MyApp".equals(this.className)) {
            if ("onCreate".equals(name)) {
                System.out.println("LogVisitor visitMethod------>onCreate:" + name);
                return new OnCreateVisitor(mv);
            }
        }

        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }

    @Override
    public void visitEnd() {
        System.out.println("LogVisitor visitEnd------>ended: " + className);
        super.visitEnd();
    }
}
