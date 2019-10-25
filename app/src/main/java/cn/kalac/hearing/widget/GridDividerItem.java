package cn.kalac.hearing.widget;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orhanobut.logger.Logger;

import cn.kalac.hearing.utils.DensityUtil;

/**
 * @author kalac.
 * @date 2019/10/22 22:49
 */
public class GridDividerItem extends RecyclerView.ItemDecoration {

    private final int mInterval;

    public GridDividerItem(int interval) {
        mInterval = interval;

    }
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        //这个为想要将控件设置为的大小
        int viewWidth = (parent.getWidth() - mInterval) / 3;
        //这个是当没有空隙的时候view的大小
        int realWidth = parent.getWidth() / 3;

        Logger.wtf("---%s",outRect.toString());

        if (parent.getChildAdapterPosition(view) % 3 == 0) {
            outRect.left = 0;
            //第一个控件需要左边贴着界边，所以右边需要分配剩余的全部空间
            outRect.right = realWidth - viewWidth;
        } else if (parent.getChildAdapterPosition(view) % 3 == 1) {
            outRect.left = (realWidth - viewWidth) / 2;
            outRect.right = (realWidth - viewWidth) / 2;
        } else if (parent.getChildAdapterPosition(view) % 3 == 2) {
            outRect.left = realWidth - viewWidth;
            outRect.right = 0;
        }

    }
}
