package morris.com.voucher.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import morris.com.voucher.model.AccountingClient;

/**
 * Created by morris on 2018/12/17.
 */
@Dao
public interface AccountingClientDAO {

    @Query("select * from AccountingClient")
    List<AccountingClient> getAll();

    @Query("select * from AccountingClient where saleMade=0 ")
    List<AccountingClient> getAllWithoutSale();

    @Query("select * from AccountingClient where clientId=:clientId ")
    AccountingClient getByClientId(String clientId);

    @Query("select * from AccountingClient where idNumber=:idNumber and lastName=:lastName")
    AccountingClient getByIdNumberAndLastName(String idNumber,String lastName);

    @Insert()
    void  saveAccountingClientData(AccountingClient data);

    @Update
    void updateAccountingData(AccountingClient data);

    @Delete
    void  deleteAccountingClientData(AccountingClient data);

    @Query("Delete from AccountingClient where saleMade=0")
    void  deleteAllWithoutSale();

    @Query("Delete from AccountingClient where 1")
    void  deleteAll();

}
