package cn.kalac.hearing.javabean.local;

/*
 * Created by Kalac on 2019/2/19
 */

public class Song {
    private int songId;
    private String songName;
    private String singerName;
    private String picUrl;
    //private String Mp3Url;

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public Song(int songId, String songName, String singerName, String picUrl) {
        this.songId = songId;
        this.songName = songName;
        this.singerName = singerName;
        this.picUrl = picUrl;
    }
//    public String getMp3Url() {
//        return Mp3Url;
//    }
//
//    public void setMp3Url(String mp3Url) {
//        Mp3Url = mp3Url;
//    }
}
