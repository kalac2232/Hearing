package cn.kalac.hearing.mvp.presenter;

import cn.kalac.hearing.mvp.view.BaseView;

/**
 * @author ghn
 * @date 2019/12/25 14:46
 */
public abstract class BasePresenter<T extends BaseView> {

    protected T mView;

    /**
     * 绑定View
     */
    public BasePresenter(T view) {
        this.mView = view;

    }
    /**
     * 做初始化的操作,需要在V的视图初始化完成之后才能调用
     * presenter进行初始化.
     */
    public abstract void onCreate();
    /**
     * 在这里结束异步操作
     */
    public void onDestroy(){

    }
    /**
     * 在V销毁的时候调用,解除绑定
     */
    public void onDetach() {
        mView = null;
    }
}
