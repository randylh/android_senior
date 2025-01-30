package com.example.myapplication.ipc.socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.myapplication.ipc.utils.MyUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class TcpServerService extends Service {

    private static final String TAG = TcpServerService.class.getSimpleName();

    private boolean mServiceDestroyed = false;

    private String[] mDefinedMessages = new String[] {
        "你好啊，哈哈",
        "请问你叫什么名字呀？",
        "今天北京天气不错啊，shy",
        "你知道吗？我可是可以和多个人同时聊天的哦",
        "给你讲个笑话吧：据说爱笑的人运气不会太差，不知道真假。"
    };

    @Override
    public void onCreate() {
        new Thread(new TcpServer()).start();
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private class TcpServer implements Runnable {
        @Override
        public void run() {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(8688);
            }catch (IOException e) {
                Log.e(TAG, "establish tcp server failed , port=8688" +e.getMessage());
                return;
            }
            while (!mServiceDestroyed) {
                try {
                    // 接受客户端请求
                    final Socket client = serverSocket.accept();
                    Log.i(TAG, "start accept");
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                responseClient(client);
                            }catch (IOException e) {
                                Log.e(TAG, "responseClient exception: " + e.getMessage());
                            }
                        }
                    }.start();
                }catch (IOException e) {

                }
            }
        }
    }

    private void responseClient(Socket client) throws IOException {
        // 接收客户端消息
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        // 发送客户端消息
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
        out.println("欢迎来到聊天室！");
        while (!mServiceDestroyed) {
            String str = in.readLine();
            Log.i(TAG, "msg from client: " + str);
            if (null == str) {
                break;
            }
            int i = new Random().nextInt(mDefinedMessages.length);
            String msg = mDefinedMessages[i];
            out.println(msg);
            Log.i(TAG, "send: " + msg);
        }
        Log.i(TAG, "client quit.");
        MyUtils.close(out);
        MyUtils.close(in);
        client.close();
    }

    @Override
    public void onDestroy() {
        mServiceDestroyed = true;
        super.onDestroy();
    }
}
