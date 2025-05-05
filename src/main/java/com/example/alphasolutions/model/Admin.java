package com.example.alphasolutions.model;

public class Admin {
    private String name;
    private String uid;
    private String pw;

    public Admin(String uid, String pw) {
        this.uid = uid;
        this.pw = pw;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "name='" + name + '\'' +
                ", uid='" + uid + '\'' +
                ", pw='" + pw + '\'' +
                '}';
    }
}
