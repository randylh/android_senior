package com.example.myapplication.network;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class LogInterceptor implements Interceptor {
    private static final String TAG = LogInterceptor.class.getSimpleName();

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        Log.i(TAG, "intercept: " + request.toString());
        Response response = chain.proceed(request);
        Log.i(TAG, "intercept: " + response.toString());
        return response;
    }
}
