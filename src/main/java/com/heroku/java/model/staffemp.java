package com.heroku.java.model;

import java.sql.Date;

public class staffemp {
    private Integer sEid;
    private String sEusr;
    private String sEpwd;
    private String sErole;

    private String sEName;
    private String sERelationStatus;
    private Date sEDate;
    private String sESex;
    private Date sEDOB;
    private String sEPhoneNo;
    private String sEPosition;

    public staffemp(Integer sEid, String sEusr, String sEpwd, String sErole, String sEName, String sERelationStatus,
            Date sEDate, String sESex, Date sEDOB, String sEPhoneNo, String sEPosition) {
        this.sEid = sEid;
        this.sEusr = sEusr;
        this.sEpwd = sEpwd;
        this.sErole = sErole;
        this.sEName = sEName;
        this.sERelationStatus = sERelationStatus;
        this.sEDate = sEDate;
        this.sESex = sESex;
        this.sEDOB = sEDOB;
        this.sEPhoneNo = sEPhoneNo;
        this.sEPosition = sEPosition;
    }

    public staffemp(Integer sEid, String sEusr, String sEpwd, String sEName, String sERelationStatus, Date sEDate,
            String sESex, Date sEDOB, String sEPhoneNo, String sEPosition) {
        this.sEid = sEid;
        this.sEusr = sEusr;
        this.sEpwd = sEpwd;
        this.sEName = sEName;
        this.sERelationStatus = sERelationStatus;
        this.sEDate = sEDate;
        this.sESex = sESex;
        this.sEDOB = sEDOB;
        this.sEPhoneNo = sEPhoneNo;
        this.sEPosition = sEPosition;
    }

    public Integer getsEid() {
        return sEid;
    }

    public void setsEid(Integer sEid) {
        this.sEid = sEid;
    }

    public String getsEusr() {
        return sEusr;
    }

    public void setsEusr(String sEusr) {
        this.sEusr = sEusr;
    }

    public String getsEpwd() {
        return sEpwd;
    }

    public void setsEpwd(String sEpwd) {
        this.sEpwd = sEpwd;
    }

    public String getsErole() {
        return sErole;
    }

    public void setsErole(String sErole) {
        this.sErole = sErole;
    }

    public String getsEName() {
        return sEName;
    }

    public void setsEName(String sEName) {
        this.sEName = sEName;
    }

    public String getsERelationStatus() {
        return sERelationStatus;
    }

    public void setsERelationStatus(String sERelationStatus) {
        this.sERelationStatus = sERelationStatus;
    }

    public Date getsEDate() {
        return sEDate;
    }

    public void setsEDate(Date sEDate) {
        this.sEDate = sEDate;
    }

    public String getsESex() {
        return sESex;
    }

    public void setsESex(String sESex) {
        this.sESex = sESex;
    }

    public Date getsEDOB() {
        return sEDOB;
    }

    public void setsEDOB(Date sEDOB) {
        this.sEDOB = sEDOB;
    }

    public String getsEPhoneNo() {
        return sEPhoneNo;
    }

    public void setsEPhoneNo(String sEPhoneNo) {
        this.sEPhoneNo = sEPhoneNo;
    }

    public String getsEPosition() {
        return sEPosition;
    }

    public void setsEPosition(String sEPosition) {
        this.sEPosition = sEPosition;
    }

    
    
    
}