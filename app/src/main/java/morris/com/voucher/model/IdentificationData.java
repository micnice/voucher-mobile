package morris.com.voucher.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

/**
 * Created by morris on 2018/12/17.
 */
@Entity
public class IdentificationData {

    @PrimaryKey(autoGenerate = true)
    int id;
    private String lmp;
    private String createdBy;
    private String dateRegistered;
    private String firstName;
    private String lastName;
    private String maritalStatus;
    private String birthDate;
    private String educationStatus;
    private Long parity;
    private String idFromServer;
    private String identificationNumber;
    private String latitude;
    private String longitude;
    private Boolean assessed = Boolean.FALSE;
    private boolean sentToServer=Boolean.FALSE;

    public IdentificationData(){};

    public IdentificationData(String lmp, String firstName, String lastName, String maritalStatus,
                              String birthDate, String educationStatus, Long parity,
                              String identificationNumber, String latitude, String longitude) {
        this.lmp = lmp;
        this.firstName = firstName;
        this.lastName = lastName;
        this.maritalStatus = maritalStatus;
        this.birthDate = birthDate;
        this.educationStatus = educationStatus;
        this.parity = parity;
        this.identificationNumber = identificationNumber;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLmp() {
        return lmp;
    }

    public void setLmp(String lmp) {
        this.lmp = lmp;
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

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getEducationStatus() {
        return educationStatus;
    }

    public void setEducationStatus(String educationStatus) {
        this.educationStatus = educationStatus;
    }

    public Long getParity() {
        return parity;
    }

    public void setParity(Long parity) {
        this.parity = parity;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public boolean isSentToServer() {
        return sentToServer;
    }

    public void setSentToServer(boolean sentToServer) {
        this.sentToServer = sentToServer;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean getAssessed() {
        return assessed;
    }

    public void setAssessed(Boolean assessed) {
        this.assessed = assessed;
    }

    public String getIdFromServer() {
        return idFromServer;
    }

    public void setIdFromServer(String idFromServer) {
        this.idFromServer = idFromServer;
    }

    public String getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(String dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IdentificationData that = (IdentificationData) o;

        if (id != that.id) return false;
        if (firstName != null ? !firstName.equals(that.firstName) : that.firstName != null)
            return false;
        if (lastName != null ? !lastName.equals(that.lastName) : that.lastName != null)
            return false;
        if (maritalStatus != null ? !maritalStatus.equals(that.maritalStatus) : that.maritalStatus != null)
            return false;
        if (birthDate != null ? !birthDate.equals(that.birthDate) : that.birthDate != null)
            return false;
        return identificationNumber != null ? identificationNumber.equals(that.identificationNumber) : that.identificationNumber == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (maritalStatus != null ? maritalStatus.hashCode() : 0);
        result = 31 * result + (birthDate != null ? birthDate.hashCode() : 0);
        result = 31 * result + (identificationNumber != null ? identificationNumber.hashCode() : 0);
        return result;
    }
}
