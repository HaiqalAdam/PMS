package com.heroku.java.model;

import java.sql.Date;

public class staff extends users {

    private int sId;
    private String sName;
    private String sRelationStatus;
    private Date sDate;
    private String sSex;
    private Date sDOB;
    private String sPhoneNo;
    private String sPosition;

    public staff() {
    }

    public staff(Integer id, String usr, String pwd, String role, String sName, String sRelationStatus, Date sDate,
            String sSex, Date sDOB, String sPhoneNo, String sPosition) {
        super(id, usr, pwd, role);
        this.sName = sName;
        this.sRelationStatus = sRelationStatus;
        this.sDate = sDate;
        this.sSex = sSex;
        this.sDOB = sDOB;
        this.sPhoneNo = sPhoneNo;
        this.sPosition = sPosition;
    }

    public staff(Integer id, String usr, String pwd, String sName, String sRelationStatus, Date sDate,
            String sSex, Date sDOB, String sPhoneNo, String sPosition) {
        super(id, usr, pwd);
        this.sName = sName;
        this.sRelationStatus = sRelationStatus;
        this.sDate = sDate;
        this.sSex = sSex;
        this.sDOB = sDOB;
        this.sPhoneNo = sPhoneNo;
        this.sPosition = sPosition;
    }

     public staff(int sId, String sName, String sRelationStatus, Date sDate,
            String sSex, Date sDOB, String sPhoneNo, String sPosition) {
        super();
        this.sId = sId;
        this.sName = sName;
        this.sRelationStatus = sRelationStatus;
        this.sDate = sDate;
        this.sSex = sSex;
        this.sDOB = sDOB;
        this.sPhoneNo = sPhoneNo;
        this.sPosition = sPosition;
    }
    public Integer getSId() {
        return this.sId;
    }

    public void setSId(int sId) {
        this.sId = sId;
    }

    public String getSName() {
        return this.sName;
    }

    public void setSName(String sName) {
        this.sName = sName;
    }

    public String getSRelationStatus() {
        return this.sRelationStatus;
    }

    public void setSRelationStatus(String sRelationStatus) {
        this.sRelationStatus = sRelationStatus;
    }

    public Date getSDate() {
        return this.sDate;
    }

    public void setSDate(Date sDate) {
        this.sDate = sDate;
    }

    public String getSSex() {
        return this.sSex;
    }

    public void setSSex(String sSex) {
        this.sSex = sSex;
    }

    public Date getSDOB() {
        return this.sDOB;
    }

    public void setSDOB(Date sDOB) {
        this.sDOB = sDOB;
    }

    public String getSPhoneNo() {
        return this.sPhoneNo;
    }

    public void setSPhoneNo(String sPhoneNo) {
        this.sPhoneNo = sPhoneNo;
    }

    public String getSPosition() {
        return this.sPosition;
    }

    public void setSPosition(String sPosition) {
        this.sPosition = sPosition;
    }

}
