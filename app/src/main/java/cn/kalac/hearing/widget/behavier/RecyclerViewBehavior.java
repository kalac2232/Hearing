package cn.kalac.hearing.widget.behavier;

import android.content.Context;
import android.content.res.TypedArray;
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
public class RecyclerViewBehavior extends CoordinatorLayout.Behavior<View> {

    private float mActionBarHeight;
    private int statusBarHeight = -1;

    public RecyclerViewBehavior() {
    }

    public RecyclerViewBehavior(Context context, AttributeSet attrs) {

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

        float y = dependency.getHeight() - dailyPagerTopView.getUpOcclusionDistance() + dependency.getTranslationY();
        if (statusBarHeight == -1) {
            statusBarHeight = DensityUtil.getStatusBarHeight(parent.getContext());
        }
        if (y < mActionBarHeight + statusBarHeight) {
            y = mActionBarHeight + statusBarHeight;
        }
        child.setY(y);
        return true;
    }
}