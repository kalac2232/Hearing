package cn.kalac.hearing.net;

/*
 * Created by Kalac on 2019/2/1
 */

import java.util.Map;

public interface IHttpProcessor {

    // Get
    void get(String url, Map<String, Object> params, ICallBack callBack);
    //Post
    void post(String url, Map<String, Object> params, ICallBack callBack);
    //Update
    //Delete

}
