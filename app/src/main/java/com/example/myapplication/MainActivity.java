package com.example.myapplication;

import android.app.ActivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        // TODO 模拟死锁导致的ANR
//        testAnr();

//        SharedPreferences sp = getSharedPreferences("cacge", Context.MODE_PRIVATE);
//        sp.edit().putString("key", "value");
    }

    private void testAnr() {
        Object lock1 = new Object();
        Object lock2 = new Object();

        new Thread(() -> {
            synchronized (lock1) {
                try {
                    Thread.sleep(100);
                    synchronized (lock2) {
                        Log.i(TAG, "testAnr: getLock2");
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
        // 主线程持有锁2，竞争锁1
        synchronized (lock2) {
            try {
                Thread.sleep(100);
                synchronized (lock1) {
                    Log.i(TAG, "testAnr: getLock1");
                }
            }catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // Activity的事件分发机制
        Log.d("MainActivity", "before dispatchTouchEvent: ");
        boolean touchEvent = super.dispatchTouchEvent(ev);
        Log.d("MainActivity", "after dispatchTouchEvent: " + touchEvent);Log.d("MainActivity", "before dispatchTouchEvent: ");
        return touchEvent;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }
}