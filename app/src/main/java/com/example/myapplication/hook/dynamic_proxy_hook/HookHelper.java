package com.example.myapplication.hook.dynamic_proxy_hook;

import android.app.Instrumentation;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class HookHelper {

    public static void attachContext() {
        try {
            // 反射获取隐藏类的方法
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Method currentActivityThreadMethod = activityThreadClass.getDeclaredMethod("currentActivityThread");
            currentActivityThreadMethod.setAccessible(true);
            //currentActivityThread是一个static函数所以可以直接invoke，不需要带实例参数
            Object activityThread = currentActivityThreadMethod.invoke(null);

            Field mInstrumentationField = activityThreadClass.getDeclaredField("mInstrumentation");
            mInstrumentationField.setAccessible(true);
            Instrumentation instrumentation = (Instrumentation) mInstrumentationField.get(activityThread);

            // 创建代理对象
            Instrumentation evilInstrumentation = new EvilInstrumentation(instrumentation);
            // 偷梁换柱
            mInstrumentationField.set(activityThread, evilInstrumentation);
        }catch (Exception e) {
            Log.e("HookHelper", "attachContext: ", e);
        }
    }
}
