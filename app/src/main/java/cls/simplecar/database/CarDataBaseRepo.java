package cls.simplecar.database;

import cls.simplecar.models.Car;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CarDataBaseRepo {
    private static final String TAG = "CarDataBaseRepo";
    private static CarDataBaseRepo instance;
    private ExecutorService executorService = Executors.newFixedThreadPool(5);
    private CarDao dao;

    public static CarDataBaseRepo getInstance(Context context) {
        if (instance == null)
            instance = new CarDataBaseRepo(context);
        return instance;
    }

    public CarDataBaseRepo(Context context) {
        dao = CarDatabase.getInstance(context).dao();
    }

    public void addCar(Car carToAdd) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: addCar" + carToAdd.getDriveDuration());
                if (carToAdd != null)
                    dao.addCar(carToAdd);

            }
        });


    }
    public void getCars(OnRetrieveListOfCars onRetrieveListOfCars){
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: ");
                onRetrieveListOfCars.theList(dao.getAll());

            }
        });

    }

    public void updateCar(Car car) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: update" + car.getSmartCarId());

                dao.updateCar(car);
            }
        });
    }

    public void getCarWithSmartCarId(String id,OnRetrieveCar onRetrieveCar) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                onRetrieveCar.car(dao.getCarWithSmartCarId(id));
            }
        });

    }

    public LiveData<Car> getLiveCarWithSmartCarId(String smartCarId) {
        return dao.getLiveCarWithSmartCarId(smartCarId);
    }

    public LiveData<List<Car>> getLiveCars() {
        return dao.getLiveAll();
    }

    public void deleteAll() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                dao.delete();
            }
        });

    }

    public interface OnRetrieveListOfCars{
        void theList(List<Car> cars);

    }

    public interface OnRetrieveCar {
        void car(Car car);
    }
}
