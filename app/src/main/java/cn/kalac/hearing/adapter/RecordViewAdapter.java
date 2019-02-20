package cn.kalac.hearing.adapter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.DrawableTransformation;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.security.MessageDigest;
import java.util.ArrayList;

import cn.kalac.hearing.HearingApplication;
import cn.kalac.hearing.R;
import cn.kalac.hearing.api.ApiHelper;
import cn.kalac.hearing.javabean.song.Song;
import cn.kalac.hearing.javabean.song.SongDetailResultBean;
import cn.kalac.hearing.net.HttpCallback;
import cn.kalac.hearing.net.HttpHelper;
import cn.kalac.hearing.utils.DisplayUtil;


/*
 * Created by Kalac on 2019/2/14
 */

public class RecordViewAdapter extends PagerAdapter {

    private final Context mContext;

    private static final String TAG = "RecordViewAdapter";
    private ImageView mCoverImageView;

    private View mCurrentView;
    private  ObjectAnimator mDiscObjectAnimator;
    private  ObjectAnimator jjj;

    public RecordViewAdapter(Context context) {
        mContext = context;

    }

    @Override
    public int getCount() {
        return HearingApplication.mPlayingSongList.size();
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
        Song song = HearingApplication.mPlayingSongList.get(HearingApplication.mCurrentPlayPos);

        RequestOptions requestOptions = new RequestOptions()
                .transform(new CompositeCoverTransformation());

        RequestBuilder<Drawable> requestBuilder = Glide.with(mContext).load(R.mipmap.ic_recordview_album_default).apply(requestOptions);

        Glide.with(mContext)
                .load(song.getPicUrl())
                .apply(requestOptions)
                .thumbnail(requestBuilder)
                .into(mCoverImageView);
        container.addView(view);

        return view;
    }

    public void createObjectAnimator() {
        Log.i(TAG, "createObjectAnimator: ");
        if (mDiscObjectAnimator != null) {
            mDiscObjectAnimator.cancel();
        }
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
        Log.i(TAG, "setPrimaryItem: ");
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
    class CompositeCoverTransformation extends BitmapTransformation{

        @Override
        protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
            return getDiscBitmap(toTransform);
        }

        @Override
        public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

        }
    }

    /**
     * 得到唱盘图片
     * 唱盘图片由空心圆盘及音乐专辑图片“合成”得到
     */
    private Bitmap getDiscBitmap(Bitmap musicPicBitmap) {
        int discSize = (int) (DisplayUtil.getScreenWidth(mContext) * DisplayUtil.SCALE_DISC_SIZE);
        int musicPicSize = (int) (DisplayUtil.getScreenWidth(mContext) * DisplayUtil.SCALE_MUSIC_PIC_SIZE);
        //获得专辑背景圆环
        Bitmap bitmapDisc = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(mContext.getResources(), R
                .drawable.ic_recordview_disc), discSize, discSize, false);
        //缩放专辑图片
        Bitmap bitmapMusicPic = Bitmap.createScaledBitmap(musicPicBitmap, musicPicSize, musicPicSize, true);
        BitmapDrawable discDrawable = new BitmapDrawable(bitmapDisc);
        RoundedBitmapDrawable roundMusicDrawable = RoundedBitmapDrawableFactory.create
                (mContext.getResources(), bitmapMusicPic);

        //抗锯齿
        discDrawable.setAntiAlias(true);
        roundMusicDrawable.setAntiAlias(true);

        Drawable[] drawables = new Drawable[2];
        drawables[0] = roundMusicDrawable;
        drawables[1] = discDrawable;

        LayerDrawable layerDrawable = new LayerDrawable(drawables);
        int musicPicMargin = (int) ((DisplayUtil.SCALE_DISC_SIZE - DisplayUtil
                .SCALE_MUSIC_PIC_SIZE) * DisplayUtil.getScreenWidth(mContext) / 2);
        //调整专辑图片的四周边距，让其显示在正中
        layerDrawable.setLayerInset(0, musicPicMargin, musicPicMargin, musicPicMargin,
                musicPicMargin);

        return drawableToBitmap(layerDrawable);
    }


    /**
     * drawable转为bitmap
     * @param drawable 要转的drawable
     * @return 转换成的bitmap
     */
    public Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565
        );
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
