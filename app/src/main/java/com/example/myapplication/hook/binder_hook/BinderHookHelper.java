package com.example.myapplication.hook.binder_hook;

import android.os.IBinder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

public class BinderHookHelper {

    public static void hookClipboardService() throws Exception {

        final String CLIPBOARD_SERVICE = "clipboard";

        Class<?> serviceManagerClazz = Class.forName("android.os.ServiceManager");
        Method getServiceMethod = serviceManagerClazz.getDeclaredMethod("getService", String.class);
        // 原始BInder，一般是个BInder代理对象
        IBinder rawBinder = (IBinder) getServiceMethod.invoke(null, CLIPBOARD_SERVICE);

        IBinder hookedBinder = (IBinder) Proxy.newProxyInstance(serviceManagerClazz.getClassLoader(),
                new Class<?>[] { IBinder.class },
                new BinderProxyHookHandler(rawBinder));


        // 把这个hook过的Binder代理对象放进ServiceManager的cache里面
        // 以后查询的时候 会优先查询缓存里面的Binder, 这样就会使用被我们修改过的Binder了
        Field cacheField = serviceManagerClazz.getDeclaredField("sCache");
        cacheField.setAccessible(true);
        Map<String, IBinder> cache = (Map) cacheField.get(null);
        cache.put(CLIPBOARD_SERVICE, hookedBinder);
    }
}
