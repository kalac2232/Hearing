package cn.kalac.hearing.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.kalac.hearing.R;
import cn.kalac.hearing.javabean.mainRecom.RecomNewMusic;
import cn.kalac.hearing.javabean.mainRecom.RecomPLayListBean;

public class MainClassifyDetailedAdapter extends RecyclerView.Adapter<MainClassifyDetailedAdapter.VH> {

    private final Context mContext;

    private  List<RecomPLayListBean.ResultBean> mPlayList;
    private List<RecomNewMusic.ResultBean> mNewMusic;

    private static final int TYPE_RECOM_PLAYLIST = 1;
    private static final int TYPE_NEW_MUSIC = 2;
    private static final int TYPE_RECOM_FM = 3;

    private int mType;
    public MainClassifyDetailedAdapter(Context context, Object result,int type) {
        mContext = context;
        mType = type;
        if (type == TYPE_RECOM_PLAYLIST) {
            if (result instanceof RecomPLayListBean) {
                RecomPLayListBean bean = (RecomPLayListBean) result;
                mPlayList = bean.getResult();
            }
        } else if (type == TYPE_NEW_MUSIC) {
            if (result instanceof RecomNewMusic) {
                RecomNewMusic bean = (RecomNewMusic) result;
                mNewMusic = bean.getResult();
            }
        }
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_main_classify_detailed, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        switch (mType) {
            case TYPE_RECOM_PLAYLIST:
                RecomPLayListBean.ResultBean bean = mPlayList.get(position);
                Glide.with(mContext).load(bean.getPicUrl()).into(holder.ivImage);
                int i = (int) (bean.getPlayCount() / 10000);
                if (i > 1) {
                    holder.tvHot.setText(i + "万");
                } else {
                    holder.tvHot.setText((int)bean.getPlayCount() + "");
                }
                holder.tvTitle.setText(bean.getName());
                break;
            case TYPE_NEW_MUSIC:
                RecomNewMusic.ResultBean resultBean = mNewMusic.get(position);
                Glide.with(mContext).load(resultBean.getSong().getAlbum().getPicUrl()).into(holder.ivImage);
                holder.tvHot.setVisibility(View.GONE);
                holder.tvTitle.setText(resultBean.getName());
                break;
            case TYPE_RECOM_FM:
                break;
        }

    }


    @Override
    public int getItemCount() {
        return 6;
    }

    public class VH extends RecyclerView.ViewHolder{


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
