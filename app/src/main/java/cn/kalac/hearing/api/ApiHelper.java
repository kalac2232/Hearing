package cn.kalac.hearing.api;

/*
 * Created by Kalac on 2019/2/1
 */

public class ApiHelper {
    public static final String HostUrl = "http://59.110.213.97:3000";
    /**
     * 手机登录
     */
    public static String getPhoneLoginUrl(String phone,String password){
        String url = HostUrl + "/login/cellphone?phone=" + phone+"&password=" + password;
        return url;
    }
    /**
     * 刷新用户状态
     */
    public static String getRefreshUrl(){
        String url = HostUrl + "/login/refresh";
        return url;
    }
    /**
     * 获取每日推荐歌曲
     */
    public static String getRecommendSongsUrl(){
        String url = HostUrl + "/recommend/songs";
        return url;
    }

    /**
     * 获取歌曲详情
     */
    public static String getSongDetailUrl(int songIds){
        String url = HostUrl + "/song/detail?ids=" + songIds;
        return url;
    }

    /**
     * 获取歌曲MP3
     */
    public static String getSongMp3Url(int songIds){
        String url = HostUrl + "/song/url?id=" + songIds;
        return url;
    }

    /**
     * 获取banner
     */
    public static String getBannerUrl(){
        String url = HostUrl + "/banner";
        return url;
    }

    /**
     * 获取热搜
     */
    public static String getSearchHot(){
        String url = HostUrl + "/search/hot";
        return url;
    }
}
