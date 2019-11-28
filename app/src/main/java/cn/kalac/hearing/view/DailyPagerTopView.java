package cn.kalac.hearing.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * @author kalac.
 * @date 2019/11/28 21:39
 */
public class DailyPagerTopView extends ConstraintLayout {

    /**
     * 当前已经位移量百分比
     */
    private float mDisplacementPercent;

    /**
     * 底部向上遮挡的位移
     */
    private int mUpOcclusionDistance;

    public DailyPagerTopView(Context context) {
        this(context,null);
    }

    public DailyPagerTopView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DailyPagerTopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mUpOcclusionDistance = (int) (getMeasuredHeight() * 0.1);
    }

    /**
     * 获取向上位移的距离
     * @return
     */
    public int getUpOcclusionDistance() {
        return mUpOcclusionDistance;
    }

    public float getDisplacementPercent() {
        return mDisplacementPercent;
    }

    public void setDisplacementPercent(float displacementPercent) {
        this.mDisplacementPercent = displacementPercent;
    }
}
