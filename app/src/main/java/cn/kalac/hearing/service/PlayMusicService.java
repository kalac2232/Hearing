package cn.kalac.hearing.service;

import android.animation.ValueAnimator;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;

import cn.kalac.hearing.api.ApiHelper;
import cn.kalac.hearing.javabean.local.MusicBean;
import cn.kalac.hearing.javabean.net.song.SongMp3ResultBean;
import cn.kalac.hearing.net.HttpCallback;
import cn.kalac.hearing.net.HttpHelper;
import cn.kalac.hearing.widget.PlayListManager;


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
    public static final String ACTION_STATUS_MUSIC_PREPARE_COMPLETE = "ACTION_STATUS_MUSIC_PREPARE_COMPLETE";
    public static final String ACTION_STATUS_MUSIC_PLAY_COMPLETE = "ACTION_STATUS_MUSIC_PLAY_COMPLETE";

    /*播放规则*/


    //private MusicBinder mBinder ;
    //初始化MediaPlayer
    private MediaPlayer mMediaPlayer = new MediaPlayer();

    private MusicOperaReceiver mMusicOperaReceiver = new MusicOperaReceiver();

    private boolean mIsMusicPause = false;


    private Context mContext;

    private MusicBinder mMusicBinder;


    @Override
    public void onCreate() {
        super.onCreate();
        initBoardCastReceiver();
        mContext = this;
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //next();
                //切换下一首歌的逻辑放到next中，而next具体是循环播放还是怎么播放不在完成监听中控制，在next中控制
                //而next只在activity中调用
                sendLocalBroadcast(ACTION_STATUS_MUSIC_PLAY_COMPLETE);
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

        if (PlayListManager.getInstance().isEmpty()) {
            return;
        }

        Log.i(TAG, "play: ");
        //如果正在播放，无须处理本次操作
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            Log.i(TAG, "play: 1");
            sendLocalBroadcast(ACTION_STATUS_MUSIC_PLAY);
            return;
        }
        //如果当前为暂停状态，直接开始播放即可
        if (mIsMusicPause) {
            Log.i(TAG, "play: 2");
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
        } else { //从未开始的状态
            Log.i(TAG, "play: 3");
            //获取当前要播放的songid
            MusicBean music = PlayListManager.getInstance().getCurrentMusic();
            int songid = music.getId();
            //加载MP3
            loadSongMp3(songid);
            //如果当前记录的播放id和要播放的id相同，说明是之前已经播放过得，但是暂停了，只需要直接调用start即可
//            if (mCurrentSongID == songid) {
//                mMediaPlayer.start();
//                sendLocalBroadcast(ACTION_STATUS_MUSIC_PLAY);
//            } else {
//                //记录当前播放的songId
//                mCurrentSongID = songid;
//                //加载MP3
//                loadSongMp3(songid);
//            }

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

        PlayListManager.getInstance().next();
        //获取当前要播放的songid
        MusicBean music = PlayListManager.getInstance().getCurrentMusic();

        loadSongMp3(music.getId());

    }

    private void prev() {
        Log.i(TAG, "prev: ");

        mMediaPlayer.reset();

        sendLocalBroadcast(ACTION_STATUS_MUSIC_PAUSE);

        PlayListManager.getInstance().pre();
        //获取当前要播放的songid
        MusicBean music = PlayListManager.getInstance().getCurrentMusic();

        loadSongMp3(music.getId());
    }

    /**
     * 加载mp3
     * @param songId 歌曲id
     */
    private void loadSongMp3(int songId) {

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
                        mMediaPlayer.reset();
                        mMediaPlayer.setDataSource(mp3Url);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //异步准备
                    mMediaPlayer.prepareAsync();
                    mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            Log.i(TAG, "onCompletion: 加载音乐完成，开始播放");
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
                            sendLocalBroadcast(ACTION_STATUS_MUSIC_PREPARE_COMPLETE);
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
