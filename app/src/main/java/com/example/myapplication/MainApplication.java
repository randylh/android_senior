package com.example.myapplication;

import android.app.Application;
import android.content.SharedPreferences;

import com.example.myapplication.cache.sp.SharedPreferencesImpl;

import java.util.HashMap;

import leakcanary.LeakCanary;

public class MainApplication extends Application {

    private static final HashMap<String, SharedPreferencesImpl> sSharedPrefs = new HashMap<>();

    public MainApplication(){}

    @Override
    public SharedPreferences getSharedPreferences(String name, int mode) {
        return super.getSharedPreferences(name, mode);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
