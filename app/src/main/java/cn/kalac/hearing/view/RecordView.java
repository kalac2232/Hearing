package cn.kalac.hearing.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import cn.kalac.hearing.R;
import cn.kalac.hearing.adapter.RecordViewAdapter;
import cn.kalac.hearing.utils.DisplayUtil;


/**
 *
 * Created by Kalac on 2019/2/7
 */

public class RecordView extends RelativeLayout {

    private static final String TAG = "RecordView";

    private View mRootView;
    private ImageView mIvNeedle;
    private int mScreenWidth;
    private int mScreenHeigth;
    private ViewPager mVpRecord;

    private RecordViewAdapter mRecordViewAdapter;

    public static final int DURATION_NEEDLE_ANIAMTOR = 500;
    private ObjectAnimator mNeedleAnimator;

    private RecordStatus mRecordStatus = RecordStatus.STOP;

    //唱片当前的状态
    public enum RecordStatus {
        PLAY, PAUSE, STOP, MOVE //move为滑动唱片的状态
    }

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
        //初始化动画
        initObjectAnimation();

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
        mVpRecord = findViewById(R.id.vp_recordview_record);

        RelativeLayout.LayoutParams layoutParams = (LayoutParams) mVpRecord.getLayoutParams();
        int marginTop = (int) (DisplayUtil.SCALE_DISC_MARGIN_TOP * mScreenHeigth);
        //设置根据屏幕大小动态设置vp的高度
        layoutParams.height = (int) (DisplayUtil.getScreenWidth(getContext()) * DisplayUtil.SCALE_DISC_SIZE);
        layoutParams.setMargins(0, marginTop, 0, 0);
        mVpRecord.setLayoutParams(layoutParams);

        //设置适配器
        mRecordViewAdapter = new RecordViewAdapter(getContext());
        //设置当前位置
        mVpRecord.setOffscreenPageLimit(1);
        mVpRecord.setAdapter(mRecordViewAdapter);
        mVpRecord.setCurrentItem(6);

        //添加监听
        mVpRecord.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //用于重新获取mDiscObjectAnimator中的getPrimaryItem(),否则无法滑动后自动旋转。
                mRecordViewAdapter.play();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state){
                    case ViewPager.SCROLL_STATE_IDLE:
                        //无动作、初始状态
                        //进行一个播放状态的恢复
                        if (mRecordStatus == RecordStatus.MOVE) {
                            mRecordStatus = RecordStatus.PLAY;
                            mNeedleAnimator.start();
                        }
                        break;
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        //点击、滑屏
                        //如果当播放状态滑动时，移回指针，停止旋转
                        if (mRecordStatus == RecordStatus.PLAY) {
                            mRecordStatus = RecordStatus.MOVE;
                            mNeedleAnimator.reverse();
                        }
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        //释放
                        break;
                }

            }
        });
    }
    //初始化动画
    private void initObjectAnimation() {

        //指针动画
        mNeedleAnimator = ObjectAnimator.ofFloat(mIvNeedle, View.ROTATION, DisplayUtil
                .ROTATION_INIT_NEEDLE, 0);
        mNeedleAnimator.setDuration(DURATION_NEEDLE_ANIAMTOR);
        mNeedleAnimator.setInterpolator(new AccelerateInterpolator());

        mNeedleAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                if (mRecordStatus == RecordStatus.PAUSE) {
                    //停止旋转唱片
                    mRecordViewAdapter.pause();
                }
                if (mRecordStatus == RecordStatus.MOVE) {
                    //这种情况就是现在处于开始滑动ViewPager的情况也需要停止
                    mRecordViewAdapter.pause();
                }
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                //当开始播放时 当指针动画结束后唱片动画才开始播放
                if (mRecordStatus == RecordStatus.PLAY) {
                    //开始旋转唱片
                    mRecordViewAdapter.play();//通过适配器控制其中的view旋转
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

    }
    public void play(){
        mRecordStatus = RecordStatus.PLAY;
        //移动指针
        mNeedleAnimator.start();

    }

    public void pause() {
        mRecordStatus = RecordStatus.PAUSE;

        mNeedleAnimator.reverse();
    }
}
