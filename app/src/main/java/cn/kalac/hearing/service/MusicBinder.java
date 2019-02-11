package cn.kalac.hearing.service;

/*
 * Created by Kalac on 2019/2/2
 */

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Application;
import android.content.Context;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import java.io.IOException;


public class MusicBinder extends Binder {

    private final MediaPlayer mMediaPlayer;
    private final WifiManager.WifiLock wifiLock;
    private final Context mContext;

    public MusicBinder(Context context) {
        mContext = context;
        //初始化MediaPlayer
        mMediaPlayer = new MediaPlayer();
        addListener();
        // 设置设备进入锁状态模式-可在后台播放或者缓冲音乐-CPU一直工作
        mMediaPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);
        // 如果你使用wifi播放流媒体，你还需要持有wifi锁
        wifiLock = ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).createWifiLock(WifiManager.WIFI_MODE_FULL, "wifilock");
        wifiLock.acquire();

    }
    private void addListener(){
        //播放错误
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });
        //播放完成
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                nextMusic();
            }
        });
        //处理音频焦点变化
        AudioManager.OnAudioFocusChangeListener focusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                switch (focusChange) {
                    case AudioManager.AUDIOFOCUS_GAIN:
                        // 获取audio focus
                        if (!mMediaPlayer.isPlaying())
                            mMediaPlayer.start();
                        mMediaPlayer.setVolume(1.0f, 1.0f);
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS:
                        // 失去audio focus很长一段时间，必须停止所有的audio播放，清理资源
                        if (mMediaPlayer.isPlaying())
                            mMediaPlayer.stop();
                        closeMedia();
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                        // 暂时失去audio focus，但是很快就会重新获得，在此状态应该暂停所有音频播放，但是不能清除资源
                        if (mMediaPlayer.isPlaying())
                            mMediaPlayer.pause();
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                        // 暂时失去 audio focus，但是允许持续播放音频(以很小的声音)，不需要完全停止播放。
                        if (mMediaPlayer.isPlaying())
                            mMediaPlayer.setVolume(0.1f, 0.1f);
                        break;
                }
            }
        };

        // 处理音频焦点-处理多个程序会来竞争音频输出设备
        AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 征对于Android 8.0+
            AudioFocusRequest audioFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                    .setOnAudioFocusChangeListener(focusChangeListener).build();
            audioFocusRequest.acceptsDelayedFocusGain();
            audioManager.requestAudioFocus(audioFocusRequest);
        } else {
            // 小于Android 8.0
            int result = audioManager.requestAudioFocus(focusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
            if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                // could not get audio focus.
            }
        }
    }
    /**
     * 设置音乐资源
     * @param path
     */
    public void setDataSource(String path){
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 准备
     */
    public void prepare(){
        try {
            mMediaPlayer.prepare();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 异步准备并且自动播放
     */
    public void AutoPlay(){
        mMediaPlayer.prepareAsync();
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (mOnPreparedListener != null) {
                    mOnPreparedListener.onPrepared();
                    start();
                }
            }
        });

    }
    /**
     * 播放音乐
     */
    public void start() {
        if (!mMediaPlayer.isPlaying()) {
            //如果还没开始播放，就开始
            mMediaPlayer.start();
            //使声音有渐变效果
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0,1);
            valueAnimator.setDuration(1500);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float values = (float) animation.getAnimatedValue();

                    mMediaPlayer.setVolume(values,values);

                }
            });
            valueAnimator.start();

        }
    }

    /**
     * 暂停播放
     */
    public void pause() {
        if (mMediaPlayer.isPlaying()) {
            //如果开始播放，就停止

            //使声音有渐变效果
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(1,0);
            valueAnimator.setDuration(1500);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float values = (float) animation.getAnimatedValue();
                    Log.i("123", "onAnimationUpdate: "+values);
                    mMediaPlayer.setVolume(values,values);
                    if (values == 0f) {
                        mMediaPlayer.pause();
                    }
                }
            });
            valueAnimator.start();

        }
    }
    public boolean isPlaying(){
        return mMediaPlayer.isPlaying();
    }
    /**
     * 下一首
     */
    public void nextMusic() {

    }

    /**
     * 上一首
     */
    public void preciousMusic() {

    }


    /**
     * 关闭播放器
     */
    public void closeMedia() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
        wifiLock.release();
    }
    /**
     * 获取歌曲长度
     **/
    public int getDuration() {

        return mMediaPlayer.getDuration();
    }

    /**
     * 获取播放位置
     */
    public int getPlayPosition() {

        return mMediaPlayer.getCurrentPosition();
    }
    /**
     * 播放指定位置
     */
    public void seekToPositon(int msec) {
        mMediaPlayer.seekTo(msec);
    }




    /**
     * 准备完成监听
     * @param onPreparedListener
     */

    public void setPreparedListener(final OnPreparedListener onPreparedListener){
        this.mOnPreparedListener = onPreparedListener;
    }

    public OnPreparedListener mOnPreparedListener = null;

    public interface OnPreparedListener{
        void onPrepared();
    }

}