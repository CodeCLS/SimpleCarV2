package cls.android.simplecar.tools;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;



public class DirectionsTool {

    private static final String API_KEY = "AIzaSyAY_1zvQ4SWzKuGPnBvIxAt1FE3bObkjAk";
    private static final String TAG = "DirectionsTool";

    public static void getExactLocation(Context context, OnSuccessListener<? super Location> listener){

        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        fusedLocationClient.getLastLocation().addOnSuccessListener(listener);

    }
    public static void getWalkingTime(Context context,
                                      Location a,
                                      cls.android.simplecar.models.Location b,
                                      OnRetrieveWalkingTime onRetrieveWalkingTime ){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "https://maps.googleapis.com/maps/api/directions/json" +
                "?origin=" + a.getLatitude()+ "," + a.getLongitude() +
                "&mode=walking"+
                "&destination=" + b.getLatitude()+ "," + b.getLongitude() +
                "&key=" +API_KEY;
        Log.d(TAG, "getWalkingTime: " +url);
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null){
                            try {
                                Log.d(TAG, "onResponse: " + response);
                                JSONObject jsonObject = new JSONObject(response);
                                int duration = jsonObject.getJSONArray("routes")
                                        .getJSONObject(0)
                                        .getJSONArray("legs")
                                        .getJSONObject(0)
                                        .getJSONObject("distance")
                                        .getInt("value");
                                onRetrieveWalkingTime.time(duration);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                onRetrieveWalkingTime.time(-1);

                            }

                        }
                        else
                            onRetrieveWalkingTime.time(-1);

                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onRetrieveWalkingTime.time(-1);
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    public interface OnRetrieveWalkingTime{
        void time(int time);
    }
}
