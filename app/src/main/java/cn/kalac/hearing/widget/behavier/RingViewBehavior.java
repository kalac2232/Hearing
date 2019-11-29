package cn.kalac.hearing.widget.behavier;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import cn.kalac.hearing.utils.DensityUtil;
import cn.kalac.hearing.view.DailyPagerTopView;

/**
 * @author ghn
 * @date 2019/11/28 11:11
 */
public class RingViewBehavior extends CoordinatorLayout.Behavior<View> {

    private float mActionBarHeight;

    public RingViewBehavior() {
    }

    public RingViewBehavior(Context context, AttributeSet attrs) {

        super(context, attrs);
        mActionBarHeight = DensityUtil.getActionBarHeight(context);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof DailyPagerTopView;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        DailyPagerTopView dailyPagerTopView = (DailyPagerTopView) dependency;
        //计算圆环的位置，当getTranslationY()为0时为初始位置
        float y = dependency.getHeight() - dailyPagerTopView.getUpOcclusionDistance() - child.getHeight() * 0.47f + dependency.getTranslationY();
        if (y < mActionBarHeight) {
            y = mActionBarHeight;
        }
        child.setY(y);

        child.setAlpha(1 - dailyPagerTopView.getDisplacementPercent());

        return true;
    }
}