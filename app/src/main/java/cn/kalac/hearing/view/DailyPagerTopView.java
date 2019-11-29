package cn.kalac.hearing.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.kalac.hearing.R;
import jp.wasabeef.blurry.Blurry;

/**
 * @author kalac.
 * @date 2019/11/28 21:39
 */
public class DailyPagerTopView extends ConstraintLayout {

    @BindView(R.id.iv_bg)
    ImageView ivBg;
    @BindView(R.id.tv_current_day)
    TextView tvCurrentDay;
    @BindView(R.id.tv_dividing_line)
    TextView tvDividingLine;
    @BindView(R.id.tv_current_month)
    TextView tvCurrentMonth;
    /**
     * 当前已经位移量百分比
     */
    private float mDisplacementPercent;

    /**
     * 底部向上遮挡的位移
     */
    private int mUpOcclusionDistance;

    /**
     * 上次模糊时位移的百分比
     */
    private float mLastBlurDisplacementPercent = 0;
    private Bitmap mBgBitmap;

    public DailyPagerTopView(Context context) {
        this(context, null);
    }

    public DailyPagerTopView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DailyPagerTopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_daily_top_view, this);
        ButterKnife.bind(this);
        mBgBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_daily_top_view_bg);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mUpOcclusionDistance = (int) (getMeasuredHeight() * 0.1);
    }

    /**
     * 获取向上位移的距离
     *
     * @return
     */
    public int getUpOcclusionDistance() {
        return mUpOcclusionDistance;
    }

    public float getDisplacementPercent() {
        return mDisplacementPercent;
    }

    public void setDisplacementPercent(float displacementPercent) {
        this.mDisplacementPercent = displacementPercent;
        updateView();
    }

    private void updateView() {
        //设置文字
        tvCurrentDay.setAlpha(1- mDisplacementPercent);
        tvDividingLine.setAlpha(1- mDisplacementPercent);
        tvCurrentMonth.setAlpha(1- mDisplacementPercent);

        if (Math.abs(mDisplacementPercent - mLastBlurDisplacementPercent) > 0.03f) {
            Blurry.with(getContext())
                    .radius((int) (1 + 10 * mDisplacementPercent))
                    .sampling(8)
                    .color(Color.argb((int)(35 * mDisplacementPercent), 0, 0, 0))
                    //.async()
                    .from(mBgBitmap)
                    .into(ivBg);
            mLastBlurDisplacementPercent = mDisplacementPercent;
        } else {
            //Logger.i("阻止一次");
        }


    }
}
