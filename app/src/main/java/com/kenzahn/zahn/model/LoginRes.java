package com.kenzahn.zahn.model;

public class LoginRes {
    private String userstatus;

    private Boolean loginstatus;

    private int userid;

    private String username;

    public String getUserstatus() {
        return userstatus;
    }

    public void setUserstatus(String userstatus) {
        this.userstatus = userstatus;
    }

    public Boolean getLoginstatus() {
        return loginstatus;
    }

    public void setLoginstatus(Boolean loginstatus) {
        this.loginstatus = loginstatus;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "ClassPojo [userstatus = " + userstatus + ", loginstatus = " + loginstatus + ", userid = " + userid + ", username = " + username + "]";
    }
}
