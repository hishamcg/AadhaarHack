package com.strata.aadhaar.model;

/**
 * Created by hisham on 6/6/15.
 */
public class NewTrasaction {
    String name,email,aadaar,status,phone_no;

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAadaar() {
        return aadaar;
    }

    public void setAadaar(String aadaar) {
        this.aadaar = aadaar;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
