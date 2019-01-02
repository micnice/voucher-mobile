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
    Boolean part1 =Boolean.FALSE;
    Boolean part2 = Boolean.FALSE;
    Boolean part3 = Boolean.FALSE;
    Boolean part4 = Boolean.FALSE;
    Boolean part5 = Boolean.FALSE;
    Boolean part6 =Boolean.FALSE;
    Boolean part7 =Boolean.FALSE;
    Boolean part8 =Boolean.FALSE;
    Boolean part9 =Boolean.FALSE;
    Boolean part10 =Boolean.FALSE;
    Boolean part11 =Boolean.FALSE;
    Boolean sentToServer=Boolean.FALSE;

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

    public Boolean getPart1() {
        return part1;
    }

    public void setPart1(Boolean part1) {
        this.part1 = part1;
    }

    public Boolean getPart2() {
        return part2;
    }

    public void setPart2(Boolean part2) {
        this.part2 = part2;
    }

    public Boolean getPart3() {
        return part3;
    }

    public void setPart3(Boolean part3) {
        this.part3 = part3;
    }

    public Boolean getPart4() {
        return part4;
    }

    public void setPart4(Boolean part4) {
        this.part4 = part4;
    }

    public Boolean getPart5() {
        return part5;
    }

    public void setPart5(Boolean part5) {
        this.part5 = part5;
    }

    public Boolean getPart6() {
        return part6;
    }

    public void setPart6(Boolean part6) {
        this.part6 = part6;
    }

    public Boolean getPart7() {
        return part7;
    }

    public void setPart7(Boolean part7) {
        this.part7 = part7;
    }

    public Boolean getPart8() {
        return part8;
    }

    public void setPart8(Boolean part8) {
        this.part8 = part8;
    }

    public Boolean getPart9() {
        return part9;
    }

    public void setPart9(Boolean part9) {
        this.part9 = part9;
    }

    public Boolean getPart10() {
        return part10;
    }

    public void setPart10(Boolean part10) {
        this.part10 = part10;
    }

    public Boolean getPart11() {
        return part11;
    }

    public void setPart11(Boolean part11) {
        this.part11 = part11;
    }

    public Boolean getSentToServer() {
        return sentToServer;
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

    public void setSentToServer(Boolean sentToServer) {
        this.sentToServer = sentToServer;
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
}
