package com.example.serverapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.Nullable;

public class MyAidlService extends Service {

    private final IMyAidlInterface.Stub binder = new IMyAidlInterface.Stub() {

        @Override
        public void sendMessage(String message) throws RemoteException {

        }

        @Override
        public String receiveMessage() throws RemoteException {
            return "Hello From AIDL Service";
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
