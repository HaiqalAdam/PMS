package com.heroku.java.model;

public class users {

    private Integer id;
    private String usr;
    private String pwd;
    private String role;

    public users(Integer id, String usr, String pwd, String role) {
    this.id = id ;
    this.usr = usr;
    this.pwd = pwd;
    this.role = role;
  }

    public String getUsr() {
        return this.usr;
    }

    public void setUsr(String usr) {
        this.usr = usr;
    }

    public String getPwd() {
        return this.pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    

}
