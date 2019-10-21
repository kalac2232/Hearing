package cn.kalac.hearing.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.orhanobut.logger.Logger;

import cn.kalac.hearing.adapter.LoopPagerAdapter;

/**
 * @author kalac.
 * @date 2019/10/20 22:56
 */
public class PunctuateIndicator extends View {

    /**
     * 点的个数
     */
    private int mPointCount = 0;
    /**
     * 当前位置
     */
    private int mCurrentPosition = 0;

    private int mPointRadius = 15;
    private Paint mWhitePaint;
    private Paint mRedPaint;

    public PunctuateIndicator(Context context) {
        this(context,null);
    }

    public PunctuateIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PunctuateIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //初始化画笔
        initPaint();
    }

    private void initPaint() {
        mRedPaint = new Paint();
        mRedPaint.setAntiAlias(true);
        mRedPaint.setColor(Color.parseColor("#FF3A39"));
        mRedPaint.setStyle(Paint.Style.FILL);
        mWhitePaint = new Paint();
        mWhitePaint.setAntiAlias(true);
        mWhitePaint.setAlpha((int) (255 * 0.5f));
        mWhitePaint.setColor(Color.WHITE);
        mWhitePaint.setStyle(Paint.Style.FILL);
    }

    public void bindViewPager(ViewPager viewPager) {

        if (viewPager == null) {
            return;
        }
        PagerAdapter adapter = viewPager.getAdapter();
        if (adapter == null) {
            return;
        }
        //获取相关数据
        if (adapter instanceof LoopPagerAdapter) {
            //如果是无限循环的adapter，则处理获取到的位置与个数信息
            mPointCount = ((LoopPagerAdapter) adapter).getItemCount();
            int currentItem = viewPager.getCurrentItem();
            mCurrentPosition = currentItem % mPointCount;
        } else {
            mPointCount = adapter.getCount();
            mCurrentPosition = viewPager.getCurrentItem();
        }

        //设置滑动监听，当滑动完成后切换指示器的位置
        initViewPagerListener(viewPager);

        requestLayout();
    }

    private void initViewPagerListener(ViewPager viewPager) {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setIndicatorPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setIndicatorPosition(int position) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = (int) (mPointCount * mPointRadius * 2 + ((mPointCount - 1) * mPointRadius * 2 * 0.75));
        int height = mPointRadius * 2;
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Logger.i("onDraw");
    }
}
