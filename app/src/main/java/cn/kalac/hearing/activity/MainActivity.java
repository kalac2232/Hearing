package cn.kalac.hearing.activity;

import android.os.Bundle;
import android.os.Handler;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Random;

import cn.kalac.hearing.R;
import cn.kalac.hearing.adapter.MainPagerFragAdapter;
import cn.kalac.hearing.api.ApiHelper;
import cn.kalac.hearing.javabean.net.SearchHotWordBean;
import cn.kalac.hearing.net.HttpCallback;
import cn.kalac.hearing.net.HttpHelper;
import cn.kalac.hearing.utils.DataUtil;
import cn.kalac.hearing.view.MiniSoundWave;
import cn.kalac.hearing.utils.IntentUtil;

public class MainActivity extends BaseActivity {

    //view
    private ViewPager mVpMainContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStatueTextColorIsGray(true);
    }

    @Override
    public boolean bindMusicReceiver() {
        return true;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        //刷新登录状态
        refreshLoginState();
    }


    @Override
    protected void initView() {
        mVpMainContent = findViewById(R.id.vp_mainContent);
        //搜索框中的热词

        MainPagerFragAdapter contentAdapter = new MainPagerFragAdapter(getSupportFragmentManager());
        mVpMainContent.setAdapter(contentAdapter);

    }

    @Override
    protected void addListener() {

    }


    /**
     * 刷新登录状态
     */
    private void refreshLoginState() {
        String url = ApiHelper.getRefreshUserStatesUrl();

        HttpHelper.getInstance().get(ApiHelper.getRefreshUserStatesUrl(), new HttpCallback<String>(this) {

            @Override
            public void onResultSuccess(String s) {

                Toast.makeText(mContext,"刷新登录状态成功",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onResultFailed(String string) {
                Toast.makeText(mContext,"刷新登录状态错误"+string,Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

}
