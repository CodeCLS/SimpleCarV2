package cls.simplecar.views;

import cls.simplecar.R;
import cls.simplecar.models.Car;
import cls.simplecar.tools.DirectionsTool;
import cls.simplecar.tools.OnCarUpdate;
import cls.simplecar.models.Car;
import cls.simplecar.tools.DirectionsTool;
import cls.simplecar.tools.OnCarUpdate;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Objects;

public class CarInfoView extends FrameLayout implements OnCarUpdate {
    private ImageView carImg;
    private TextView name;
    private TextView timeWalking;
    private TextView range;
    private TextView odometer;
    private Car car;
    private TextView titleName;

    public CarInfoView(@NonNull Context context) {
        super(context);
        init();
    }

    public CarInfoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public CarInfoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    public CarInfoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();

    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.bubble_car_info,this);
        carImg = findViewById(R.id.car_bubble_car_info);
        range = findViewById(R.id.text_charge_car_info);
        timeWalking = findViewById(R.id.text_walk_car_info);
        titleName = findViewById(R.id.your_car_txt_info_car);
        name = findViewById(R.id.your_car_txt_info_car_type);
        odometer = findViewById(R.id.text_odometer_car_info);
        Glide.with(getContext()).load("https://www.downloadclipart.net/large/tesla-png-transparent.png").centerInside().into(carImg);


    }

    private static final String TAG = "CarInfoView";
    @Override
    public void update(Car car) {
        post(new Runnable() {
            @Override
            public void run() {
                range.setText(car.getDriveProductAmount() + " km");
                odometer.setText(car.getOdometer() + " km");
                Spannable spannable = new SpannableString("Your Car (" + car.getVin() + ")");
                spannable.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                titleName.setText(spannable);
                if (car.getVin() == null)
                    titleName.setText("Your Car");
                if (car.getDriveProductAmount() < 0 )
                    range.setText("Loading");
                if (car.getOdometer() < 0)
                    odometer.setText("Loading");
                name.setText(mergeCarTitle(car.getBrand(),car.getName(),car.getModel()));

                DirectionsTool.getExactLocation(getContext(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (car.getLocation() == null || car.getLocation().getLatitude() == 51.5 )
                            return;
                        getWalktingTime(location);
                    }

                    private void getWalktingTime(Location location) {
                        DirectionsTool.getWalkingTime(getContext(),
                                location, car.getLocation(),
                                new DirectionsTool.OnRetrieveWalkingTime() {
                                    private static final String TAG = "CarInfoView";
                            @Override
                            public void time(int time) {
                                if (time < 1){
                                    timeWalking.setText("Unavailable");
                                }
                                else {
                                    Log.d(TAG, "time: " + time);
                                    timeWalking.setText(Math.round(time / 60) + " min");
                                }
                            }
                        });
                    }
                });

            }

            private String mergeCarTitle(String brand, String name, String model) {
                boolean isNameEmpty = name.equals("");
                if (isNameEmpty){
                    return brand + " " + model;
                }
                return brand +" " + name + " " + model;
            }
        });
        this.car = car;

    }
}
