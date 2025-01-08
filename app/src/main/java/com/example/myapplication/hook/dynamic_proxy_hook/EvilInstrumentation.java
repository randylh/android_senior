package com.example.myapplication.hook.dynamic_proxy_hook;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.Method;

public class EvilInstrumentation extends Instrumentation {

    private static final String TAG = "EvilInstrumentation";

    // ActivityThread原始对象，保存起来
    Instrumentation mBase;

    public EvilInstrumentation(Instrumentation base) {
        mBase = base;
    }

    public ActivityResult execStartActivity(
            Context who, IBinder contextThread, IBinder token, Activity target,
            Intent intent, int requestCode, Bundle options) {
        // 打印日志
        Log.d(TAG, "\n执行了startActivity, 参数如下: \n" + "who = [" + who + "], " +
                "\ncontextThread = [" + contextThread + "], \ntoken = [" + token + "], " +
                "\ntarget = [" + target + "], \nintent = [" + intent +
                "], \nrequestCode = [" + requestCode + "], \noptions = [" + options + "]");


        // 接下来调用原始方法
        Log.i(TAG, "execStartActivity android version: " +getAndroidVersionInfo());
        try {
            Method execStartActivityMethod = Instrumentation.class.getDeclaredMethod("execStartActivity",
                    Context.class, IBinder.class, IBinder.class, Activity.class,
                    Intent.class, int.class, Bundle.class);
            execStartActivityMethod.setAccessible(true);
            return (ActivityResult) execStartActivityMethod.invoke(mBase, who, contextThread, token, target, intent, requestCode, options);
        }catch (Exception e) {
            Log.e(TAG, "execStartActivity: ", e);
            throw new RuntimeException("do not support!!! pls adapt it");
        }
    }

    private String getAndroidVersionInfo() {
        int apiLevel = Build.VERSION.SDK_INT;
        String releaseVersion = Build.VERSION.RELEASE;
        String codeName = Build.VERSION.CODENAME;

        return "API Level: " +apiLevel +", Release Version: " + releaseVersion +", Codename: " +codeName;
    }

}

