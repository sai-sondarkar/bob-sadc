package xyz.nvrsettle.bobsmartaadhar.Models;

/**
 * Created by sai on 12/11/17.
 */

public class TranstModel {

    public String uid;
    public String fromAadharNo;
    public String toAadharNo;
    public String byAadharNo;
    public float amountTranst;
    public long date;

    public TranstModel() {
    }

    public String getByAadharNo() {
        return byAadharNo;
    }

    public void setByAadharNo(String byAadharNo) {
        this.byAadharNo = byAadharNo;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFromAadharNo() {
        return fromAadharNo;
    }

    public void setFromAadharNo(String fromAadharNo) {
        this.fromAadharNo = fromAadharNo;
    }

    public String getToAadharNo() {
        return toAadharNo;
    }

    public void setToAadharNo(String toAadharNo) {
        this.toAadharNo = toAadharNo;
    }

    public float getAmountTranst() {
        return amountTranst;
    }

    public void setAmountTranst(float amountTranst) {
        this.amountTranst = amountTranst;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
