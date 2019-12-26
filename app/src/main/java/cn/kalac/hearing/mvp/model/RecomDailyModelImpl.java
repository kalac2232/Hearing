package cn.kalac.hearing.mvp.model;

import java.util.List;

import cn.kalac.hearing.javabean.local.MusicBean;
import cn.kalac.hearing.javabean.net.NetRecommendSongsBean;
import cn.kalac.hearing.mvp.presenter.BasePresenter;
import cn.kalac.hearing.mvp.presenter.RecomDailyPresenter;
import cn.kalac.hearing.net.HttpCallback;
import cn.kalac.hearing.net.HttpHelper;
import cn.kalac.hearing.utils.DataUtil;
import cn.kalac.hearing.utils.JavaBeanConvertUtil;
import cn.kalac.hearing.utils.TimeUtil;

/**
 * @author ghn
 * @date 2019/12/26 15:29
 */
public class RecomDailyModelImpl extends ModelHelper<RecomDailyPresenter> implements RecomDailyModel {

    public RecomDailyModelImpl(RecomDailyPresenter recomDailyPresenter) {
        super(recomDailyPresenter);
    }

    @Override
    public void requstDailyListData(String url) {
        HttpHelper.getInstance().get(url, new HttpCallback<NetRecommendSongsBean>() {

            @Override
            public void onSuccess(NetRecommendSongsBean netRecommendSongsBean) {

                mPresenter.requstDataSuccess(url,netRecommendSongsBean);
            }

            @Override
            public void onFailed(String string) {
                mPresenter.requstDataFailed(string);
            }
        });
    }
}
