package com.example.myapplication.component.service.binder;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class BinderActivity extends AppCompatActivity {

    private static final String TAG = BinderActivity.class.getSimpleName();

    private ServiceConnection serviceConnection;
    private LocalService localService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.binder_activity);

        Button bindServiceBtn = findViewById(R.id.bind_service_btn);
        Button unBindServiceBtn = findViewById(R.id.un_bind_service_btn);
        Button getDataBtn = findViewById(R.id.data_btn);
        final Intent intent = new Intent(this, LocalService.class);
        bindServiceBtn.setOnClickListener(v -> {

            Log.d(TAG, "bindService");
            // 先绑定服务
            bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        });

        unBindServiceBtn.setOnClickListener(v -> {
            Log.d(TAG, "unBindService");
            if (null != localService) {
                localService = null;
                unbindService(serviceConnection);
            }
        });


        getDataBtn.setOnClickListener(v -> {
            if (null != localService) {
                Log.d(TAG, "getCount: " + localService.getCount());
            }else {
                Log.d(TAG, "localService is null");
            }
        });

        serviceConnection = new ServiceConnection() {
            /**
             * 与服务器端交互的接口方法 绑定服务的时候被回调，在这个方法获取绑定Service传递过来的IBinder对象，
             * 通过这个IBinder对象，实现宿主和Service的交互。
             */
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d(TAG, "onServiceConnected");
                LocalService.LocalBinder binder = (LocalService.LocalBinder) service;
                localService = binder.getService();
            }

            /**
             * 当取消绑定的时候被回调。但正常情况下是不被调用的，它的调用时机是当Service服务被意外销毁时，
             * 例如内存的资源不足时这个方法才被自动调用。
             */
            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d(TAG, "onServiceDisconnected");
                localService = null;
            }
        };
    }
}
