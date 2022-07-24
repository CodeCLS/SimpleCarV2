package cls.simplecar;

import android.content.Context;


public class Application extends android.app.Application {
    private static Context context;

    public static String getSimpleCarApiCode() {
        return "oaiu3490S=?nD2103_qR094Pn-+0";
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
