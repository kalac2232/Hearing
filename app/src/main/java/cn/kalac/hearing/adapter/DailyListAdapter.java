package cn.kalac.hearing.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author kalac.
 * @date 2019/11/28 21:28
 */
public class DailyListAdapter extends RecyclerView.Adapter<DailyListAdapter.VH> {
    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(new TextView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.textView.setText("H哈: " + (position+1));
        holder.textView.setPadding(0,30,0,40);
    }

    @Override
    public int getItemCount() {
        return 25;
    }

    public class VH extends RecyclerView.ViewHolder{
        TextView textView;

        public VH(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView;
        }
    }
}
