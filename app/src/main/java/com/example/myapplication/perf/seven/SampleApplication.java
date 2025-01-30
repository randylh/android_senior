package com.example.myapplication.perf.seven;

import android.app.Application;
import android.content.Context;

public class SampleApplication extends Application {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
    }

    public static Context getContext() {
        return sContext;
    }
}
