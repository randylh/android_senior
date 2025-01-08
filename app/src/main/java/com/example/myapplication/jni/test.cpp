#include <jni.h>
#include <stdio.h>
#include <android/log.h>

#define LOG_TAG "MyNativeLib"
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN, LOG_TAG, __VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG, __VA_ARGS__)

#ifdef __cplusplus
extern "C" {
#endif

void callJavaMethod(JNIEnv *env, jobject thiz) {
    // 首先根据类名找到类
    jclass clazz = env->FindClass("com/example/myapplication/JniActivity");
    if (clazz == NULL) {
//        printf("find class JniActivity error!");
        LOGI("find class JniActivity error!");
        return;
    }
//    再根据方法名找到方法，其中(Ljava/lang/String;)V 是 methodCalledByJni 方法的签名
    jmethodID id = env->GetStaticMethodID(clazz, "methodCalledByJni", "(Ljava/lang/String;)V");
    if (id == NULL) {
//        printf("find method methodCalledByJni error!");
        LOGI("find method methodCalledByJni error!");
    }
//    再根据JNIEnv 对象的 CallStaticVoidMethod 方法来完成最终的调用过程
    jstring msg = env->NewStringUTF("msg send by callJavaMethod in test.cpp.");
    env->CallStaticVoidMethod(clazz, id, msg);
}

jstring Java_com_example_myapplication_JniActivity_get(JNIEnv *env, jobject thiz) {
//    printf("invoke get in c++\n");
    LOGI("invoke get in c++");
    callJavaMethod(env, thiz);
    return env->NewStringUTF("Hello from JNI in libjni-test.so !");
}

void Java_com_example_myapplication_JniActivity_set(JNIEnv *env, jobject thiz, jstring string) {
//    printf("invoke set from C++\n");
    LOGI("invoke set from C++");
    char* str = (char*)env->GetStringUTFChars(string,NULL);
//    printf("%s\n", str);
    LOGI("str=%s", str);
    env->ReleaseStringUTFChars(string, str);
}

#ifdef __cplusplus
}
#endif
