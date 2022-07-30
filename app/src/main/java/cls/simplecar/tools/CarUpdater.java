package cls.simplecar.tools;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.security.auth.callback.Callback;

import cls.simplecar.api.Exception;
import cls.simplecar.api.LocationCallback;
import cls.simplecar.api.Odometer;
import cls.simplecar.api.Oil;
import cls.simplecar.api.OilCallback;
import cls.simplecar.api.Range;
import cls.simplecar.api.RangeCallback;
import cls.simplecar.api.SimpleCarSdk;
import cls.simplecar.api.VehicleAttributes;
import cls.simplecar.api.VehicleCallback;
import cls.simplecar.api.VehicleIdListCallback;
import cls.simplecar.api.VehicleOdometerCallback;
import cls.simplecar.api.VehiclePermissionsCallback;
import cls.simplecar.database.CarDataBaseRepo;
import cls.simplecar.models.Car;
import cls.simplecar.models.Location;

public class CarUpdater {
    private ExecutorService executorService = Executors.newFixedThreadPool(4);
    private final SimpleCarSdk simpleCarSdk;
    private static final String TAG = "CarUpdater";

    public CarUpdater(SimpleCarSdk simpleCarSdk) {
        this.simpleCarSdk = simpleCarSdk;
    }

    public void startUpdatingAttrs(Context context){
        CarDataBaseRepo.getInstance(context).getCars(cars -> {
            for (Car car : cars) {
                updateAllAttrs(context, car);
                UpdaterConnection updaterConnection = new UpdaterConnection(() -> updateAllAttrs(context,car));
                CarAttributesUpdater carAttributesUpdater = new CarAttributesUpdater(updaterConnection);
                carAttributesUpdater.run();
            }
        });


    }

    private void updateAllAttrs(Context context,Car car) {
        ArrayList<String> permissions = car.getHasPermissions();
        Future<?> i = executorService.submit(() -> {
            new SmartCarInspectPermissionsUtil(simpleCarSdk).updateToPermission(context, car,permissions);



        });

    }
    public void updateLocationOfCar(Context context, Car car) {
        simpleCarSdk.getLocation(car.getSmartCarId(),new LocationCallback() {
            @Override
            public void location(@Nullable cls.simplecar.api.Location location) {
                CarDataBaseRepo.getInstance(context).getCarWithSmartCarId(car.getSmartCarId(),
                        new CarDataBaseRepo.OnRetrieveCar() {
                            @Override
                            public void car(Car car) {
                                Log.d(TAG, "car:1 " + car.getRoomId() + car.getSmartCarId());
                                if (location != null) {
                                    Log.d(TAG, "car:11 " + car.getRoomId() + car.getSmartCarId() + " " + location);
                                    car.setLocation(new Location(location.getLatitude(), location.getLongitude()));
                                    CarDataBaseRepo.getInstance(context).updateCar(car);
                                }

                            }
                        });
            }

            @Override
            public void exception(@NonNull Exception exception) {

                //Toast.makeText(context, exception.getMsg(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void updateRangeOfCar(Context context, Car car) {
        simpleCarSdk.getRange(car.getSmartCarId(),new RangeCallback() {
            @Override
            public void range(@Nullable Range range) {
                CarDataBaseRepo.getInstance(context).getCarWithSmartCarId(car.getSmartCarId(),
                        new CarDataBaseRepo.OnRetrieveCar() {
                            @Override
                            public void car(Car car) {
                                if (range != null) {
                                    car.setDriveProductAmountPercent(range.getPercent());
                                    car.setDriveProductAmount(range.getAmount());
                                    CarDataBaseRepo.getInstance(context).updateCar(car);

                                }

                            }
                        });
            }

            @Override
            public void exception(@NonNull Exception exception) {
                Log.d(TAG, "exception:123 " + exception);
                //Toast.makeText(context, exception.getMsg(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateOilOfCar(Context context, Car car) {
        simpleCarSdk.getOil(car.getSmartCarId(),new OilCallback() {
            @Override
            public void oil(@Nullable Oil oil) {
                CarDataBaseRepo.getInstance(context).getCarWithSmartCarId(car.getSmartCarId(),
                        new CarDataBaseRepo.OnRetrieveCar() {
                            @Override
                            public void car(Car car) {
                                if (oil != null) {
                                    car.setOilPercentage(oil.getOilPercentage());
                                    CarDataBaseRepo.getInstance(context).updateCar(car);




                                }

                            }
                        });
            }

            @Override
            public void exception(@NonNull Exception exception) {
                Log.d(TAG, "exception:123 " + exception);
                //Toast.makeText(context, exception.getMsg(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateOdometerOfCar(Context context, Car car) {
        simpleCarSdk.getOdometer(car.getSmartCarId(),new VehicleOdometerCallback() {
            @Override
            public void result(@Nullable Odometer result) {
                CarDataBaseRepo.getInstance(context).getCarWithSmartCarId(car.getSmartCarId(),
                        new CarDataBaseRepo.OnRetrieveCar() {
                            @Override
                            public void car(Car car) {
                                if (result != null) {
                                    car.setOdometer(result.getOdometer());
                                    CarDataBaseRepo.getInstance(context).updateCar(car);

                                }

                            }
                        });
            }

            @Override
            public void exception(@NonNull Exception exception) {
                Log.d(TAG, "exception:123 " + exception);
                //Toast.makeText(context, exception.getMsg(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updatePermissionsOfCar(Context context, Car car) {
        simpleCarSdk.getPermissions(car.getSmartCarId(),new VehiclePermissionsCallback() {
            @Override
            public void result(@Nullable ArrayList<String> result) {
                CarDataBaseRepo.getInstance(context).getCarWithSmartCarId(car.getSmartCarId(),
                        new CarDataBaseRepo.OnRetrieveCar() {
                            @Override
                            public void car(Car car) {
                                if (result != null) {
                                    car.setHasPermissions(result);
                                    CarDataBaseRepo.getInstance(context).updateCar(car);

                                }

                            }
                        });
            }

            @Override
            public void exception(@NonNull Exception exception) {
                Log.d(TAG, "exception:123 " + exception);
                //Toast.makeText(context, exception.getMsg(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void updateSingleCarVars(Context context, Car car) {
        updateLocationOfCar(context,car);
        updateRangeOfCar(context,car);
        updateOdometerOfCar(context,car);
        updateOilOfCar(context,car);
        updatePermissionsOfCar(context,car);

    }
    public void updateCarsFromOnline(Context context) {
        simpleCarSdk.getVehicleIds(new VehicleIdListCallback() {
            @Override
            public void getVehicles(@Nullable List<String> list) {
                Log.d(TAG, "getVehicles: " + list);
                if (list != null) {
                    for (String id: list){
                        if (id == null)
                            continue;

                        simpleCarSdk.getVehicleAttributes(id, new VehicleCallback() {
                            @Override
                            public void getVehicle(@Nullable VehicleAttributes vehicleAttributes) {
                                if (vehicleAttributes == null)
                                    return;
                                CarDataBaseRepo.getInstance(context)
                                        .addCar(Car.parseCar(vehicleAttributes));
                            }

                            @Override
                            public void exception(@NonNull Exception exception) {
                                Log.e(TAG, "exception: "+ exception );

                            }
                        });

                    }
                }
                Log.d(TAG, "getVehicles: null");
            }

            @Override
            public void exception(@NonNull Exception exception) {
                Log.e(TAG, "exception: "+ exception);

            }
        });
    }
}
