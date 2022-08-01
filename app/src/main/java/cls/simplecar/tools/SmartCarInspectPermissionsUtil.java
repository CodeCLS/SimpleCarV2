package cls.simplecar.tools;

import android.content.Context;

import java.util.ArrayList;

import cls.simplecar.api.SimpleCarSdk;
import cls.simplecar.models.Car;

public class SmartCarInspectPermissionsUtil extends CarUpdater {
    public SmartCarInspectPermissionsUtil(SimpleCarSdk simpleCarSdk) {
        super(simpleCarSdk);
    }

    public void updateToPermission(Context context, Car car, ArrayList<String> permissions) {

        if (permissions.contains("read_engine_oil")){
            updateOilOfCar(context,car);
        }
        if (permissions.contains("read_location")){
            updateLocationOfCar(context, car);
        }
        if (permissions.contains("read_battery") || permissions.contains("read_fuel")) {
            updateRangeOfCar(context, car);
        }
        if (permissions.contains("read_odometer")){
            updateOdometerOfCar(context,car);
        }
    }
}
