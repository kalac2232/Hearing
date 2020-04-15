package cn.kalac.hearing.adapter;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.kalac.hearing.R;
import cn.kalac.hearing.api.ApiHelper;
import cn.kalac.hearing.javabean.net.mainRecom.NetRecomNewMusic;
import cn.kalac.hearing.javabean.net.mainRecom.NetRecomPLayListBean;
import cn.kalac.hearing.net.HttpCallback;
import cn.kalac.hearing.net.HttpHelper;
import cn.kalac.hearing.utils.DataUtil;
import cn.kalac.hearing.utils.DensityUtil;
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
            default:
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
            rcvDetailed.addItemDecoration(new GridDividerItem(DensityUtil.dip2px(mContext,25)));

        }
    }

    /**
     * 获取推荐歌单
     *
     * @param recyclerView
     */
    private void initRecomPlayList(final RecyclerView recyclerView) {
        HttpHelper.getInstance().get(ApiHelper.getRecomPlayList(), new HttpCallback<NetRecomPLayListBean>() {
            @Override
            public void onResultSuccess(NetRecomPLayListBean netRecomPLayListBean) {

                MainClassifyDetailedAdapter adapter = new MainClassifyDetailedAdapter(mContext, netRecomPLayListBean, TYPE_RECOM_PLAYLIST);
                recyclerView.setAdapter(adapter);
                //缓存数据
                String json = getResult();
                DataUtil.saveJson(ApiHelper.getRecomPlayList(), json);
            }

            @Override
            public void onResultFailed(String string) {
                NetRecomPLayListBean netRecomPLayListBean = DataUtil.loadBeanFormLoacl(ApiHelper.getRecomPlayList(), NetRecomPLayListBean.class);
                if (netRecomPLayListBean == null) {
                    return;
                }
                MainClassifyDetailedAdapter adapter = new MainClassifyDetailedAdapter(mContext, netRecomPLayListBean, TYPE_RECOM_PLAYLIST);
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
        HttpHelper.getInstance().get(ApiHelper.getRecomNewMusic(), new HttpCallback<NetRecomNewMusic>() {
            @Override
            public void onResultSuccess(NetRecomNewMusic netRecomNewMusic) {

                MainClassifyDetailedAdapter adapter = new MainClassifyDetailedAdapter(mContext, netRecomNewMusic, TYPE_NEW_MUSIC);
                recyclerView.setAdapter(adapter);
                //缓存数据
                String json = getResult();
                DataUtil.saveJson(ApiHelper.getRecomPlayList(), json);
            }

            @Override
            public void onResultFailed(String string) {
                NetRecomNewMusic netRecomNewMusic = DataUtil.loadBeanFormLoacl(ApiHelper.getRecomPlayList(), NetRecomNewMusic.class);
                if (netRecomNewMusic == null) {
                    return;
                }
                MainClassifyDetailedAdapter adapter = new MainClassifyDetailedAdapter(mContext, netRecomNewMusic, TYPE_NEW_MUSIC);
                recyclerView.setAdapter(adapter);
            }
        });
    }


}
