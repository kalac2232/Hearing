package cn.kalac.hearing.utils;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.internal.Primitives;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static cn.kalac.hearing.utils.SharedPreUtils.getPref;
import static cn.kalac.hearing.utils.SharedPreUtils.savePref;

public class DataUtil {

    public static void saveJson(String key,String json) {
        if("".equals(json)) {
            return;
        }
        //用url经过md5转换后做为key保存数据方便断网时候取
        savePref(MD5Utils.convert(key),json);
    }

    public static <T> void saveList(String key,List<T> list) {
        if(list == null) {
            return;
        }
        Type type = new TypeToken<ArrayList<T>>(){}.getType();

        String toJson = new Gson().toJson(list, type);

        //用url经过md5转换后做为key保存数据方便断网时候取
        savePref(MD5Utils.convert(key),toJson);
    }

    /**
     * 从本地读取数据并返回javabean对象
     * @param key
     * @param classOfT
     * @param <T>
     * @return
     */
    public static <T> T loadBeanFormLoacl(String key, Class<T> classOfT) {
        String json = getPref(MD5Utils.convert(key), "");
        if ("".equals(json)) {
            return null;
        }
        Gson gson = new Gson();
        Object object =  gson.fromJson(json, (Type) classOfT);
        //从gson中偷来的 将object转为T类型
        return Primitives.wrap(classOfT).cast(object);
    }

    public static <T> List<T> loadListFormLoacl(String key, Class<T> classOfT) {
        String json = getPref(MD5Utils.convert(key), "");
        if ("".equals(json)) {
            return null;
        }
        List<T> lst = new ArrayList<>();
        try {
            JsonArray array = new JsonParser().parse(json).getAsJsonArray();
            for (final JsonElement elem : array) {
                lst.add(new Gson().fromJson(elem, classOfT));
            }
        } catch (Exception e) {
        }
        return lst;
    }

}
