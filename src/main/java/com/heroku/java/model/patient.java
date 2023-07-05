package com.heroku.java.model;

import java.sql.Date;

public class patient {

    private Integer pId;
    private String pName;
    private String pIc;
    private String pSex;
    private String pAddress;
    private Date pDate;
    private String pStatus;
    private Date pDOB;
    private String pPhoneNo;
    private String pBloodType;

    public patient(Integer pId, String pName, String pIc, String pSex, String pAddress, Date pDate, String pStatus,
            Date pDOB, String pPhoneNo, String pBloodType) {
        this.pId = pId;
        this.pName = pName;
        this.pIc = pIc;
        this.pSex = pSex;
        this.pAddress = pAddress;
        this.pDate = pDate;
        this.pStatus = pStatus;
        this.pDOB = pDOB;
        this.pPhoneNo = pPhoneNo;
        this.pBloodType = pBloodType;
    }


    public Integer getPId() {
        return pId;
    }

    public void setPId(Integer pId) {
        this.pId = pId;
    }

    public String getPName() {
        return this.pName;
    }

    public void setPName(String pName) {
        this.pName = pName;
    }

      public String getPIc() {
        return pIc;
    }

    public void setPIc(String pIc) {
        this.pIc = pIc;
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

}
