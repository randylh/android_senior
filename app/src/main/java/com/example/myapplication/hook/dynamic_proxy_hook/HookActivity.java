package com.example.myapplication.hook.dynamic_proxy_hook;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
/**
 * 测试功能：hook每次调用startActivity之前打印一行日志
 */
public class HookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button btn = new Button(this);
        btn.setText("测试界面");

        setContentView(btn);

        btn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("http://www.baidu.com"));
            // 采用context启动Activity
            // 因为Activity对象的startActivity使用的并不是ContextImpl的mInstrumentation
            // 而是自己的mInstrumentation, 如果你需要这样, 可以自己Hook
            // 比较简单, 直接替换这个Activity的此字段即可.
            getApplicationContext().startActivity(intent);
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        try {
            HookHelper.attachContext();
        }catch (Exception e) {
            Log.e("TAG", "attachBaseContext: ", e);
        }
    }
}

