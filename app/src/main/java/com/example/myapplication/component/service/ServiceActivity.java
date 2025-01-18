package com.example.myapplication.component.service;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class ServiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_activity);

        Button startServiceBtn = findViewById(R.id.start_service_btn);
        startServiceBtn.setOnClickListener(v -> {
            // 启动服务
            Intent intent = new Intent(this, SimpleService.class);
            startService(intent);
        });

        Button stopServiceBtn = findViewById(R.id.stop_service_btn);
        stopServiceBtn.setOnClickListener(v -> {
            // 停止服务
            Intent intent = new Intent(this, SimpleService.class);
            stopService(intent);
        });

//        Button bindServiceBtn = findViewById(R.id.bind_service_btn);
//        bindServiceBtn.setOnClickListener(v -> {
//            // 绑定服务
//            Intent intent = new Intent(this, SimpleService.class);
//            bindService(intent, serviceConnection, BIND_AUTO_CREATE);
//        });
    }
}
