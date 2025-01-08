package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class JniActivity extends AppCompatActivity {

    static {
        System.loadLibrary("jni-test");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jni_activity);
        TextView textView = findViewById(R.id.test_jni_text);
        set("hello world from JniTestApp");
        textView.setText(get());
    }

    public static void methodCalledByJni(String msgFromJni) {
        Log.d("JniActivity", "methodCalledByJni, msg: " + msgFromJni);
    }

    public native String get();
    public native void set(String str);
}
