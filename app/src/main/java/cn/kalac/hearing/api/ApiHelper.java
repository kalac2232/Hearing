package cn.kalac.hearing.api;

/*
 * Created by Kalac on 2019/2/1
 */

public class ApiHelper {

    /**
     * 手机登录
     */
    public static String getPhoneLoginUrl(String phone,String password){
        String url = Api.HostUrl + "/login/cellphone?phone=" + phone+"&password=" + password;
        return url;
    }
    /**
     * 获取每日推荐歌曲
     */
    public static String getRecommendSongsUrl(){
        String url = Api.HostUrl + "/recommend/songs";
        return url;
    }

    /**
     * 获取歌曲详情
     */
    public static String getSongDetailUrl(int songIds){
        String url = Api.HostUrl + "/song/detail?ids=" + songIds;
        return url;
    }

    /**
     * 获取歌曲MP3
     */
    public static String getSongMp3Url(int songIds){
        String url = Api.HostUrl + "/song/url?id=" + songIds;
        return url;
    }
}
