package cn.kalac.hearing.api;

/*
 * Created by Kalac on 2019/2/1
 */

public class ApiHelper {

    /**
     * 获取歌曲详情
     */
    public static String getSongDetail(int songIds){
        String url = Api.HostUrl + "/song/detail?ids=" + songIds;
        return url;
    }

    /**
     * 获取歌曲MP3
     */
    public static String getSongMp3(int songIds){
        String url = Api.HostUrl + "/song/url?id=" + songIds;
        return url;
    }
}
