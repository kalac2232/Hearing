package cn.kalac.hearing.service;

import android.animation.ValueAnimator;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;

import cn.kalac.hearing.HearingApplication;
import cn.kalac.hearing.api.ApiHelper;
import cn.kalac.hearing.javabean.song.Song;
import cn.kalac.hearing.javabean.song.SongMp3ResultBean;
import cn.kalac.hearing.net.HttpCallback;
import cn.kalac.hearing.net.HttpHelper;


/*
 * Created by Kalac on 2019/2/2
 */

public class PlayMusicService extends Service {

    private static final String TAG = "PlayMusicService";

    /*操作指令*/
    public static final String ACTION_OPT_MUSIC_PLAY = "ACTION_OPT_MUSIC_PLAY";
    public static final String ACTION_OPT_MUSIC_PAUSE = "ACTION_OPT_MUSIC_PAUSE";
    public static final String ACTION_OPT_MUSIC_NEXT = "ACTION_OPT_MUSIC_NEXT";
    public static final String ACTION_OPT_MUSIC_PREV = "ACTION_OPT_MUSIC_PREV";

    /*状态指令*/
    public static final String ACTION_STATUS_MUSIC_PLAY = "ACTION_STATUS_MUSIC_PLAY";
    public static final String ACTION_STATUS_MUSIC_PAUSE = "ACTION_STATUS_MUSIC_PAUSE";
    public static final String ACTION_STATUS_MUSIC_COMPLETE = "ACTION_STATUS_MUSIC_COMPLETE";


    //private MusicBinder mBinder ;
    //初始化MediaPlayer
    private MediaPlayer mMediaPlayer = new MediaPlayer();

    private MusicOperaReceiver mMusicOperaReceiver = new MusicOperaReceiver();

    private boolean mIsMusicPause = false;
    /**
     * 当前歌曲是否处于加载中，防止多次加载
     */
    private boolean mIsPreparing = false;

    private Context mContext;
    private int mCurrentSongID = -1;
    private MusicBinder mMusicBinder;

