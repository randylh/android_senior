package com.example.myapplication.hook.ams_pms_hook;

import android.os.Build;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

/**
 * https://github.com/Jeremy-linkt/AndroidHookDemo
 * https://github.com/jeanboydev/Android-ReadTheFuckingSourceCode/blob/master/article/android/plugin/04_hook_ams.md
 */
public class HookHelper {
    private static final String TAG = HookHandler.class.getSimpleName();

        public static void hookActivityManager() {

            Field activityManagerSingletonField = null;
            // 最终想要拿到的对象类型
            Class<?> clazz;
            try {
                // Android 10 之后
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Class<?> activityManagerSingletonClass = Class.forName("android.app.ActivityTaskManager");
                    activityManagerSingletonField = activityManagerSingletonClass.getDeclaredField("IActivityTaskManagerSingleton");
                    clazz = Class.forName("android.app.IActivityTaskManager");
                }
                // Android 8.0 到Android9.0
                else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Class<?> amnClass = Class.forName("android.app.ActivityManager");
                    activityManagerSingletonField = amnClass.getDeclaredField("IActivityManagerSingleton");
                    clazz = Class.forName("android.app.IActivityManager");
                }else {
                    Class<?> amnClass = Class.forName("android.app.ActivityManagerNative");
                    activityManagerSingletonField = amnClass.getDeclaredField("gDefault");
                    clazz = Class.forName("android.app.IActivityManager");
                }
                activityManagerSingletonField.setAccessible(true);

                Object gDefault = activityManagerSingletonField.get(null);
                Log.i(TAG, "gDefault: " + gDefault);
                // 去除单例对象里面的字段
                Class<?> singletonClass = Class.forName("android.util.Singleton");
                Field mInstanceField = singletonClass.getDeclaredField("mInstance");
                mInstanceField.setAccessible(true);
                //这里拿到IActivityManager对象，Android 10是IActivityTaskManager
                Object instance = mInstanceField.get(gDefault);
                Log.i(TAG, "instance: " + instance);

                // 动态代理，使用asmProxy代理对象，替换掉原来的IActivityManager对象
                HookHandler hookHandler = new HookHandler(instance);
                Object proxy = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
                        new Class[]{clazz}, hookHandler);
                mInstanceField.set(gDefault, proxy);

            }catch (Exception e) {
                Log.e(TAG, "hookActivityManager: ", e);
            }
        }
}
