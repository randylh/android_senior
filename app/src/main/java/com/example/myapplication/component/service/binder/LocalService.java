package com.example.myapplication.component.service.binder;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class LocalService extends Service {

    private static final String TAG = LocalService.class.getSimpleName();

    private LocalBinder mBinder = new LocalBinder();

    private Thread thread;
    private boolean quit;
    private int count;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalBinder extends Binder {
        public LocalService getService() {
            return LocalService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Service is created");
        thread = new Thread(() -> {
            while (!quit) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Log.e(TAG, "Interrupted", e);
                }
                count++;
            }
        });
        thread.start();
    }

    public int getCount() {
        return count;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "Service is unbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Service is destroyed");
        this.quit = true;
        super.onDestroy();
    }
}
