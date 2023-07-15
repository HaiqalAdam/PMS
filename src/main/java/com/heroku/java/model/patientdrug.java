package com.heroku.java.model;

import java.sql.Date;

public class patientdrug {

    private Integer pdId;
    private String pdName;
    private String pdIc;
    private String pdSex;
    private String pdAddress;
    private Date pdDate;
    private String pdStatus;
    private Date pdDOB;
    private String pdPhoneNo;
    private String pdBloodType;
    private String pdDrugType;

    public patientdrug(Integer pdId, String pdName, String pdIc, String pdSex, String pdAddress, Date pdDate,
            String pdStatus, Date pdDOB, String pdPhoneNo, String pdBloodType, String pdDrugType) {
        this.pdId = pdId;
        this.pdName = pdName;
        this.pdIc = pdIc;
        this.pdSex = pdSex;
        this.pdAddress = pdAddress;
        this.pdDate = pdDate;
        this.pdStatus = pdStatus;
        this.pdDOB = pdDOB;
        this.pdPhoneNo = pdPhoneNo;
        this.pdBloodType = pdBloodType;
        this.pdDrugType = pdDrugType;
    }

    public patientdrug() {
        
    }

    public Integer getPdId() {
        return pdId;
    }

    public void setPdId(Integer pdId) {
        this.pdId = pdId;
    }

    public String getPdName() {
        return pdName;
    }

    public void setPdName(String pdName) {
        this.pdName = pdName;
    }

    public String getPdIc() {
        return pdIc;
    }

    public void setPdIc(String pdIc) {
        this.pdIc = pdIc;
    }

    public String getPdSex() {
        return pdSex;
    }

    public void setPdSex(String pdSex) {
        this.pdSex = pdSex;
    }

    public String getPdAddress() {
        return pdAddress;
    }

    public void setPdAddress(String pdAddress) {
        this.pdAddress = pdAddress;
    }

    public Date getPdDate() {
        return pdDate;
    }

    public void setPdDate(Date pdDate) {
        this.pdDate = pdDate;
    }

    public String getPdStatus() {
        return pdStatus;
    }

    public void setPdStatus(String pdStatus) {
        this.pdStatus = pdStatus;
    }

    public Date getPdDOB() {
        return pdDOB;
    }

    public void setPdDOB(Date pdDOB) {
        this.pdDOB = pdDOB;
    }

    public String getPdPhoneNo() {
        return pdPhoneNo;
    }

    public void setPdPhoneNo(String pdPhoneNo) {
        this.pdPhoneNo = pdPhoneNo;
    }

    public String getPdBloodType() {
        return pdBloodType;
    }

    public void setPdBloodType(String pdBloodType) {
        this.pdBloodType = pdBloodType;
    }

    public String getPdDrugType() {
        return pdDrugType;
    }

    public void setPdDrugType(String pdDrygType) {
        this.pdDrugType = pdDrygType;
    }

    
    
  
}
