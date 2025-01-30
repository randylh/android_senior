package com.example.myapplication.hook.ams_pms_hook;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class HookHandler implements InvocationHandler {

    private static final String TAG = HookHandler.class.getSimpleName();

    private Object am;

    private Context context;

    public HookHandler(Object am, Context context) {
        this.am = am;
        this.context = context;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Log.i(TAG, "invoke: " + method.getName());
        if("startActivity".equals(method.getName())) {
            for (Object arg: args) {
                if (arg instanceof Intent) {
                    Intent intent = (Intent) arg;

                    Log.i(TAG, "action: " +intent.getAction() +", data: " + intent.getDataString());
                    if (Intent.ACTION_VIEW.equals(intent.getAction())) {
                        Log.d("", "拦截启动Activity做一些事情");
                        Toast.makeText(context, "成功拦截Activity", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
        // 原有的执行逻辑
        return method.invoke(am, args);
    }
}
