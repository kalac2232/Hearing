package cn.kalac.hearing.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import butterknife.BindView;
import cn.kalac.hearing.R;
import cn.kalac.hearing.utils.IntentUtil;

public class SplashActivity extends BaseActivity {

    @BindView(R.id.iv_splash_logo)
    ImageView ivSplashLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideBottomUIMenu();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //跳转主页
                IntentUtil.get().goActivity(mContext,MainActivity.class);
                finish();
            }
        }, 3 * 1000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ivSplashLogo.setVisibility(View.VISIBLE);
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
