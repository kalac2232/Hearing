package cn.kalac.hearing.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import java.lang.reflect.Field;

import cn.kalac.hearing.R;
import cn.kalac.hearing.adapter.RecordViewAdapter;
import cn.kalac.hearing.utils.DensityUtil;

/**
 *
 * Created by Kalac on 2019/2/7
 */

public class RecordView extends ConstraintLayout {

    private static final String TAG = "RecordView";

    private ImageView mIvNeedle;
    private int mScreenWidth;
    private int mScreenHeigth;
    private ViewPager mVpRecord;

    private RecordViewAdapter mRecordViewAdapter;

    public static final int DURATION_NEEDLE_ANIAMTOR = 500;
    private ObjectAnimator mNeedleAnimator;

    private RecordStatus mRecordStatus = RecordStatus.STOP;
    private NeedleStatus mNeedleStatus = NeedleStatus.PAUSE;

    private int mCurrentVPPosition = 0;

    //唱片当前的状态
    public enum RecordStatus {
        PLAY, PAUSE, STOP, MOVE //move为滑动唱片的状态
    }
    //指针当前的状态
    public enum NeedleStatus {
        PLAY, PAUSE, TOPLAY, TOPAUSE
    }

    /*手柄起始角度*/
    public static final float ROTATION_INIT_NEEDLE = -30;

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
        //获取屏幕的宽高信息
        mScreenHeigth = DensityUtil.getScreenHeight(context);
        mScreenWidth = DensityUtil.getScreenWidth(context);
        // 导入布局
        importLayout(context);

        //初始化指针
        initNeedle();
        //初始化ViewPager
        initViewPager();
        //更改Viewpager的滑动速度
        fixViewPager();
        //初始化动画
        initObjectAnimation();

    }

    private void importLayout(Context context) {

        View view = LayoutInflater.from(context).inflate(R.layout.recordview_layout, this, false);
        addView(view,mScreenWidth, (int) (mScreenWidth * 1.2f));
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mScreenWidth, mScreenWidth);
    }

    /**
     * 初始化指针
     */
    private void initNeedle() {
        //获取指针
        mIvNeedle = findViewById(R.id.iv_recordview_Needle);


        //设置旋转点
        mIvNeedle.post(new Runnable() {
            @Override
            public void run() {
                float pivotX = mIvNeedle.getWidth() * 0.144f;
                float pivotY = mIvNeedle.getWidth() * 0.164f;
                mIvNeedle.setPivotX(pivotX);
                mIvNeedle.setPivotY(pivotY);
                mIvNeedle.setRotation(ROTATION_INIT_NEEDLE);
            }
        });

    }


    /**
     * 初始化Viewpager
     */
    private void initViewPager() {
        mVpRecord = findViewById(R.id.vp_recordview_record);

        //设置适配器
        mRecordViewAdapter = new RecordViewAdapter(getContext());
        //设置当前位置
        mVpRecord.setOffscreenPageLimit(1);
        mVpRecord.setAdapter(mRecordViewAdapter);

        //添加监听
        mVpRecord.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //用于重新获取mDiscObjectAnimator中的getPrimaryItem(),否则无法滑动后自动旋转。
                Log.i(TAG, "RecordViewAdapteronPageSelected: ");
                mRecordViewAdapter.cleanObjectAnimator();
//                if (mRecordStatus == RecordStatus.PLAY) {
//                    mRecordViewAdapter.play();
//                }
                //如果选中的页使之前页的后一页，回调lisitener
                if (position == mCurrentVPPosition) {

                } else {
                    if (position > mCurrentVPPosition) {
                        if (mOnSrollListener != null) {
                            mOnSrollListener.next();
                        }
                    } else{
                        //前一页
                        if (mOnSrollListener != null) {
                            mOnSrollListener.prev();
                        }
                    }
                    //重新记录当前页码
                    mCurrentVPPosition = position;
                    if (mRecordStatus == RecordStatus.MOVE) {
                        mRecordStatus = RecordStatus.PAUSE;
                    }
                }



                //mRecordViewAdapter.notifyDataSetChanged();
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
                            Log.i(TAG, "onPageScrollStateChanged: ");
                            mRecordStatus = RecordStatus.MOVE;
                            mNeedleAnimator.reverse();
                        }
                        if (mNeedleStatus == NeedleStatus.PLAY) {

                        }
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        //释放
                        break;
                }

            }
        });
    }

    /**
     *
     */
    private void fixViewPager() {
        try {
            //自定义viewpager的滑动速度
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            FixedSpeedScroller mScroller = new FixedSpeedScroller(mVpRecord.getContext(),
                    new AccelerateInterpolator());
            mField.set(mVpRecord, mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //初始化动画
    private void initObjectAnimation() {

        //指针动画
        mNeedleAnimator = ObjectAnimator.ofFloat(mIvNeedle, View.ROTATION, ROTATION_INIT_NEEDLE, 0);
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
        if (mRecordStatus == RecordStatus.PLAY) {
            return;
        }
        mRecordStatus = RecordStatus.PLAY;
        //移动指针
        mNeedleAnimator.start();

    }

    public void pause() {
        Log.i(TAG, "pause: ");
        if (mRecordStatus == RecordStatus.PAUSE) {
            return;
        }
        mRecordStatus = RecordStatus.PAUSE;

        mNeedleAnimator.reverse();
    }


    public void nextMusic() {
        mVpRecord.setCurrentItem(mVpRecord.getCurrentItem() + 1);
    }

    public void prevMusic() {
        mVpRecord.setCurrentItem(mVpRecord.getCurrentItem() - 1);
    }

    /**
     * 自定义viewpager滑动速度类
     */
    public class FixedSpeedScroller extends Scroller {
        private int mDuration = 200;

        public FixedSpeedScroller(Context context) {
            super(context);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        public void setmDuration(int time) {
            mDuration = time;
        }

        public int getmDuration() {
            return mDuration;
        }
    }

    public interface onSrollListener{
        void next();
        void prev();
    }
    private onSrollListener mOnSrollListener = null;

    public void setOnSrollListener(onSrollListener onSrollListener) {
        mOnSrollListener = onSrollListener;
    }
}
