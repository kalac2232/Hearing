package cn.kalac.hearing.widget;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.kalac.hearing.javabean.local.MusicBean;
import cn.kalac.hearing.utils.DataUtil;

/**
 * @author kalac.
 * @date 2019/12/26 23:20
 */
public class PlayListManager {

    private static PlayListManager mPlayListManager;

    private List<MusicBean> mList;
    private static int mCurrentPlayPos = -1;
    private static final String LIST_KEY = "_list_key";

    private PlayListManager() {
        mList = new ArrayList<>();
        //从本地加载之前添加过的播放列表
        List<MusicBean> localList = loadListFromLocal();
        if (localList != null && !localList.isEmpty()) {
            mList.addAll(localList);
            mCurrentPlayPos = 0;
        }
    }

    private List<MusicBean> loadListFromLocal() {
        return DataUtil.loadListFormLoacl(LIST_KEY,MusicBean.class);
    }

    private void saveListToLocal(List<MusicBean> list) {
        DataUtil.saveList(LIST_KEY,list);
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
        mList.clear();
        mList.addAll(list);
        mCurrentPlayPos = position;
        saveListToLocal(mList);
    }

    public int getsize() {
        return mList == null ? 0 : mList.size();
    }
}
