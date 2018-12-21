package morris.com.voucher.converter;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by morris on 2018/12/17.
 */

public class DateConverter {

    @TypeConverter
    public static Date toDate(Long dateLong){
        return dateLong == null ? null: new Date(dateLong);
    }

    @TypeConverter
    public static long fromDate(Date date){
        return date == null ? null :date.getTime();
    }

}
