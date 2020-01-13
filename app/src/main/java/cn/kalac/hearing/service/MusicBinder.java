package cn.kalac.hearing.service;

/*
 * Created by Kalac on 2019/2/2
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Binder;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import cn.kalac.easymediaplayer.MediaManager;
import cn.kalac.easymediaplayer.MediaOperator;


public class MusicBinder extends Binder {


    private final Context mContext;

    private MediaManager mMediaManager;

    private MusicStatusReceiver mMusicStatusReceiver;
    /**
     * 音乐播放状态枚举类
     */
    public enum MusicStatue{
        STOP,PLAY,PAUSE;
    }
    MusicStatue mMusicStatue = MusicStatue.STOP;

    public MusicBinder(Context context, MediaManager mediaManager) {
        mContext = context;
        mMediaManager = mediaManager;
        registerMusicStatusReciver();
    }


    public void setOnBufferingUpdateListener(final OnBufferingUpdateListener onBufferingUpdateListener){
//        mMediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
//            @Override
//            public void onBufferingUpdate(MediaPlayer mp, int percent) {
//                onBufferingUpdateListener.onBufferingUpdate(percent);
//            }
//        });
    }

    /**
     * 获取播放时长
     * @return
     */
    public int getCurrentPosition(){
        return mMediaManager.getCurrentPosition();
    }

    /**
     * seekto
     * @param progress
     */
    public void seekto(int progress) {
        mMediaManager.seekTo(progress);
    }

    public boolean isStart() {
        return mMusicStatue != MusicStatue.STOP;
    }

    /**
     * 获取歌曲时长
     * @return
     */
    public int getDuration() {
        return mMediaManager.getDuration();
    }
    public interface OnBufferingUpdateListener{
        void onBufferingUpdate(int percent);
    }

    /**
     * 解除注册
     */
    public void unRegisterMusicStatusReciver() {
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mMusicStatusReceiver);
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

        LocalBroadcastManager.getInstance(mContext).registerReceiver(mMusicStatusReceiver,intentFilter);
    }

    /**
     * 用于接收状态改变的广播接收者
     */
    class MusicStatusReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case PlayMusicService.ACTION_STATUS_MUSIC_PLAY:
                    mMusicStatue = MusicStatue.PLAY;
                    break;
                case PlayMusicService.ACTION_STATUS_MUSIC_PAUSE:
                    mMusicStatue = MusicStatue.PAUSE;
                    break;
                case PlayMusicService.ACTION_STATUS_MUSIC_PREPARE_COMPLETE:

                    break;
            }
        }
    }
}