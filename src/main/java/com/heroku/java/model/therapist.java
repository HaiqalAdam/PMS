package com.heroku.java.model;

import java.sql.Date;

public class therapist extends users {

    private String tName;
    private String tRelationStatus;
    private Date tDate;
    private String tSex;
    private Date tDOB;
    private String tPhoneNo;
    private String tSpecialist;

    public therapist() {
    }

    public therapist(String tName, String tRelationStatus, Date tDate, String tSex, Date tDOB, String tPhoneNo,
            String tSpecialist) {
        this.tName = tName;
        this.tRelationStatus = tRelationStatus;
        this.tDate = tDate;
        this.tSex = tSex;
        this.tDOB = tDOB;
        this.tPhoneNo = tPhoneNo;
        this.tSpecialist = tSpecialist;
    }

    public therapist(Integer id, String usr, String pwd, String role, String tName, String tRelationStatus, Date tDate,
            String tSex, Date tDOB, String tPhoneNo,
            String tSpecialist) {
        super(id, usr, pwd, role);
        this.tName = tName;
        this.tRelationStatus = tRelationStatus;
        this.tDate = tDate;
        this.tSex = tSex;
        this.tDOB = tDOB;
        this.tPhoneNo = tPhoneNo;
        this.tSpecialist = tSpecialist;
    }

    public String getTName() {
        return this.tName;
    }

    public void setTName(String tName) {
        this.tName = tName;
    }

    public String getTRelationStatus() {
        return this.tRelationStatus;
    }

    public void setTRelationStatus(String tRelationStatus) {
        this.tRelationStatus = tRelationStatus;
    }

    public Date getTDate() {
        return this.tDate;
    }

    public void setTDate(Date tDate) {
        this.tDate = tDate;
    }

    public String getTSex() {
        return this.tSex;
    }

    public void setTSex(String tSex) {
        this.tSex = tSex;
    }

    public Date getTDOB() {
        return this.tDOB;
    }

    public void setTDOB(Date tDOB) {
        this.tDOB = tDOB;
    }

    public String getTPhoneNo() {
        return this.tPhoneNo;
    }

    public void setTPhoneNo(String tPhoneNo) {
        this.tPhoneNo = tPhoneNo;
    }

    public String getTSpecialist() {
        return this.tSpecialist;
    }

    public void setTSpecialist(String tSpecialist) {
        this.tSpecialist = tSpecialist;
    }

}
