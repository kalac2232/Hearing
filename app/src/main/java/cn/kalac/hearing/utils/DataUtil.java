package cn.kalac.hearing.utils;


import com.google.gson.Gson;
import com.google.gson.internal.Primitives;

import java.lang.reflect.Type;

import static cn.kalac.hearing.utils.SharedPreUtils.getPref;
import static cn.kalac.hearing.utils.SharedPreUtils.savePref;

public class DataUtil {

    public static void saveJson(String url,String json) {
        if("".equals(json)) {
            return;
        }
        //用url经过md5转换后做为key保存数据方便断网时候取
        savePref(MD5Utils.convert(url),json);
    }

    /**
     * 从本地读取数据并返回javabean对象
     * @param url
     * @param classOfT
     * @param <T>
     * @return
     */
    public static <T> T loadBeanFormLoacl(String url, Class<T> classOfT) {
        String json = getPref(MD5Utils.convert(url), "");
        if ("".equals(json)) {
            return null;
        }
        Gson gson = new Gson();
        Object object =  gson.fromJson(json, (Type) classOfT);
        //从gson中偷来的 将object转为T类型
        return Primitives.wrap(classOfT).cast(object);
    }


}
