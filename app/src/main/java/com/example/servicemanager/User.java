package com.example.servicemanager;

public class User {
    String name;
    String mobile_no;

    public User(String name, String mobile_no) {
        this.name = name;
        this.mobile_no = mobile_no;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }
}
