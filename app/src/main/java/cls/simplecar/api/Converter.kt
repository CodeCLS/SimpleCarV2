package cls.simplecar.api;

import android.util.Log
import org.json.JSONArray
import org.json.JSONObject

class Converter : LocationConversion, RangeConversion,VehicleListConversion,ApiResultConversion, SmartCarAuthConversion{
    override fun convertLocation(body: String?): Location? {
        var jsonObject = JSONObject(body);
        var isSuccessful = jsonObject.getBoolean(ApiManager.SUCCESSFUL_ACTION)
        if (isSuccessful) {
            var latitude: Double = jsonObject.getDouble(ApiManager.LATITUDE)
            var longitude: Double = jsonObject.getDouble(ApiManager.LONGITUDE)
            Log.d(TAG, "convertLocation: " + latitude + longitude)
            return Location(latitude, longitude)
        }
        return null
    }

    override fun convertRange(body: String?): Range? {
        var jsonObject = JSONObject(body);
        var isSuccessful = jsonObject.getBoolean(ApiManager.SUCCESSFUL_ACTION)
        if (isSuccessful) {
            var percent: Double = jsonObject.getDouble(ApiManager.RANGE_PERCENT)
            var radius: Double = jsonObject.getDouble(ApiManager.RANGE_RADIUS)
            return Range(percent,radius)
        }
        return null
    }

    override fun convertVehicleList(body: String?): List<String>? {
        if (body == null)
            return null
        var jsonObject = JSONObject(body);
        var isSuccessful = jsonObject.getBoolean(ApiManager.SUCCESSFUL_ACTION)
        if (isSuccessful) {
            var jsonArray : JSONArray = jsonObject.getJSONArray(ApiManager.VEHICLE_IDS)
            var list : ArrayList<String> = ArrayList<String>()
            Log.d(TAG, "convertVehicleList: " + jsonArray.length())
            for (i in 0 until (jsonArray.length())){
                list.add(jsonArray.getString(i))

            }
            return list
        }
        return null
    }

    override fun convertVehicleAttributes(string: String?): VehicleAttributes? {
        var jsonObject = JSONObject(string);
        var isSuccessful = jsonObject.getBoolean(ApiManager.SUCCESSFUL_ACTION)
        if (isSuccessful) {
            var vehicleId: String = jsonObject.getString(ApiManager.VEHICLE_ID)
            var vehicleMake: String = jsonObject.getString(ApiManager.VEHICLE_MAKE)
            var vehicleModel: String = jsonObject.getString(ApiManager.VEHICLE_MODEL)
            var vehicleYear: String = jsonObject.getString(ApiManager.VEHICLE_YEAR)
            return VehicleAttributes(vehicleId,vehicleMake,vehicleModel,vehicleYear)
        }
        return null
    }


    private val TAG = "Converter"
    override fun convertApiResult(body: String?): Boolean {
        if (body == null)
            return false;
        var jsonObject = JSONObject(body);
        Log.d(TAG, "convertApiResult: "+jsonObject.getBoolean(ApiManager.SUCCESSFUL_ACTION))
        return jsonObject.getBoolean(ApiManager.SUCCESSFUL_ACTION)
    }

    override fun convertAdditional(parent: String?, body: String?) : JSONObject {
        return JSONObject(body).getJSONObject(parent)
    }

    override fun convertException(body: String?): Exception {
        if (body == null)
            return cls.simplecar.api.Exception("None",-1)
        var jsonObject = JSONObject(body);
        Log.d(TAG, "convertException: " + body)

        var isSuccessful = jsonObject.getBoolean(ApiManager.SUCCESSFUL_ACTION)
        if (!isSuccessful) {
            var exceptionMsg: String = jsonObject.getString(ApiManager.EXCEPTION_MSG)
            var exceptionCode: Int = jsonObject.getInt(ApiManager.EXCEPTION_CODE)
            return cls.simplecar.api.Exception(exceptionMsg,exceptionCode)
        }
        return cls.simplecar.api.Exception("",-1)
    }

    override fun convertAuthResult(body: String?) : ApiSmartCarAuthPackage? {
        if (body == null)
            return null
        var jsonObject = JSONObject(body);
        var isSuccessful = jsonObject.getBoolean(ApiManager.SUCCESSFUL_ACTION)
        if (isSuccessful) {
            var accessToken: String = jsonObject.getString(ApiManager.ACCESS_TOKEN)
            var refreshToken: String = jsonObject.getString(ApiManager.REFRESH_TOKEN)
            var authClient: String = jsonObject.getString(ApiManager.AUTH_CLIENT)
            var auth: String = jsonObject.getString(ApiManager.AUTH)
            return ApiSmartCarAuthPackage(accessToken,refreshToken,auth,authClient)
        }
        return null
    }

}