package cn.kalac.hearing.mvp.presenter;

import java.util.List;

import cn.kalac.hearing.javabean.local.MusicBean;
import cn.kalac.hearing.javabean.net.NetRecommendSongsBean;
import cn.kalac.hearing.mvp.view.RecomDailyView;

/**
 * @author ghn
 * @date 2019/12/25 18:55
 */
public interface RecomDailyPresenter extends BasePresenter {
    void getDailyListData();

    void requstDataSuccess(String url,NetRecommendSongsBean musicBeans);

    void requstDataFailed(String msg);

    void playAllMusic();
}
