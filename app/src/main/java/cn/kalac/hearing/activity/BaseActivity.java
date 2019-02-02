package cn.kalac.hearing.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;


/*
 * Created by Kalac on 2019/2/1
 */

public abstract class BaseActivity extends Activity {
    protected Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(getLayoutResID());
        //实现状态栏透明
        View decorView = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(option);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        initView();
        initData();
        addListener();
    }


    protected abstract int getLayoutResID();
    protected abstract void initData();
    protected abstract void initView();
    protected abstract void addListener();
}
