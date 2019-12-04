package cn.kalac.hearing.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;

import com.orhanobut.logger.Logger;

import java.util.Arrays;

import cn.kalac.hearing.R;
import cn.kalac.hearing.utils.DensityUtil;

/**
 * @author kalac.
 * @date 2019/10/17 22:39
 */
public class MiniSoundWave extends View {

    private Paint mPaint;

    /**
     * 线段的长度
     */
    private int mLintStrokeWidth = 6;
    private float[][] mLines;
    /**
     * 线段占整个高度的百分比
     */
    private float[] mLinesInitPercent;
    private Context mContext;
    private ValueAnimator mValueAnimator;
    private int mHeightValue;
    private boolean isRunning = false;

    public MiniSoundWave(Context context) {
        this(context,null);
    }

    public MiniSoundWave(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MiniSoundWave(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(context);
        setLineColor(attrs);
    }

    private void setLineColor(AttributeSet attrs) {
        //取出attrs中我们为View设置的相关值
        TypedArray tArray = mContext.obtainStyledAttributes(attrs, R.styleable.MiniSoundWave);
        int color = tArray.getColor(R.styleable.MiniSoundWave_lineColor, Color.BLACK);
        mPaint.setColor(color);
        tArray.recycle();
    }

    private void init(Context context) {

        mLintStrokeWidth = DensityUtil.dip2px(context,1.5f);

        mPaint = initPaint();

        mLinesInitPercent = initLinesData();

        getViewTreeObserver().addOnWindowFocusChangeListener(new ViewTreeObserver.OnWindowFocusChangeListener() {
            @Override
            public void onWindowFocusChanged(boolean hasFocus) {
                changePlayStates(hasFocus);
            }
        });


    }

    private float[] initLinesData() {
        return new float[]{80/160f,150/160f,100/160f,120/160f};
    }

    private void changePlayStates(boolean hasFocus) {
        if (!isRunning()) {
            return;
        }

        if (hasFocus) {
            start();
        } else {
            pause();
        }

    }

    private boolean isRunning() {
        return mValueAnimator != null && isRunning;
    }

    private void intTimer() {
        mValueAnimator = ValueAnimator.ofInt(0, getMeasuredHeight() / 3);
        mValueAnimator.setDuration(400);
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mValueAnimator.setInterpolator(new AccelerateInterpolator());
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mHeightValue = (int) animation.getAnimatedValue();
                //Logger.i("value:%d", mHeightValue);
                postInvalidate();
            }
        });
    }

    private void initLinesPoint() {
        //第一位为第几条线，第二位为这个线的4个数值
        mLines = new float[4][4];
        //第一条线 (mLintStrokeWidth/ 2f是为了让圆弧的显现出来，绘制直线时的xy是包含圆弧的xy)

        int measuredWidth = getMeasuredWidth();
        int interval = (measuredWidth - 4 * mLintStrokeWidth) / 3;
        for (int i = 0; i < mLines.length; i++) {
            mLines[i][0] = mLintStrokeWidth / 2f + interval * i;
            mLines[i][1] = getMeasuredHeight() * (1 - mLinesInitPercent[i]) + mLintStrokeWidth / 2f;
            mLines[i][2] = mLintStrokeWidth / 2f + interval * i;
            mLines[i][3] = getMeasuredHeight() - mLintStrokeWidth / 2f;
        }

    }

    private Paint initPaint() {
        Paint paint = new Paint();
        paint.setStrokeWidth(mLintStrokeWidth);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        return paint;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float[][] linesPoint = calculateLinesPoint();
        for (float[] mLine : linesPoint) {
            canvas.drawLine(mLine[0],mLine[1],mLine[2],mLine[3],mPaint);
        }
    }

    private float[][] calculateLinesPoint() {
        //如果是第一次绘制则先初始化4条线段的初始位置
        if (mLines == null) {
            initLinesPoint();
            intTimer();
            return mLines;
        }
        float[][] cloneLines = new float[4][4];
        //复制二维数组
        for (int i = 0; i < mLines.length; i++) {
            cloneLines[i] = mLines[i].clone();
        }
        cloneLines[0][1] -= mHeightValue;
        cloneLines[2][1] -= mHeightValue;
        cloneLines[1][1] += mHeightValue * 1.3;
        cloneLines[3][1] += mHeightValue * 1.3;

        return cloneLines;
    }

    public void start() {
        if (mValueAnimator != null) {
            mValueAnimator.start();
            postInvalidate();
            isRunning = true;
        }
    }

    public void pause() {
        if (mValueAnimator != null) {
            mValueAnimator.cancel();
            mLinesInitPercent = initLinesData();
            postInvalidate();
            isRunning = false;
        }
    }

}
