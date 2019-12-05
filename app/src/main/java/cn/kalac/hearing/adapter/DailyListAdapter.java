package cn.kalac.hearing.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import cn.kalac.hearing.R;
import cn.kalac.hearing.javabean.local.AlbumBean;
import cn.kalac.hearing.javabean.local.MusicBean;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * @author kalac.
 * @date 2019/11/28 21:28
 */
public class DailyListAdapter extends RecyclerView.Adapter<DailyListAdapter.VH> {
    private List<MusicBean> musicBeans;
    public DailyListAdapter(List<MusicBean> musicBeans) {
        this.musicBeans = musicBeans;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recom_daily_list, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        MusicBean musicBean = musicBeans.get(position);
        holder.name.setText(musicBean.getName());
        holder.singer.setText(musicBean.getArtistBean().getName() + " - " + musicBean.getAlbumBean().getName());
        Glide.with(holder.img.getContext()).load(musicBean.getAlbumBean().getPicUrl())
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(10,0))).into(holder.img);
        //设置<SQ>
        if (musicBean.getAlbumBean().isSQ()) {
            holder.sq.setVisibility(View.VISIBLE);
        } else {
            holder.sq.setVisibility(View.GONE);
        }
        //设置《翻唱》
        if (musicBean.getAlbumBean().getFlag() == AlbumBean.Flag.sole) {
            holder.sole.setVisibility(View.VISIBLE);
            Glide.with(holder.img.getContext()).load(R.mipmap.ic_sole_small_icon).into(holder.sole);
        } else {
            holder.sole.setVisibility(View.GONE);
        }
        //设置别名
        if (musicBean.getAlias() != null && !musicBean.getAlias().isEmpty()) {
            holder.alias.setVisibility(View.VISIBLE);
            holder.alias.setText("（" + musicBean.getAlias().get(0) + "）");
        } else {
            holder.alias.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return musicBeans == null ? 0 : musicBeans.size();
    }

    public class VH extends RecyclerView.ViewHolder{
        TextView name;
        TextView singer;
        ImageView img;
        ImageView sq;
        ImageView sole;
        TextView alias;

        public VH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_music_name);
            img = itemView.findViewById(R.id.iv_music_cover);
            singer = itemView.findViewById(R.id.tv_music_singer);
            sq = itemView.findViewById(R.id.iv_sq_icon);
            sole = itemView.findViewById(R.id.iv_sole_icon);
            alias = itemView.findViewById(R.id.tv_music_alias);
        }
    }
}
