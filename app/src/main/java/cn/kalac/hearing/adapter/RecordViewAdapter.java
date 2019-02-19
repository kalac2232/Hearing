package cn.kalac.hearing.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;

import java.security.MessageDigest;

import cn.kalac.hearing.HearingApplication;
import cn.kalac.hearing.R;
import cn.kalac.hearing.api.ApiHelper;
import cn.kalac.hearing.javabean.song.SongDetailBean;
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

        ImageView discImageView = view.findViewById(R.id.iv_recordview_Disc);
        //获取当前播放的歌曲id
        Integer songId = HearingApplication.mPlayingSongList.get(HearingApplication.mCurrentPlayPos);

        //loadSongDetail(songId);
        String url = "https://p2.music.126.net/TwAneq36w2qHc3CPGQyvtw==/19078725765198700.jpg";
        Glide.with(mContext).load(url).into(mCoverImageView);
        //获取唱片图片
        Bitmap bitmap = getDiscBitmap();
        discImageView.setImageBitmap(bitmap);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);

    }

    /**
     * 通过歌曲id获取封面，歌曲名，歌手信息
     * @param songId id
     */
    private void loadSongDetail(int songId) {
        String url = ApiHelper.getSongDetailUrl(songId);

        HttpHelper.getInstance().get(url, new HttpCallback<SongDetailBean>() {

            @Override
            public void onSuccess(SongDetailBean songDetailBean) {
                int code = songDetailBean.getCode();
                if (code == 200) {
                    SongDetailBean.SongsBean songsBean = songDetailBean.getSongs().get(0);
                    //获取封面地址
                    String picUrl = songsBean.getAl().getPicUrl();
                    Log.i(TAG, "onSuccess: picurl"+picUrl);
                    RequestOptions requestOptions = new RequestOptions()
                            .placeholder(R.drawable.ic_recordview_album_default)//图片加载出来前，显示的图片
                            .fallback( R.drawable.ic_recordview_album_default) //url为空的时候,显示的图片
                            .error(R.drawable.ic_recordview_album_default);//图片加载失败后，显示的图片

                    Glide.with(mContext).load(picUrl).apply(requestOptions).into(mCoverImageView);
                }
            }

            @Override
            public void onFailed(String string) {
                Log.i(TAG, "onFailed: "+string);
            }
        });
    }

    class CompositeCoverTransformation extends BitmapTransformation{

        @Override
        protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
            return toTransform;
        }

        @Override
        public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

        }
    }

    /**
     * 得到唱盘图片
     * 唱盘图片由空心圆盘及音乐专辑图片“合成”得到
     */
    private Bitmap getDiscBitmap() {
        int discSize = (int) (DisplayUtil.getScreenWidth(mContext) * DisplayUtil.SCALE_DISC_SIZE);

        Bitmap bitmapDisc = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(mContext.getResources(), R
                .drawable.ic_recordview_disc), discSize, discSize, false);

        return bitmapDisc;
    }

    /**
     * 获取专辑图片
     * @param musicPicSize
     * @param musicPicRes
     * @return
     */
    private Bitmap getMusicPicBitmap(int musicPicSize, int musicPicRes) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeResource(mContext.getResources(),musicPicRes,options);
        int imageWidth = options.outWidth;

        int sample = imageWidth / musicPicSize;
        int dstSample = 1;
        if (sample > dstSample) {
            dstSample = sample;
        }
        options.inJustDecodeBounds = false;
        //设置图片采样率
        options.inSampleSize = dstSample;
        //设置图片解码格式
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(mContext.getResources(),
                musicPicRes, options), musicPicSize, musicPicSize, true);
    }
}
