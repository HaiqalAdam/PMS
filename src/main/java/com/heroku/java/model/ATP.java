package com.heroku.java.model;

import java.sql.Date;
import java.sql.Timestamp;

public class ATP {
    private int id;
    private String therapistName;
    private int patientid;
    private String patientname;
    private int admissionid;
    private Timestamp admissionIn;
    private Timestamp admissionOut;

    public ATP() {
    }

    public ATP(int id, String therapistName, int patientid, String patientname, int admissionid,
            Timestamp admissionIn, Timestamp admissionOut) {
        this.id = id;
        this.therapistName = therapistName;
        this.patientid = patientid;
        this.patientname = patientname;
        this.admissionid = admissionid;
        this.admissionIn = admissionIn;
        this.admissionOut = admissionOut;
    }

    public ATP(int id, String therapistName) {
        this.id = id;
        this.therapistName = therapistName;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTherapistName() {
        return this.therapistName;
    }

    public void setTherapistName(String therapistName) {
        this.therapistName = therapistName;
    }

    public int getPatientid() {
        return this.patientid;
    }

    public void setPatientid(int patientid) {
        this.patientid = patientid;
    }

    public String getPatientname() {
        return this.patientname;
    }

    public void setPatientname(String patientname) {
        this.patientname = patientname;
    }

    public int getAdmissionid() {
        return this.admissionid;
    }

    public void setAdmissionid(int admissionid) {
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
