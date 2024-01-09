package com.heroku.java.model;

import java.sql.Date;

public class admission {

    
    private Integer admissionid;
    private Integer Pid;
    private Integer Tid;
    private Date InDate;
    private Date OutDate;
    private String Pname;

    

    public admission(Integer admissionid, Integer pid, Integer tid, String Pname, Date inDate, Date outDate) {
        this.admissionid = admissionid;
        Pid = pid;
        Tid = tid;
        InDate = inDate;
        OutDate = outDate;
        this.Pname = Pname;
    }

    
    public Integer getAdmissionid() {
        return admissionid;
    }

    public Integer getTid() {
        return Tid;
    }

    public Integer getPid() {
        return Pid;
    }

    public Date getInDate() {
        return InDate;
    }

    public Date getOutDate() {
        return OutDate;
    }

    public void setAdmissionid(Integer admissionid) {
        this.admissionid = admissionid;
    }

    public void setTid(Integer tid) {
        Tid = tid;
    }

    public void setPid(Integer pid) {
        Pid = pid;
    }

    public void setInDate(Date inDate) {
        InDate = inDate;
    }

    public void setOutDate(Date outDate) {
        OutDate = outDate;
    }

    public String getPname() {
        return Pname;
    }

    public void setPname(String pname) {
        Pname = pname;
    }   
    
}
