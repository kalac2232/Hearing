package cn.kalac.hearing.javabean.local;

/**
 * 歌手信息
 * @author ghn
 * @date 2019/12/4 17:02
 */
public class ArtistBean {
    private int id;
    /**
     * 姓名
     */
    private String name;

    /**
     * 照片地址
     */
    private String picUrl;

    public ArtistBean(int id, String name, String picUrl) {
        this.id = id;
        this.name = name;
        this.picUrl = picUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
