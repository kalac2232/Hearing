package cn.kalac.hearing.mvp.view;

import java.util.List;

import cn.kalac.hearing.javabean.local.MusicBean;

/**
 * @author ghn
 * @date 2019/12/25 16:25
 */
public interface RecomDailyView extends BaseView {
    void setAdapterData(List<MusicBean> musicBeans);
}
