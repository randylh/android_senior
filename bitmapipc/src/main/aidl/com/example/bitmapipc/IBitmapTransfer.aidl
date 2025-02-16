package com.example.bitmapipc;

interface IBitmapTransfer {
    // 传递共享内存文件描述符和 Bitmap 元数据
    void sendBitmap(in ParcelFileDescriptor pfd, int width, int height, String format);
}