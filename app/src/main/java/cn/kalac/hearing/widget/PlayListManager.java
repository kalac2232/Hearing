package cn.kalac.hearing.widget;

import java.util.ArrayList;
import java.util.List;

import cn.kalac.hearing.javabean.local.MusicBean;

/**
 * @author kalac.
 * @date 2019/12/26 23:20
 */
public class PlayListManager {

    private static PlayListManager mPlayListManager;

    private List<MusicBean> mList;
    private static int mCurrentPlayPos = -1;

    private PlayListManager() {
        mList = new ArrayList<>();
    }

    public static PlayListManager getInstance() {
        if (mPlayListManager == null) {
            mPlayListManager = new PlayListManager();
        }
        return mPlayListManager;
    }

    public boolean isEmpty() {
        return mList == null || mList.isEmpty();
    }

    public MusicBean getCurrentMusic () {

        if (mCurrentPlayPos == -1) {
            return null;
        }

        return mList.get(mCurrentPlayPos);
    }

    public MusicBean getMusic(int position) {

        if (position >= mList.size()) {
            return null;
        }


        return mList.get(position);
    }

    public MusicBean getNext() {
        mCurrentPlayPos ++;
        return getCurrentMusic();
    }

    public MusicBean getPre() {
        mCurrentPlayPos --;
        return getCurrentMusic();
    }

    public void setCurrentPlayPos(int position) {
        mCurrentPlayPos = position;
    }

    public int getCurrentPlayPos() {
        return mCurrentPlayPos;
    }

    public void setMusicList(List<MusicBean> list,int position) {
        mList.addAll(list);
        mCurrentPlayPos = position;
    }

    public int getsize() {
        return mList == null ? 0 : mList.size();
    }
}
