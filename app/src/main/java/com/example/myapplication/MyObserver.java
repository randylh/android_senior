package com.example.myapplication;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;

import androidx.lifecycle.LifecycleObserver;

public class MyObserver implements LifecycleObserver {

    private static final String TAG = "MyObserver";

    @OnLifecycleEvent(value = Lifecycle.Event.ON_RESUME)
    public void connect() {

    }

    @OnLifecycleEvent(value = Lifecycle.Event.ON_PAUSE)
    public void disconnect() {

    }
}
