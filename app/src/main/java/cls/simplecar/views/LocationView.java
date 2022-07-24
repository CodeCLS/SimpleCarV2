package cls.simplecar.views;
import cls.simplecar.R;
import cls.simplecar.models.Car;
import cls.simplecar.models.Location;
import cls.simplecar.tools.OnCarUpdate;
import cls.simplecar.models.Car;
import cls.simplecar.models.Location;
import cls.simplecar.tools.OnCarUpdate;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationView extends FrameLayout implements OnCarUpdate, OnMapReadyCallback{
    private static final int TAG_CODE_PERMISSION_LOCATION = 123;
    private Car car;
    private Location location;

    private MapView mapView;
    private GoogleMap googleMap;
    private Bundle savedInstance;
    private GoogleMap map;

    public LocationView(@NonNull Context context) {
        super(context);
        init();
    }

    public LocationView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LocationView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LocationView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View view =LayoutInflater.from(getContext()).inflate(R.layout.view_location,this);
        mapView = view.findViewById(R.id.map_google);
        mapView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                this.onClick(view);
            }
        });


    }




    @Override
    public void update(Car car) {
        this.car = car;
        this.location = car.getLocation();
        init();



    }




    private static final String TAG = "LocationView";


    public void setSavedInstance(Bundle savedInstanceState) {
        this.savedInstance = savedInstanceState;
    }

    public MapView getMapView() {
        return mapView;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: ");

        map = googleMap;
        map.clear();
        map.getUiSettings().setMyLocationButtonEnabled(false);
        //map.setMyLocationEnabled(true);
        map.getUiSettings().setScrollGesturesEnabled(false);
        //googleMap.setMyLocationEnabled(true);

        // Updates the location and zoom of the MapView
        if (car == null || car.getLocation() == null)
            return;
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(car.getLocation().getLatitude(), car.getLocation().getLongitude()), 14);
        map.animateCamera(cameraUpdate);
        map.addMarker(new MarkerOptions().position(new LatLng(car.getLocation().getLatitude(), car.getLocation().getLongitude())).title("Car"));

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                    LocationView.this.callOnClick();
                }
            }
        });
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getContext(), R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

    }
}
