package com.heroku.java.model;

public class drug_usage {
    private Integer dId;
    private Integer pId;

    public drug_usage() {
    }
    public drug_usage(Integer dId) {
        this.dId = dId;
        
    }
    public drug_usage(Integer dId, Integer pId) {
        this.dId = dId;
        this.pId = pId;
    }

    public Integer getDId() {
        return dId;
    }

    public void setDId(Integer dId) {
        this.dId = dId;
    }

    public Integer getPId() {
        return pId;
    }

    public void setPId(Integer pId) {
        this.pId = pId;
    }
    
}
