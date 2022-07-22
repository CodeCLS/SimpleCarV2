package cls.android.simplecar.fragments;

import cls.android.simplecar.Application;
import cls.android.simplecar.R;
import cls.android.simplecar.SaveDataTool;
import cls.android.simplecar.Spawner;
import cls.android.simplecar.UserRepository;
import cls.android.simplecar.api.ApiAuthPackageCallback;
import cls.android.simplecar.api.ApiResult;
import cls.android.simplecar.api.ApiSmartCarAuthPackage;
import cls.android.simplecar.api.Exception;
import cls.android.simplecar.api.LocationCallback;
import cls.android.simplecar.api.RangeCallback;
import cls.android.simplecar.api.SimpleCarSdk;
import cls.android.simplecar.api.VehicleAttributes;
import cls.android.simplecar.api.VehicleCallback;
import cls.android.simplecar.api.VehicleIdListCallback;
import cls.android.simplecar.database.CarDataBaseRepo;
import cls.android.simplecar.models.Car;
import cls.android.simplecar.models.Location;
import cls.android.simplecar.SmartCarLauncher;


import cls.android.simplecar.models.Range;
import cls.android.simplecar.models.User;
import cls.android.simplecar.tools.CarAttributesUpdater;
import cls.android.simplecar.tools.DateUtil;
import cls.android.simplecar.tools.UpdaterConnection;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.smartcar.sdk.SmartcarResponse;

import java.util.List;
import java.util.concurrent.Executors;

public class CarViewModel extends ViewModel {
    private static final String TAG = "CarViewModel";
    private SimpleCarSdk simpleCarSdk;
    private SaveDataTool saveDataTool;
    public MutableLiveData<Car> carMutableLiveData = new MutableLiveData<Car>();
    public LiveData<List<Car>> carsLiveData = new MutableLiveData<List<Car>>();


    public MutableLiveData<Location> monthlySubscriptionMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> isNotificationsEnabled = new MutableLiveData<>();
    public MutableLiveData<Boolean> hasAccess = new MutableLiveData<>(false);

    private SmartCarLauncher launcher;

    public CarViewModel() {
        launcher = new SmartCarLauncher();


    }
    public void init(Context context){
        User user = UserRepository.getInstance(context).getUser();
        saveDataTool = new SaveDataTool(context);
        if (user != null)
            hasAccess.setValue(true);
        simpleCarSdk = SimpleCarSdk.Companion.get(
                Application.getSimpleCarApiCode(),
                user.getAccessTokenSmartCar(),
                user.getUidFire()
        );
    }

    public LiveData<List<Car>> getCarsLiveData() {
        return carsLiveData;
    }

    public MutableLiveData<Car> getCarMutableLiveData() {
        return carMutableLiveData;
    }

    public MutableLiveData<Boolean> getIsNotificationsEnabled() {
        return isNotificationsEnabled;
    }

    public void setIsNotificationsEnabled(MutableLiveData<Boolean> isNotificationsEnabled) {
        this.isNotificationsEnabled = isNotificationsEnabled;
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
        Executors.newFixedThreadPool(5).execute(() -> {
            updateLocationOfCar(context, car);
            updateRangeOfCar(context, car);

        });

    }

