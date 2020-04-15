package cn.kalac.hearing.net;

/*
 * Created by Kalac on 2019/2/1
 */

import android.content.Context;
import android.os.Handler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import cn.kalac.hearing.net.cookies.PersistentCookieStore;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpProcessor implements IHttpProcessor {

    private OkHttpClient mOkHttpClient;

    /**
     * 用于将结果数据post到主线程
     */
    private Handler myHandler;

    public OkHttpProcessor(Context context) {

        final PersistentCookieStore cookieStore = new PersistentCookieStore(context);

        //创建cookieJar 用来保存登录的状态
        CookieJar cookieJar = new CookieJar(){
            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                if (cookies != null && cookies.size() > 0) {
                    for (Cookie item : cookies) {
                        cookieStore.add(url, item);
                    }
                }
            }
            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url);
                return cookies;
            }
        };
        //通过newBuilder设置cookjar并构建OkHttpClient对象
        mOkHttpClient = new OkHttpClient()
                .newBuilder()
                .cookieJar(cookieJar)
                .build();

        myHandler = new Handler();
    }

    @Override
    public void get(String url, final HttpCallback callBack) {

        final Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                throwErrorMessage(callBack,e.toString());

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                if (response.isSuccessful()) {
                    final String json = response.body().string();

                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        if (jsonObject.has("code")) {
                            int code = jsonObject.getInt("code");
                            if (code >= 200 && code < 300) {
                                myHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        callBack.onSuccess(json);
                                    }
                                });
                            } else {
                                throwErrorMessage(callBack,"request error ,return " + code);
                            }
                        } else {
                            myHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    callBack.onSuccess(json);
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                } else {
                    throwErrorMessage(callBack,response.message());

                }
            }
        });
    }

    @Override
    public void post(String url, Map<String, Object> params,final HttpCallback callBack) {
        RequestBody requestbody = appendBody(params);

        final Request request = new Request.Builder()
                .post(requestbody)
                .url(url)
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                throwErrorMessage(callBack,e.toString());

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                if (response.isSuccessful()) {
                    final String result = response.body().string();

                    myHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onSuccess(result);

                        }
                    });
                } else {

                    throwErrorMessage(callBack,response.message());

                }
            }
        });
    }

    private void throwErrorMessage(HttpCallback callBack,String msg) {
        myHandler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onFailed(msg);

            }
        });
    }


    /**
     * 快速构建参数
     * @param params
     * @return
     */
    private RequestBody appendBody(Map<String, Object> params) {
        FormBody.Builder body = new FormBody.Builder();
        if (params == null || params.isEmpty()) {
            return body.build();
        }

        for (Map.Entry<String, Object> entry:
                params.entrySet()) {
            body.add(entry.getKey(), entry.getValue().toString());
        }
        return body.build();
    }
}
