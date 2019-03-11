package cn.kalac.hearing.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cn.kalac.hearing.fragment.DiscoverFragment;
import cn.kalac.hearing.fragment.MineFragment;


/*
 * Created by Kalac on 2019/2/26
 */

public class MainPagerFragAdapter extends FragmentPagerAdapter {
    public MainPagerFragAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new DiscoverFragment();
            case 1:
                return new DiscoverFragment();
            case 2:
                return new MineFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
