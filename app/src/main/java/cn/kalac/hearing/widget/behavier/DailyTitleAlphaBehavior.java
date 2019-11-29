package cn.kalac.hearing.widget.behavier;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import cn.kalac.hearing.utils.DensityUtil;
import cn.kalac.hearing.view.DailyPagerTopView;

/**
 * @author ghn
 * @date 2019/11/28 11:11
 */
public class DailyTitleAlphaBehavior extends CoordinatorLayout.Behavior<TextView> {

    private float mActionBarHeight;

    public DailyTitleAlphaBehavior() {
    }

    public DailyTitleAlphaBehavior(Context context, AttributeSet attrs) {

        super(context, attrs);
        mActionBarHeight = DensityUtil.getActionBarHeight(context);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, TextView child, View dependency) {
        return dependency instanceof DailyPagerTopView;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, TextView child, View dependency) {
        DailyPagerTopView dailyPagerTopView = (DailyPagerTopView) dependency;

        child.setAlpha(2 * dailyPagerTopView.getDisplacementPercent() - 1);

        return true;
    }
}