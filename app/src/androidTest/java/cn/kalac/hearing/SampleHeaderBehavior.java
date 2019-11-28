package cn.kalac.hearing;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * @author ghn
 * @date 2019/11/28 11:13
 */
public class SampleHeaderBehavior extends CoordinatorLayout.Behavior<TextView> {


    private float actionBarHeight;

    public SampleHeaderBehavior() {
    }

    public SampleHeaderBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull TextView child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    /**
     * @param coordinatorLayout
     * @param child
     * @param target
     * @param dx
     * @param dy 这一次滑动所位移的距离
     * @param consumed
     * @param type
     */
    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull TextView child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        if (target instanceof RecyclerView) {
            RecyclerView list = (RecyclerView) target;
            // 列表第一个全部可见Item的位置
            int pos = ((LinearLayoutManager) list.getLayoutManager()).findFirstCompletelyVisibleItemPosition();

            // 整体可以滑动，否则RecyclerView消费滑动事件
            if (canScroll(child, dy) && pos == 0) {
                //Log.i("---", "onNestedPreScroll: dy" + dy);
                //Log.i("---", "child.getTranslationY()" + child.getTranslationY());
                float finalY = child.getTranslationY() - dy;
                if (finalY < -child.getHeight() + actionBarHeight) {
                    finalY = -child.getHeight() + actionBarHeight;
                } else if (finalY > 0) {
                    finalY = 0;
                }
                child.setTranslationY(finalY);
                // 让CoordinatorLayout消费滑动事件
                consumed[1] = dy;
            }
        }
    }

    private boolean canScroll(View child, float scrollY) {

        TypedArray actionbarSizeTypedArray = child.getContext().obtainStyledAttributes(new int[] { android.R.attr.actionBarSize });
        actionBarHeight = actionbarSizeTypedArray.getDimension(0, 0);
        actionbarSizeTypedArray.recycle();

        if (scrollY > 0 && Math.abs(child.getTranslationY()) == child.getHeight() - actionBarHeight) {
            return false;
        }

        return true;
    }
}