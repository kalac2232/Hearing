package cn.kalac.hearing;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import cn.kalac.hearing.javabean.song.Song;
import cn.kalac.hearing.net.HttpHelper;
import cn.kalac.hearing.net.OkHttpProcessor;


/*
 * Created by Kalac on 2019/2/1
 */

public class HearingApplication extends Application {
    public static List<Song> mPlayingSongList = new ArrayList<>();
    public static int mCurrentPlayPos = -1;
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化httpHelper 使用okhttp完成网络请求
        HttpHelper.init(new OkHttpProcessor(this));
    }
}
