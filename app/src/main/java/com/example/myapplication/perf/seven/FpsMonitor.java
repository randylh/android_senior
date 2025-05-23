package com.example.myapplication.perf.seven;

import android.view.Choreographer;

public class FpsMonitor {

    private static final int MAX_FRAME_RATE = 60;

    /**
     * 信息采集时间 内存和cpu
     */
    private static final int NORMAL_SAMPLING_TIME = 500;

    private int mMaxFrameRate = MAX_FRAME_RATE;
    /**
     * 当前的帧率
     */
    private int mLastFrameRate = mMaxFrameRate;


    public long getLastFrameRate() {
        return mLastFrameRate;
    }

    private class FrameRateRunnable implements Runnable, Choreographer.FrameCallback {

        private int totalFramePerSecond;

        @Override
        public void doFrame(long frameTimeNanos) {
            mLastFrameRate = totalFramePerSecond;
            if (mLastFrameRate > mMaxFrameRate) {
                mLastFrameRate = mMaxFrameRate;
            }

            // 保存fps数据

        }

        @Override
        public void run() {

        }
    }
}
