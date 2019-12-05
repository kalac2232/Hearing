package cn.kalac.hearing.javabean.local;

import java.util.List;

/**
 * @author ghn
 * @date 2019/12/4 17:13
 */
public class MusicBean {
    private int id;
    private String name;
    private ArtistBean artistBean;
    private AlbumBean albumBean;
    /**
     * 别名
     */
    private List<String> alias;

    private int mvid;

    public MusicBean(int id, String name, ArtistBean artistBean, AlbumBean albumBean) {
        this.id = id;
        this.name = name;
        this.artistBean = artistBean;
        this.albumBean = albumBean;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArtistBean getArtistBean() {
        return artistBean;
    }

    public void setArtistBean(ArtistBean artistBean) {
        this.artistBean = artistBean;
    }

    public AlbumBean getAlbumBean() {
        return albumBean;
    }

    public void setAlbumBean(AlbumBean albumBean) {
        this.albumBean = albumBean;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getAlias() {
        return alias;
    }

    public void setAlias(List<String> alias) {
        this.alias = alias;
    }

    public int getMvid() {
        return mvid;
    }

    public void setMvid(int mvid) {
        this.mvid = mvid;
    }
}
