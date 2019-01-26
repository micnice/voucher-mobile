package morris.com.voucher.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import morris.com.voucher.model.ClientAssessment;

/**
 * Created by morris on 2018/12/17.
 */
@Dao
public interface ClientAssessmentDAO {

    @Query("select * from ClientAssessment")
    List<ClientAssessment> getAll();

    @Query("select * from ClientAssessment where sentToServer=0 and markAsFinalised=1")
    List<ClientAssessment> getAllFinalisedNotSentToServer();

    @Query("select * from ClientAssessment where clientId=:id and sentToServer=0")
    ClientAssessment getByClientId(String id);

    @Insert()
    void  saveClientAssessmentData(ClientAssessment data);

    @Update
    void updateClientAssessmentData(ClientAssessment data);

    @Delete
    void  deleteClientAssessmentData(ClientAssessment data);

    @Query("Delete from ClientAssessment where 1")
    void  deleteAll();

}
