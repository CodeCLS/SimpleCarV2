package cls.simplecar.database;

import cls.simplecar.models.Car;
import cls.simplecar.tools.Converter;

import android.content.Context;

import androidx.room.Database;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Car.class}, version = 14)
@TypeConverters({Converter.class})
public abstract class CarDatabase extends RoomDatabase {
    private static final String DB_NAME = "cars";
    public abstract CarDao dao();
    private static CarDatabase instance;
    public static synchronized CarDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context,CarDatabase.class,
                    DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

}
