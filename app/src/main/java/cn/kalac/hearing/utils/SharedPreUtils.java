package cn.kalac.hearing.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import cn.kalac.hearing.HearingApplication;

public class SharedPreUtils {

    private static SharedPreferences getSharedPreferences(Context context) {
        String pkgName = context.getPackageName();
        return context.getSharedPreferences(pkgName+"_preferenrces", Context.MODE_PRIVATE);
    }

    public static void savePref(String key, String value) {
        SharedPreferences.Editor edit = getSharedPreferences(HearingApplication.getInstance()).edit();
        edit.putString(key,value);
        edit.apply();//提交
    }

    public static void savePref(String key, Integer value) {
        SharedPreferences.Editor edit = getSharedPreferences(HearingApplication.getInstance()).edit();
        edit.putInt(key,value);
        edit.apply();//提交
    }

    public static void savePref(String key, Long value) {
        SharedPreferences.Editor edit = getSharedPreferences(HearingApplication.getInstance()).edit();
        edit.putLong(key,value);
        edit.apply();//提交
    }

    public static void savePref(String key, Boolean value) {
        SharedPreferences.Editor edit = getSharedPreferences(HearingApplication.getInstance()).edit();
        edit.putBoolean(key,value);
        edit.apply();//提交
    }

    public static String getPref(String key, String defult) {
        SharedPreferences sharedPreferences = getSharedPreferences(HearingApplication.getInstance());
        return sharedPreferences.getString(key,defult);
    }
    public static Integer getPref(String key, Integer defult) {
        SharedPreferences sharedPreferences = getSharedPreferences(HearingApplication.getInstance());
        return sharedPreferences.getInt(key,defult);
    }

    public static Long getPref(String key, Long defult) {
        SharedPreferences sharedPreferences = getSharedPreferences(HearingApplication.getInstance());
        return sharedPreferences.getLong(key,defult);
    }

    public static Boolean getPref(String key, Boolean defult) {
        SharedPreferences sharedPreferences = getSharedPreferences(HearingApplication.getInstance());
        return sharedPreferences.getBoolean(key,defult);
    }



}
