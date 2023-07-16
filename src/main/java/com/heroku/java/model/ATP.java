package com.heroku.java.model;

import java.sql.Date;
import java.sql.Timestamp;

public class ATP {
    private Integer id;
    private String therapistName;
    private Integer patientid;
    private String patientname;
    private Integer admissionid;
    private Timestamp admissionIn;
    private Timestamp admissionOut;

    public ATP() {
    }

    public ATP(Integer id, String therapistName, Integer patientid, String patientname, Integer admissionid,
            Timestamp admissionIn, Timestamp admissionOut) {
        this.id = id;
        this.therapistName = therapistName;
        this.patientid = patientid;
        this.patientname = patientname;
        this.admissionid = admissionid;
        this.admissionIn = admissionIn;
        this.admissionOut = admissionOut;
    }

    public ATP(Integer id, String therapistName) {
        this.id = id;
        this.therapistName = therapistName;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTherapistName() {
        return this.therapistName;
    }

    public void setTherapistName(String therapistName) {
        this.therapistName = therapistName;
    }

    public Integer getPatientid() {
        return this.patientid;
    }

    public void setPatientid(Integer patientid) {
        this.patientid = patientid;
    }

    public String getPatientname() {
        return this.patientname;
    }

    public void setPatientname(String patientname) {
        this.patientname = patientname;
    }

    public Integer getAdmissionid() {
        return this.admissionid;
    }

    public void setAdmissionid(Integer admissionid) {
        this.admissionid = admissionid;
    }

    public Timestamp getAdmissionIn() {
        return this.admissionIn;
    }

    public void setAdmissionIn(Timestamp admissionIn) {
        this.admissionIn = admissionIn;
    }

    public Timestamp getAdmissionOut() {
        return this.admissionOut;
    }

    public void setAdmissionOut(Timestamp admissionOut) {
        this.admissionOut = admissionOut;
    }

}
