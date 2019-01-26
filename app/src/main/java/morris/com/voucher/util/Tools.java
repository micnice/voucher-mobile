package morris.com.voucher.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by morris on 22/12/18.
 */

public class Tools {

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    public static Date getDateFromString(String date) {

        if(date==null ||date.isEmpty()){
            return null;
        }
        try {
            DateFormat formatter = new SimpleDateFormat("d/M/yyyy");

            Date localDate = formatter.parse(date);

            return localDate;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
    public static int getAge(Date dateFrom,Date dateTo) {
        long ageInMillis =dateTo.getTime() - dateFrom.getTime();

        Date age = new Date(ageInMillis);

        return age.getYear();
    }
    public static String setAge(int year, int month, int day,Date dateTo){
        Calendar calendarFrom = Calendar.getInstance();
        Calendar  calendarTo= Calendar.getInstance();

        calendarFrom.set(year, month, day);
        calendarTo.setTime(dateTo);

        int age = calendarTo.get(Calendar.YEAR) - calendarFrom.get(Calendar.YEAR);

        if (calendarTo.get(Calendar.DAY_OF_YEAR) < calendarFrom.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

    public static String ZIMBABWE="^(\\d{2}-\\d{6,10}-\\w{1}-?\\d{2})$";

}
