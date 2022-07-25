package cls.simplecar.views;

import cls.simplecar.R;
import cls.simplecar.models.Car;
import cls.simplecar.tools.DirectionsTool;
import cls.simplecar.tools.OnCarUpdate;
import cls.simplecar.models.Car;
import cls.simplecar.tools.DirectionsTool;
import cls.simplecar.tools.OnCarUpdate;

import android.content.Context;
import android.location.Location;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;

public class CarInfoView extends FrameLayout implements OnCarUpdate {
    private ImageView carImg;
    private TextView name;
    private TextView timeWalking;
    private TextView range;
    private Car car;

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
        name = findViewById(R.id.your_car_txt_info_car_type);
        Glide.with(getContext()).load("https://www.downloadclipart.net/large/tesla-png-transparent.png").centerInside().into(carImg);


    }

    @Override
    public void update(Car car) {
        this.car = car;
        post(new Runnable() {
            @Override
            public void run() {
                range.setText(car.getDriveProductAmount() + " km");
                if (car.getDriveProductAmount() < 0 )
                    range.setText("Loading");
                name.setText(mergeCarTitle(car.getBrand(),car.getName(),car.getModel()));
                DirectionsTool.getExactLocation(getContext(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
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


    }
}
