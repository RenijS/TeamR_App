package com.example.assign;

public class UserInfo {
    public String name;
    public String dob;
    public String number;
    public String address;

    public UserInfo(){

    }

    public UserInfo(String name, String dob, String number, String address) {
        this.name = name;
        this.dob = dob;
        this.number = number;
        this.address = address;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
