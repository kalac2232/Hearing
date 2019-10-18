package cn.kalac.hearing.adapter;

import android.content.Context;
import android.os.Handler;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import cn.kalac.hearing.api.ApiHelper;
import cn.kalac.hearing.javabean.BannerBean;
import cn.kalac.hearing.javabean.mainRecom.RecomNewMusic;
import cn.kalac.hearing.javabean.mainRecom.RecomPLayListBean;
import cn.kalac.hearing.javabean.RecommendSongsBean;
import cn.kalac.hearing.javabean.song.Song;
import cn.kalac.hearing.net.HttpCallback;
import cn.kalac.hearing.net.HttpHelper;
import cn.kalac.hearing.utils.DataUtil;
import cn.kalac.hearing.utils.TimeUtil;


public class MainContentClassifyAdapter extends RecyclerView.Adapter<MainContentClassifyAdapter.VH> {

    private static final String TAG = "MainContentClassifyAdap";
    private final Context mContext;

    private int mDelayMillis = 5000;
    private BannerAdapter mBannerAdapter;
    private Thread mInfiniteThread;
    private TextView mTvDaily;
    private ViewPager mVpBanner;
    private View mBtnGetList;
    private Handler mHander = new Handler();

    private static final int TYPE_HEAD = 0;
    private static final int TYPE_RECOM_PLAYLIST = 1;
    private static final int TYPE_NEW_MUSIC = 2;
    private static final int TYPE_RECOM_FM = 3;

