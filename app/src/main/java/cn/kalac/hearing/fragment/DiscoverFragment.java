package cn.kalac.hearing.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import cn.kalac.hearing.HearingApplication;
import cn.kalac.hearing.R;
import cn.kalac.hearing.adapter.BannerAdapter;
import cn.kalac.hearing.adapter.MainContentClassifyAdapter;
import cn.kalac.hearing.api.ApiHelper;
import cn.kalac.hearing.javabean.BannerBean;
import cn.kalac.hearing.javabean.RecommendSongsBean;
import cn.kalac.hearing.javabean.song.Song;
import cn.kalac.hearing.net.HttpCallback;
import cn.kalac.hearing.net.HttpHelper;
import cn.kalac.hearing.utils.DataUtil;
import cn.kalac.hearing.utils.TimeUtil;
import cn.kalac.hearing.view.LoopViewPager;
import cn.kalac.hearing.view.PunctuateIndicator;


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
        mTvDaily.setText(TimeUtil.getTime(System.currentTimeMillis(), "dd"));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new MainContentClassifyAdapter(getContext()));
        mRecyclerView.setNestedScrollingEnabled(false);
    }

    protected void addListener() {
        mIvDaily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = ApiHelper.getRecommendSongsUrl();

                HttpHelper.getInstance().get(url, new HttpCallback<RecommendSongsBean>() {

                    @Override
                    public void onSuccess(RecommendSongsBean recommendSongsBean) {
                        List<RecommendSongsBean.RecommendBean> recommendSongBeanList = recommendSongsBean.getRecommend();
                        Toast.makeText(mContext, "获取了" + recommendSongBeanList.size() + "个数据", Toast.LENGTH_SHORT).show();
                        //Log.i(TAG, "onSuccess: "+recommendSongsBean);
                        //提取日推列表中歌曲的id方便进行播放
                        extractSongIdFromRecommendList(recommendSongBeanList);
                        //设置将从第一个开始播放
                        HearingApplication.mCurrentPlayPos = 0;
                    }

                    @Override
                    public void onFailed(String string) {
                        Toast.makeText(mContext, "获取失败" + string, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private void initBanner() {
        final String bannerUrl = ApiHelper.getBannerUrl();
        HttpHelper.getInstance().get(bannerUrl, new HttpCallback<BannerBean>() {

            @Override
            public void onSuccess(BannerBean bannerBean) {
                if (bannerBean.getCode() == 200) {
                    List<BannerBean.BannersBean> banners = bannerBean.getBanners();

                    setAdapterData(banners);

                    String json = getResult();
                    DataUtil.saveJson(bannerUrl, json);
                }
            }

            @Override
            public void onFailed(String string) {
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


    /**
     * 提取日推列表中歌曲的id方便进行播放
     *
     * @param recommendSongBeanList 日推列表
     */
    private void extractSongIdFromRecommendList(List<RecommendSongsBean.RecommendBean> recommendSongBeanList) {
        ArrayList<Song> list = new ArrayList<>();
        for (RecommendSongsBean.RecommendBean bean : recommendSongBeanList) {
            int songId = bean.getId();
            String songName = bean.getName();
            String singerName = bean.getArtists().get(0).getName();
            String picUrl = bean.getAlbum().getPicUrl();
            list.add(new Song(songId, songName, singerName, picUrl));
        }
        if (list.size() > 0) {
            //将数据存放到application中用于全局使用
            HearingApplication.mPlayingSongList.clear();
            HearingApplication.mPlayingSongList.addAll(list);

        }
    }

}
