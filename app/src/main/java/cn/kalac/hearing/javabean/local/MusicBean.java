package cn.kalac.hearing.javabean.local;

/**
 * @author ghn
 * @date 2019/12/4 17:13
 */
public class MusicBean {
    private int id;
    private String name;
    private ArtistBean artistBean;
    private AlbumBean albumBean;


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
}
