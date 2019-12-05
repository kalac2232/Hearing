package cn.kalac.hearing;

import android.app.Application;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.Arrays;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;
import cn.kalac.hearing.net.HttpHelper;
import cn.kalac.hearing.net.OkHttpProcessor;


/*
 * Created by Kalac on 2019/2/1
 */

public class HearingApplication extends Application {

    public static HearingApplication mApplication;
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化httpHelper 使用okhttp完成网络请求
        HttpHelper.init(new OkHttpProcessor(this));
        mApplication = this;
        /**
         * 必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回
         * 第一个参数：应用程序上下文
         * 第二个参数：如果发现滑动返回后立即触摸界面时应用崩溃，请把该界面里比较特殊的 View 的 class 添加到该集合中，目前在库中已经添加了 WebView 和 SurfaceView
         */
        BGASwipeBackHelper.init(this, null);

        Logger.addLogAdapter(new AndroidLogAdapter());


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
            ShortcutInfo shortcut1 = new ShortcutInfo.Builder(this, "id1")
                    .setShortLabel("Web site")
                    .setLongLabel("播放本地音乐")
                    .setIcon(Icon.createWithResource(this, R.mipmap.shortcut_local_music))
                    .setIntent(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.mysite.example.com/")))
                    .build();
            ShortcutInfo shortcut2 = new ShortcutInfo.Builder(this, "id2")
                    .setShortLabel("Web site")
                    .setLongLabel("搜索")
                    .setIcon(Icon.createWithResource(this, R.mipmap.shortcut_search))
                    .setIntent(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.mysite.example.com/")))
                    .build();
            ShortcutInfo shortcut3 = new ShortcutInfo.Builder(this, "id3")
                    .setShortLabel("Web site")
                    .setLongLabel("私人FM")
                    .setIcon(Icon.createWithResource(this, R.mipmap.shortcut_fm))
                    .setIntent(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.mysite.example.com/")))
                    .build();
            ShortcutInfo shortcut4 = new ShortcutInfo.Builder(this, "id4")
                    .setShortLabel("Web site")
                    .setLongLabel("听歌识曲")
                    .setIcon(Icon.createWithResource(this, R.mipmap.shortcut_identify))
                    .setIntent(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.mysite.example.com/")))
                    .build();
            shortcutManager.setDynamicShortcuts(Arrays.asList(shortcut1,shortcut2,shortcut3,shortcut4));
        }


    }

    public static HearingApplication getInstance() {
        return mApplication;
    }
}
