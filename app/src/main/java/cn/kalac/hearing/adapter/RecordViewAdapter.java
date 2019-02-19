package cn.kalac.hearing.adapter;

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
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.security.MessageDigest;

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


    public RecordViewAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return 3;
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
        Song song = HearingApplication.mPlayingSongList.get(HearingApplication.mCurrentPlayPos + position);

//        int discSize = (int) (DisplayUtil.getScreenWidth(mContext) * DisplayUtil.SCALE_DISC_SIZE);
//        Bitmap bitmapDefault = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(mContext.getResources(), R
//                .drawable.ic_recordview_album_default), discSize, discSize, false);
//
//        RoundedBitmapDrawable defaultDrawable = RoundedBitmapDrawableFactory.create
//                (mContext.getResources(), bitmapDefault);


        RequestOptions requestOptions = new RequestOptions()
                .transform(new CompositeCoverTransformation())
                .placeholder(R.drawable.ic_recordview_album_default)//图片加载出来前，显示的图片
                .fallback( R.drawable.ic_recordview_album_default) //url为空的时候,显示的图片
                .error(R.drawable.ic_recordview_album_default);//图片加载失败后，显示的图片
        Glide.with(mContext)
                .load(song.getPicUrl())
                .apply(requestOptions)
                .into(mCoverImageView);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);

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
