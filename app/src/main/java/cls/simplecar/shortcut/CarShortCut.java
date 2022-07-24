package cls.simplecar.shortcut;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.drawable.IconCompat;

import cls.simplecar.R;


public class CarShortCut {
    public CarShortCut(Context context) {
        doWork(context);
    }

    private void doWork(Context context) {

        ShortcutInfoCompat shortcut = new ShortcutInfoCompat.Builder(context, "unlock_func")
                .setShortLabel("Unlock Car")
                .setLongLabel("Unlock your Car")
                .setIcon(IconCompat.createWithResource(context, R.drawable.ic_unlock))
                .setIntent(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.mysite.example.com/")))
                .build();

        ShortcutManagerCompat.pushDynamicShortcut(context, shortcut);
    }
}
