package cn.kalac.hearing.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.kalac.hearing.HearingApplication;
import cn.kalac.hearing.R;
import cn.kalac.hearing.activity.PlayMusicActivity;
import cn.kalac.hearing.adapter.BannerAdapter;
import cn.kalac.hearing.api.ApiHelper;
import cn.kalac.hearing.javabean.BannerBean;
import cn.kalac.hearing.javabean.LoginResultBean;
import cn.kalac.hearing.javabean.RecommendSongsBean;
import cn.kalac.hearing.javabean.song.Song;
import cn.kalac.hearing.net.HttpCallback;
import cn.kalac.hearing.net.HttpHelper;

import static android.support.constraint.Constraints.TAG;


/*
 * Created by Kalac on 2019/2/26
 */

public class DiscoverFragment extends Fragment {
    private View mBtnLogin;
    private View mBtnGetList;
    private View mBtnJump;
    private ViewPager mVpBanner;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_discover, container, false);
        mBtnLogin = view.findViewById(R.id.btn_login);
        mBtnGetList = view.findViewById(R.id.btn_getList);
        mBtnJump = view.findViewById(R.id.btn_jumpTOPlay);
        mVpBanner = view.findViewById(R.id.vp_main_banner);
        addListener();
        //获取banner
        initBanner();
        return view;
    }

    protected void addListener() {
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = ApiHelper.getPhoneLoginUrl("17684721017","GHN,.155070");

                HttpHelper.getInstance().get(url, new HttpCallback<LoginResultBean>() {

                    @Override
                    public void onSuccess(LoginResultBean loginResultBean) {
                        Toast.makeText(getContext(),"登录成功"+loginResultBean.getProfile().getNickname(),Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed(String string) {
                        Toast.makeText(getContext(),"登录失败"+string,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        mBtnGetList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = ApiHelper.getRecommendSongsUrl();
                Log.i(TAG, "onClick: "+url);
                HttpHelper.getInstance().get(url, new HttpCallback<RecommendSongsBean>() {

                    @Override
                    public void onSuccess(RecommendSongsBean recommendSongsBean) {
                        List<RecommendSongsBean.RecommendBean> recommendSongBeanList = recommendSongsBean.getRecommend();
                        Toast.makeText(getContext(),"获取了"+recommendSongBeanList.size()+"个数据",Toast.LENGTH_SHORT).show();
                        //Log.i(TAG, "onSuccess: "+recommendSongsBean);
                        //提取日推列表中歌曲的id方便进行播放
                        extractSongIdFromRecommendList(recommendSongBeanList);
                        //设置将从第一个开始播放
                        HearingApplication.mCurrentPlayPos = 0;
                    }

                    @Override
                    public void onFailed(String string) {
                        Toast.makeText(getContext(),"获取失败"+string,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        mBtnJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    /**
     * 提取日推列表中歌曲的id方便进行播放
     * @param recommendSongBeanList 日推列表
     */
    private void extractSongIdFromRecommendList(List<RecommendSongsBean.RecommendBean> recommendSongBeanList) {
        ArrayList<Song> list = new ArrayList<>();
        for (RecommendSongsBean.RecommendBean bean : recommendSongBeanList) {
            int songId = bean.getId();
            String songName = bean.getName();
            String singerName = bean.getArtists().get(0).getName();
            String picUrl = bean.getAlbum().getPicUrl();
            list.add(new Song(songId,songName,singerName,picUrl));
        }
        if (list.size() > 0) {
            //将数据存放到application中用于全局使用
            HearingApplication.mPlayingSongList.clear();
            HearingApplication.mPlayingSongList.addAll(list);

        }
    }

    private void initBanner() {
        String bannerUrl = ApiHelper.getBannerUrl();
        HttpHelper.getInstance().get(bannerUrl, new HttpCallback<BannerBean>() {

            @Override
            public void onSuccess(BannerBean bannerBean) {
                if (bannerBean.getCode() == 200) {
                    List<BannerBean.BannersBean> banners = bannerBean.getBanners();
                    BannerAdapter bannerAdapter = new BannerAdapter(getContext(), banners);
                    mVpBanner.setAdapter(bannerAdapter);
                }
            }

            @Override
            public void onFailed(String string) {

            }

        });
    }
}
