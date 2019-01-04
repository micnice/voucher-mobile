package morris.com.voucher.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import morris.com.voucher.model.AssessmentDataFromServer;
/**
 * Created by morris on 2018/12/17.
 */
@Dao
public interface AssessmentDataFromServerDAO {

    @Query("select * from AssessmentDataFromServer")
    List<AssessmentDataFromServer> getAll();

    @Query("select * from AssessmentDataFromServer where assessed=0  ORDER BY lname,fname DESC")
    List<AssessmentDataFromServer> getAllNotAssessed();

    @Query("select * from AssessmentDataFromServer where clientId=:idFromServer  ORDER BY lname,fname DESC")
    AssessmentDataFromServer getByIdFromServer(String idFromServer);

    @Insert()
    void  saveAssessmentDataFromServer(AssessmentDataFromServer data);

    @Update
    void updateAssessmentDataFromServer(AssessmentDataFromServer data);


    @Delete
    void  deleteAssessmentDataFromServer(AssessmentDataFromServer data);

    @Query("Delete from AssessmentDataFromServer where 1")
    void  deleteAll();

}
