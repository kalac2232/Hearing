package cn.kalac.hearing.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;


/*
 * Created by Kalac on 2019/2/2
 */

public class PlayMusicService extends Service {
    private MusicBinder mBinder ;

    @Override
    public void onCreate() {
        mBinder = new MusicBinder(getApplicationContext());
        super.onCreate();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
