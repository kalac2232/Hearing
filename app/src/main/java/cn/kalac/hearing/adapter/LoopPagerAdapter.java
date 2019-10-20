package cn.kalac.hearing.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

/**
 * @author kalac.
 * @date 2019/10/20 19:04
 */
public abstract class LoopPagerAdapter extends PagerAdapter {
    @Override
    public int getCount() {
        return getItemCount() * 200;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }


    public abstract int getItemCount();

    public int getItemPosition(int position) {
        return position % getItemCount();
    }
}
