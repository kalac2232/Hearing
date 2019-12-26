package cn.kalac.hearing.mvp.presenter;

import android.content.Context;

import cn.kalac.hearing.mvp.model.BaseModel;
import cn.kalac.hearing.mvp.view.BaseView;

/**
 * @author ghn
 * @date 2019/12/25 14:46
 */
public abstract class PresenterHelper<T extends BaseView,K extends BaseModel> {

    protected T mView;
    protected K mModel;
    /**
     * 绑定View
     */
    public PresenterHelper(T view) {
        this.mView = view;
        this.mModel = bindModel();
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

    public abstract K bindModel();

    public Context getContext() {
        return mView.getContext();
    }
}
