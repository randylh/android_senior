package com.example.myapplication.ipc.socket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.ipc.utils.MyUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class TcpClientActivity extends Activity implements View.OnClickListener {

    private static final String TAG = TcpClientActivity.class.getSimpleName();

    private static final int MESSAGE_RECEIVE_NEW_MSG = 1;

    private static final int MESSAGE_SOCKET_CONNECTED = 2;

    private Button mSendButton;
    private TextView mMessageTextView;
    private EditText mMessageEditText;

    private PrintWriter mPrintWriter;
    private Socket mClientSocket;

    private Handler mHandler;
    // 定义静态内部类 Handler
    private static class MyHandler extends Handler {
        private final WeakReference<TcpClientActivity> mActivity;

        MyHandler(TcpClientActivity activity) {
            // 显式指定Looper
            super(Looper.getMainLooper());
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            TcpClientActivity activity = mActivity.get();
            if (activity != null) {
                activity.updateUI(msg);
            }
        }
    }

    private void updateUI(Message msg) {
        if (Objects.isNull(msg)) {
            return;
        }
        switch (msg.what) {
            case MESSAGE_RECEIVE_NEW_MSG:
                mMessageTextView.setText(mMessageTextView.getText() + (String) msg.obj);
                break;
            case MESSAGE_SOCKET_CONNECTED:
                mSendButton.setEnabled(true);
                break;
            default:
                break;
        }
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcpclient);
        mHandler = new MyHandler(this);
        mMessageTextView = findViewById(R.id.msg_container);
        mSendButton = findViewById(R.id.send);
        mSendButton.setOnClickListener(this);
        mMessageEditText = findViewById(R.id.msg);
        Intent service = new Intent(this, TcpServerService.class);
        startService(service);
        new Thread() {
            @Override
            public void run() {
                connectTCPServer();
            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        if (v == mSendButton) {
            final String msg = mMessageEditText.getText().toString();
            if (!TextUtils.isEmpty(msg) && null != mPrintWriter) {
                new Thread(() -> mPrintWriter.println(msg)).start();

                mMessageEditText.setText("");
                String time = formatDateTime(System.currentTimeMillis());
                final String showedMsg = "self " + time + ":" + msg + "\n";
                mMessageTextView.setText(mMessageTextView.getText() + showedMsg);
            }
        }
    }

    private void connectTCPServer() {
        Socket socket = null;
        while (null == socket) {
            try {
                socket = new Socket("localhost", 8688);
                mClientSocket = socket;
                mPrintWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                mHandler.sendEmptyMessage(MESSAGE_SOCKET_CONNECTED);
                Log.i(TAG, "connect server success");
            }catch (IOException e) {
                Log.e(TAG, "connect tcp server failed, retry...");
            }
        }

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (!TcpClientActivity.this.isFinishing()) {
                String msg = br.readLine();
                Log.i(TAG, "receive: " + msg);
                if (null != msg) {
                    String time = formatDateTime(System.currentTimeMillis());
                    final String showedMsg = "server " + time + ":" + msg
                            + "\n";
                    mHandler.obtainMessage(MESSAGE_RECEIVE_NEW_MSG, showedMsg)
                            .sendToTarget();
                }
            }
            Log.i(TAG, "quit...");
            MyUtils.close(mPrintWriter);
            MyUtils.close(br);
            socket.close();
        }catch (IOException e) {
            Log.e(TAG, "");
        }
    }

    private String formatDateTime(long time) {
        return new SimpleDateFormat("(HH:mm:ss)").format(new Date(time));
    }

    @Override
    protected void onDestroy() {
        if (null != mClientSocket) {
            try {
                mClientSocket.shutdownInput();
                mClientSocket.close();
            }catch (IOException e) {
                Log.e(TAG, "");
            }
        }
        super.onDestroy();
    }
}
