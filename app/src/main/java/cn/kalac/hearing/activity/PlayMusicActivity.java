package cn.kalac.hearing.activity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;

import cn.kalac.hearing.R;
import cn.kalac.hearing.api.ApiHelper;
import cn.kalac.hearing.javabean.song.SongDetailBean;
import cn.kalac.hearing.javabean.song.SongMp3Bean;
import cn.kalac.hearing.net.HttpCallback;
import cn.kalac.hearing.net.HttpHelper;


/*
 * 播放音乐activity
 * Created by Kalac on 2019/1/29
 */

public class PlayMusicActivity extends BaseActivity {

    private static final String TAG = "PlayMusicActivity";
    private ImageView mBgCoverIV;

    private ImageView mCoverIV;
    private TextView mCurrentTimeTV;
    private TextView mTotalTimeTV;
    private SeekBar mPlayProgressSB;
    private ImageView mPlaybtn;
    private TextView mSongName;
    private TextView mSongAuthor;
    private MediaPlayer mMediaPlayer;

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
        mCoverIV = findViewById(R.id.iv_playmusic_cover);
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

    }

    @Override
    protected void addListener() {
        mPlaybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMediaPlayer != null) {
                    //如果正在播放
                    if (mMediaPlayer.isPlaying()) {
                        Log.i(TAG, "mMediaPlayer pause: ");
                        mMediaPlayer.pause();
                        mPlaybtn.setImageResource(R.drawable.ic_playing_bar_play);
                    } else {
                        Log.i(TAG, "mMediaPlayer start: ");
                        mMediaPlayer.start();
                        mPlaybtn.setImageResource(R.drawable.ic_playing_bar_pause);
                    }
                } else {
                    Log.i(TAG, "mMediaPlayer null: ");
                }
            }
        });
        mMediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                Log.i(TAG, "onBufferingUpdate: percent"+percent);
                mPlayProgressSB.setSecondaryProgress(percent);
            }
        });

    }

    @Override
    protected void initData() {
        //创建MediaPlayer对象
        mMediaPlayer = new MediaPlayer();
        Log.i(TAG, "mMediaPlayer Create Success: ");
        //获取歌曲详情
        loadSongDetail(108401);
        //获取歌曲
        loadSongMp3(108401);

    }

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
     * @param songId
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

                    try {
                        mMediaPlayer.setDataSource(mp3Url);
                        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置媒体流类型
                        mMediaPlayer.prepare();
                        Log.i(TAG, "mMediaPlayer setDataSource Success: ");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

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
     * @param songsBean
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
                .fallback( R.drawable.ic_playing_bar_play) //url为空的时候,显示的图片
                .error(R.drawable.testfenmian);//图片加载失败后，显示的图片


        Glide.with(mContext).load(picUrl).apply(options).into(mCoverIV);
        //设置背景封面
        Glide.with(mContext).load(picUrl).into(mBgCoverIV);
    }
}