    private void updateLocationOfCar(Context context, Car car) {
        simpleCarSdk.getLocation(car.getSmartCarId(),new LocationCallback() {
            @Override
            public void location(@Nullable cls.android.simplecar.api.Location location) {
                CarDataBaseRepo.getInstance(context).getCarWithSmartCarId(car.getSmartCarId(),
                        new CarDataBaseRepo.OnRetrieveCar() {
                    @Override
                    public void car(Car car) {
                        if (location != null)
                            car.setLocation(new Location(location.getLatitude(),location.getLongitude()));

                    }
                });
            }

            @Override
            public void exception(@NonNull Exception exception) {
                Toast.makeText(context, exception.getMsg(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateRangeOfCar(Context context, Car car) {
        simpleCarSdk.getRange(car.getSmartCarId(),new RangeCallback() {
            @Override
            public void range(@Nullable cls.android.simplecar.api.Range range) {
                CarDataBaseRepo.getInstance(context).getCarWithSmartCarId(car.getSmartCarId(),
                        new CarDataBaseRepo.OnRetrieveCar() {
                            @Override
                            public void car(Car car) {
                                if (range != null) {
                                    car.setDriveProductAmountPercent(range.getPercent());
                                    car.setDriveProductAmount(range.getAmount());
                                }

                            }
                        });
            }

            @Override
            public void exception(@NonNull Exception exception) {
                Toast.makeText(context, exception.getMsg(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public MutableLiveData<Boolean> getHasAccessAfterCheck(Context context){
        User user = UserRepository.getInstance(context).getUser();
        if (user != null && user.getAccessTokenSmartCar() != null){
            hasAccess.setValue(true);
            simpleCarSdk.isTokenValid(new ApiResult() {
                @Override
                public void result(boolean result) {
                    hasAccess.setValue(result);
                    if (!result)
                        Toast.makeText(context, R.string.access_expired, Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
            hasAccess.setValue(false);


        return hasAccess;

    }
    public MutableLiveData<Boolean> getHasAccess() {
        return hasAccess;
    }


    public void connect(Context context) {
        launcher.connect(context, smartcarResponse -> updateAccess(smartcarResponse, context));
    }

    private void updateAccess(SmartcarResponse smartcarResponse, Context context) {
        simpleCarSdk.getAccessTokenWithAuthToken(smartcarResponse.getCode(), new ApiAuthPackageCallback() {
            @Override
            public void result(@Nullable ApiSmartCarAuthPackage packageSmartCar) {
                Log.d(TAG, "result: " +packageSmartCar.getAccessToken());
                if (packageSmartCar != null){
                    User user = UserRepository.getInstance(context).getUser();
                    user.setAccessTokenSmartCar(packageSmartCar.getAccessToken());
                    user.setAuthClientSmartCar(packageSmartCar.getAuthClient());
                    user.setAuthSmartCar(packageSmartCar.getAuth());
                    user.setRefreshTokenSmartCar(packageSmartCar.getRefreshToken());
                    UserRepository.getInstance(context).saveUser(user);
                    getHasAccess().setValue(true);

                    simpleCarSdk.getVehicleIds(new VehicleIdListCallback() {
                        @Override
                        public void getVehicles(@Nullable List<String> list) {
                            Log.d(TAG, "getVehicles: " + list);
                            if (list != null) {
                                Log.d(TAG, "getVehicles: " + list);
                                for (String id: list){
                                    Log.d(TAG, "getVehicles: " + list);
                                    simpleCarSdk.getVehicleAttributes(id, new VehicleCallback() {
                                        @Override
                                        public void getVehicle(@Nullable VehicleAttributes vehicleAttributes) {
                                            Log.d(TAG, "getVehicle: " + vehicleAttributes);
                                            CarDataBaseRepo.getInstance(context)
                                                    .addCar(new Car.parseCar(vehicleAttributes));
                                        }

                                        @Override
                                        public void exception(@NonNull Exception exception) {

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

            @Override
            public void exception(@NonNull Exception exception) {
                Log.d(TAG, "exception: "+ exception.getMsg());
                Toast.makeText(context, R.string.internal_error, Toast.LENGTH_SHORT).show();

            }
        });
    }


    public void updateCars(Context context) {
        String code = saveDataTool.get(SaveDataTool.SMARTCAR_ACCESS_KEY,null);
        //simpleCarSdk.getVehicles(new VehicleCallback() {
        //                             @Override
        //                             public void getVehicles(@Nullable List<String> list) {
        //                                 if (list == null)
        //                                     return;
        //                                 for (String id : list){
        //                                     simpleCarSdk.getVehicleAttributes(id, new VehicleAttributesCallback() {
        //                                         @Override
        //                                         public void result(@Nullable VehicleAttributes vehicleAttributes) {
        //                                             CarDataBaseRepo.getInstance(context).addCar(parseCar(vehicleAttributes));
        //                                             carMutableLiveData.setValue(null);
        //                                         }
        //                                     });
        //                                 }
        //                             }
//
        //                             @Override
        //                             public void exception(@NonNull Exception exception) {
        //                                 Log.e(TAG, "exception: "+exception.getMsg());
//
        //                             }
        //                         });
        //        carsLiveData = CarDataBaseRepo.getInstance(context).getLiveCars();

    }

    public void getCarsFromDB(Context context, CarDataBaseRepo.OnRetrieveListOfCars onRetrieveListOfCars) {
        CarDataBaseRepo.getInstance(context).getCars(onRetrieveListOfCars);
    }
    public void getCarFromDB(Context context,String id, CarDataBaseRepo.OnRetrieveCar OnRetrieveCar) {
        CarDataBaseRepo.getInstance(context).getCarWithSmartCarId(id,OnRetrieveCar);
    }

    public void unlockCar(Context context,ApiResult apiResult) {
        String code = saveDataTool.get(SaveDataTool.SMARTCAR_ACCESS_KEY,null);

        simpleCarSdk.unlockVehicle(carMutableLiveData.getValue().getSmartCarId(),apiResult);
    }

    public LiveData<Car> getLiveCarFromDB(Context context, String smartCarId) {
        return CarDataBaseRepo.getInstance(context).getLiveCarWithSmartCarId(smartCarId);


    }

    public void signup(String phone, String email, String firstName, String secondName, SimpleCarSdk.OnSimpleCarSignUpFeedback simpleCarSignUpFeedback) {
        simpleCarSdk.signup(phone,email,firstName,secondName,simpleCarSignUpFeedback);

    }

    public void saveUser(Context context,User parseUser) {
        UserRepository.getInstance(context).saveUser(parseUser);
    }
    public static Car parseCar(VehicleAttributes vehicleAttributes) {
        if (vehicleAttributes== null)
            return null;
        Car car = new Car();
        car.setBrand(vehicleAttributes.getVehicleMake());
        car.setSmartCarId(vehicleAttributes.getVehicleId());
        car.setModel(vehicleAttributes.getVehicleModel());
        car.setYear(DateUtil.convertYearToLong(vehicleAttributes.getVehicleYear()));
        return car;
    }
}
