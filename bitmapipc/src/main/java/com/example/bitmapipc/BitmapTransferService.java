package com.example.bitmapipc;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class BitmapTransferService extends Service {

    private static final String TAG = BitmapTransferService.class.getSimpleName();

    private final IBitmapTransfer.Stub binder = new IBitmapTransfer.Stub() {
        @Override
        public void sendBitmap(ParcelFileDescriptor pfd, int width, int height, String format) throws RemoteException {
            if (null == pfd) {
                return;
            }
            // 从共享内存读取Bitmap
            Bitmap bitmap = readBitmapFromSharedMemory(pfd, width, height, format);
            if (null != bitmap) {
                new Handler(Looper.getMainLooper()).post(() ->
                        Toast.makeText(BitmapTransferService.this, "Received Bitmap: " +
                                bitmap.getWidth() + "x" + bitmap.getHeight(), Toast.LENGTH_LONG).show());
            }else {
                Log.e(TAG, "bitmap is null");
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private Bitmap readBitmapFromSharedMemory(ParcelFileDescriptor pfd, int width, int height, String format) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(pfd.getFileDescriptor());
            FileChannel fileChannel = fis.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) fileChannel.size());
            fileChannel.read(byteBuffer);
            byteBuffer.rewind();

            // 根据格式创建Bitmap
            Bitmap.Config config = Bitmap.Config.valueOf(format);
            Bitmap bitmap = Bitmap.createBitmap(width, height, config);
            bitmap.copyPixelsFromBuffer(byteBuffer);
            return bitmap;
        }catch (IOException e) {
            Log.e(TAG, "create bitmap fail:" + e);
            return null;
        }finally {
            try {
                if (null != fis) {
                    fis.close();
                }
                pfd.close();
            }catch (IOException e) {
                Log.e(TAG, "stream close: " + e);
            }
        }
    }
}
