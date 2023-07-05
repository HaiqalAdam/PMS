package com.heroku.java.model;

public class drug {
    private Integer drugid;
    private String drugtype;


    public drug(Integer drugid, String drugtype) {
        this.drugid = drugid;
        this.drugtype = drugtype;
    }


    public Integer getDrugid() {
        return drugid;
    }


    public void setDrugid(Integer drugid) {
        this.drugid = drugid;
    }


    public String getDrugtype() {
        return drugtype;
    }


    public void setDrugtype(String drugtype) {
        this.drugtype = drugtype;
    }

    
}
