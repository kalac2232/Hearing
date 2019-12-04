package cn.kalac.hearing.javabean.local;

/**
 * 专辑
 * @author ghn
 * @date 2019/12/4 17:07
 */
public class AlbumBean {
    private int id;
    private String name;
    /**
     * 专辑类型
     */
    private String type;
    private String picUrl;
    /**
     * 公司
     */
    private String company;

    public AlbumBean(int id, String name, String type, String picUrl, String company) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.picUrl = picUrl;
        this.company = company;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
