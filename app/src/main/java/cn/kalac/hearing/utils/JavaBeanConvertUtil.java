package cn.kalac.hearing.utils;

import java.util.ArrayList;
import java.util.List;

import cn.kalac.hearing.javabean.local.AlbumBean;
import cn.kalac.hearing.javabean.local.ArtistBean;
import cn.kalac.hearing.javabean.local.MusicBean;
import cn.kalac.hearing.javabean.net.NetRecommendSongsBean;

/**
 * @author kalac.
 * @date 2019/11/26 22:02
 */
public class JavaBeanConvertUtil {
    public static List<MusicBean> recomDailyListConvert(NetRecommendSongsBean netRecommendSongsBean) {
        ArrayList<MusicBean> musicBeans = new ArrayList<>();
        for (NetRecommendSongsBean.RecommendBean recommendBean : netRecommendSongsBean.getRecommend()) {
            NetRecommendSongsBean.RecommendBean.AlbumBean netAlbum = recommendBean.getAlbum();
            AlbumBean albumBean = new AlbumBean(netAlbum.getId(), netAlbum.getName(), netAlbum.getType(), netAlbum.getBlurPicUrl(), netAlbum.getCompany());

            NetRecommendSongsBean.RecommendBean.ArtistsBeanX artistsBeanX = recommendBean.getArtists().get(0);
            ArtistBean artistBean = new ArtistBean(artistsBeanX.getId(), artistsBeanX.getName(), artistsBeanX.getPicUrl());

            musicBeans.add(new MusicBean(recommendBean.getId(),recommendBean.getName(),artistBean,albumBean));
        }

        return musicBeans;
    }
}
