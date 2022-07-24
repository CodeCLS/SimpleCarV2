package cls.simplecar;

import android.content.Context;

import com.smartcar.sdk.SmartcarAuth;
import com.smartcar.sdk.SmartcarCallback;

public class SmartCarLauncher {
    private static final String TAG = "SmartCarLauncher";

    public void connect(Context context,SmartcarCallback smartcarCallback){
        SmartcarAuth smartcarAuth = new SmartcarAuth(
                "3b683bb7-48a3-4b4c-8a2f-7337a8a0ee19",
                "sc3b683bb7-48a3-4b4c-8a2f-7337a8a0ee19://myapp.com/callback",
                new String[] {"read_vehicle_info", "read_odometer","read_engine_oil","read_battery" ,"read_charge" ,
                        "control_charge" , "read_thermometer","read_fuel" ,"read_location","control_security",
                        "read_odometer","read_tires","read_vehicle_info","read_vin"},true,
                // Create a callback to handle the redirect response changing
                smartcarCallback);
        smartcarAuth.launchAuthFlow(context);
    }

}
