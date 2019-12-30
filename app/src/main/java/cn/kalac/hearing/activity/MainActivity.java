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

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import cn.kalac.hearing.R;
import cn.kalac.hearing.adapter.MainPagerFragAdapter;
import cn.kalac.hearing.api.ApiHelper;
import cn.kalac.hearing.javabean.net.SearchHotWordBean;
import cn.kalac.hearing.net.HttpCallback;
import cn.kalac.hearing.net.HttpHelper;
import cn.kalac.hearing.service.PlayMusicService;
import cn.kalac.hearing.utils.DataUtil;
import cn.kalac.hearing.view.MiniSoundWave;
import cn.kalac.hearing.utils.IntentUtil;

public class MainActivity extends BaseActivity {
    /**
     * 数据
     */
    private static final String[] CHANNELS = new String[]{"个性推荐", "主播电台","我的音乐"};
    private List<String> mDataList = Arrays.asList(CHANNELS);
    //view
    private ViewPager mVpMainContent;
    private TextView mTvHotWord;
    private MiniSoundWave mBtnJumpPlay;
    //状态
    private boolean isFillHotWord = false;

    //工具

    private Handler mHandle = new Handler();
    private View llBottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //fixStatusBarColor();
        setStatueTextColor(true);
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
        //初始化服务
        initService();
        //获取热词
        getSearchHotWord();
    }

    /**
     * 获取热搜
     */
    private void getSearchHotWord() {
        final String url = ApiHelper.getSearchHot();
        HttpHelper.getInstance().get(url, new HttpCallback<SearchHotWordBean>() {
            @Override
            public void onSuccess(SearchHotWordBean searchHotWordBean) {
                List<SearchHotWordBean.ResultBean.HotsBean> hotsBeans = searchHotWordBean.getResult().getHots();
                //设置最新的热词
                setSearchHotWords(hotsBeans);
                //保存数据至本地
                String result = getResult();
                DataUtil.saveJson(url,result);

            }
            @Override
            public void onFailed(String string) {
                //连接错误的时候从本地获取
                SearchHotWordBean searchHotWordBean = DataUtil.loadBeanFormLoacl(url, SearchHotWordBean.class);
                if (searchHotWordBean == null) {
                    return;
                }
                List<SearchHotWordBean.ResultBean.HotsBean> hotsBeans = searchHotWordBean.getResult().getHots();
                setSearchHotWords(hotsBeans);
            }

        });
    }


    @Override
    protected void initView() {
        //设置滑动返回不可用
        //mSwipeBackHelper.setSwipeBackEnable(false);
        mVpMainContent = findViewById(R.id.vp_mainContent);
        //搜索框中的热词
        mTvHotWord = findViewById(R.id.tv_searchbar_hotword);
        MainPagerFragAdapter contentAdapter = new MainPagerFragAdapter(getSupportFragmentManager());
        mVpMainContent.setAdapter(contentAdapter);
        //跳转播放页按钮
        mBtnJumpPlay = findViewById(R.id.btn_jumpTOPlay);

        llBottomBar = findViewById(R.id.ll_bottom_bar);
        //BlurDrawable blurDrawable = new BlurDrawable();
        //llBottomBar.setBackgroundDrawable();

    }

    //设置热词
    private void setSearchHotWords(List<SearchHotWordBean.ResultBean.HotsBean> hotsBeans) {
        Random random = new Random();
        int randomNum = random.nextInt(hotsBeans.size());
        mTvHotWord.setText(hotsBeans.get(randomNum).getFirst());
        //mHandle
    }

    @Override
    protected void addListener() {
        mBtnJumpPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转播放界面
                IntentUtil.get().goActivity(mContext,PlayMusicActivity.class);
            }
        });
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
        String url = ApiHelper.getRefreshUserStatesUrl();

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


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void musicStatusToPlay() {
        mBtnJumpPlay.start();
        Log.i(TAG, "musicStatusToPlay: ");
    }

    @Override
    protected void musicStatusToPause() {
        Log.i(TAG, "musicStatusToPause: ");
        mBtnJumpPlay.pause();
    }
}
