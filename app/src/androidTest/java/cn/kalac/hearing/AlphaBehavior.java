package cn.kalac.hearing;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * @author ghn
 * @date 2019/11/28 10:48
 */
public class AlphaBehavior extends CoordinatorLayout.Behavior<View> {

    public AlphaBehavior() {
    }

    public AlphaBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        Log.i("---", "onDependentViewChanged: ");
        TypedArray actionbarSizeTypedArray = parent.getContext().obtainStyledAttributes(new int[] { android.R.attr.actionBarSize });
        float actionBarHeight = actionbarSizeTypedArray.getDimension(0, 0);
        actionbarSizeTypedArray.recycle();

        float v = Math.abs(dependency.getTranslationY()) / (dependency.getHeight() - actionBarHeight);
        child.setAlpha(v);
        return true;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof TextView;
    }
}
