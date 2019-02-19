package cn.kalac.hearing.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import cn.kalac.hearing.R;
import cn.kalac.hearing.adapter.RecordViewAdapter;
import cn.kalac.hearing.utils.DisplayUtil;


/*
 * Created by Kalac on 2019/2/7
 */

public class RecordView extends RelativeLayout {

    private View mRootView;
    private ImageView mIvNeedle;
    private int mScreenWidth;
    private int mScreenHeigth;

    public RecordView(Context context) {
        this(context,null);
    }

    public RecordView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RecordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        // 导入布局
        mRootView = LayoutInflater.from(context).inflate(R.layout.recordview_layout, this, true);
        //获取屏幕的宽高信息
        mScreenHeigth = DisplayUtil.getScreenHeight(context);
        mScreenWidth = DisplayUtil.getScreenWidth(context);
        //初始化指针
        initNeedle();
        //初始化背景圆盘
        initDiscBlackground();
        //初始化ViewPager
        initViewPager();


    }


    /**
     * 初始化指针
     */
    private void initNeedle() {
        //获取指针
        mIvNeedle = mRootView.findViewById(R.id.iv_recordview_Needle);

        int needleWidth = (int) (DisplayUtil.SCALE_NEEDLE_WIDTH * mScreenWidth);
        int needleHeight = (int) (DisplayUtil.SCALE_NEEDLE_HEIGHT * mScreenHeigth);

        /*设置手柄的外边距为负数，让其隐藏一部分*/
        int marginTop = (int) (DisplayUtil.SCALE_NEEDLE_MARGIN_TOP * mScreenHeigth) * -1;
        int marginLeft = (int) (DisplayUtil.SCALE_NEEDLE_MARGIN_LEFT * mScreenWidth);

        Bitmap originBitmap = BitmapFactory.decodeResource(getResources(), R.drawable
                .ic_playing_needle);
        Bitmap bitmap = Bitmap.createScaledBitmap(originBitmap, needleWidth, needleHeight, false);

        RelativeLayout.LayoutParams layoutParams = (LayoutParams) mIvNeedle.getLayoutParams();
        layoutParams.setMargins(marginLeft, marginTop, 0, 0);

        int pivotX = (int) (DisplayUtil.SCALE_NEEDLE_PIVOT_X * mScreenWidth);
        int pivotY = (int) (DisplayUtil.SCALE_NEEDLE_PIVOT_Y * mScreenWidth);

        mIvNeedle.setPivotX(pivotX);
        mIvNeedle.setPivotY(pivotY);
        mIvNeedle.setRotation(DisplayUtil.ROTATION_INIT_NEEDLE);
        mIvNeedle.setImageBitmap(bitmap);
        mIvNeedle.setLayoutParams(layoutParams);
    }

    /*得到唱盘背后半透明的圆形背景*/
    private Drawable getDiscBlackgroundDrawable() {
        int discSize = (int) (mScreenWidth * DisplayUtil.SCALE_DISC_BG_SIZE);
        Bitmap bitmapDisc = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R
                .drawable.ic_recordview_disc_blackground), discSize, discSize, false);
        RoundedBitmapDrawable roundDiscDrawable = RoundedBitmapDrawableFactory.create
                (getResources(), bitmapDisc);
        return roundDiscDrawable;
    }

    /**
     * 初始化背景圆盘
     */
    private void initDiscBlackground() {
        ImageView mDiscBlackground = findViewById(R.id.iv_recordview_DiscBlackgound);
        mDiscBlackground.setImageDrawable(getDiscBlackgroundDrawable());

        int marginTop = (int) (DisplayUtil.SCALE_DISC_BG_MARGIN_TOP * mScreenHeigth);
        RelativeLayout.LayoutParams layoutParams = (LayoutParams) mDiscBlackground
                .getLayoutParams();
        layoutParams.setMargins(0, marginTop, 0, 0);

        mDiscBlackground.setLayoutParams(layoutParams);
    }

    /**
     * 初始化Viewpager
     */
    private void initViewPager() {
        ViewPager mVpRecord = findViewById(R.id.vp_recordview_record);

        RelativeLayout.LayoutParams layoutParams = (LayoutParams) mVpRecord.getLayoutParams();
        int marginTop = (int) (DisplayUtil.SCALE_DISC_MARGIN_TOP * mScreenHeigth);
        layoutParams.setMargins(0, marginTop, 0, 0);
        mVpRecord.setLayoutParams(layoutParams);

        //设置适配器
        RecordViewAdapter recordViewAdapter = new RecordViewAdapter(getContext());
        mVpRecord.setOffscreenPageLimit(1);
        mVpRecord.setAdapter(recordViewAdapter);
    }
}
