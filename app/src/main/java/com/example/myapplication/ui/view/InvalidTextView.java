package com.example.myapplication.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class InvalidTextView extends AppCompatTextView {

    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    public InvalidTextView(@NonNull Context context) {
        super(context);
        initDraw();
    }

    public InvalidTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initDraw();
    }

    public InvalidTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDraw();
    }

    private void initDraw() {
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth((float) 1.5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        canvas.drawLine(0, (float) height / 2, width, (float) height / 2, mPaint);
    }
}
