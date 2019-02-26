package cn.kalac.hearing.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;
import cn.kalac.hearing.R;
import cn.kalac.hearing.service.PlayMusicService;


/*
 * Created by Kalac on 2019/2/1
 */

public abstract class BaseActivity extends FragmentActivity implements BGASwipeBackHelper.Delegate{

    String TAG = ""+getClass().toString();
    protected Context mContext;
    private MusicStatusReceiver mMusicStatusReceiver;
    protected BGASwipeBackHelper mSwipeBackHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(getLayoutResID());
        //实现状态栏透明
        View decorView = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(option);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        View statusBarView = findViewById(R.id.statusBarView);
        if (statusBarView != null) {
            statusBarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(mContext));
            statusBarView.setLayoutParams(layoutParams);
        }
        initView();
        initData();
        addListener();
        initSwipeBackFinish();
        //判断是否需要注册音乐状态改变广播接收者
        if (registerReciver()){
            registerMusicStatusReciver();
        }
    }


    /**
     * 获取状态栏的高度
     * @param context
     * @return
     */
    private static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }



    /**
     * 注册广播接收者
     */
    private void registerMusicStatusReciver() {
        mMusicStatusReceiver = new MusicStatusReceiver();

        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(PlayMusicService.ACTION_STATUS_MUSIC_PLAY);
        intentFilter.addAction(PlayMusicService.ACTION_STATUS_MUSIC_PAUSE);
        intentFilter.addAction(PlayMusicService.ACTION_STATUS_MUSIC_PREPARE_COMPLETE);
        intentFilter.addAction(PlayMusicService.ACTION_STATUS_MUSIC_PLAY_COMPLETE);

        LocalBroadcastManager.getInstance(mContext).registerReceiver(mMusicStatusReceiver,intentFilter);
    }

    /**
     * 设置状态栏颜色
     * @param isGray
     */
    public void setStatueTextColor(boolean isGray) {
        if (isGray) {
            // 灰色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            // 白色
            getWindow().getDecorView().setSystemUiVisibility(0);
        }
    }

    /**
     * 启动一个activity
     * @param activityClass 目标activity
     */
    protected void startActivty(Class activityClass){
        Intent intent = new Intent(mContext, activityClass);
        startActivity(intent);
    }
    /**
     * 启动一个service
     * @param serviceClass 目标service
     */
    protected void startService(Class serviceClass){
        Intent intent = new Intent(mContext, serviceClass);
        startService(intent);
    }
    /**
     * 发送一个广播
     * @param action action
     */
    protected void sendBroadcast(String action){
        Intent intent = new Intent();
        intent.setAction(action);
        sendBroadcast(intent);
    }
    /**
     * 发送一个本地广播
     * @param action action
     */
    protected void sendLocalBroadcast(String action){
        Intent intent = new Intent();
        intent.setAction(action);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }




    protected void musicStatusToPause() {

    }

    protected void musicStatusToPlay() {

    }

    protected void musicPrepareComplete() {

    }

    protected void musicPlayComplete() {

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (registerReciver()) {
            //如果注册了广播接收者 那么取消注册
            LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mMusicStatusReceiver);
        }
    }

    public boolean registerReciver(){
        return false;
    }
    protected abstract int getLayoutResID();
    protected abstract void initData();
    protected abstract void initView();
    protected abstract void addListener();


    /**
     * 用于接收状态改变的广播接收者
     */
    class MusicStatusReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case PlayMusicService.ACTION_STATUS_MUSIC_PLAY:
                    musicStatusToPlay();
                    break;
                case PlayMusicService.ACTION_STATUS_MUSIC_PAUSE:
                    musicStatusToPause();
                    break;
                case PlayMusicService.ACTION_STATUS_MUSIC_PREPARE_COMPLETE:
                    musicPrepareComplete();
                    break;
                case PlayMusicService.ACTION_STATUS_MUSIC_PLAY_COMPLETE:
                    musicPlayComplete();
                    break;
            }
        }
    }
    /**
     * 初始化滑动返回。在 super.onCreate(savedInstanceState) 之前调用该方法
     */
    private void initSwipeBackFinish() {
        mSwipeBackHelper = new BGASwipeBackHelper(this, this);

        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回」
        // 下面几项可以不配置，这里只是为了讲述接口用法。

        // 设置滑动返回是否可用。默认值为 true
        mSwipeBackHelper.setSwipeBackEnable(true);
        // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
        mSwipeBackHelper.setIsOnlyTrackingLeftEdge(true);
        // 设置是否是微信滑动返回样式。默认值为 true
        mSwipeBackHelper.setIsWeChatStyle(true);
        // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
        mSwipeBackHelper.setShadowResId(R.drawable.bga_sbl_shadow);
        // 设置是否显示滑动返回的阴影效果。默认值为 true
        mSwipeBackHelper.setIsNeedShowShadow(true);
        // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
        mSwipeBackHelper.setIsShadowAlphaGradient(true);
        // 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
        mSwipeBackHelper.setSwipeBackThreshold(0.3f);
        // 设置底部导航条是否悬浮在内容上，默认值为 false
        mSwipeBackHelper.setIsNavigationBarOverlap(false);
    }

    /**
     * 是否支持滑动返回。这里在父类中默认返回 true 来支持滑动返回，如果某个界面不想支持滑动返回则重写该方法返回 false 即可
     *
     * @return
     */
    @Override
    public boolean isSupportSwipeBack() {
        return true;
    }

    /**
     * 正在滑动返回
     *
     * @param slideOffset 从 0 到 1
     */
    @Override
    public void onSwipeBackLayoutSlide(float slideOffset) {
    }

    /**
     * 没达到滑动返回的阈值，取消滑动返回动作，回到默认状态
     */
    @Override
    public void onSwipeBackLayoutCancel() {
    }

    /**
     * 滑动返回执行完毕，销毁当前 Activity
     */
    @Override
    public void onSwipeBackLayoutExecuted() {
        mSwipeBackHelper.swipeBackward();
    }

    @Override
    public void onBackPressed() {
        // 正在滑动返回的时候取消返回按钮事件
        if (mSwipeBackHelper.isSliding()) {
            return;
        }
        mSwipeBackHelper.backward();
    }

}
