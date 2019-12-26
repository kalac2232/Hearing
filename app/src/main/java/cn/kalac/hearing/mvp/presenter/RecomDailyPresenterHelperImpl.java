package cn.kalac.hearing.mvp.presenter;

import android.widget.Toast;

import java.util.List;

import cn.kalac.hearing.activity.PlayMusicActivity;
import cn.kalac.hearing.adapter.DailyListAdapter;
import cn.kalac.hearing.api.ApiHelper;
import cn.kalac.hearing.javabean.local.MusicBean;
import cn.kalac.hearing.javabean.net.NetRecommendSongsBean;
import cn.kalac.hearing.mvp.model.RecomDailyModel;
import cn.kalac.hearing.mvp.model.RecomDailyModelImpl;
import cn.kalac.hearing.mvp.view.RecomDailyView;
import cn.kalac.hearing.net.HttpCallback;
import cn.kalac.hearing.net.HttpHelper;
import cn.kalac.hearing.utils.DataUtil;
import cn.kalac.hearing.utils.JavaBeanConvertUtil;
import cn.kalac.hearing.utils.TimeUtil;
import cn.kalac.hearing.widget.IntentUtil;
import cn.kalac.hearing.widget.PlayListManager;

/**
 * @author ghn
 * @date 2019/12/25 16:25
 */
public class RecomDailyPresenterHelperImpl extends PresenterHelper<RecomDailyView, RecomDailyModel> implements RecomDailyPresenter {

    private List<MusicBean> mMusicBeanList;

    /**
     * 绑定View
     *
     * @param view
     */
    public RecomDailyPresenterHelperImpl(RecomDailyView view) {
        super(view);

    }

    @Override
    public void onCreate() {

    }

    @Override
    public RecomDailyModel bindModel() {
        return new RecomDailyModelImpl(this);
    }

    @Override
    public void getDailyListData() {
        String url = ApiHelper.getRecommendSongsUrl();
        //判断今天是否已经获取过了，获取过的话直接使用

        mMusicBeanList = DataUtil.loadListFormLoacl(url + TimeUtil.getTime(System.currentTimeMillis(), "yy/MM/dd"), MusicBean.class);

        if (mMusicBeanList == null) {
            mModel.requstDailyListData(url);

        } else {
            Toast.makeText(getContext(), "本地获取了" + mMusicBeanList.size() + "个数据", Toast.LENGTH_SHORT).show();
            mView.setAdapterData(mMusicBeanList);
        }

    }

    @Override
    public void requstDataSuccess(String url, NetRecommendSongsBean netRecommendSongsBean) {
        mMusicBeanList = JavaBeanConvertUtil.recomDailyListConvert(netRecommendSongsBean);

        Toast.makeText(getContext(), "获取了" + mMusicBeanList.size() + "个数据", Toast.LENGTH_SHORT).show();

        DataUtil.saveList(url + TimeUtil.getTime(System.currentTimeMillis(), "yy/MM/dd"), mMusicBeanList);
        mView.setAdapterData(mMusicBeanList);
    }

    @Override
    public void requstDataFailed(String msg) {
        Toast.makeText(getContext(), "获取失败" + msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void playAllMusic() {
        PlayListManager.getInstance().setMusicList(mMusicBeanList,0);

        IntentUtil.get().goActivity(getContext(), PlayMusicActivity.class);
    }
}
