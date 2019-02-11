package cn.kalac.hearing.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.SimpleDateFormat;

import cn.kalac.hearing.R;
import cn.kalac.hearing.api.ApiHelper;
import cn.kalac.hearing.javabean.song.SongDetailBean;
import cn.kalac.hearing.javabean.song.SongMp3Bean;
import cn.kalac.hearing.net.HttpCallback;
import cn.kalac.hearing.net.HttpHelper;
import cn.kalac.hearing.service.MusicBinder;
import cn.kalac.hearing.service.PlayMusicService;


/*
 * 播放音乐activity
 * Created by Kalac on 2019/1/29
 */

public class PlayMusicActivity extends BaseActivity {

    private static final String TAG = "PlayMusicActivity";
    private ImageView mBgCoverIV;

    //private ImageView mCoverIV;
    private TextView mCurrentTimeTV;
    private TextView mTotalTimeTV;
    private SeekBar mPlayProgressSB;
    private ImageView mPlaybtn;
    private TextView mSongName;
    private TextView mSongAuthor;

    private MusicBinder mMusicBinder;

    //进度条下面的当前进度文字，将毫秒化为mm:ss格式
    private SimpleDateFormat time = new SimpleDateFormat("mm:ss");

    Handler mHandler = new Handler();
    private View mNextbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_playmusic;
    }


    protected void initView() {
        //封面背景
        mBgCoverIV = findViewById(R.id.iv_playmusic_cover_bg);
        //封面
        //mCoverIV = findViewById(R.id.iv_playmusic_cover);
        //音乐名称
        mSongName = findViewById(R.id.tv_playmusic_songName);
        //音乐作者
        mSongAuthor = findViewById(R.id.tv_playmusic_songAuthor);
        //当前音乐时间
        mCurrentTimeTV = findViewById(R.id.tv_playmusic_currentTime);
        //总音乐时间
        mTotalTimeTV = findViewById(R.id.tv_playmusic_totalTime);
        //音乐进度条seekbar
        mPlayProgressSB = findViewById(R.id.sb_playmusic_playProgress);
        //音乐播放按钮
        mPlaybtn = findViewById(R.id.iv_playmusic_playbtn);
        //下一首
        mNextbtn = findViewById(R.id.iv_playmusic_nextbtn);

    }

    @Override
    protected void addListener() {
        mPlaybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //如果正在播放
                if (mMusicBinder.isPlaying()) {
                    //暂停播放
                    mMusicBinder.pause();
                    //更换图标
                    mPlaybtn.setImageResource(R.drawable.ic_playing_bar_play_selector);
                    //暂停刷新时间
                    mHandler.removeCallbacks(mReFreshTime);
                } else {
                    //开始播放
                    mMusicBinder.start();
                    //更换图标
                    mPlaybtn.setImageResource(R.drawable.ic_playing_bar_pause_selector);
                    //开始刷新
                    mHandler.postDelayed(mReFreshTime,1000);
                }
            }
        });
        /*
        mPlayProgressSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //如果是人主动的拖拽
                if (fromUser){
                    mMusicBinder.seekToPositon(progress);
                    //刷新当前时间显示
                    //获取当前播放的时长
                    int playPosition = mMusicBinder.getPlayPosition();
                    //转换时间，更新时间
                    String currentTime = time.format(playPosition);
                    mCurrentTimeTV.setText(currentTime);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        */
        mNextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlaybtn.setImageResource(R.drawable.ic_playing_bar_play_selector);
                //获取歌曲详情
                loadSongDetail(108402);
                //获取歌曲
                loadSongMp3(108402);
            }
        });
    }

    @Override
    protected void initData() {

//        Intent intent = new Intent(this, PlayMusicService.class);
//        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
//        //获取歌曲详情
//        loadSongDetail(108401);
//        //获取歌曲
//        loadSongMp3(108401);


    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            mMusicBinder = (MusicBinder) service;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    /**
     * 通过歌曲id获取封面，歌曲名，歌手信息
     * @param songId id
     */
    private void loadSongDetail(int songId) {
        String url = ApiHelper.getSongDetail(songId);

        HttpHelper.getInstance().get(url, null, new HttpCallback<SongDetailBean>() {

            @Override
            public void onSuccess(SongDetailBean songDetailBean) {
                int code = songDetailBean.getCode();
                if (code == 200) {
                    SongDetailBean.SongsBean songsBean = songDetailBean.getSongs().get(0);
                    showSongInfo(songsBean);

                }
            }

            @Override
            public void onFailed(String string) {
                Log.i(TAG, "onFailed: "+string);
            }
        });
    }

    /**
     * 加载mp3
     * @param songId 歌曲id
     */
    private void loadSongMp3(int songId) {
        String url = ApiHelper.getSongMp3(songId);

        HttpHelper.getInstance().get(url, null, new HttpCallback<SongMp3Bean>() {

            @Override
            public void onSuccess(SongMp3Bean songMp3Bean) {
                int code = songMp3Bean.getCode();
                if (code == 200) {
                    SongMp3Bean.DataBean dataBean = songMp3Bean.getData().get(0);
                    String mp3Url = dataBean.getUrl();

                    mMusicBinder.setDataSource(mp3Url);
                    
                    mMusicBinder.setPreparedListener(new MusicBinder.OnPreparedListener() {
                        @Override
                        public void onPrepared() {
                            Log.i(TAG, "onPrepared: ");
                            int duration = mMusicBinder.getDuration();
                            //设置seekbar总大小
                            mPlayProgressSB.setMax(duration);
                            //设置歌曲总时长
                            String totalTime = time.format(duration);
                            mTotalTimeTV.setText(totalTime);
                            //开始刷新
                            mHandler.postDelayed(mReFreshTime,1000);
                            //更改图标
                            mPlaybtn.setImageResource(R.drawable.ic_playing_bar_pause_selector);
                        }
                    });

                    mMusicBinder.AutoPlay();

                }
            }

            @Override
            public void onFailed(String string) {
                Log.i(TAG, "onFailed: "+string);
            }
        });
    }

    /**
     * 显示获取到的面，歌曲名，歌手信息
     * @param songsBean 歌曲id
     */
    private void showSongInfo(SongDetailBean.SongsBean songsBean) {
        //获取歌曲名称
        String name = songsBean.getName();
        mSongName.setText(name);
        //获取歌曲作者
        String author = songsBean.getAr().get(0).getName();
        mSongAuthor.setText(author);
        //获取封面地址
        String picUrl = songsBean.getAl().getPicUrl();
        //设置封面
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_back_btn)//图片加载出来前，显示的图片
                .fallback( R.drawable.ic_playing_bar_play_selector) //url为空的时候,显示的图片
                .error(R.drawable.testfenmian);//图片加载失败后，显示的图片


        //Glide.with(mContext).load(picUrl).apply(options).into(mCoverIV);
        //设置背景封面
        Glide.with(mContext).load(picUrl).into(mBgCoverIV);
    }

    /**
     * 用于刷新当前运行时间
     */
    Runnable mReFreshTime = new Runnable(){

        @Override
        public void run() {
            mHandler.postDelayed(this,1000);
            //获取当前播放的时长
            int playPosition = mMusicBinder.getPlayPosition();
            //设置给seekbar
            mPlayProgressSB.setProgress(playPosition);
            //转换时间，更新时间
            String currentTime = time.format(playPosition);
            mCurrentTimeTV.setText(currentTime);

        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        if (mMusicBinder != null && mMusicBinder.isPlaying()) {
            //停止刷新
            mHandler.removeCallbacks(mReFreshTime);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMusicBinder != null && mMusicBinder.isPlaying()) {
            //开始刷新
            mHandler.postDelayed(mReFreshTime,1000);
            mPlaybtn.setImageResource(R.drawable.ic_playing_bar_pause_selector);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mReFreshTime);
        mMusicBinder.closeMedia();
        unbindService(mServiceConnection);
    }
}
