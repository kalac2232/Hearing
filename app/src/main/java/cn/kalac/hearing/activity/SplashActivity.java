package cn.kalac.hearing.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.kalac.hearing.R;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //跳转主页
                startActivty(MainActivity.class);
                finish();
            }
        }, 2 * 1000);
    }


    @Override
    protected int getLayoutResID() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void addListener() {

    }
}
