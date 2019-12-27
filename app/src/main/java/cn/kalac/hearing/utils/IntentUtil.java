package cn.kalac.hearing.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * @author kalac.
 * @date 2019/11/26 21:48
 */
public class IntentUtil {
    private static IntentUtil manager;
    private static Intent intent;

    private IntentUtil() {

    }

    public static IntentUtil get() {
        if (manager == null) {
            synchronized (IntentUtil.class) {
                if (manager == null) {
                    manager = new IntentUtil();
                }
            }
        }
        intent = new Intent();
        return manager;
    }


    /**
     * 启动一个Activity
     *
     * @param context
     * @param clazz
     */
    public void goActivity(Context context, Class<? extends Activity> clazz) {
        intent.setClass(context, clazz);
        context.startActivity(intent);
    }
    

    /**
     * 回调跳转
     *
     * @param context
     * @param clazz
     * @param requestCode
     */
    public void goActivityResult(Activity context, Class<? extends Activity> clazz, int requestCode) {
        intent.setClass(context, clazz);
        context.startActivityForResult(intent, requestCode);
    }


}