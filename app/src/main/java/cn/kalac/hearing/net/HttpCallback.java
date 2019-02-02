package cn.kalac.hearing.net;

/*
 * Created by Kalac on 2019/2/1
 */

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class HttpCallback<Result> implements ICallBack {

    @Override
    public void onSuccess(String result) {
        Gson gson = new Gson();
        Class<?> cls = analysisClzzInfo();

        Result objResult = (Result) gson.fromJson(result, cls);
        onSuccess(objResult);
    }

    public abstract void onSuccess(Result result);

    /**
     * 利用反射获得类的信息
     * @return Class<?> 需要实现的Json解析类
     */
    private Class<?> analysisClzzInfo() {

        Type genType = getClass().getGenericSuperclass();

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        return (Class<?>) params[0];
    }

}

