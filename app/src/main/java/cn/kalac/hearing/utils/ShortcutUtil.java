package cn.kalac.hearing.utils;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;

import java.util.Arrays;

import cn.kalac.hearing.R;

/**
 * @author ghn
 * @date 2019/12/25 11:46
 */
public class ShortcutUtil {

    public static void addShortcut(Application application) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            ShortcutManager shortcutManager = application.getSystemService(ShortcutManager.class);
            ShortcutInfo shortcut1 = new ShortcutInfo.Builder(application, "id1")
                    .setShortLabel("Web site")
                    .setLongLabel("播放本地音乐")
                    .setIcon(Icon.createWithResource(application, R.mipmap.shortcut_local_music))
                    .setIntent(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.mysite.example.com/")))
                    .build();
            ShortcutInfo shortcut2 = new ShortcutInfo.Builder(application, "id2")
                    .setShortLabel("Web site")
                    .setLongLabel("搜索")
                    .setIcon(Icon.createWithResource(application, R.mipmap.shortcut_search))
                    .setIntent(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.mysite.example.com/")))
                    .build();
            ShortcutInfo shortcut3 = new ShortcutInfo.Builder(application, "id3")
                    .setShortLabel("Web site")
                    .setLongLabel("私人FM")
                    .setIcon(Icon.createWithResource(application, R.mipmap.shortcut_fm))
                    .setIntent(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.mysite.example.com/")))
                    .build();
            ShortcutInfo shortcut4 = new ShortcutInfo.Builder(application, "id4")
                    .setShortLabel("Web site")
                    .setLongLabel("听歌识曲")
                    .setIcon(Icon.createWithResource(application, R.mipmap.shortcut_identify))
                    .setIntent(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.mysite.example.com/")))
                    .build();
            shortcutManager.setDynamicShortcuts(Arrays.asList(shortcut1,shortcut2,shortcut3,shortcut4));
        }
    }
}
