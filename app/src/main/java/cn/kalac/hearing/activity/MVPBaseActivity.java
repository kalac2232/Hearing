package cn.kalac.hearing.activity;

import android.os.Bundle;

import cn.kalac.hearing.mvp.presenter.BasePresenter;
import cn.kalac.hearing.mvp.view.BaseView;

/**
 * @author ghn
 * @date 2019/12/25 18:27
 */
public abstract class MVPBaseActivity<T extends BasePresenter> extends BaseActivity implements BaseView {

    protected T mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //创建Presenter
        mPresenter = initPresenter();
        //初始化Presenter
        mPresenter.onCreate();
    }



    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        mPresenter.onDetach();
        super.onDestroy();
    }



    /**
     * 创建prensenter
     * @return <T extends BasePresenter> 必须是BasePresenter的子类
     */
    public abstract T initPresenter();
}
