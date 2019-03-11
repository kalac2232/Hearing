package cn.kalac.hearing.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {
    public static String getTime(long sec, String formatStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr, Locale.getDefault());
        String r = "";
        Date d = new Date();
        d.setTime(sec);
        r = sdf.format(d);
        return r;
    }
}
