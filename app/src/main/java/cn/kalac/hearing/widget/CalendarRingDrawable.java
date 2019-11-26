package cn.kalac.hearing.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author kalac.
 * @date 2019/11/26 22:34
 */
public class CalendarRingDrawable extends Drawable {

    private final Paint mPaint;

    public CalendarRingDrawable() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Rect bounds = getBounds();
        //获取宽
        int width = bounds.right - bounds.left;
        //绘制底圆
        mPaint.setColor(Color.parseColor("#E6E6E6"));
        canvas.drawOval(bounds.left,bounds.bottom - width,bounds.right,bounds.bottom,mPaint);
        //绘制圆上面的竖条
        mPaint.setColor(Color.WHITE);
        int strokeWidth = width / 2;

        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawLine(bounds.left + width / 2,bounds.top + strokeWidth,bounds.left + width / 2,bounds.bottom - width / 2,mPaint);
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }
}
