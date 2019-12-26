package cn.kalac.hearing.activity;

import android.content.Context;
import android.os.Bundle;

import cn.kalac.hearing.mvp.presenter.PresenterHelper;
import cn.kalac.hearing.mvp.view.BaseView;

/**
 * @author ghn
 * @date 2019/12/25 18:27
 */
public abstract class MVPBaseActivity<T extends PresenterHelper> extends BaseActivity implements BaseView {

    protected T mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //创建Presenter
        mPresenter = initPresenter();
        //初始化Presenter
        mPresenter.onCreate();
        super.onCreate(savedInstanceState);
    }



    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        mPresenter.onDetach();
        super.onDestroy();
    }


    @Override
    public Context getContext() {
        return this;
    }

    /**
     * 创建prensenter
     * @return <T extends PresenterHelper> 必须是BasePresenter的子类
     */
    public abstract T initPresenter();
}
