package cn.kalac.hearing.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;


/*
 * Created by Kalac on 2019/2/1
 */

public abstract class BaseActivity extends Activity {

    String TAG = ""+getClass().toString();
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
    public void setStatueTextColor(boolean isGray) {
        if (isGray) {
            // 灰色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            // 白色
            getWindow().getDecorView().setSystemUiVisibility(0);
        }
    }

    /**
     * 启动一个activity
     * @param activityClass 目标activity
     */
    protected void startActivty(Class activityClass){
        Intent intent = new Intent(mContext, activityClass);
        startActivity(intent);
    }
    protected abstract int getLayoutResID();
    protected abstract void initData();
    protected abstract void initView();
    protected abstract void addListener();
}
