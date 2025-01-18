package com.example.myapplication.hook.binder_hook;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BinderHookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            BinderHookHelper.hookClipboardService();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 获取 ClipboardManager 实例
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        // 检查剪贴板是否有数据
        if (!clipboardManager.hasPrimaryClip()) {
            Toast.makeText(this, "剪贴板为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // 获取 ClipData
        android.content.ClipData clipData = clipboardManager.getPrimaryClip();
        if (clipData == null) {
            Toast.makeText(this, "剪贴板为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // 获取 ClipData.Item
        android.content.ClipData.Item item = clipData.getItemAt(0);

        // 获取数据
        CharSequence text = item.getText();
        if (text != null) {
            Toast.makeText(this, "剪贴板内容：" + text, Toast.LENGTH_LONG).show();
            System.out.println("剪贴板内容：" + text);
        } else {
            Toast.makeText(this, "剪贴板中不是文本数据", Toast.LENGTH_SHORT).show();
        }

    }
}
