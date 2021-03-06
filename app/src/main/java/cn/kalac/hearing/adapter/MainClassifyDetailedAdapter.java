package cn.kalac.hearing.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;

import java.util.List;

import cn.kalac.hearing.R;
import cn.kalac.hearing.javabean.net.mainRecom.NetRecomNewMusic;
import cn.kalac.hearing.javabean.net.mainRecom.NetRecomPLayListBean;
import cn.kalac.hearing.utils.DensityUtil;

public class MainClassifyDetailedAdapter extends RecyclerView.Adapter<MainClassifyDetailedAdapter.VH> {

    private final Context mContext;

    private List<NetRecomPLayListBean.ResultBean> mPlayList;
    private List<NetRecomNewMusic.ResultBean> mNewMusic;

    private static final int TYPE_RECOM_PLAYLIST = 1;
    private static final int TYPE_NEW_MUSIC = 2;
    //private static final int TYPE_RECOM_FM = 3;

    private int mType;

    public MainClassifyDetailedAdapter(Context context, Object result, int type) {
        mContext = context;
        mType = type;
        if (type == TYPE_RECOM_PLAYLIST) {
            if (result instanceof NetRecomPLayListBean) {
                NetRecomPLayListBean bean = (NetRecomPLayListBean) result;
                mPlayList = bean.getResult();
            }
        } else if (type == TYPE_NEW_MUSIC) {
            if (result instanceof NetRecomNewMusic) {
                NetRecomNewMusic bean = (NetRecomNewMusic) result;
                mNewMusic = bean.getResult();
            }
        }
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_main_classify_detailed, parent, false);
        //动态计算item的大小
        int width = (parent.getWidth() - DensityUtil.dip2px(parent.getContext(),25)) / 3;

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width, (int) (width * 1.45));
        view.setLayoutParams(layoutParams);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        switch (mType) {
            case TYPE_RECOM_PLAYLIST:
                NetRecomPLayListBean.ResultBean bean = mPlayList.get(position);
                Glide.with(mContext).load(bean.getPicUrl()).into(holder.ivImage);


                int i = (int) (bean.getPlayCount() / 10000);
                if (i > 10000) {
                    holder.tvHot.setText(i / 10000 + "亿");
                } else if (i > 1){
                    holder.tvHot.setText(i + "万");
                } else {
                    holder.tvHot.setText((int) bean.getPlayCount() + "");
                }
                holder.tvTitle.setText(bean.getName());
                break;
            case TYPE_NEW_MUSIC:
                NetRecomNewMusic.ResultBean resultBean = mNewMusic.get(position);
                Glide.with(mContext).load(resultBean.getSong().getAlbum().getPicUrl()).into(holder.ivImage);
                holder.tvHot.setVisibility(View.GONE);
                holder.tvTitle.setText(resultBean.getName());
                break;
//            case TYPE_RECOM_FM:
//                break;
            default:
                break;
        }

    }


    @Override
    public int getItemCount() {
        return 6;
    }

    public class VH extends RecyclerView.ViewHolder {


        private final ImageView ivImage;
        private final TextView tvHot;
        private final TextView tvTitle;

        public VH(View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_classify_detailed_img);
            tvHot = itemView.findViewById(R.id.tv_classify_detailed_hot);
            tvTitle = itemView.findViewById(R.id.tv_classify_detailed_title);
        }
    }


}
