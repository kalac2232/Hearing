package cn.kalac.hearing.net;

/*
 * Created by Kalac on 2019/2/1
 */

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpProcessor implements IHttpProcessor {
    private final String TAG = "OkHttpProcessor";

    private OkHttpClient mOkHttpClient;

    private Handler myHandler;

    public OkHttpProcessor() {
        mOkHttpClient = new OkHttpClient();
        myHandler = new Handler();
    }

    @Override
    public void get(String url, Map<String, Object> params, final ICallBack callBack) {
        final Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onFailed(e.toString());
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

                    myHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onFailed(response.message().toString());
                        }
                    });

                }
            }
        });
    }

    @Override
    public void post(String url, Map<String, Object> params,final ICallBack callBack) {
        RequestBody requestbody = appendBody(params);

        final Request request = new Request.Builder()
                .post(requestbody)
                .url(url)
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                myHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onFailed(e.toString());

                    }
                });

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
                    myHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onFailed(response.message());

                        }
                    });

                }
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
