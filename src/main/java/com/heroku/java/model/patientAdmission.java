package com.heroku.java.model;

import java.util.Date;

public class patientAdmission {
    private int admissionid;
    private String patientname;
    private String patientsex;
    private String patientbloodtype;
    private String patientstatus;
    private Date patientdob;

    public patientAdmission(int admissionid, String patientname, String patientsex,
            String patientbloodtype, String patientstatus, Date patientdob) {
        this.admissionid = admissionid;
        this.patientname = patientname;
        this.patientsex = patientsex;
        this.patientbloodtype = patientbloodtype;
        this.patientstatus = patientstatus;
        this.patientdob = patientdob;
    }

    public patientAdmission() {
    }

    public int getAdmissionid() {
        return this.admissionid;
    }

    public void setAdmissionid(int admissionid) {
        this.admissionid = admissionid;
    }

    public String getPatientname() {
        return this.patientname;
    }

    public void setPatientname(String patientname) {
        this.patientname = patientname;
    }

    public String getPatientsex() {
        return this.patientsex;
    }

    public void setPatientsex(String patientsex) {
        this.patientsex = patientsex;
    }

    public String getPatientbloodtype() {
        return this.patientbloodtype;
    }

    public void setPatientbloodtype(String patientbloodtype) {
        this.patientbloodtype = patientbloodtype;
    }

    public String getPatientstatus() {
        return this.patientstatus;
    }

    public void setPatientstatus(String patientstatus) {
        this.patientstatus = patientstatus;
    }

    public Date getPatientdob() {
        return this.patientdob;
    }

    public void setPatientdob(Date patientdob) {
        this.patientdob = patientdob;
    }
}
