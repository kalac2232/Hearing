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
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        if (parent.getChildAdapterPosition(view) % 3 == 0) {
            outRect.left = 0;
            outRect.right = 0;
        } else if (parent.getChildAdapterPosition(view) % 3 == 1) {
            outRect.left = DensityUtil.dip2px(parent.getContext(),6);
            outRect.right = 0;
        } else if (parent.getChildAdapterPosition(view) % 3 == 2) {
            outRect.left = DensityUtil.dip2px(parent.getContext(),13);
            outRect.right = 0;
        }
    }
}
