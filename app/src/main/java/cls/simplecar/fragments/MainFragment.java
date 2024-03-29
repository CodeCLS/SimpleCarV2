package cls.simplecar.fragments;

import cls.simplecar.MainActivity;

import cls.simplecar.R;
import cls.simplecar.SaveDataTool;
import cls.simplecar.Spawner;

import cls.simplecar.api.Exception;
import cls.simplecar.models.Car;
import cls.simplecar.views.CarChargeView;
import cls.simplecar.views.CarInfoView;
import cls.simplecar.views.LocationView;
import cls.simplecar.views.MonthlyPlanView;
import cls.simplecar.views.StandardButton;
import cls.simplecar.api.ApiResult;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {
    private StandardButton unlock;
    private StandardButton information;
    private StandardButton notifications;

    private ConstraintLayout parent;
    private CarViewModel viewModel;

    private CarInfoView carInfoView;
    private CarChargeView carOilView;
    private CarChargeView carChargeView;
    private MonthlyPlanView monthlyPlanView;
    private StandardButton lock;
    private StandardButton startChargeBtn;
    private StandardButton endChargeBtn;
    private MapView mapView;
    private View rootView;
    private LocationView locationView;
    private GoogleMap map;
    private LinearLayout carOptionsContainer;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView= inflater.inflate(R.layout.fragment_main,container,false);

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mapView == null)
            return;
        mapView.onCreate(savedInstanceState);



    }

    private static final String TAG = "MainFragment";

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ((MainActivity)getActivity()).getViewModelCar();



        unlock = view.findViewById(R.id.main_unlock_btn);
        lock = view.findViewById(R.id.main_lock_btn);
        unlock.setHasTemporarelyCancelledFunction(true);
        lock.setHasTemporarelyCancelledFunction(true);
        startChargeBtn = view.findViewById(R.id.main_car_start_charge_btn);
        endChargeBtn = view.findViewById(R.id.main_car_end_charge_btn);
        MapsInitializer.initialize(getContext());
        carOptionsContainer = view.findViewById(R.id.car_list_linearlayout);


        parent = view.findViewById(R.id.main_fragment_parent);
        locationView = view.findViewById(R.id.location_main_fragment);
        carOilView = view.findViewById(R.id.car_oil_view);

        mapView = locationView.getMapView();
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(locationView);

        carInfoView = view.findViewById(R.id.car_info_view_main);
        carChargeView = view.findViewById(R.id.car_charge_view);
        monthlyPlanView = view.findViewById(R.id.monthly_plan_view_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            monthlyPlanView.setAlpha(0.2F);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                monthlyPlanView.setForegroundTintList(ColorStateList.valueOf(Color.GRAY));
            }
        }

        information = view.findViewById(R.id.standard_button_information);
        notifications = view.findViewById(R.id.standard_button_notifications);
        //notifications.setClickableTheme(false);
        SaveDataTool saveDataTool = new SaveDataTool(getContext());
        boolean isEnabled = saveDataTool.getBoolean(SaveDataTool.SIMPLECAR_NOTIFICATIONS,false);
        notifications.theme(isEnabled ? StandardButton.DISABLED : StandardButton.ENABLED);

        viewModel.startUpdatingAttrs(getContext());


        notifications.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick:notifications ");

                SaveDataTool saveDataTool = new SaveDataTool(getContext());
                boolean isEnabled = saveDataTool.getBoolean(SaveDataTool.SIMPLECAR_NOTIFICATIONS,false);
                Log.d(TAG, "onClick:notifications " + isEnabled);
                if (isEnabled){
                    notifications.theme(StandardButton.ENABLED);
                    saveDataTool.saveBoolean(SaveDataTool.SIMPLECAR_NOTIFICATIONS,false);
                }
                else{
                    notifications.theme(StandardButton.DISABLED);
                    saveDataTool.saveBoolean(SaveDataTool.SIMPLECAR_NOTIFICATIONS,true);

                }


            }
        });
        unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                viewModel.unlockCar(getContext(), new ApiResult() {
                    @Override
                    public void exception(@NonNull Exception exception) {
                        Log.e(TAG, "exception: "+ exception);

                    }

                    @Override
                    public void result(boolean result) {
                        if (result)
                            Toast.makeText(getContext(), R.string.unlock_request_send, Toast.LENGTH_SHORT).show();
                        else{
                            Toast.makeText(getContext(), R.string.unlock_request_send_failure, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        startChargeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                viewModel.startChargingCar(getContext(), new ApiResult() {
                    @Override
                    public void exception(@NonNull Exception exception) {

                    }

                    @Override
                    public void result(boolean result) {
                        if (result)
                            Toast.makeText(getContext(), R.string.unlock_request_send, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        endChargeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                viewModel.stopChargingCar(getContext(), new ApiResult() {
                    @Override
                    public void exception(@NonNull Exception exception) {

                    }

                    @Override
                    public void result(boolean result) {
                        if (result)
                            Toast.makeText(getContext(), R.string.unlock_request_send, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        locationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectToMaps();
            }

            private void redirectToMaps() {
                Uri gmmIntentUri = Uri.parse("geo:" + viewModel.getCarMutableLiveData().getValue().getLocation().getLatitude() + " , " + viewModel.getCarMutableLiveData().getValue().getLocation().getLongitude());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getContext().getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        });
        lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                viewModel.lockCar(getContext(), new ApiResult() {
                    @Override
                    public void exception(@NonNull Exception exception) {
                        Log.e(TAG, "exception: "+ exception);

                    }

                    @Override
                    public void result(boolean result) {
                        if (result)
                            Toast.makeText(getContext(), R.string.lock_request_send, Toast.LENGTH_SHORT).show();
                        else{
                            Toast.makeText(getContext(), R.string.lock_request_send_failure, Toast.LENGTH_SHORT).show();

                        }
                       // if (result) {
                       //     String title = "Lock";
                       //     String desc = "A lock request has been sent to your car. If your car isn't locked within the minute, please try again later or contact support";
                       //     Spawner.queueSpawnMessageToast(title, desc, true, view, parent);
                       // }
                    }
                });

                //Spawner.spawnMessageToast(
                //        view,
                //        "Lock" ,
                //        "A lock request has been sent to your car. If your car isn't locked within the minute, please try again later or contact support.",
                //        parent);

            }
        });
        information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Spawner.queueSpawnMessageToast(
                        "SimpleCar" ,
                        "SimpleCar is an app developed by Caleb Seeling in May 2022. It focuses on the attributes of a car. Support: primocaleb@gmail.com ",
                        true,
                        view,
                        parent);
            }
        });
        viewModel.getCarMutableLiveData().observe(getActivity(), new Observer<Car>() {
            @Override
            public void onChanged(Car car) {
                viewModel.currentCar = car;
                if (car != null) {
                    updateViews(car);
                    locationView.setVisibility(View.VISIBLE);
                    carInfoView.setVisibility(View.VISIBLE);
                    monthlyPlanView.setVisibility(View.VISIBLE);
                    carChargeView.setVisibility(View.VISIBLE);
                    carOilView.setVisibility(View.VISIBLE);
                }
                else{
                    locationView.setVisibility(View.GONE);
                    carInfoView.setVisibility(View.GONE);
                    monthlyPlanView.setVisibility(View.GONE);
                    carChargeView.setVisibility(View.GONE);
                    carOilView.setVisibility(View.GONE);
                    //TODO reauthenticate with car
                }
            }
        });
        viewModel.getCarsLiveData().observe(getActivity(), new Observer<List<Car>>() {
            private List<Car> carOptions;

            @Override
            public void onChanged(List<Car> cars) {
                this.carOptions = cars;
                if (!MainFragment.this.isVisible()) {
                    return;
                }
                carOptionsContainer.removeAllViews();
                if (viewModel.currentCar == null)
                    viewModel.setSelectedCar(getContext());


                for (Car car: cars) {
                    StandardButton standardButton = new StandardButton(getActivity());
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            standardButton.setText(mergeCarTitle(car.getBrand(),car.getName(),car.getModel()));
                            standardButton.postInvalidate();

                        }
                    });
                    drawCarOptionView(car, standardButton);
                }
            }
            private String mergeCarTitle(String brand, String name, String model) {
                boolean isNameEmpty = name.equals("");
                if (isNameEmpty){
                    return brand + " " + model;
                }
                return brand +" " + name + " " + model;
            }

            private void drawCarOptionView(Car car, StandardButton standardButton) {
                standardButton.setText(mergeCarTitle(car.getBrand(),car.getName(),car.getModel()));
                FrameLayout.LayoutParams layoutparam = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                int margin = getResources().getDimensionPixelSize(R.dimen.margin_regular);
                standardButton.setLayoutParams(layoutparam);
                standardButton.setPadding(0, 0, margin, 0);
                carOptionsContainer.addView(standardButton);
                standardButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewModel.changeSelectedCar(getContext(),car);
                    }
                });

                carOptionsContainer.postInvalidate();
            }
        });

        //SupportMapFragment supportMapFragment =(SupportMapFragment)getChildFragmentManager().findFragmentByTag("123");
        //if (supportMapFragment != null) {
        //    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
        //        @Override
        //        public void onMapReady(@NonNull GoogleMap googleMap) {
//
        //        }
        //    });
        //}
    }



    private void updateViews(Car car) {
        ArrayList<String> permissions = car.getHasPermissions();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            carOilView.setAlpha(0.2F);

            locationView.setAlpha(0.2F);
            carChargeView.setAlpha(0.2F);
            monthlyPlanView.setAlpha(0.2F);
            if (permissions.contains("read_engine_oil")) {
                carOilView.setAlpha(1.0F);
            }
            if (permissions.contains("read_location")) {
                locationView.setAlpha(1.0F);
            }
            if (permissions.contains("read_battery") || permissions.contains("read_fuel")) {
                carChargeView.setAlpha(1.0F);
            }
        }
        mapView.getMapAsync(locationView);
        locationView.update(car);
        carInfoView.update(car);
        carChargeView.update(CarChargeView.CHARGE,car);
        monthlyPlanView.update(car);
        carOilView.update(CarChargeView.OIL,car);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);



    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();

    }

    @Override
    public void onStop() {
        super.onStop();

        mapView.onStop();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


}
