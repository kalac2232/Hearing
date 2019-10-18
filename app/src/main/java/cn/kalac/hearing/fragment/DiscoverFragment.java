package cn.kalac.hearing.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.kalac.hearing.R;
import cn.kalac.hearing.adapter.MainContentClassifyAdapter;


/*
 * Created by Kalac on 2019/2/26
 */

public class DiscoverFragment extends Fragment {



    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_discover, container, false);
        initView(view);
        initData();
        addListener();

        return view;
    }


    private void initView(View view) {

        mRecyclerView = view.findViewById(R.id.rcv_main_content);
    }

    private void initData() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new MainContentClassifyAdapter(getContext()));
    }

    protected void addListener() {


    }

}
