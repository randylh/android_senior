package com.example.myapplication.ui.home;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.myapplication.R;

public class QuestionDialog extends Dialog {


    private TextView mTvTitle;
    private Button mBtnYes;
    private Button mBtnNo;

    private Handler sUiHandler = new Handler(Looper.getMainLooper());


    public QuestionDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public QuestionDialog(@NonNull Context context) {
        super(context);

        setContentView(R.layout.dialog_question);
        mTvTitle = findViewById(R.id.tv_title);
        mBtnYes = findViewById(R.id.btn_yes);
        mBtnNo = findViewById(R.id.btn_no);

        mBtnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String s = mTvTitle.getText().toString();
//                mTvTitle.setText(s+"？");
//                // 非ui线程
//                boolean uiThread = Looper.myLooper() == Looper.getMainLooper();
//                Toast.makeText(getContext(),"Ui thread = " + uiThread , Toast.LENGTH_LONG).show();


                sUiHandler.post(() -> {
                    String s = mTvTitle.getText().toString();
                    mTvTitle.setText(s+"？");
                });
            }
        });

        mBtnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printViewParentHierarchy(mTvTitle, 0);
            }
        });

    }

    private void printViewParentHierarchy(Object view, int level) {
        if (view == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append("\t");
        }
        sb.append(view.getClass().getSimpleName());
        Log.d("lmj", sb.toString());

        if (view instanceof View) {
            printViewParentHierarchy(((View) view).getParent(), level + 1);
        }

    }


    public void show(String title) {
        mTvTitle.setText(title);
        show();
    }
}
