package cn.kalac.hearing.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.orhanobut.logger.Logger;

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

    public MiniSoundWave(Context context) {
        this(context,null);
    }

    public MiniSoundWave(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MiniSoundWave(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        mLintStrokeWidth = DensityUtil.dip2px(context,4);

        mPaint = initPaint();


    }

    private void initLinesPoint() {
        //第一位为第几条线，第二位为这个线的4个数值
        mLines = new float[4][4];
        //第一条线 (mLintStrokeWidth/ 2f是为了让圆弧的显现出来，绘制直线时的xy是包含圆弧的xy)
        mLines[0][0] = mLintStrokeWidth / 2f;
        mLines[0][1] = mLintStrokeWidth / 2f;
        mLines[0][2] = mLintStrokeWidth / 2f;
        mLines[0][3] = getMeasuredHeight() - mLintStrokeWidth / 2f;
    }

    private Paint initPaint() {
        Paint paint = new Paint();
        paint.setStrokeWidth(mLintStrokeWidth);
        paint.setColor(Color.BLACK);
        paint.setStrokeCap(Paint.Cap.ROUND);
        return paint;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Logger.i("测量高度:%s",getMeasuredHeight());
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //canvas.drawLine(mLines[0][0],mLines[0][1],mLines[0][2],mLines[0][3],mPaint);
    }
}
