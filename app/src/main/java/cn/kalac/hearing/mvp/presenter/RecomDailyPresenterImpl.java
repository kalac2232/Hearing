package cn.kalac.hearing.mvp.presenter;

import cn.kalac.hearing.mvp.model.RecomDailyModel;
import cn.kalac.hearing.mvp.view.RecomDailyView;

/**
 * @author ghn
 * @date 2019/12/25 16:25
 */
public class RecomDailyPresenterImpl extends BasePresenter<RecomDailyView> implements RecomDailyPresenter {

    private final RecomDailyModel mRecomDailyModel;

    /**
     * 绑定View
     *
     * @param view
     */
    public RecomDailyPresenterImpl(RecomDailyView view) {
        super(view);
        mRecomDailyModel = new RecomDailyModel(this);
    }


    @Override
    public void onCreate() {

    }
}
