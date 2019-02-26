package cn.kalac.hearing.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cn.kalac.hearing.fragment.DiscoverFragment;


/*
 * Created by Kalac on 2019/2/26
 */

public class MainContentAdapter extends FragmentPagerAdapter {
    public MainContentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new DiscoverFragment();
    }

    @Override
    public int getCount() {
        return 2;
    }
}
