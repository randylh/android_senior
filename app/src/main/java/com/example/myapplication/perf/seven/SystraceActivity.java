package com.example.myapplication.perf.seven;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class SystraceActivity extends AppCompatActivity {

    private static final String TAG = SystraceActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.systrace_activity);

        Button button = findViewById(R.id.testGc);
        button.setOnClickListener(v -> {
            for (int i = 0; i < 10000; i ++) {
                int[] test = new int[10000];
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }
}
