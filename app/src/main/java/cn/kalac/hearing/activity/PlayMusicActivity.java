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

import cn.kalac.hearing.R;
import cn.kalac.hearing.javabean.local.Song;
import cn.kalac.hearing.service.MusicBinder;
import cn.kalac.hearing.service.PlayMusicService;
import cn.kalac.hearing.view.RecordView;
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
    private View mBackBtn;
    private RecordView mRecordView;
    private View mPrevbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //播放音乐
        sendLocalBroadcast(PlayMusicService.ACTION_OPT_MUSIC_PLAY);

    }

    @Override
    public boolean bindMusicReceiver() {
        return true;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_playmusic;
    }


    @Override
    protected void initView() {
        //返回按钮
        mBackBtn = findViewById(R.id.ib_goback_btn);
        //标题
        mTitle = findViewById(R.id.ll_playmusic_title);
        //封面背景
        mBgCoverIV = findViewById(R.id.iv_playmusic_cover_bg);
        //唱片
        mRecordView = findViewById(R.id.Rv_playmusic_RecordView);
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
        //上一首
        mPrevbtn = findViewById(R.id.iv_playmusic_prevbtn);

    }

    @Override
    protected void addListener() {
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
                //因为再RecordView的回调中已经完成了发送广播的事件，在这发送回重复
                //sendLocalBroadcast(PlayMusicService.ACTION_OPT_MUSIC_NEXT);
                //使唱片向下一个滑
                mRecordView.nextMusic();
                //将当前进度置为初始值
                String currentTime = time.format(0);
                mCurrentTimeTV.setText(currentTime);

            }
        });
        mPrevbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //使唱片向上一个滑
                mRecordView.prevMusic();
                //将当前进度置为初始值
                String currentTime = time.format(0);
                mCurrentTimeTV.setText(currentTime);

            }
        });
        mRecordView.setOnSrollListener(new RecordView.onSrollListener() {
            @Override
            public void next() {
                //发送广播
                sendLocalBroadcast(PlayMusicService.ACTION_OPT_MUSIC_NEXT);
            }

            @Override
            public void prev() {
                //发送广播
                sendLocalBroadcast(PlayMusicService.ACTION_OPT_MUSIC_PREV);
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
            //如果进入播放页的时候已经开始了播放，显示封面，歌曲等信息
            if (mMusicBinder.isStart()){
                musicPrepareComplete();
            }
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
        //使唱片的动画开始播放
        mRecordView.play();
    }

    @Override
    protected void musicStatusToPause() {
        Log.i(TAG, "musicStatusToPause: ");
        //更改图片
        mPlaybtn.setImageResource(R.drawable.ic_playing_bar_play_selector);

        isMusicPlaying = false;

        //停止刷新时间
        mHandler.removeCallbacks(mReFreshTime);
        //使唱片的动画暂停
        mRecordView.pause();

    }

    @Override
    protected void musicPrepareComplete() {
        Log.i(TAG, "musicComplete: ");
        //显示标题栏
        mTitle.setVisibility(View.VISIBLE);
        //获取当前播放的歌曲id
        Song song = PlayMusicService.mPlayingSongList.get(PlayMusicService.mCurrentPlayPos);
        //设置歌曲详情
        showSongDetail(song);
        //设置seekbar总时长
        int duration = mMusicBinder.getDuration();
        mPlayProgressSB.setMax(duration);
        //初始化seekbar状态
        int currentPosition = mMusicBinder.getCurrentPosition();
        mPlayProgressSB.setProgress(currentPosition);
        //转换时间，更新时间
        String totalTime = time.format(duration);
        mTotalTimeTV.setText(totalTime);


    }

    /**
     * 歌曲播放完成
     */
    @Override
    protected void musicPlayComplete() {
        Log.i(TAG, "musicPlayComplete: ");
        mRecordView.nextMusic();
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
