package cn.kalac.hearing.adapter;

import android.content.Context;
import android.graphics.Color;
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
import cn.kalac.hearing.widget.GridDividerItem;


public class MainContentClassifyAdapter extends RecyclerView.Adapter<MainContentClassifyAdapter.VH> {

    private static final String TAG = "MainContentClassifyAdap";
    private final Context mContext;

    private static final int TYPE_RECOM_PLAYLIST = 1;
    private static final int TYPE_NEW_MUSIC = 2;
    //private static final int TYPE_RECOM_FM = 3;

    public MainContentClassifyAdapter(Context context) {
        mContext = context;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_main_classify, parent, false);
        return new VH(view);
    }


    @Override
    public void onBindViewHolder(VH holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_RECOM_PLAYLIST:
                holder.tvTitle.setText("推荐歌单");
                holder.tvMore.setText("歌单广场");
                //获取推荐歌单
                initRecomPlayList(holder.rcvDetailed);
                break;
            case TYPE_NEW_MUSIC:
                holder.tvTitle.setText("最新音乐");
                holder.tvMore.setVisibility(View.INVISIBLE);
                //获取最新音乐
                initRecomNewMusic(holder.rcvDetailed);
                break;
//            case TYPE_RECOM_FM:
//                holder.tvTitle.setText("主播电台");
//                holder.tvMore.setVisibility(View.INVISIBLE);
//                break;

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_RECOM_PLAYLIST;
        } else if (position == 1) {
            return TYPE_NEW_MUSIC;
        }

        return 1;
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public class VH extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private TextView tvMore;
        private RecyclerView rcvDetailed;

        public VH(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_item_classify_title);
            tvMore = itemView.findViewById(R.id.tv_item_classify_more);
            rcvDetailed = itemView.findViewById(R.id.rcv_item_classify_detailed);
            //设置网格布局
            rcvDetailed.setLayoutManager(new GridLayoutManager(mContext, 3));
            rcvDetailed.addItemDecoration(new GridDividerItem());

        }
    }

    /**
     * 获取推荐歌单
     *
     * @param recyclerView
     */
    private void initRecomPlayList(final RecyclerView recyclerView) {
        HttpHelper.getInstance().get(ApiHelper.getRecomPlayList(), new HttpCallback<RecomPLayListBean>() {
            @Override
            public void onSuccess(RecomPLayListBean recomPLayListBean) {

                MainClassifyDetailedAdapter adapter = new MainClassifyDetailedAdapter(mContext, recomPLayListBean, TYPE_RECOM_PLAYLIST);
                recyclerView.setAdapter(adapter);
                //缓存数据
                String json = getResult();
                DataUtil.saveJson(ApiHelper.getRecomPlayList(), json);
            }

            @Override
            public void onFailed(String string) {
                RecomPLayListBean recomPLayListBean = DataUtil.loadBeanFormLoacl(ApiHelper.getRecomPlayList(), RecomPLayListBean.class);
                if (recomPLayListBean == null) {
                    return;
                }
                MainClassifyDetailedAdapter adapter = new MainClassifyDetailedAdapter(mContext, recomPLayListBean, TYPE_RECOM_PLAYLIST);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    /**
     * 获取推荐新音乐
     *
     * @param recyclerView
     */
    private void initRecomNewMusic(final RecyclerView recyclerView) {
        HttpHelper.getInstance().get(ApiHelper.getRecomNewMusic(), new HttpCallback<RecomNewMusic>() {
            @Override
            public void onSuccess(RecomNewMusic recomNewMusic) {

                MainClassifyDetailedAdapter adapter = new MainClassifyDetailedAdapter(mContext, recomNewMusic, TYPE_NEW_MUSIC);
                recyclerView.setAdapter(adapter);
                //缓存数据
                String json = getResult();
                DataUtil.saveJson(ApiHelper.getRecomPlayList(), json);
            }

            @Override
            public void onFailed(String string) {
                RecomNewMusic recomNewMusic = DataUtil.loadBeanFormLoacl(ApiHelper.getRecomPlayList(), RecomNewMusic.class);
                if (recomNewMusic == null) {
                    return;
                }
                MainClassifyDetailedAdapter adapter = new MainClassifyDetailedAdapter(mContext, recomNewMusic, TYPE_NEW_MUSIC);
                recyclerView.setAdapter(adapter);
            }
        });
    }


}
