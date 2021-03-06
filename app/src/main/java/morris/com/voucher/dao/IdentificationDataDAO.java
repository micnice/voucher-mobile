package morris.com.voucher.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import morris.com.voucher.model.IdentificationData;

/**
 * Created by morris on 2018/12/17.
 */
@Dao
public interface IdentificationDataDAO {

    @Query("select * from IdentificationData")
    List<IdentificationData> getAll();

    @Query("select * from IdentificationData where sentToServer=0 and markAsFinalised=1 ORDER BY lastName,firstName DESC")
    List<IdentificationData> getAllFinalisedNotSetToServer();

    @Query("select * from IdentificationData where id=:clientId")
    IdentificationData getByClientId(int clientId);


    @Insert()
    void  saveIdentificationData(IdentificationData data);

    @Update
    void updateIdentificationData(IdentificationData data);

    @Delete
    void  deleteIdentificationData(IdentificationData data);

    @Query("Delete from IdentificationData where 1")
    void  deleteAll();

}
