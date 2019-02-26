package cn.kalac.hearing.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.kalac.hearing.R;
import cn.kalac.hearing.javabean.BannerBean;

public class BannerAdapter extends PagerAdapter {
    Context mContext;
    List<BannerBean.BannersBean> mBannersList;
    public BannerAdapter(Context context, List<BannerBean.BannersBean> banners) {
        mContext = context;
        mBannersList = banners;
    }

    @Override
    public int getCount() {
        return mBannersList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.main_banner_layout, container, false);
        ImageView banner = view.findViewById(R.id.iv_main_banner);
        Glide.with(mContext).load(mBannersList.get(position).getImageUrl()).into(banner);
        container.addView(view);
        return view;
    }
}
