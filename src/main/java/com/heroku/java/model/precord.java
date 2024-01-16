package com.heroku.java.model;

import java.sql.Date;

public class precord {
    private Integer Rid;
    private Date Rdate;
    private String Ractivities;
    private Integer admissionid;
    
    public precord(Integer Rid, Date Rdate, String Ractivities, Integer admissionid) {
        this.Rid = Rid;
        this.Rdate = Rdate;
        this.Ractivities = Ractivities;
        this.admissionid = admissionid;
    }
    public Integer getRid() {
        return Rid;
    }
    public void setRid(Integer Rid) {
        this.Rid = Rid;
    }
    public Date getRdate() {
        return Rdate;
    }
    public void setRdate(Date Rdate) {
        this.Rdate = Rdate;
    }
    public String getRactivities() {
        return Ractivities;
    }
    public void setRacivities(String Ractivities) {
        this.Ractivities = Ractivities;
    }
    public Integer getAdmissionid() {
        return admissionid;
    }
    public void setAdmissionid(Integer admissionid) {
        this.admissionid = admissionid;
    }
}
