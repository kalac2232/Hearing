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
            //专辑
            AlbumBean albumBean = new AlbumBean(netAlbum.getId(), netAlbum.getName(), netAlbum.getType(),
                    netAlbum.getBlurPicUrl(), netAlbum.getCompany(),recommendBean.getPrivilege().getMaxbr() >= 999000);
            //设置是否为《独家》
            albumBean.setFlag(recommendBean.getPrivilege().getFlag() == 64 ? AlbumBean.Flag.sole : null);

            NetRecommendSongsBean.RecommendBean.ArtistsBeanX artistsBeanX = recommendBean.getArtists().get(0);
            //歌手
            ArtistBean artistBean = new ArtistBean(artistsBeanX.getId(), artistsBeanX.getName(), artistsBeanX.getPicUrl());

            MusicBean musicBean = new MusicBean(recommendBean.getId(), recommendBean.getName(), artistBean, albumBean);
            //设置别名
            musicBean.setAlias(recommendBean.getAlias());
            //设置mvid
            musicBean.setMvid(recommendBean.getMvid());
            musicBeans.add(musicBean);
        }

        return musicBeans;
    }
}
