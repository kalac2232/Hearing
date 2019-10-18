package cn.kalac.hearing.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import cn.kalac.hearing.R;
import cn.kalac.hearing.javabean.BannerBean;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class BannerAdapter extends PagerAdapter {
    Context mContext;
    List<BannerBean.BannersBean> mBannersList;
    public BannerAdapter(Context context, List<BannerBean.BannersBean> banners) {
        mContext = context;
        mBannersList = banners;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE / 2;
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
        //计算出当前的位置
        int realPosition = position % mBannersList.size();
        View view = LayoutInflater.from(mContext).inflate(R.layout.main_banner_layout, container, false);
        ImageView banner = view.findViewById(R.id.iv_main_banner);
        //此处使用Glide进行圆角转换虚拟机会异常的卡顿
        Glide.with(mContext).load(mBannersList.get(realPosition).getImageUrl()).into(banner);
        container.addView(view);
        return view;
    }
}