    @Override
    public void onCreate() {
        super.onCreate();
        initBoardCastReceiver();
        mContext = this;
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                next();
            }
        });
    }


    @Override
    public IBinder onBind(Intent intent) {
        mMusicBinder = new MusicBinder(mContext, mMediaPlayer);
        return mMusicBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 初始化接受操作指令的广播接受者
     */
    private void initBoardCastReceiver() {
        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(ACTION_OPT_MUSIC_PLAY);
        intentFilter.addAction(ACTION_OPT_MUSIC_PAUSE);
        intentFilter.addAction(ACTION_OPT_MUSIC_NEXT);
        intentFilter.addAction(ACTION_OPT_MUSIC_PREV);

        LocalBroadcastManager.getInstance(this).registerReceiver(mMusicOperaReceiver,intentFilter);
    }


    private void play() {
        Log.i(TAG, "play: ");
        //如果当前为暂停状态，直接开始播放即可
        if (mIsMusicPause) {
            //使声音有渐变效果
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0,1);
            valueAnimator.setDuration(1000);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float values = (float) animation.getAnimatedValue();

                    mMediaPlayer.setVolume(values,values);

                }
            });
            valueAnimator.start();
            mMediaPlayer.start();
            sendLocalBroadcast(ACTION_STATUS_MUSIC_PLAY);
            mIsMusicPause = false;
        } else {
            //获取当前要播放的songid
            Song song = HearingApplication.mPlayingSongList.get(HearingApplication.mCurrentPlayPos);
            int songid = song.getSongId();
            //如果当前记录的播放id和要播放的id相同，说明是之前已经播放过得，但是暂停了，只需要直接调用start即可
            if (mCurrentSongID == songid) {
                mMediaPlayer.start();
                sendLocalBroadcast(ACTION_STATUS_MUSIC_PLAY);
            } else {
                //记录当前播放的songId
                mCurrentSongID = songid;
                //加载MP3
                loadSongMp3(songid);
            }

        }


    }

    private void pause() {
        Log.i(TAG, "pause: ");
        if (mMediaPlayer == null) {
            return;
        }
        //使声音有渐变效果
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1,0);
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float values = (float) animation.getAnimatedValue();
                mMediaPlayer.setVolume(values,values);
                if (values == 0f) {
                    mMediaPlayer.pause();
                    mIsMusicPause = true;
                    //发送暂停状态

                }
            }
        });
        valueAnimator.start();
        sendLocalBroadcast(ACTION_STATUS_MUSIC_PAUSE);
    }

    private void next() {
        Log.i(TAG, "next: ");
        mMediaPlayer.reset();
        //
        sendLocalBroadcast(ACTION_STATUS_MUSIC_PAUSE);

        HearingApplication.mCurrentPlayPos ++;
        //获取当前要播放的songid
        Song song = HearingApplication.mPlayingSongList.get(HearingApplication.mCurrentPlayPos);

        loadSongMp3(song.getSongId());

    }

    private void prev() {
        Log.i(TAG, "prev: ");

        mMediaPlayer.reset();

        sendLocalBroadcast(ACTION_STATUS_MUSIC_PAUSE);

        HearingApplication.mCurrentPlayPos --;
        //获取当前要播放的songid
        Song song = HearingApplication.mPlayingSongList.get(HearingApplication.mCurrentPlayPos);

        loadSongMp3(song.getSongId());
    }

    /**
     * 加载mp3
     * @param songId 歌曲id
     */
    private void loadSongMp3(int songId) {
        //判断是否当前是否为加载中，加载中其实也是处于暂停状态，再次点击播放会导致崩溃
        if (mIsPreparing) {
            return;
        }
        String url = ApiHelper.getSongMp3Url(songId);

        HttpHelper.getInstance().get(url, new HttpCallback<SongMp3ResultBean>() {

            @Override
            public void onSuccess(SongMp3ResultBean songMp3ResultBean) {
                int code = songMp3ResultBean.getCode();
                if (code == 200) {
                    SongMp3ResultBean.DataBean dataBean = songMp3ResultBean.getData().get(0);
                    //判断是否成功获取到了mp3地址 (有可能出现这个歌曲没有版权而导致无法获取到地址)
                    if (dataBean.getCode() != 200) {
                        return;
                    }
                    String mp3Url = dataBean.getUrl();
                    try {
                        mMediaPlayer.setDataSource(mp3Url);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //异步准备
                    mMediaPlayer.prepareAsync();
                    //正在加载歌曲
                    mIsPreparing = true;
                    mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            Log.i(TAG, "onCompletion: 加载音乐完成，开始播放");
                            mIsPreparing = false;
                            //使声音有渐变效果
                            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0,1);
                            valueAnimator.setDuration(1000);
                            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    float values = (float) animation.getAnimatedValue();

                                    mMediaPlayer.setVolume(values,values);

                                }
                            });
                            valueAnimator.start();
                            mp.start();
                            //发送状态改变广播
                            sendLocalBroadcast(ACTION_STATUS_MUSIC_PLAY);
                            //完成状态是为了其他页展示出相关信息
                            sendLocalBroadcast(ACTION_STATUS_MUSIC_COMPLETE);
                        }

                    });


                }
            }

            @Override
            public void onFailed(String string) {
                Log.i(TAG, "onFailed: "+string);
            }
        });

    }
    @Override
    public boolean onUnbind(Intent intent) {
        mMusicBinder.unRegisterMusicStatusReciver();
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void sendLocalBroadcast(String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

    /**
     * 音乐操作指令广播接收者
     */
    class MusicOperaReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case ACTION_OPT_MUSIC_PLAY:
                    play();
                    break;
                case ACTION_OPT_MUSIC_PAUSE:
                    pause();
                    break;
                case ACTION_OPT_MUSIC_PREV:
                    prev();
                    break;
                case ACTION_OPT_MUSIC_NEXT:
                    next();
                    break;

            }

        }
    }

}
