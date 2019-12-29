package cn.kalac.hearing.utils;

import android.content.Context;

/**
 * 唱片尺寸计算类
 */
public class TurntableDisplayUtil {

    /*手柄起始角度*/
    public static final float ROTATION_INIT_NEEDLE = -30;

    /*唱针宽高、距离等比例*/
    public static final float SCALE_NEEDLE_PIVOT_X = (float) (123 / 482);
    public static final float SCALE_NEEDLE_PIVOT_Y = (float) (120 / 775);

    /*唱盘比例*/
    public static final float SCALE_DISC_SIZE = (float) (807.0 / 1080.0);


    /*专辑图片比例*/
    public static final float SCALE_MUSIC_PIC_SIZE = (float) (533.0 / 1080.0);

}