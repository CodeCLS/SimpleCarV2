package cls.android.simplecar.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static Long convertYearToLong(String vehicleYear) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            Date date = sdf.parse(vehicleYear);

            long startDate = date.getTime();
            return startDate;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1L;
    }
}
