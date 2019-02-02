package cn.kalac.hearing.service;

/*
 * Created by Kalac on 2019/2/2
 */

import android.media.MediaPlayer;
import android.os.Binder;

import java.io.IOException;


public class MusicBinder extends Binder {

    private final MediaPlayer mMediaPlayer;

    public MusicBinder() {
        //初始化MediaPlayer
        mMediaPlayer = new MediaPlayer();

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
                    mMediaPlayer.start();
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
        }
    }

    /**
     * 暂停播放
     */
    public void pause() {
        if (mMediaPlayer.isPlaying()) {
            //如果还没开始播放，就开始
            mMediaPlayer.pause();
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


    public void setOnCompletionListener(final OnCompletionListener onCompletionListener){
        this.mOnCompletionListener = onCompletionListener;
    }

    public OnCompletionListener mOnCompletionListener = null;

    public interface OnCompletionListener{
        void onCompletion();
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