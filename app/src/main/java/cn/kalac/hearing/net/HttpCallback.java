package cn.kalac.hearing.net;

/*
 * Created by Kalac on 2019/2/1
 */

import android.app.Activity;

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class HttpCallback<Result>{
    private String mResult;
    private Activity activity;
    private boolean isSetActivity;

    public HttpCallback() {
        isSetActivity = false;
    }


    public HttpCallback(Activity activity) {
        this.activity = activity;
        isSetActivity = true;
    }


    protected void onSuccess(String result) {
        Class<?> cls = analysisClazzInfo();
        Result objResult;
        if (cls.equals(String.class)) {
            objResult = (Result) result;
        } else {
            Gson gson = new Gson();
            mResult = result;
            objResult = (Result) gson.fromJson(result, cls);
        }


        if (isSetActivity) {
            if (isActivityAlive(activity)) {
                onResultSuccess(objResult);
            }
        } else {
            onResultSuccess(objResult);
        }



    }

    private boolean isActivityAlive(Activity activity) {
        return activity != null && !activity.isFinishing();
    }

    protected void onFailed(String msg) {
        if (isSetActivity) {
            if (isActivityAlive(activity)) {
                onResultFailed(msg);
            }
        } else {
            onResultFailed(msg);
        }

    }

    public abstract void onResultSuccess(Result result);
    public abstract void onResultFailed(String errorMsg);

    /**
     * 利用反射获得类的信息
     * @return Class<?> 需要实现的Json解析类
     */
    private Class<?> analysisClazzInfo() {

        Type genType = getClass().getGenericSuperclass();

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        return (Class<?>) params[0];
    }

    public String getResult(){
        return mResult;
    }
}

