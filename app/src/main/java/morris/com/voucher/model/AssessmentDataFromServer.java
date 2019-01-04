package morris.com.voucher.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by morris on 2018/12/31.
 */

@Entity
public class AssessmentDataFromServer {

    @PrimaryKey(autoGenerate = true)
    int id;
    String clientId;
    String fname;
    String lname;
    String idNumber;
    boolean assessed = Boolean.FALSE;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public boolean isAssessed() {
        return assessed;
    }

    public void setAssessed(boolean assessed) {
        this.assessed = assessed;
    }
}
