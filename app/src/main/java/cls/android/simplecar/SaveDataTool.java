package cls.android.simplecar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.android.billingclient.api.Purchase;
import com.google.gson.Gson;

import cls.android.simplecar.models.User;


public class SaveDataTool {
    public static final String SMARTCAR_AUTH_CLIENT = "smartcar_auth_client";
    public static final String SMARTCAR_AUTH = "smartcar_auth";
    public static final String SIMPLECAR_NOTIFICATIONS = "simplecar_notifications_enabled_disabled";
    public static final String SIMPLECAR_PURCHASE = "simplecar_purchase";
    public static final String SIMPLECAR_USER_KEY = "simplecar_user_key";
    private static final String TAG = "SaveDataTool";
    public static final String SMARTCAR_ACCESS_KEY = "smart_car_access_key";
    public static final String CARLO_APP_SHARED_PREFERENCES = "carlo_app";
    public static final String SMARTCAR_AUTH_KEY = "smart_car_auth_key";
    public static final String SMARTCAR_CAR = "smart_car_car";

    private static SharedPreferences sharedPreferences;
    private final Context context;

    public SaveDataTool(Context context) {
        //ApplicationCOntext
        this.context = context;
        if (sharedPreferences == null)
            initShared();
    }

    private void initShared() {
        sharedPreferences = context.getSharedPreferences(CARLO_APP_SHARED_PREFERENCES, Context.MODE_PRIVATE);
    }
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public void saveString(String key, String code) {
        SharedPreferences.Editor editor =sharedPreferences.edit()
                .putString(key,code);
        editor.apply();

    }
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public void saveBoolean(String key, Boolean bool) {
        SharedPreferences.Editor editor =sharedPreferences.edit()
                .putBoolean(key,bool);
        Log.d(TAG,editor.commit()+ "save: ");
        editor.apply();

    }

    public String get(String key, Object defaultVal) {
        return sharedPreferences.getString(key,(String)defaultVal);
    }
    public Boolean getBoolean(String key, Object defaultVal) {
        return sharedPreferences.getBoolean(key,(Boolean)defaultVal);
    }

    public void savePurchase(Purchase purchase) {
        Gson gson = new Gson();
        SharedPreferences.Editor editor =sharedPreferences.edit()
                .putString(SIMPLECAR_PURCHASE,gson.toJson(purchase,Purchase.class));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            editor.apply();
        }
    }
    public Purchase getPurchase() {
        String val = get(SIMPLECAR_PURCHASE,null);
        Gson gson = new Gson();
        return gson.fromJson(val,Purchase.class);

    }

    public void listen(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    public void stopListening(SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    public void saveUser(User parseUser) {
        Gson gson = new Gson();
        SharedPreferences.Editor editor =sharedPreferences.edit()
                .putString(SIMPLECAR_USER_KEY,gson.toJson(parseUser,User.class));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            editor.apply();
        }

    }
    public User getUser() {
        String val = get(SaveDataTool.SIMPLECAR_USER_KEY,null);
        Gson gson = new Gson();
        return gson.fromJson(val,User.class);

    }
}
