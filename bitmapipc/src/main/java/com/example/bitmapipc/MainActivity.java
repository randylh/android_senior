package com.example.bitmapipc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.IBinder;
import android.os.MemoryFile;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.bitmapipc.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

import java.io.FileDescriptor;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private IBitmapTransfer bitmapTransfer;

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bitmapTransfer = IBitmapTransfer.Stub.asInterface(service);
            sentBitmapToService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bitmapTransfer = null;
        }
    };

    private void sentBitmapToService() {
        Bitmap bitmap = Bitmap.createBitmap(800, 600, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        // TODO 示例  绘制红色背景
        canvas.drawColor(Color.RED);

        try {
            ParcelFileDescriptor pfd = writeBitmapToSharedMemory(bitmap);
            if (pfd != null) {
                bitmapTransfer.sendBitmap(pfd, bitmap.getWidth(), bitmap.getHeight(), "ARGB_8888");
            }
        }catch (RemoteException e) {
            Log.e(TAG, "sendBitmap exception: " + e);
        }
    }

    private ParcelFileDescriptor writeBitmapToSharedMemory(Bitmap bitmap) {
        MemoryFile memoryFile = null;
        try {
            // 获取实际分配的内存大小
            ByteBuffer byteBuffer = ByteBuffer.allocate(bitmap.getAllocationByteCount());
            bitmap.copyPixelsToBuffer(byteBuffer);
            byteBuffer.rewind();

            // 创建共享内存对象
            memoryFile = new MemoryFile("bitmap_shm", byteBuffer.remaining());
            memoryFile.writeBytes(byteBuffer.array(), 0, 0, byteBuffer.remaining());

            // 通过反射获取 FileDescriptor
            Method getFileDescriptor = MemoryFile.class.getDeclaredMethod("getFileDescriptor");
            FileDescriptor fd = (FileDescriptor) getFileDescriptor.invoke(memoryFile);
            return ParcelFileDescriptor.dup(fd);
        }catch (Exception e) {
            Log.e(TAG, "writeBitmapToSharedMemory exception: " + e);
            return null;
        }finally {
            if (null != memoryFile) {
                memoryFile.close();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.fab)
                        .setAction("Action", null).show();
            }
        });

        // 绑定服务
        bindService(
                new Intent(this, BitmapTransferService.class),
                serviceConnection,
                Context.BIND_AUTO_CREATE
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }
}