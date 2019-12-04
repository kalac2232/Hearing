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
        Glide.with(holder.img.getContext()).load(musicBean.getAlbumBean().getPicUrl())
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(10,0))).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return musicBeans == null ? 0 : musicBeans.size();
    }

    public class VH extends RecyclerView.ViewHolder{
        TextView name;
        ImageView img;

        public VH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_music_name);
            img = itemView.findViewById(R.id.iv_music_cover);
        }
    }
}
