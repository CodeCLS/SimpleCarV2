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
import cls.simplecar.api.Oil;
import cls.simplecar.api.OilCallback;
import cls.simplecar.api.RangeCallback;
import cls.simplecar.api.VehicleAttributes;
import cls.simplecar.api.VehicleCallback;
import cls.simplecar.api.VehicleIdListCallback;
import cls.simplecar.api.VehicleOdometerCallback;
import cls.simplecar.api.VehiclePermissionsCallback;
import cls.simplecar.database.CarDataBaseRepo;
import cls.simplecar.models.Car;
import cls.simplecar.models.Location;
import cls.simplecar.SmartCarLauncher;


import cls.simplecar.models.User;
import cls.simplecar.tools.CarAttributesUpdater;
import cls.simplecar.tools.CarUpdater;
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

import java.util.ArrayList;
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



    public MutableLiveData<Location> monthlySubscriptionMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> isNotificationsEnabled = new MutableLiveData<>();
    public MutableLiveData<Boolean> hasSmartCarAccess = new MutableLiveData<>(true);
    public MutableLiveData<Boolean> hasSimpleCarAccess = new MutableLiveData<>(true);

    private SmartCarLauncher launcher;
    public Car currentCar;
    private CarUpdater carUpdater;

    public CarViewModel() {
        launcher = new SmartCarLauncher();


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
        }

        simpleCarSdk = SimpleCarSdk.Companion.get(
                Application.getSimpleCarApiCode(),
                accessTokenSmartCar,
                uid);
        carUpdater = new CarUpdater(simpleCarSdk);

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
                        carUpdater.updateCarsFromOnline(context, new Runnable() {
                            @Override
                            public void run() {
                                setSelectedCar(context);
                            }
                        });
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

    private void updateAccess(SmartcarResponse smartcarResponse, Context context) {
        simpleCarSdk.getAccessTokenWithAuthToken(smartcarResponse.getCode(), new ApiAuthPackageCallback() {
            @Override
            public void result(@Nullable ApiSmartCarAuthPackage packageSmartCar) {
                Log.d(TAG, "result: " +packageSmartCar.getAccessToken());
                if (packageSmartCar != null){
                    updateUserAccess(context,packageSmartCar);
                    carUpdater.updateCarsFromOnline(context, new Runnable() {
                        @Override
                        public void run() {
                            setSelectedCar(context);
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

    public void unlockCar(Context context,ApiResult apiResult) {
        if (currentCar!= null)
            simpleCarSdk.unlockVehicle(currentCar.getSmartCarId(),apiResult);
    }
    public void lockCar(Context context,ApiResult apiResult) {
        if (currentCar!= null)
            simpleCarSdk.lockVehicle(currentCar.getSmartCarId(),apiResult);
    }

    public LiveData<Car> getLiveCarFromDB(Context context, String smartCarId) {
        return CarDataBaseRepo.getInstance(context).getLiveCarWithSmartCarId(smartCarId);


    }

    public void signup(String phone, String email, String firstName, String secondName, SimpleCarSdk.OnSimpleCarSignUpFeedback simpleCarSignUpFeedback) {
        simpleCarSdk.signup(phone,email,firstName,secondName,simpleCarSignUpFeedback);

    }

    public void saveUser(Context context,User parseUser) {
        UserRepository.getInstance(context).saveUser(parseUser);
        hasSimpleCarAccess.setValue(true);}


    public void changeSelectedCar(Context context,Car car) {
        setCarValueUiThread(car);
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

    public void updateCarsFromOnline(Context applicationContext) {
        carUpdater.updateCarsFromOnline(applicationContext, new Runnable() {
            @Override
            public void run() {
                setSelectedCar(applicationContext);
            }
        });
    }

    public void startUpdatingAttrs(Context context) {
        carUpdater.startUpdatingAttrs(context);
    }
}
