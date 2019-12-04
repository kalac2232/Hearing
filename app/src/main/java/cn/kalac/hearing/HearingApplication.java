package cn.kalac.hearing;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

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
    }

    public static HearingApplication getInstance() {
        return mApplication;
    }
}
