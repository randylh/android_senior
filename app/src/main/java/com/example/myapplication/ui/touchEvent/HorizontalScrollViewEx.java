package com.example.myapplication.ui.touchEvent;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * 父容器
 * 外部拦截法，父容器需要此事件就拦截
 */
public class HorizontalScrollViewEx extends ViewGroup {

    private static final String TAG = "HorizontalScrollViewEx";


    private int mChildrenSize;
    private int mChildWidth;
    private int mChildIndex;

    // 分别记录上次滑动的坐标
     private int mLastX = 0;
     private int mLastY = 0;

    // 分别记录上次滑动的坐标(onInterceptTouchEvent)
    private int mLastXIntercept = 0;
    private int mLastYIntercept = 0;

    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;

    public HorizontalScrollViewEx(Context context) {
        super(context);
        init();
    }

    public HorizontalScrollViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HorizontalScrollViewEx(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        if (null == mScroller) {
            mScroller = new Scroller(getContext());
            mVelocityTracker = VelocityTracker.obtain();
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //  根据实际需要的条件来拦截事件
        boolean intercepted = false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        switch (ev.getAction()) {

            case MotionEvent.ACTION_DOWN: {
                // 不拦截
                intercepted = false;
                if (!mScroller.isFinished()) {
                    // 假如用户正在水平滑动，但是在水平滑动停止之前如果用户再迅速进行竖直滑动，就会导致界面在水平方向无法滑动到终点从而处
                    // 于一种中间状态。为了避免这种不好的体验，当水平方向正在滑动时，下一个序列的点击事件仍然交给父容器处理，这样水平方向就不会停留在中间状态了。
                    // 为了优化滑动体验
                    mScroller.abortAnimation();
                    intercepted = true;
                }
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                // 移动的位移
                int deltaX = x - mLastXIntercept;
                int deltaY = y - mLastXIntercept;
                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    intercepted = true;
                }else {
                    intercepted = false;
                }
                break;
            }

            case MotionEvent.ACTION_UP: {
                intercepted = false;
                break;
            }
            default:
                break;
        }
        Log.d("", "intercepted="+ intercepted);
        mLastX = x;
        mLastY = y;
        mLastXIntercept = x;
        mLastYIntercept = y;

        return intercepted;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mVelocityTracker.addMovement(event);
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:{
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
            }

            case MotionEvent.ACTION_MOVE:{
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                //TODO  理解一下？
                scrollBy(-deltaX, 0);
            }

            case MotionEvent.ACTION_UP:{
                int scrollX = getScrollX();
                // 计算当前的速度
                mVelocityTracker.computeCurrentVelocity(1000);
                float xVelocity = mVelocityTracker.getXVelocity();

                if (Math.abs(xVelocity) >= 50) {
                    mChildIndex = xVelocity > 0? mChildIndex - 1 : mChildIndex + 1;
                }else {
                    mChildIndex = (scrollX + mChildWidth / 2) / mChildWidth;
                }

                mChildIndex = Math.max(0, Math.min(mChildIndex, mChildrenSize - 1));
                int dx = mChildIndex * mChildWidth - scrollX;

                smoothScrollBy(dx,0);
                mVelocityTracker.clear();
                break;
            }
            default:
                break;
        }

        mLastX = x;
        mLastY = y;
        return true;
    }

    private void smoothScrollBy(int dx, int dy) {
        mScroller.startScroll(getScrollX(), 0, dx, 0, 500);
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int measureWidth = 0;
        int measureHeight = 0;
        // 获取子view的数量
        final int childCount = getChildCount();
        measureChildren(widthMeasureSpec, heightMeasureSpec);

    }
}
