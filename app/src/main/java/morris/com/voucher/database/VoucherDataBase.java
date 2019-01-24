package morris.com.voucher.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import morris.com.voucher.converter.DateConverter;
import morris.com.voucher.dao.AccountingClientDAO;
import morris.com.voucher.dao.AssessmentDataFromServerDAO;
import morris.com.voucher.dao.ClaimDAO;
import morris.com.voucher.dao.ClientAssessmentDAO;
import morris.com.voucher.dao.IdentificationDataDAO;
import morris.com.voucher.dao.SaleIdentificationDataDAO;
import morris.com.voucher.model.AccountingClient;
import morris.com.voucher.model.AssessmentDataFromServer;
import morris.com.voucher.model.Claim;
import morris.com.voucher.model.ClientAssessment;
import morris.com.voucher.model.IdentificationData;
import morris.com.voucher.model.SaleIdentificationData;

/**
 * Created by morris on 2018/12/17.
 */

@Database(entities = {IdentificationData.class, ClientAssessment.class, AssessmentDataFromServer.class, AccountingClient.class, SaleIdentificationData.class, Claim.class},version = 1,exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class VoucherDataBase extends RoomDatabase {
    private  static  VoucherDataBase INSTANCE;

    public static  VoucherDataBase getDatabase(Context context){

        if(INSTANCE ==null){


            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),VoucherDataBase.class,"Voucher").allowMainThreadQueries().fallbackToDestructiveMigration().build();

        }
        return   INSTANCE;

    }

    public  abstract IdentificationDataDAO identificationDataDAO();

    public abstract ClientAssessmentDAO clientAssessmentDAO();

    public  abstract AccountingClientDAO accountingClientDAO();

    public abstract AssessmentDataFromServerDAO assessmentDataFromServerDAO();

    public abstract SaleIdentificationDataDAO saleIdentificationDataDAO();

    public abstract ClaimDAO claimDAO();
}
