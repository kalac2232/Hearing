package cn.kalac.hearing.activity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import cn.kalac.easymediaplayer.EasyMediaPlayer;
import cn.kalac.hearing.R;
import cn.kalac.hearing.adapter.DailyListAdapter;
import cn.kalac.hearing.api.ApiHelper;
import cn.kalac.hearing.javabean.local.MusicBean;
import cn.kalac.hearing.javabean.net.NetRecommendSongsBean;
import cn.kalac.hearing.mvp.presenter.RecomDailyPresenterHelperImpl;
import cn.kalac.hearing.mvp.view.RecomDailyView;
import cn.kalac.hearing.net.HttpCallback;
import cn.kalac.hearing.net.HttpHelper;
import cn.kalac.hearing.utils.DataUtil;
import cn.kalac.hearing.utils.DensityUtil;
import cn.kalac.hearing.utils.JavaBeanConvertUtil;
import cn.kalac.hearing.utils.TimeUtil;
import cn.kalac.hearing.widget.CalendarRingDrawable;

/**
 * @author kalac.
 * @date 2019/11/26 21:38
 */
public class RecomDailyActivity extends MVPBaseActivity<RecomDailyPresenterHelperImpl> implements RecomDailyView{
    @BindView(R.id.v_ring_left)
    View vRingLeft;
    @BindView(R.id.v_ring_right)
    View vRingRight;
    @BindView(R.id.rv_daily_list)
    RecyclerView rvDailyList;
    @BindView(R.id.cl_daily_list_core)
    ConstraintLayout clDailyListCore;
    @BindView(R.id.tv_bar_title)
    TextView tvTitle;

    @Override
    public RecomDailyPresenterHelperImpl initPresenter() {
        return new RecomDailyPresenterHelperImpl(this);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_recom_daily;
    }

    @Override
    protected void initData() {
        mPresenter.getDailyListData();
    }



    @Override
    protected void initView() {
        vRingLeft.setBackground(new CalendarRingDrawable());
        vRingRight.setBackground(new CalendarRingDrawable());

        rvDailyList.setLayoutManager(new LinearLayoutManager(this));

        ViewGroup.LayoutParams layoutParams = clDailyListCore.getLayoutParams();
        layoutParams.height = (int) (DensityUtil.getScreenHeight(this) - DensityUtil.getActionBarHeight(this));
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) tvTitle.getLayoutParams();
        params.topMargin = DensityUtil.getStatusBarHeight(mContext);

        calculateRingX();
    }

    /**
     * 计算背景上的挂件的X坐标
     */
    private void calculateRingX() {
        int screenWidth = DensityUtil.getScreenWidth(this);
        int dp = DensityUtil.dip2px(this, 80);
        vRingLeft.setX(dp);
        vRingRight.setX(screenWidth - dp);
    }

    @Override
    protected void addListener() {

    }


    @Override
    public void setAdapterData(List<MusicBean> musicBeans) {
        rvDailyList.setAdapter(new DailyListAdapter(musicBeans));
    }
}
