package cn.kalac.hearing.mvp.model;

import cn.kalac.hearing.mvp.presenter.BasePresenter;

/**
 * @author ghn
 * @date 2019/12/26 15:30
 */
public class ModelHelper<T extends BasePresenter> {

    protected T mPresenter;


    public ModelHelper(T t) {
        this.mPresenter = t;
    }


}
