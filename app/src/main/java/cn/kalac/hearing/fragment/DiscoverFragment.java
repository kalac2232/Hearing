package cn.kalac.hearing.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.util.List;

import cn.kalac.hearing.R;
import cn.kalac.hearing.activity.RecomDailyActivity;
import cn.kalac.hearing.adapter.BannerAdapter;
import cn.kalac.hearing.adapter.MainContentClassifyAdapter;
import cn.kalac.hearing.api.ApiHelper;
import cn.kalac.hearing.javabean.net.BannerBean;
import cn.kalac.hearing.net.HttpCallback;
import cn.kalac.hearing.net.HttpHelper;
import cn.kalac.hearing.utils.DataUtil;
import cn.kalac.hearing.utils.TimeUtil;
import cn.kalac.hearing.view.LoopViewPager;
import cn.kalac.hearing.view.PunctuateIndicator;
import cn.kalac.hearing.utils.IntentUtil;


/**
 * @author Kalac
 * Created by Kalac on 2019/2/26
 */

public class DiscoverFragment extends Fragment {

    private LoopViewPager mVpBanner;
    private RecyclerView mRecyclerView;
    private TextView mTvDaily;
    private View mIvDaily;

    private BannerAdapter mBannerAdapter;

    private Context mContext;
    private PunctuateIndicator mIndicator;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mContext = getContext();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_discover, container, false);
        initView(view);
        initData();
        addListener();

        return view;
    }


    private void initView(View view) {
        mVpBanner = view.findViewById(R.id.vp_main_banner);
        mRecyclerView = view.findViewById(R.id.rcv_main_content);
        //指示器
        mIndicator = view.findViewById(R.id.punctuateIndicator);

        mTvDaily = view.findViewById(R.id.tv_main_daily);
        mIvDaily = view.findViewById(R.id.iv_main_daily);

    }

    private void initData() {
        //获取banner
        initBanner();
        //设置日推的每日时间
        mTvDaily.setText(TimeUtil.getTime(System.currentTimeMillis(), "d"));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new MainContentClassifyAdapter(getContext()));
        mRecyclerView.setNestedScrollingEnabled(false);
    }

    protected void addListener() {
        mIvDaily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.get().goActivity(mContext, RecomDailyActivity.class);
            }
        });

    }

    private void initBanner() {
        final String bannerUrl = ApiHelper.getBannerUrl();
        HttpHelper.getInstance().get(bannerUrl, new HttpCallback<BannerBean>(getActivity()) {

            @Override
            public void onResultSuccess(BannerBean bannerBean) {
                List<BannerBean.BannersBean> banners = bannerBean.getBanners();

                setAdapterData(banners);

                String json = getResult();
                DataUtil.saveJson(bannerUrl, json);
            }

            @Override
            public void onResultFailed(String string) {
                Toast.makeText(mContext, "网络错误：" + string, Toast.LENGTH_SHORT).show();
                Logger.e(string);
                BannerBean bannerBean = DataUtil.loadBeanFormLoacl(bannerUrl, BannerBean.class);

                if (bannerBean == null) {
                    return;
                }
                setAdapterData(bannerBean.getBanners());

            }

        });
    }

    private void setAdapterData(List<BannerBean.BannersBean> banners) {
        mBannerAdapter = new BannerAdapter(mContext, banners);
        mVpBanner.setAdapter(mBannerAdapter);
        mIndicator.bindViewPager(mVpBanner);
        mVpBanner.start();
    }

}
