package cn.kalac.hearing.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
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

import cn.kalac.hearing.HearingApplication;
import cn.kalac.hearing.R;
import cn.kalac.hearing.api.ApiHelper;
import cn.kalac.hearing.javabean.song.Song;
import cn.kalac.hearing.javabean.song.SongDetailResultBean;
import cn.kalac.hearing.net.HttpCallback;
import cn.kalac.hearing.net.HttpHelper;
import cn.kalac.hearing.service.MusicBinder;
import cn.kalac.hearing.service.PlayMusicService;
import jp.wasabeef.glide.transformations.BlurTransformation;


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

    private boolean isMusicPlaying = false;
    private View mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //播放音乐
        sendLocalBroadcast(PlayMusicService.ACTION_OPT_MUSIC_PLAY);

    }

    @Override
    public boolean registerReciver() {
        return true;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_playmusic;
    }


    protected void initView() {
        //标题
        mTitle = findViewById(R.id.ll_playmusic_title);
        //封面背景
        mBgCoverIV = findViewById(R.id.iv_playmusic_cover_bg);
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
                if (isMusicPlaying) {
                    sendLocalBroadcast(PlayMusicService.ACTION_OPT_MUSIC_PAUSE);
                } else {
                    sendLocalBroadcast(PlayMusicService.ACTION_OPT_MUSIC_PLAY);
                }
            }
        });

        mPlayProgressSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //如果是人主动的拖拽
                if (fromUser){
                    mMusicBinder.seekto(progress);
                    //刷新当前时间显示

                    //转换时间，更新时间
                    String currentTime = time.format(progress);
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

        mNextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //下一首
                sendLocalBroadcast(PlayMusicService.ACTION_OPT_MUSIC_NEXT);

            }
        });
    }

    @Override
    protected void initData() {

        Intent intent = new Intent(this, PlayMusicService.class);
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);

    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            mMusicBinder = (MusicBinder) service;
            //通过binder设置缓冲监听
            mMusicBinder.setOnBufferingUpdateListener(new MusicBinder.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(int percent) {
                    mPlayProgressSB.setSecondaryProgress(percent / 100 * mPlayProgressSB.getMax());
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };



    /**
     * 显示获取到的面，歌曲名，歌手信息
     * @param song 歌曲
     */
    private void showSongDetail(Song song) {
        //获取歌曲名称
        String name = song.getSongName();
        mSongName.setText(name);
        //获取歌曲作者
        String author = song.getSingerName();
        mSongAuthor.setText(author);
        //获取封面地址
        String picUrl = song.getPicUrl();
        //设置gilde的设置
        RequestOptions requestOptions = new RequestOptions()
                .transform(new BlurTransformation(25,3))//设置模糊效果
                .placeholder(R.drawable.ic_playmusic_bg_default)//图片加载出来前，显示的图片
                .fallback( R.drawable.ic_playmusic_bg_default) //url为空的时候,显示的图片
                .error(R.drawable.ic_playmusic_bg_default);//图片加载失败后，显示的图片


        //Glide.with(mContext).load(picUrl).apply(options).into(mCoverIV);

        //设置背景封面
        Glide.with(mContext).load(picUrl).apply(requestOptions).into(mBgCoverIV);
    }

    /**
     * 歌曲状态转到了播放状态
     */
    @Override
    protected void musicStatusToPlay() {
        Log.i(TAG, "musicStatusToPlay: ");
        //更改图片
        mPlaybtn.setImageResource(R.drawable.ic_playing_bar_pause_selector);
        //更改状态
        isMusicPlaying = true;

        //开始刷新时间
        mHandler.postDelayed(mReFreshTime,1000);

    }

    @Override
    protected void musicStatusToPause() {
        Log.i(TAG, "musicStatusToPause: ");
        //更改图片
        mPlaybtn.setImageResource(R.drawable.ic_playing_bar_play_selector);

        isMusicPlaying = false;

        //停止刷新时间
        mHandler.removeCallbacks(mReFreshTime);
    }

    @Override
    protected void musicComplete(int duration) {
        Log.i(TAG, "musicComplete: ");
        //显示标题栏
        mTitle.setVisibility(View.VISIBLE);
        //获取当前播放的歌曲id
        Song song = HearingApplication.mPlayingSongList.get(HearingApplication.mCurrentPlayPos);
        //设置歌曲详情
        showSongDetail(song);
        //设置seekbar总时长
        mPlayProgressSB.setMax(duration);
        //初始化seekbar状态
        mPlayProgressSB.setProgress(0);
        //转换时间，更新时间
        String totalTime = time.format(duration);
        mTotalTimeTV.setText(totalTime);


    }

    /**
     * 用于刷新当前运行时间
     */
    Runnable mReFreshTime = new Runnable(){

        @Override
        public void run() {
            mHandler.postDelayed(this,1000);
            //获取当前播放的时长
            int playPosition = mMusicBinder.getCurrentPosition();
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


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }

}
