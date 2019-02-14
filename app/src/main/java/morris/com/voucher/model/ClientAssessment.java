package morris.com.voucher.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by morris on 2018/12/31.
 */

@Entity
public class ClientAssessment {

    @PrimaryKey(autoGenerate = true)
    int id;
    String clientId;
    String fname;
    String lname;
    String idNumber;
    String dateAssesed;
    String idFromServer;
    String pregnancyStatus;
    String assessedBy;
    private String latitude;
    private String longitude;
    boolean markAsFinalised = Boolean.FALSE;
    boolean part1 =Boolean.FALSE;
    boolean part2 = Boolean.FALSE;
    boolean part3 = Boolean.FALSE;
    boolean part4 = Boolean.FALSE;
    boolean part5 = Boolean.FALSE;
    boolean part6 =Boolean.FALSE;
    boolean part7 =Boolean.FALSE;
    boolean part8 =Boolean.FALSE;
    boolean part9 =Boolean.FALSE;
    boolean part10 =Boolean.FALSE;
    boolean part11 =Boolean.FALSE;
    boolean sentToServer=Boolean.FALSE;

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

    public String getDateAssesed() {
        return dateAssesed;
    }

    public void setDateAssesed(String dateAssesed) {
        this.dateAssesed = dateAssesed;
    }

    public String getIdFromServer() {
        return idFromServer;
    }

    public void setIdFromServer(String idFromServer) {
        this.idFromServer = idFromServer;
    }

    public boolean isMarkAsFinalised() {
        return markAsFinalised;
    }

    public void setMarkAsFinalised(boolean markAsFinalised) {
        this.markAsFinalised = markAsFinalised;
    }

    public boolean isPart1() {
        return part1;
    }

    public void setPart1(boolean part1) {
        this.part1 = part1;
    }

    public boolean isPart2() {
        return part2;
    }

    public void setPart2(boolean part2) {
        this.part2 = part2;
    }

    public boolean isPart3() {
        return part3;
    }

    public void setPart3(boolean part3) {
        this.part3 = part3;
    }

    public boolean isPart4() {
        return part4;
    }

    public void setPart4(boolean part4) {
        this.part4 = part4;
    }

    public boolean isPart5() {
        return part5;
    }

    public void setPart5(boolean part5) {
        this.part5 = part5;
    }

    public boolean isPart6() {
        return part6;
    }

    public void setPart6(boolean part6) {
        this.part6 = part6;
    }

    public boolean isPart7() {
        return part7;
    }

    public void setPart7(boolean part7) {
        this.part7 = part7;
    }

    public boolean isPart8() {
        return part8;
    }

    public void setPart8(boolean part8) {
        this.part8 = part8;
    }

    public boolean isPart9() {
        return part9;
    }

    public void setPart9(boolean part9) {
        this.part9 = part9;
    }

    public boolean isPart10() {
        return part10;
    }

    public void setPart10(boolean part10) {
        this.part10 = part10;
    }

    public boolean isPart11() {
        return part11;
    }

    public void setPart11(boolean part11) {
        this.part11 = part11;
    }

    public boolean isSentToServer() {
        return sentToServer;
    }

    public void setSentToServer(boolean sentToServer) {
        this.sentToServer = sentToServer;
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

    public String getPregnancyStatus() {
        return pregnancyStatus;
    }

    public void setPregnancyStatus(String pregnancyStatus) {
        this.pregnancyStatus = pregnancyStatus;
    }

    public String getAssessedBy() {
        return assessedBy;
    }

    public void setAssessedBy(String assessedBy) {
        this.assessedBy = assessedBy;
    }
}
