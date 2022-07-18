package cls.android.simplecar;

import android.content.Context;


public class Application extends android.app.Application {
    private static Context context;

    public static String getSimpleCarApiCode() {
        return "oaiu3490S=?n´´03´´ß094ßn´´0";
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

    }
    public static Context getContext(){
        return context;
    }
}
