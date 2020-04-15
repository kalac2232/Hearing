package cn.kalac.hearing.net;

/*
 * Created by Kalac on 2019/2/1
 */

import java.util.Map;

public interface IHttpProcessor {


    void get(String url, HttpCallback callBack);

    void post(String url, Map<String, Object> params, HttpCallback callBack);


}
