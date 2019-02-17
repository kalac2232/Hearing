package cn.kalac.hearing.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.request.RequestOptions;

import java.text.SimpleDateFormat;

import cn.kalac.hearing.HearingApplication;
import cn.kalac.hearing.R;
import cn.kalac.hearing.api.ApiHelper;
import cn.kalac.hearing.javabean.song.SongDetailBean;
import cn.kalac.hearing.net.HttpCallback;
import cn.kalac.hearing.net.HttpHelper;
import cn.kalac.hearing.service.MusicBinder;
import cn.kalac.hearing.service.PlayMusicService;
import cn.kalac.hearing.utils.DisplayUtil;
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

        mNextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendLocalBroadcast(PlayMusicService.ACTION_OPT_MUSIC_NEXT);
                //获取歌曲详情
                //loadSongDetail(108404);

            }
        });
    }

    @Override
    protected void initData() {

        //Intent intent = new Intent(this, PlayMusicService.class);
        //bindService(intent, mServiceConnection, BIND_AUTO_CREATE);

    }



    /**
     * 通过歌曲id获取封面，歌曲名，歌手信息
     * @param songId id
     */
    private void loadSongDetail(int songId) {
        String url = ApiHelper.getSongDetailUrl(songId);

        HttpHelper.getInstance().get(url, new HttpCallback<SongDetailBean>() {

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
        //设置模糊效果
        RequestOptions requestOptions = new RequestOptions().transform(
                new BlurTransformation(25,3));
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



    }

    @Override
    protected void musicStatusToPause() {
        Log.i(TAG, "musicStatusToPause: ");
        //更改图片
        mPlaybtn.setImageResource(R.drawable.ic_playing_bar_play_selector);

        isMusicPlaying = false;
    }

    @Override
    protected void musicComplete() {
        Log.i(TAG, "musicComplete: ");
        //获取当前播放的歌曲id
        Integer songId = HearingApplication.mPlayingSongList.get(HearingApplication.mCurrentPlayPos);
        //获取歌曲详情
        loadSongDetail(songId);

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

    }

}