    public MainContentClassifyAdapter(Context context) {
        mContext = context;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_HEAD) {
            view = LayoutInflater.from(mContext).inflate(R.layout.head_main_classify_layout, parent, false);
            initHeadView(view);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_main_classify, parent, false);
        }
        return new VH(view,viewType);
    }

    private void initHeadView(View view) {
        mVpBanner = view.findViewById(R.id.vp_main_banner);
        mTvDaily = view.findViewById(R.id.tv_main_daily);
        mBtnGetList = view.findViewById(R.id.btn_getList);
        //设置日推的每日时间
        mTvDaily.setText(TimeUtil.getTime(System.currentTimeMillis(),"dd"));
        //获取banner
        initBanner();
        //无限轮播的thread
        mInfiniteThread = new InfiniteThread();

        mVpBanner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //当按住vp的时候 停止自动滚动
                        mHander.removeCallbacks(mInfiniteThread);
                        break;
                    case MotionEvent.ACTION_UP:

                    case MotionEvent.ACTION_CANCEL:
                        //开始自动滚动
                        mHander.postDelayed(mInfiniteThread,mDelayMillis);
                        break;
                }
                return false;
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
                        Toast.makeText(mContext,"获取了"+recommendSongBeanList.size()+"个数据",Toast.LENGTH_SHORT).show();
                        //Log.i(TAG, "onSuccess: "+recommendSongsBean);
                        //提取日推列表中歌曲的id方便进行播放
                        extractSongIdFromRecommendList(recommendSongBeanList);
                        //设置将从第一个开始播放
                        HearingApplication.mCurrentPlayPos = 0;
                    }

                    @Override
                    public void onFailed(String string) {
                        Toast.makeText(mContext,"获取失败"+string,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEAD:
                break;
            case TYPE_RECOM_PLAYLIST:
                holder.tvTitle.setText("推荐歌单");
                //获取推荐歌单
                initRecomPlayList(holder.rcvDetailed);
                break;
            case TYPE_NEW_MUSIC:
                holder.tvTitle.setText("最新音乐");
                //获取最新音乐
                initRecomNewMusic(holder.rcvDetailed);
                break;
            case TYPE_RECOM_FM:
                holder.tvTitle.setText("主播电台");
                break;

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEAD;
        } else if (position == 1) {
            return TYPE_RECOM_PLAYLIST;
        }
        else if (position == 2) {
            return TYPE_NEW_MUSIC;
        }
        else if (position == 3) {
            return TYPE_RECOM_FM;
        }
        return 1;
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class VH extends RecyclerView.ViewHolder{

        private TextView tvTitle;
        private RecyclerView rcvDetailed;

        public VH(View itemView,int type) {
            super(itemView);
            if (type != TYPE_HEAD) {
                tvTitle = itemView.findViewById(R.id.tv_item_classify_title);
                rcvDetailed = itemView.findViewById(R.id.rcv_item_classify_detailed);
                //设置网格布局
                rcvDetailed.setLayoutManager(new GridLayoutManager(mContext,3));
            }

        }
    }

    /**
     * 获取推荐歌单
     * @param recyclerView
     */
    private void initRecomPlayList(final RecyclerView recyclerView) {
        HttpHelper.getInstance().get(ApiHelper.getRecomPlayList(), new HttpCallback<RecomPLayListBean>() {
            @Override
            public void onSuccess(RecomPLayListBean recomPLayListBean) {
                
                MainClassifyDetailedAdapter adapter = new MainClassifyDetailedAdapter(mContext, recomPLayListBean,TYPE_RECOM_PLAYLIST);
                recyclerView.setAdapter(adapter);
                //缓存数据
                String json = getResult();
                DataUtil.saveJson(ApiHelper.getRecomPlayList(),json);
            }

            @Override
            public void onFailed(String string) {
                RecomPLayListBean recomPLayListBean = DataUtil.loadBeanFormLoacl(ApiHelper.getRecomPlayList(), RecomPLayListBean.class);
                if (recomPLayListBean == null) {
                    return;
                }
                MainClassifyDetailedAdapter adapter = new MainClassifyDetailedAdapter(mContext, recomPLayListBean,TYPE_RECOM_PLAYLIST);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    /**
     * 获取推荐新音乐
     * @param recyclerView
     */
    private void initRecomNewMusic(final RecyclerView recyclerView) {
        HttpHelper.getInstance().get(ApiHelper.getRecomNewMusic(), new HttpCallback<RecomNewMusic>() {
            @Override
            public void onSuccess(RecomNewMusic recomNewMusic) {

                MainClassifyDetailedAdapter adapter = new MainClassifyDetailedAdapter(mContext, recomNewMusic,TYPE_NEW_MUSIC);
                recyclerView.setAdapter(adapter);
                //缓存数据
                String json = getResult();
                DataUtil.saveJson(ApiHelper.getRecomPlayList(),json);
            }

            @Override
            public void onFailed(String string) {
                RecomNewMusic recomNewMusic = DataUtil.loadBeanFormLoacl(ApiHelper.getRecomPlayList(), RecomNewMusic.class);
                if (recomNewMusic == null) {
                    return;
                }
                MainClassifyDetailedAdapter adapter = new MainClassifyDetailedAdapter(mContext, recomNewMusic,TYPE_NEW_MUSIC);
                recyclerView.setAdapter(adapter);
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
                    mBannerAdapter = new BannerAdapter(mContext, banners);
                    mVpBanner.setAdapter(mBannerAdapter);

                    //设置到中间位置，防止一开始就向左滑出现滑不动的情况
                    mVpBanner.setCurrentItem(2 * banners.size());
                    mHander.postDelayed(mInfiniteThread,mDelayMillis);

                    String json = getResult();
                    DataUtil.saveJson(bannerUrl,json);
                }
            }

            @Override
            public void onFailed(String string) {
                Toast.makeText(mContext,"网络错误：" + string,Toast.LENGTH_SHORT).show();
                Logger.e(string);
                BannerBean bannerBean = DataUtil.loadBeanFormLoacl(bannerUrl, BannerBean.class);

                if (bannerBean == null) {
                    return;
                }
                List<BannerBean.BannersBean> banners = bannerBean.getBanners();
                mBannerAdapter = new BannerAdapter(mContext, banners);
                mVpBanner.setAdapter(mBannerAdapter);
                //设置到中间位置，防止一开始就向左滑出现滑不动的情况
                mVpBanner.setCurrentItem(2 * banners.size());
                mHander.postDelayed(mInfiniteThread,mDelayMillis);
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

    /**
     * 用于无限轮播图的线程
     */
    class InfiniteThread extends Thread{
        @Override
        public void run() {
            mHander.postDelayed(this,mDelayMillis);
            mVpBanner.setCurrentItem(mVpBanner.getCurrentItem() + 1);
        }
    }
}
