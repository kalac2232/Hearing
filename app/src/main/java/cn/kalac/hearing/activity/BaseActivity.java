package cn.kalac.hearing.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;

import cn.kalac.hearing.service.PlayMusicService;


/*
 * Created by Kalac on 2019/2/1
 */

public abstract class BaseActivity extends Activity {

    String TAG = ""+getClass().toString();
    protected Context mContext;
    private MusicStatusReceiver mMusicStatusReceiver;

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
        initView();
        initData();
        addListener();
        //判断是否需要注册音乐状态改变广播接收者
        if (registerReciver()){
            registerMusicStatusReciver();
        }
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

    public abstract boolean registerReciver() ;
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


}
