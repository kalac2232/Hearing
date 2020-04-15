package cn.kalac.hearing.net;

/*
 * Created by Kalac on 2019/2/1
 */

import android.app.Activity;

import java.util.Map;

public class HttpHelper {
    /**
     * 被代理的对象
     */
    private static IHttpProcessor mIHttpProcessor;

    private static HttpHelper instance;

    private HttpHelper(){

    }

    public static HttpHelper getInstance() {

        if (instance == null) {
            instance = new HttpHelper();
        }
        return instance;
    }

    /**
     * 初始化立即启动
     * 传入被代理的对象，Volly实现类 okHttp实现类 等等
     *
     */
    public static void init(IHttpProcessor httpProcessor){
        mIHttpProcessor = httpProcessor;
    }



    public void get(String url, HttpCallback callBack) {
        mIHttpProcessor.get(url, callBack);
    }

    private boolean isActivityAlive(Activity activity) {
        return activity != null && !activity.isFinishing();
    }


    public void post(String url, Map<String, Object> params, HttpCallback callBack) {
        mIHttpProcessor.post(url, params, callBack);
    }
}

