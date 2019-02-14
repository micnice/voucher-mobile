package morris.com.voucher.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by morris on 2019/01/24.
 */

@Entity
public class Claim {

    @PrimaryKey(autoGenerate = true)
    int id;
    String claimId;
    String voucherTypeName;
    String providerId;
    Boolean redeemed = Boolean.FALSE;
    Boolean hasOTP = Boolean.FALSE;
    String  saleId;
    String processedBy;
    Boolean redeemStatusFromServer;

    public String getClaimId() {
        return claimId;
    }

    public void setClaimId(String claimId) {
        this.claimId = claimId;
    }

    public String getVoucherTypeName() {
        return voucherTypeName;
    }

    public void setVoucherTypeName(String voucherTypeName) {
        this.voucherTypeName = voucherTypeName;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public Boolean getRedeemed() {
        return redeemed;
    }

    public void setRedeemed(Boolean redeemed) {
        this.redeemed = redeemed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getRedeemStatusFromServer() {
        return redeemStatusFromServer;
    }

    public void setRedeemStatusFromServer(Boolean redeemStatusFromServer) {
        this.redeemStatusFromServer = redeemStatusFromServer;
    }

    public Boolean getHasOTP() {
        return hasOTP;
    }

    public void setHasOTP(Boolean hasOTP) {
        this.hasOTP = hasOTP;
    }

    public String getSaleId() {
        return saleId;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public String getProcessedBy() {
        return processedBy;
    }

    public void setProcessedBy(String processedBy) {
        this.processedBy = processedBy;
    }
}
