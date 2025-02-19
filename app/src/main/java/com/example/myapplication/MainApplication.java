package com.example.myapplication;

import android.app.Application;
import android.content.SharedPreferences;

import leakcanary.LeakCanary;

public class MainApplication extends Application {

    @Override
    public SharedPreferences getSharedPreferences(String name, int mode) {
        return super.getSharedPreferences(name, mode);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
