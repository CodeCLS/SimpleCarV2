package cls.android.simplecar.database;

import cls.android.simplecar.models.Car;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CarDao {
    @Query("SELECT * FROM cars")
    List<Car> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addCar(Car car);

    @Query("SELECT * FROM cars WHERE roomId = :id")
    Car getCarWithId(Long id);
    @Query("SELECT * FROM cars WHERE smartCarId = :id")
    Car getCarWithSmartCarId(String id);
    @Update
    void updateCar(Car car);
    @Query("SELECT * FROM cars WHERE smartCarId = :id")
    LiveData<Car>getLiveCarWithSmartCarId(String id);
    @Query("SELECT * FROM cars")
    LiveData<List<Car>> getLiveAll();
    @Query("DELETE FROM cars")
    void delete();
}
