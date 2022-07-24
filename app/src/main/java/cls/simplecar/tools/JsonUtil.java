package cls.simplecar.tools;

import cls.simplecar.api.ApiManager;
import cls.simplecar.models.Car;
import cls.simplecar.models.Location;
import cls.simplecar.models.Range;
import cls.simplecar.api.Status;
import cls.simplecar.models.User;
import cls.simplecar.models.Car;
import cls.simplecar.models.Location;
import cls.simplecar.models.Range;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class JsonUtil {
    public JsonUtil() {
    }

    public Car parseCar(JSONObject json){
        Car car = new Car();
        try {
            car.setSmartCarId(json.getString("vehicleId"));
            car.setBrand(json.getString("vehicleMake"));
            Calendar calendar = Calendar.getInstance();
            calendar.set(Integer.parseInt(json.getString("vehicleYear")),0,0);
            long time = calendar.getTimeInMillis();
            car.setYear(time);
            car.setModel(json.getString("vehicleModel"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return car;



    }

    private static final String TAG = "JsonUtil";
    public Location parseLocation(JSONObject jsonObject) {
        Location location = new Location();
        try {
            location.setLongitude(Double.parseDouble(jsonObject.getString("longitude")));
            location.setLatitude(Double.parseDouble(jsonObject.getString("latitude")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "parseLocation: " + location.getLongitude());
        return location;
    }

    public boolean parseIsLocked(String response) {
        return response.contains("true");
    }

    public Range parseRange(String response) {
        Range range = new Range();
        try {
            range.setPercent(Double.parseDouble(new JSONObject(response).getString("percent")));
            range.setAmount(Double.parseDouble(new JSONObject(response).getString("range")));
            Log.d(TAG, "parseRange: " + response);

            return range;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String parseCode(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            return jsonObject.getString("accessCode");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String parseAuthClient(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            return jsonObject.getString("client");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String parseAuth(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            return jsonObject.getString("auth");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }    }

    public Boolean parseIsElectric(String responseMsg) {
        try {
            JSONObject jsonObject = new JSONObject(responseMsg);
            return jsonObject.getBoolean("is_electric");
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Car> parseCars(String body) {
        ArrayList<Car> cars = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(body);
            for (int i = 0; i < jsonArray.length(); i++){
                cars.add(new Car(jsonArray.getJSONObject(i).getString("id")));
                Log.d(TAG, "parseCars: " + jsonArray.getJSONObject(i).getString("id"));
            }
            Log.d(TAG, "parseCars: " + cars);
            return cars;


        } catch (JSONException e) {
            e.printStackTrace();
            return cars;
        }
    }

    public Status parseStatus(String body) {
        Status status = new Status();
        JSONObject jsonObject= null;
        try {
            jsonObject = new JSONObject(body);
            status.setErrorCode(jsonObject.getInt("error_code"));
            status.setSuccessfulAction(jsonObject.getBoolean("successful_action"));
            status.setAdditionalInformation(new JSONObject(jsonObject.getString("additional_information")));

        } catch (JSONException e) {
            e.printStackTrace();
            status = null;
        }
        return status;
    }

    public User parseUser(JSONObject additionalInformation) {
        User user = new User();
        Log.d(TAG, "parseUser: " + additionalInformation);
        try {
            user.setFirstName(additionalInformation.getString(ApiManager.Companion.getFIRST_NAME()));
            user.setSecondName(additionalInformation.getString(ApiManager.Companion.getFIRST_NAME()));
            user.setEmail(additionalInformation.getString(ApiManager.Companion.getEMAIL()));
            user.setPhone(additionalInformation.getInt(ApiManager.Companion.getPHONE()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }
}
