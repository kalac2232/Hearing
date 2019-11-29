package cn.kalac.hearing.widget.behavier;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cn.kalac.hearing.utils.DensityUtil;
import cn.kalac.hearing.view.DailyPagerTopView;

/**
 * @author ghn
 * @date 2019/11/28 11:13
 */
public class DailyHeaderBehavior extends CoordinatorLayout.Behavior<DailyPagerTopView> {


    private float actionBarHeight;

    public DailyHeaderBehavior() {
    }

    public DailyHeaderBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull DailyPagerTopView child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
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
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull DailyPagerTopView child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        if (target instanceof RecyclerView) {
            RecyclerView list = (RecyclerView) target;
            // 列表第一个全部可见Item的位置
            int pos = ((LinearLayoutManager) list.getLayoutManager()).findFirstCompletelyVisibleItemPosition();

            // 整体可以滑动，否则RecyclerView消费滑动事件
            if (canScroll(child, dy) && pos == 0) {
                //Log.i("---", "onNestedPreScroll: dy" + dy);
                //Log.i("---", "child.getTranslationY()" + child.getTranslationY());
                //计算当前滑动的距离
                float finalY = child.getTranslationY() - dy;
                //判断是否超出了最大滑动范围
                if (finalY < -getOffsetLimited(child)) {
                    finalY = -getOffsetLimited(child);
                } else if (finalY > 0) {
                    finalY = 0;
                }
                child.setTranslationY(finalY);
                child.setDisplacementPercent(Math.abs(finalY) / getOffsetLimited(child));
                // 让CoordinatorLayout消费滑动事件
                consumed[1] = dy;
            }
        }
    }

    /**
     * 滑动最大限制
     * @param child
     * @return
     */
    private float getOffsetLimited(DailyPagerTopView child) {
        return child.getHeight() - child.getUpOcclusionDistance() - actionBarHeight - DensityUtil.getStatusBarHeight(child.getContext());
    }

    private boolean canScroll(DailyPagerTopView child, float scrollY) {

        TypedArray actionbarSizeTypedArray = child.getContext().obtainStyledAttributes(new int[] { android.R.attr.actionBarSize });
        actionBarHeight = actionbarSizeTypedArray.getDimension(0, 0);
        actionbarSizeTypedArray.recycle();

        if (scrollY > 0 && Math.abs(child.getTranslationY()) == getOffsetLimited(child)) {
            return false;
        }

        return true;
    }
}