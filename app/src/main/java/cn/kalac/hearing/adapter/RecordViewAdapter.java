package cn.kalac.hearing.adapter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;

import androidx.annotation.NonNull;

import androidx.viewpager.widget.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


import cn.kalac.hearing.R;
import cn.kalac.hearing.javabean.local.MusicBean;

import cn.kalac.hearing.widget.PlayListManager;


/*
 * Created by Kalac on 2019/2/14
 */

public class RecordViewAdapter extends PagerAdapter {

    private final Context mContext;

    private static final String TAG = "RecordViewAdapter";
    private ImageView mCoverImageView;

    private View mCurrentView;
    private  ObjectAnimator mDiscObjectAnimator;

    public RecordViewAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return PlayListManager.getInstance().getsize();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recordview_viewpager_layout, container, false);
        mCoverImageView = view.findViewById(R.id.iv_recordview_Album);

        //获取当前播放的歌曲id
        MusicBean music = PlayListManager.getInstance().getMusic(position);

        Glide.with(mContext)
                .load(music.getAlbumBean().getPicUrl())
                .into(mCoverImageView);
        container.addView(view);

        return view;
    }
    public void cancelObjectAnimator() {
        Log.i(TAG, "cancelObjectAnimator: ");
        if (mDiscObjectAnimator != null) {
            mDiscObjectAnimator.cancel();
        }
    }
    public void createObjectAnimator() {
        Log.i(TAG, "createObjectAnimator: ");
        
        //唱片旋转动画
        mDiscObjectAnimator = ObjectAnimator.ofFloat(getPrimaryItem(), View.ROTATION, 0, 360);
        mDiscObjectAnimator.setDuration(20 * 1000);
        //使ObjectAnimator动画匀速平滑旋转
        LinearInterpolator lir = new LinearInterpolator();
        mDiscObjectAnimator.setInterpolator(lir);
        //无限循环旋转
        mDiscObjectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mDiscObjectAnimator.setRepeatMode(ValueAnimator.RESTART);
        mDiscObjectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {
                //当关闭动画的时候，清除状态，恢复原来的角度
                animator.removeListener(this);
                animator.setDuration(0);
                animator.setInterpolator(new ReverseInterpolator());
                animator.start();
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }
    public void play() {
        Log.i(TAG, "play: ");
        if (mDiscObjectAnimator != null ) {
            Log.i(TAG, "play: 1");
            if (mDiscObjectAnimator.isPaused()) {
                Log.i(TAG, "play: 2");
                mDiscObjectAnimator.resume();
            } else {
                createObjectAnimator();
                Log.i(TAG, "play: 3");
                mDiscObjectAnimator.start();
            }

        } else {
            Log.i(TAG, "play: 4");
            createObjectAnimator();
            mDiscObjectAnimator.start();
        }

    }

    public void pause() {
        Log.i(TAG, "pause: ");
        if (mDiscObjectAnimator != null) {
            mDiscObjectAnimator.pause();
        }
    }
    public void cleanObjectAnimator() {
        Log.i(TAG, "cleanObjectAnimator: ");
        if (mDiscObjectAnimator != null) {
            mDiscObjectAnimator.cancel();
        }
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        mCurrentView = (View)object;
    }

    /**
     * 获取当前显示的view，用于旋转
     * @return
     */
    private View getPrimaryItem() {
        return mCurrentView;
    }
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);

    }

    public class ReverseInterpolator implements Interpolator {

        private final Interpolator delegate;

        public ReverseInterpolator(Interpolator delegate){
            this.delegate = delegate;
        }

        public ReverseInterpolator(){
            this(new LinearInterpolator());
        }

        @Override
        public float getInterpolation(float input) {
            return 1 - delegate.getInterpolation(input);
        }
    }



}
