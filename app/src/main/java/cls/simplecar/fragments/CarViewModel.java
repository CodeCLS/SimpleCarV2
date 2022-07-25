package cls.simplecar.fragments;

import cls.simplecar.Application;
import cls.simplecar.R;
import cls.simplecar.SaveDataTool;
import cls.simplecar.UserRepository;

import cls.simplecar.api.ApiAuthPackageCallback;
import cls.simplecar.api.ApiResult;
import cls.simplecar.api.ApiSmartCarAuthPackage;
import cls.simplecar.api.LocationCallback;
import cls.simplecar.api.Odometer;
import cls.simplecar.api.RangeCallback;
import cls.simplecar.api.VehicleAttributes;
import cls.simplecar.api.VehicleCallback;
import cls.simplecar.api.VehicleIdListCallback;
import cls.simplecar.api.VehicleOdometerCallback;
import cls.simplecar.database.CarDataBaseRepo;
import cls.simplecar.models.Car;
import cls.simplecar.models.Location;
import cls.simplecar.SmartCarLauncher;


import cls.simplecar.models.User;
import cls.simplecar.tools.CarAttributesUpdater;
import cls.simplecar.tools.DateUtil;
import cls.simplecar.tools.UpdaterConnection;
import cls.simplecar.api.Exception;
import cls.simplecar.api.Range;
import cls.simplecar.api.SimpleCarSdk;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.smartcar.sdk.SmartcarResponse;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CarViewModel extends ViewModel {
    private static final String TAG = "CarViewModel";
    private SimpleCarSdk simpleCarSdk;
    private SaveDataTool saveDataTool;
    public MutableLiveData<Car> carMutableLiveData = new MutableLiveData<>();
    public LiveData<List<Car>> carsLiveData = new MutableLiveData<List<Car>>();

    private ExecutorService executorService = Executors.newFixedThreadPool(4);


    public MutableLiveData<Location> monthlySubscriptionMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> isNotificationsEnabled = new MutableLiveData<>();
    public MutableLiveData<Boolean> hasSmartCarAccess = new MutableLiveData<>(true);
    public MutableLiveData<Boolean> hasSimpleCarAccess = new MutableLiveData<>(true);

    private SmartCarLauncher launcher;
    public Car currentCar;

    public CarViewModel() {
        launcher = new SmartCarLauncher();


    }

    public MutableLiveData<Boolean> getHasSimpleCarAccess() {
        return hasSimpleCarAccess;
    }

    public void init(Context context){
        carsLiveData = CarDataBaseRepo.getInstance(context).getLiveCars();
        User user = UserRepository.getInstance(context).getUser();
        saveDataTool = new SaveDataTool(context);
        String accessTokenSmartCar = "";
        String uid = "";
        if (user != null) {
            accessTokenSmartCar = user.getAccessTokenSmartCar();
            uid = user.getUidFire();
            hasSimpleCarAccess.setValue(true);
        }
        else {
            hasSimpleCarAccess.setValue(false);
            Log.d(TAG, "getHasSmartCarAccessAfterCheckfalse ");
        }

        simpleCarSdk = SimpleCarSdk.Companion.get(
                Application.getSimpleCarApiCode(),
                accessTokenSmartCar,
                uid);
        setSelectedCar(context);
    }

    private void setSelectedCar(Context context) {
        CarDataBaseRepo.getInstance(context).getCars(new CarDataBaseRepo.OnRetrieveListOfCars() {
            @Override
            public void theList(List<Car> cars) {
                if (cars != null && cars.size() != 0){

                    CarDataBaseRepo
                            .getInstance(context)
                            .getCarWithSmartCarId(cars.get(0).getSmartCarId(), new CarDataBaseRepo.OnRetrieveCar() {
                                @Override
                                public void car(Car car) {
                                    setCarValueUiThread(car);
                                }
                            });
                }
            }
        });
    }

    private void setCarValueUiThread(Car car) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                carMutableLiveData.setValue(car);
                currentCar = car;

            }
        });
    }
    public LiveData<List<Car>> getCarsLiveData() {
        return carsLiveData;
    }

    public LiveData<Car> getCarMutableLiveData() {
        return carMutableLiveData;
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
        Future<?> i = executorService.submit(() -> {
            updateLocationOfCar(context, car);
            updateRangeOfCar(context, car);
            updateOdometerOfCar(context,car);
        });

    }

    private void updateLocationOfCar(Context context, Car car) {
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
    private void updateRangeOfCar(Context context, Car car) {
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
    private void updateOdometerOfCar(Context context, Car car) {
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


    public LiveData<Boolean> getHasSmartCarAccessAfterCheck(Context context){
        User user = UserRepository.getInstance(context).getUser();
        if (user != null && user.getAccessTokenSmartCar() != null){
            hasSmartCarAccess.setValue(true);
            simpleCarSdk.isTokenValid(new ApiResult() {
                @Override
                public void exception(@NonNull Exception exception) {
                }

                @Override
                public void result(boolean result) {
                    handleTokenResult(result);

                }

                private void handleTokenResult(boolean result) {
                    if (!result) {
                        refreshToken(user, context);
                        Toast.makeText(context, R.string.access_expired, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        updateCarsFromOnline(context);
                        hasSmartCarAccess.setValue(result);

                    }
                }
            });
        }
        else {
            hasSmartCarAccess.setValue(false);
        }


        return hasSmartCarAccess;

    }

    private void refreshToken(User user, Context context) {
        Log.d(TAG, "onResponse:123 ");
        simpleCarSdk.refreshToken(user.getAuthSmartCar(), user.getAuthClientSmartCar(), new ApiAuthPackageCallback() {
            @Override
            public void result(@Nullable ApiSmartCarAuthPackage packageSmartCar) {
                if (packageSmartCar != null) {
                    updateUserAccess(context,packageSmartCar);
                    hasSmartCarAccess.setValue(true);

                } else {
                    hasSmartCarAccess.setValue(false);
                }
            }
            @Override
            public void exception(@NonNull Exception exception) {
                hasSmartCarAccess.setValue(false);
            }
        });
    }

    public MutableLiveData<Boolean> getHasSmartCarAccess() {
        return hasSmartCarAccess;
    }


    public void connect(Context context) {
        launcher.connect(context, smartcarResponse -> updateAccess(smartcarResponse, context));
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
                                                            CarDataBaseRepo.getInstance(context)
                                                                    .addCar(Car.parseCar(vehicleAttributes));
                                                            CarDataBaseRepo.getInstance(context).getCars(new CarDataBaseRepo.OnRetrieveListOfCars() {
                                                                @Override
                                                                public void theList(List<Car> cars) {
                                                                    if (cars != null && cars.size() != 0)
                                                                        setCarValueUiThread(cars.get(0));

                                                                }
                                                            });
                                                        }



                                                        else{
                                                            updateSingleCarVars(context, car);
                                                        }

                                                    }
                                                });

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
    private void updateAccess(SmartcarResponse smartcarResponse, Context context) {
        simpleCarSdk.getAccessTokenWithAuthToken(smartcarResponse.getCode(), new ApiAuthPackageCallback() {
            @Override
            public void result(@Nullable ApiSmartCarAuthPackage packageSmartCar) {
                Log.d(TAG, "result: " +packageSmartCar.getAccessToken());
                if (packageSmartCar != null){
                    updateUserAccess(context,packageSmartCar);
                    updateCarsFromOnline(context);



                }
            }





            @Override
            public void exception(@NonNull Exception exception) {
                Log.d(TAG, "exception: "+ exception.getMsg());
                Toast.makeText(context, R.string.internal_error, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void updateSingleCarVars(Context context,Car car) {
        updateLocationOfCar(context,car);
        updateRangeOfCar(context,car);
        updateOdometerOfCar(context,car);

    }

    private void updateUserAccess(Context context,@NonNull ApiSmartCarAuthPackage packageSmartCar) {
        User user = UserRepository.getInstance(context).getUser();
        if (user == null) {
            Log.d(TAG, "getHasSmartCarAccessAfterCheckfalse ");
            hasSimpleCarAccess.setValue(false);
            hasSmartCarAccess.setValue(false);
            return;
        }
        user.setAccessTokenSmartCar(packageSmartCar.getAccessToken());
        user.setAuthClientSmartCar(packageSmartCar.getAuthClient());
        user.setAuthSmartCar(packageSmartCar.getAuth());
        user.setRefreshTokenSmartCar(packageSmartCar.getRefreshToken());
        UserRepository.getInstance(context).saveUser(user);
        getHasSmartCarAccess().setValue(true);
        simpleCarSdk.setSmartCarCode((packageSmartCar.getAccessToken()));
    }

    public void getCarsFromDB(Context context, CarDataBaseRepo.OnRetrieveListOfCars onRetrieveListOfCars) {
        CarDataBaseRepo.getInstance(context).getCars(onRetrieveListOfCars);
    }
    public void getCarFromDB(Context context,String id, CarDataBaseRepo.OnRetrieveCar OnRetrieveCar) {
        CarDataBaseRepo.getInstance(context).getCarWithSmartCarId(id,OnRetrieveCar);
    }

    public void unlockCar(Context context,ApiResult apiResult) {
        if (currentCar!= null)
            simpleCarSdk.unlockVehicle(currentCar.getSmartCarId(),apiResult);
    }

    public LiveData<Car> getLiveCarFromDB(Context context, String smartCarId) {
        return CarDataBaseRepo.getInstance(context).getLiveCarWithSmartCarId(smartCarId);


    }

    public void signup(String phone, String email, String firstName, String secondName, SimpleCarSdk.OnSimpleCarSignUpFeedback simpleCarSignUpFeedback) {
        simpleCarSdk.signup(phone,email,firstName,secondName,simpleCarSignUpFeedback);

    }

    public void saveUser(Context context,User parseUser) {
        UserRepository.getInstance(context).saveUser(parseUser);
        hasSimpleCarAccess.setValue(true);
        Log.d(TAG, "saveUser: true");
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

    public void changeSelectedCar(Context context,Car car) {
        setCarValueUiThread(car);


        Log.d(TAG, "changeSelectedCar: " + carMutableLiveData + " " + carMutableLiveData.getValue());
    }


    public void lockCar(Context context,ApiResult apiResult) {
        if (currentCar!= null)
            simpleCarSdk.lockVehicle(currentCar.getSmartCarId(),apiResult);
    }

    public void updateCurrentCar(List<Car> cars) {
        if (cars == null || cars.size() == 0)
            return;
        if (currentCar == null)
            currentCar = cars.get(0);
        for (Car car : cars){
            if (car.getSmartCarId().equals(currentCar.getSmartCarId())){
                currentCar = car;
            }
        }
    }
}
