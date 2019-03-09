package cn.kalac.hearing.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import cn.kalac.hearing.HearingApplication;
import cn.kalac.hearing.R;
import cn.kalac.hearing.adapter.BannerAdapter;
import cn.kalac.hearing.adapter.MainContentAdapter;
import cn.kalac.hearing.api.ApiHelper;
import cn.kalac.hearing.javabean.BannerBean;
import cn.kalac.hearing.javabean.LoginResultBean;
import cn.kalac.hearing.javabean.RecommendSongsBean;
import cn.kalac.hearing.javabean.SearchHotWordBean;
import cn.kalac.hearing.javabean.song.Song;
import cn.kalac.hearing.net.HttpCallback;
import cn.kalac.hearing.net.HttpHelper;
import cn.kalac.hearing.service.PlayMusicService;
import cn.kalac.hearing.utils.DataUtil;
import cn.kalac.hearing.utils.MD5Utils;
import cn.kalac.hearing.utils.SharedPreUtils;

public class MainActivity extends BaseActivity {
    //数据
    private static final String[] CHANNELS = new String[]{"个性推荐", "主播电台","我的音乐"};
    private List<String> mDataList = Arrays.asList(CHANNELS);
    //view
    private ViewPager mVpMainContent;
    private TextView mTvHotWord;
    //状态
    private boolean isFillHotWord = false;

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
                //如果已经有本地数据在resume时填充，那么就只刷新数据
                if (!isFillHotWord) {
                    setSearchHotWords(hotsBeans);
                }
                //保存数据
                String result = getResult();
                DataUtil.saveJson(url,result);

            }
            @Override
            public void onFailed(String string) {
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
        MainContentAdapter contentAdapter = new MainContentAdapter(getSupportFragmentManager());
        mVpMainContent.setAdapter(contentAdapter);
        //初始化Indicator
        initMagicIndicator();

    }

    //设置热词
    private void setSearchHotWords(List<SearchHotWordBean.ResultBean.HotsBean> hotsBeans) {
        Random random = new Random();
        int randomNum = random.nextInt(hotsBeans.size());
        mTvHotWord.setHint(hotsBeans.get(randomNum).getFirst());
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

    private void initMagicIndicator() {
        MagicIndicator magicIndicator =  findViewById(R.id.magic_indicator);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                //设置文字
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setNormalColor(Color.parseColor("#88ffffff"));
                simplePagerTitleView.setSelectedColor(Color.WHITE);

                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mVpMainContent.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                //设置指示器
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.setColors(Color.parseColor("#ffffff"));
                indicator.setLineHeight(UIUtil.dip2px(context, 2));
                indicator.setRoundRadius(UIUtil.dip2px(context, 7));
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        LinearLayout titleContainer = commonNavigator.getTitleContainer(); // must after setNavigator
        titleContainer.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        titleContainer.setDividerDrawable(new ColorDrawable() {
            @Override
            public int getIntrinsicWidth() {
                //控制指示器的长度
                return UIUtil.dip2px(mContext, 50);
            }
        });
        ViewPagerHelper.bind(magicIndicator, mVpMainContent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //从本地获取热词数据
        SearchHotWordBean searchHotWordBean = DataUtil.loadBeanFormLoacl(ApiHelper.getSearchHot(), SearchHotWordBean.class);
        if (searchHotWordBean != null) {
            setSearchHotWords(searchHotWordBean.getResult().getHots());
            isFillHotWord = true;
        }
    }
}
