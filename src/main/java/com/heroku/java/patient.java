package com.heroku.java;

import java.sql.Date;

public class patient {

    private String pName;
    private String pSex;
    private String pAddress;
    private Date pDate;
    private String pStatus;
    private Date pDOB;
    private String pPhoneNo;
    private String pBloodType;
    private String pIc;

    public String getPName() {
        return this.pName;
    }

    public void setPName(String pName) {
        this.pName = pName;
    }

    public String getPSex() {
        return this.pSex;
    }

    public void setPSex(String pSex) {
        this.pSex = pSex;
    }

    public String getPAddress() {
        return this.pAddress;
    }

    public void setPAddress(String pAddress) {
        this.pAddress = pAddress;
    }

    public Date getPDate() {
        return this.pDate;
    }

    public void setPDate(Date pDate) {
        this.pDate = pDate;
    }

    public String getPStatus() {
        return this.pStatus;
    }

    public void setPStatus(String pStatus) {
        this.pStatus = pStatus;
    }

    public Date getPDOB() {
        return this.pDOB;
    }

    public void setPDOB(Date pDOB) {
        this.pDOB = pDOB;
    }

    public String getPPhoneNo() {
        return this.pPhoneNo;
    }

    public void setPPhoneNo(String pPhoneNo) {
        this.pPhoneNo = pPhoneNo;
    }

    public String getPBloodType() {
        return this.pBloodType;
    }

    public void setPBloodType(String pBloodType) {
        this.pBloodType = pBloodType;
    }

    public String getPIc() {
        return this.pIc;
    }

    public void setPIc(String pIc) {
        this.pIc = pIc;
    }

}
