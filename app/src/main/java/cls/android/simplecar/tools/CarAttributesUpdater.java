package cls.android.simplecar.tools;

import android.util.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CarAttributesUpdater {
    private static final String TAG = "CarAttributesUpdater";
    private final UpdaterConnection updaterConnection;

    public CarAttributesUpdater(UpdaterConnection updaterConnection) {
        this.updaterConnection = updaterConnection;
    }
    public void run(){
        ScheduledExecutorService scheduler =
                Executors.newScheduledThreadPool(5);

        scheduler.scheduleAtFixedRate
                (new Runnable() {
                    public void run() {
                        updaterConnection.update();
                        Log.d(TAG, "run:updated ");
                    }
                }, 0, 600, TimeUnit.SECONDS);
    }

}

