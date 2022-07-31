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
    public LiveData<Car> carMutableLiveData = new MutableLiveData<>();

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
        setSelectedCar(context);

        User user = UserRepository.getInstance(context).getUser();
        saveDataTool = new SaveDataTool(context);
        String accessTokenSmartCar = "";
        Log.d(TAG, "init:123 " + user.getAccessTokenSmartCar() +" " +  user.getRefreshTokenSmartCar() + " " + user.getAuthClientSmartCar() + user.getAuthSmartCar());

        String uid = "";
        if (user != null) {
            accessTokenSmartCar = user.getAccessTokenSmartCar();
            Log.d(TAG, "init:123 " + accessTokenSmartCar);
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

    }

    public void setSelectedCar(Context context) {
        CarDataBaseRepo.getInstance(context).getCars(new CarDataBaseRepo.OnRetrieveListOfCars() {
            @Override
            public void theList(List<Car> cars) {
                if (cars != null && cars.size() != 0)
                    carMutableLiveData = CarDataBaseRepo.getInstance(context).getLiveCarWithSmartCarId(cars.get(0).getSmartCarId());
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
            simpleCarSdk.isTokenValid(new ApiResult() {
                @Override
                public void exception(@NonNull Exception exception) {
                    hasSmartCarAccess.setValue(false);

                    Log.e(TAG, "exception: "+ exception);
                }

                @Override
                public void result(boolean result) {
                    Log.d(TAG, "result: ");
                    handleTokenResult(result);

                }

                private void handleTokenResult(boolean result) {
                    hasSmartCarAccess.setValue(result);

                    if (!result) {

                        refreshToken(user, context);
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
                Log.e(TAG, "exception: " +exception);
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
                if (packageSmartCar != null){
                    Log.d(TAG, "result: " +packageSmartCar.getAccessToken());
                    updateUserAccess(context,packageSmartCar);




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
        carsLiveData = CarDataBaseRepo.getInstance(context).getLiveCars();
        setSelectedCar(context);//TODO set uid as smartcar uid
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
        carMutableLiveData = CarDataBaseRepo.getInstance(context).getLiveCarWithSmartCarId(car.getSmartCarId());
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
        carUpdater.updateCarsFromOnline(applicationContext);
    }

    public void startUpdatingAttrs(Context context) {
        carUpdater.startUpdatingAttrs(context);
    }

    public void getCarFromDB(Context context, String smartCarId, CarDataBaseRepo.OnRetrieveCar callback) {
        CarDataBaseRepo.getInstance(context).getCarWithSmartCarId(smartCarId,callback);

    }

    public void startChargingCar(Context context, ApiResult apiResult) {
        if (currentCar!= null)
            simpleCarSdk.startChargingCar(currentCar.getSmartCarId(),apiResult);
    }
    public void stopChargingCar(Context context, ApiResult apiResult) {
        if (currentCar!= null)
            simpleCarSdk.stopChargingCar(currentCar.getSmartCarId(),apiResult);
    }
}
