package morris.com.voucher.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import morris.com.voucher.model.AccountingClient;
import morris.com.voucher.model.Claim;

/**
 * Created by morris on 2018/12/17.
 */
@Dao
public interface ClaimDAO {

    @Query("select * from Claim")
    List<Claim> getAll();

    @Query("select * from Claim where redeemed=1 ")
    List<Claim> getAllRedeemed();

    @Query("select * from Claim where claimId=:claimId ")
    Claim getByClaimId(String claimId);

    @Insert()
    void  saveClaim(Claim data);

    @Update
    void updateClaim(Claim data);

    @Query("Delete from Claim where 1")
    void  deleteAll();

}
