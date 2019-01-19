package morris.com.voucher.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by morris on 2018/12/31.
 */

@Entity
public class AccountingClient {

    @PrimaryKey(autoGenerate = true)
    int id;
    String clientId;
    String firstName;
    String lastName;
    String idNumber;
    Long povertyScore;
    boolean saleMade = Boolean.FALSE;

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public boolean isSaleMade() {
        return saleMade;
    }

    public void setSaleMade(boolean saleMade) {
        this.saleMade = saleMade;
    }

    public Long getPovertyScore() {
        return povertyScore;
    }

    public void setPovertyScore(Long povertyScore) {
        this.povertyScore = povertyScore;
    }
}
