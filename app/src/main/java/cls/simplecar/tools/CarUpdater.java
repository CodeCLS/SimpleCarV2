package cls.simplecar.tools;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import javax.security.auth.callback.Callback;

import cls.simplecar.api.CarMarketValue;
import cls.simplecar.api.CarMarketValueCallback;
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
    private ExecutorService executorService;
    private final SimpleCarSdk simpleCarSdk;
    private static final String TAG = "CarUpdater";

    public CarUpdater(SimpleCarSdk simpleCarSdk) {
        this.simpleCarSdk = simpleCarSdk;
        ThreadFactory namedThreadFactory =
                new ThreadFactoryBuilder().setNameFormat("CarUpdaterThread").build();
        executorService = Executors.newFixedThreadPool(4,namedThreadFactory);
    }

    public void startUpdatingAttrs(Context context){
        CarDataBaseRepo.getInstance(context).getCars(cars -> {
            for (Car car : cars) {
                UpdaterConnection updaterConnection = new UpdaterConnection(() -> updateAllAttrs(context,car));
                CarAttributesUpdater carAttributesUpdater = new CarAttributesUpdater(updaterConnection);
                carAttributesUpdater.run();
            }
        });


    }

    private void updateAllAttrs(Context context,Car car) {
        ArrayList<String> permissions = car.getHasPermissions();
        executorService.execute(() -> {
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
                                if (car != null && location != null) {
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
        Log.d(TAG, "updateRangeOfCar: ");
        simpleCarSdk.getRange(car.getSmartCarId(),new RangeCallback() {
            @Override
            public void range(@Nullable Range range) {
                CarDataBaseRepo.getInstance(context).getCarWithSmartCarId(car.getSmartCarId(),
                        new CarDataBaseRepo.OnRetrieveCar() {
                            @Override
                            public void car(Car car) {
                                Log.d(TAG, "c123ar: " + car  + " " + range);
                                if (range != null) {
                                    Log.d(TAG, "car: " + car  + " " + range);
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
                                if (oil != null && car != null) {
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

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                updatePermissionsOfCar(context,car);
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        new SmartCarInspectPermissionsUtil(simpleCarSdk).updateToPermission(context,car, car.getHasPermissions());
                    }
                });

            }
        });

    }
    public void updateCarsFromOnline(Context context) {
        simpleCarSdk.getVehicleIds(new VehicleIdListCallback() {
            @Override
            public void getVehicles(@Nullable List<String> list) {
                if (list != null) {
                    for (String id: list){
                        if (id == null)
                            return;

                        simpleCarSdk.getVehicleAttributes(id, new VehicleCallback() {
                            @Override
                            public void getVehicle(@Nullable VehicleAttributes vehicleAttributes) {
                                if (vehicleAttributes == null)
                                    return;
                                CarDataBaseRepo.getInstance(context)
                                        .getCarWithSmartCarId(vehicleAttributes.getVehicleId()
                                                , new CarDataBaseRepo.OnRetrieveCar() {
                                                    @Override
                                                    public void car(Car car) {
                                                        if (car == null) {
                                                            Car newCar =Car.parseCar(vehicleAttributes);
                                                            CarDataBaseRepo.getInstance(context)
                                                                    .addCar(newCar);
                                                            updateMarketValue(newCar);
                                                        }
                                                        else{
                                                            updateSingleCarVars(context, car);
                                                        }

                                                    }
                                                });

                            }

                            private void updateMarketValue(Car newCar) {
                                simpleCarSdk.getMarketValue(newCar.getSmartCarId(), new CarMarketValueCallback() {
                                    @Override
                                    public void result(@NonNull CarMarketValue value) {
                                        newCar.setCarMarketValue(value);
                                    }
                                    @Override
                                    public void exception(@NonNull Exception exception) {
                                        Log.e(TAG, "exception1: "+ exception);
                                    }
                                });
                            }

                            @Override
                            public void exception(@NonNull Exception exception) {
                                Log.e(TAG, "exception2: "+ exception );

                            }
                        });

                    }
                }
                Log.d(TAG, "getVehicles: null");
            }

            @Override
            public void exception(@NonNull Exception exception) {
                Log.e(TAG, "exception3: "+ exception);

            }
        });
    }
}
