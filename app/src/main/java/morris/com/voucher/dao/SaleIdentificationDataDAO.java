package morris.com.voucher.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import java.util.List;
import morris.com.voucher.model.SaleIdentificationData;

/**
 * Created by morris on 2019/1/23.
 */
@Dao
public interface SaleIdentificationDataDAO {

    @Query("select * from SaleIdentificationData")
    List<SaleIdentificationData> getAll();

    @Insert()
    void  saveSaleIdentificationData(SaleIdentificationData data);

    @Query("select saleId from SaleIdentificationData where saleId=:saleId")
    String  getSaleId(String saleId);

    @Query("Delete from SaleIdentificationData where 1")
    void  deleteAll();

}
