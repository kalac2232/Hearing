package cn.kalac.hearing.service;

/*
 * Created by Kalac on 2019/2/2
 */

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Binder;


public class MusicBinder extends Binder {


    private final Context mContext;

    MediaPlayer mMediaPlayer;

    public MusicBinder(Context context, MediaPlayer mediaPlayer) {
        mContext = context;
        mMediaPlayer = mediaPlayer;

    }


    public void setOnBufferingUpdateListener(final OnBufferingUpdateListener onBufferingUpdateListener){
        mMediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                onBufferingUpdateListener.onBufferingUpdate(percent);
            }
        });
    }

    /**
     * 获取播放时长
     * @return
     */
    public int getCurrentPosition(){
        return mMediaPlayer.getCurrentPosition();
    }

    /**
     * seekto
     * @param progress
     */
    public void seekto(int progress) {
        mMediaPlayer.seekTo(progress);
    }

    public interface OnBufferingUpdateListener{
        void onBufferingUpdate(int percent);
    }

}