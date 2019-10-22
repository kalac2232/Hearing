package cn.kalac.hearing.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.orhanobut.logger.Logger;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import cn.kalac.hearing.adapter.LoopPagerAdapter;
import cn.kalac.hearing.utils.DensityUtil;

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

    /**
     * 点的半径
     */
    private int mPointRadius = 15;
    /**
     * 点的间距
     */
    private int mPointSpacing = 25;

    private Paint mWhitePaint;
    private Paint mRedPaint;
    private LinkedHashMap<Point, Paint> mPointPaintHashMap;


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
        mPointRadius = DensityUtil.dip2px(getContext(),3);
        mPointSpacing = DensityUtil.dip2px(getContext(),5);

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
        mWhitePaint.setColor(Color.WHITE);
        mWhitePaint.setAlpha((int) (255 * 0.5f));
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

        initPoint();

        requestLayout();

        setIndicatorPosition(mCurrentPosition);
    }

    /**
     * 初始化所有的点，先计算位置与设置其默认颜色
     */
    private void initPoint() {

        mPointPaintHashMap = new LinkedHashMap<>(mPointCount);
        for (int i = 0; i < mPointCount; i++) {
            Point point = new Point(mPointRadius + (mPointRadius * 2 + mPointSpacing) * i, mPointRadius);
            mPointPaintHashMap.put(point,mWhitePaint);
        }
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
        //防止是循环ViewPager中位置不正确的情况
        position = position % mPointCount;

        Iterator<Map.Entry<Point, Paint>> iterator = mPointPaintHashMap.entrySet().iterator();
        int index = 0;
        while (iterator.hasNext()) {
            Map.Entry<Point, Paint> next = iterator.next();
            if (index == position) {
                next.setValue(mRedPaint);
            } else {
                next.setValue(mWhitePaint);
            }
            index++;
        }
        //使其重绘
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = (int) (mPointCount * mPointRadius * 2 + ((mPointCount - 1) * mPointSpacing));
        int height = mPointRadius * 2;
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mPointPaintHashMap == null) {
            return;
        }

        Iterator<Map.Entry<Point, Paint>> iterator = mPointPaintHashMap.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry<Point, Paint> next = iterator.next();
            canvas.drawCircle(next.getKey().x,next.getKey().y,mPointRadius,next.getValue());
        }
    }
}
