package cn.kalac.hearing.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.kalac.hearing.HearingApplication;
import cn.kalac.hearing.R;
import cn.kalac.hearing.activity.PlayMusicActivity;
import cn.kalac.hearing.adapter.BannerAdapter;
import cn.kalac.hearing.adapter.MainContentClassifyAdapter;
import cn.kalac.hearing.api.ApiHelper;
import cn.kalac.hearing.javabean.BannerBean;
import cn.kalac.hearing.javabean.LoginResultBean;
import cn.kalac.hearing.javabean.RecommendSongsBean;
import cn.kalac.hearing.javabean.song.Song;
import cn.kalac.hearing.net.HttpCallback;
import cn.kalac.hearing.net.HttpHelper;
import cn.kalac.hearing.utils.DataUtil;
import cn.kalac.hearing.utils.TimeUtil;

import static android.support.constraint.Constraints.TAG;


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
