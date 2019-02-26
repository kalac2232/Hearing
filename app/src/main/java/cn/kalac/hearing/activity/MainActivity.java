package cn.kalac.hearing.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.kalac.hearing.HearingApplication;
import cn.kalac.hearing.R;
import cn.kalac.hearing.adapter.BannerAdapter;
import cn.kalac.hearing.adapter.MainContentAdapter;
import cn.kalac.hearing.api.ApiHelper;
import cn.kalac.hearing.javabean.BannerBean;
import cn.kalac.hearing.javabean.LoginResultBean;
import cn.kalac.hearing.javabean.RecommendSongsBean;
import cn.kalac.hearing.javabean.song.Song;
import cn.kalac.hearing.net.HttpCallback;
import cn.kalac.hearing.net.HttpHelper;
import cn.kalac.hearing.service.PlayMusicService;

public class MainActivity extends BaseActivity {


    private ViewPager mVpMainContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //fixStatusBarColor();
    }

    @Override
    public boolean registerReciver() {
        return true;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }

    protected void initData() {
        //刷新登录状态
        refreshLoginState();
        //初始化服务
        initService();

    }



    @Override
    protected void initView() {
        //设置滑动返回不可用
        //mSwipeBackHelper.setSwipeBackEnable(false);
        mVpMainContent = findViewById(R.id.vp_mainContent);
        MainContentAdapter contentAdapter = new MainContentAdapter(getSupportFragmentManager());
        mVpMainContent.setAdapter(contentAdapter);
    }

    @Override
    protected void addListener() {

    }

    /**
     * 初始化播放service
     */
    private void initService() {
        startService(PlayMusicService.class);
    }



    /**
     * 刷新登录状态
     */
    private void refreshLoginState() {
        String url = ApiHelper.getRefreshUrl();

        HttpHelper.getInstance().get(url, new HttpCallback<String>() {

            @Override
            public void onSuccess(String s) {

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);
                    int code = jsonObject.getInt("code");
                    if (code == 200) {
                        Toast.makeText(mContext,"刷新登录状态成功"+s,Toast.LENGTH_SHORT).show();
                    } else {
                        String msg = jsonObject.getString("msg");
                        Toast.makeText(mContext,msg + s,Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailed(String string) {
                Toast.makeText(mContext,"刷新登录状态错误"+string,Toast.LENGTH_SHORT).show();
            }
        });
    }


}
