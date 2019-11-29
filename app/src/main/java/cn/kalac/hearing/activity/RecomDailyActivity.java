package cn.kalac.hearing.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.kalac.hearing.R;
import cn.kalac.hearing.adapter.DailyListAdapter;
import cn.kalac.hearing.api.ApiHelper;
import cn.kalac.hearing.javabean.RecommendSongsBean;
import cn.kalac.hearing.javabean.song.Song;
import cn.kalac.hearing.net.HttpCallback;
import cn.kalac.hearing.net.HttpHelper;
import cn.kalac.hearing.service.PlayMusicService;
import cn.kalac.hearing.utils.DensityUtil;
import cn.kalac.hearing.widget.CalendarRingDrawable;

/**
 * @author kalac.
 * @date 2019/11/26 21:38
 */
public class RecomDailyActivity extends BaseActivity {
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
    protected int getLayoutResID() {
        return R.layout.activity_recom_daily;
    }

    @Override
    protected void initData() {
        getDailyListData();
    }

    private void getDailyListData() {
        String url = ApiHelper.getRecommendSongsUrl();

        HttpHelper.getInstance().get(url, new HttpCallback<RecommendSongsBean>() {

            @Override
            public void onSuccess(RecommendSongsBean recommendSongsBean) {
                List<RecommendSongsBean.RecommendBean> recommendSongBeanList = recommendSongsBean.getRecommend();
                Toast.makeText(mContext, "获取了" + recommendSongBeanList.size() + "个数据", Toast.LENGTH_SHORT).show();
                //Log.i(TAG, "onSuccess: "+recommendSongsBean);
                //提取日推列表中歌曲的id方便进行播放
                extractSongIdFromRecommendList(recommendSongBeanList);
                //设置将从第一个开始播放
                PlayMusicService.mCurrentPlayPos = 0;
            }

            @Override
            public void onFailed(String string) {
                Toast.makeText(mContext, "获取失败" + string, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void initView() {
        vRingLeft.setBackground(new CalendarRingDrawable());
        vRingRight.setBackground(new CalendarRingDrawable());

        rvDailyList.setLayoutManager(new LinearLayoutManager(this));
        rvDailyList.setAdapter(new DailyListAdapter());

        ViewGroup.LayoutParams layoutParams = clDailyListCore.getLayoutParams();
        layoutParams.height = (int) (DensityUtil.getScreenHeight(this) - DensityUtil.getActionBarHeight(this));
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) tvTitle.getLayoutParams();
        params.topMargin = DensityUtil.getStatusBarHeight(mContext);

        calculateRingX();
    }

    /**
     * 计算背景上的挂件的位置
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

    /**
     * 提取日推列表中歌曲的id方便进行播放
     *
     * @param recommendSongBeanList 日推列表
     */
    private void extractSongIdFromRecommendList(List<RecommendSongsBean.RecommendBean> recommendSongBeanList) {
        ArrayList<Song> list = new ArrayList<>();
        for (RecommendSongsBean.RecommendBean bean : recommendSongBeanList) {
            int songId = bean.getId();
            String songName = bean.getName();
            String singerName = bean.getArtists().get(0).getName();
            String picUrl = bean.getAlbum().getPicUrl();
            list.add(new Song(songId, songName, singerName, picUrl));
        }
        if (list.size() > 0) {

            PlayMusicService.mPlayingSongList.clear();
            PlayMusicService.mPlayingSongList.addAll(list);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
