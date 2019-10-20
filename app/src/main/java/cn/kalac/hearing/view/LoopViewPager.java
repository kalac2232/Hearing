package cn.kalac.hearing.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.orhanobut.logger.Logger;

import cn.kalac.hearing.adapter.LoopPagerAdapter;

/**
 * @author kalac.
 * @date 2019/10/20 18:48
 */
public class LoopViewPager extends ViewPager {

    private Context mContext;
    private Thread mInfiniteThread;
    private int mDelayMillis = 5000;
    private Handler mHander;
    public LoopViewPager(@NonNull Context context) {
        this(context,null);
    }

    public LoopViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        mHander = new Handler();
        //无限轮播的thread
        mInfiniteThread = new InfiniteThread();
    }

    /**
     * 用于无限轮播图的线程
     */
    class InfiniteThread extends Thread {
        @Override
        public void run() {
            mHander.postDelayed(this, mDelayMillis);
            setCurrentItem(getCurrentItem() + 1);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //当按住vp的时候 停止自动滚动
                stop();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                //开始自动滚动
                start();
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    @Deprecated
    public void setAdapter(@Nullable PagerAdapter adapter) {
        super.setAdapter(adapter);
    }

    public void setAdapter(@Nullable LoopPagerAdapter adapter) {
        super.setAdapter(adapter);
        //设置到中间位置，防止一开始就向左滑出现滑不动的情况

        if (adapter == null) {
            return;
        }
        int innerCount = adapter.getCount() / 2;
        setCurrentItem(innerCount - innerCount % adapter.getItemCount());
    }

    public void start() {

        if (getAdapter() == null) {
            return;
        }

        mHander.removeCallbacks(mInfiniteThread);
        mHander.postDelayed(mInfiniteThread, mDelayMillis);
    }

    public void stop() {

        if (mHander != null) {
            mHander.removeCallbacks(mInfiniteThread);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            start();
        } else {
            stop();
        }
    }
}
