package com.example.myapplication.hook.ams_pms_hook;

import android.content.Intent;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class HookHandler implements InvocationHandler {

    private Object am;

    public HookHandler(Object am) {
        this.am = am;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Log.i("", "invoke: " + method.getName());
        if("startActivity".equals(method.getName())) {
            for (Object arg: args) {
                if (arg instanceof Intent) {
                    Intent intent = (Intent) arg;

                    Log.i("", "action: " +intent.getAction() +", data: " + intent.getDataString());
                    if (Intent.ACTION_VIEW.equals(intent.getAction())) {
                        Log.d("", "拦截启动Activity做一些十强");
                    }
                }
            }
        }

        return method.invoke(am, args);
    }
}
