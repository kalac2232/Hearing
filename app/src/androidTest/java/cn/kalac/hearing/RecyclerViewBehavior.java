package cn.kalac.hearing;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * @author ghn
 * @date 2019/11/28 11:11
 */
public class RecyclerViewBehavior extends CoordinatorLayout.Behavior<View> {

    public RecyclerViewBehavior() {
    }

    public RecyclerViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof TextView;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        //计算列表y坐标，最小为0
        TypedArray actionbarSizeTypedArray = parent.getContext().obtainStyledAttributes(new int[] { android.R.attr.actionBarSize });
        float actionBarHeight = actionbarSizeTypedArray.getDimension(0, 0);
        actionbarSizeTypedArray.recycle();
        float y = dependency.getHeight() + dependency.getTranslationY();
        if (y < actionBarHeight) {
            y = actionBarHeight;
        }
        child.setY(y);
        return true;
    }
}