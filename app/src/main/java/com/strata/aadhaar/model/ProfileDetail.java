package com.strata.aadhaar.model;

public class    ProfileDetail {
    private String phone_no,email,name,tan_no,error;
    private Boolean success;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTan_no() {
        return tan_no;
    }

    public void setTan_no(String tan_no) {
        this.tan_no = tan_no;
    }
}
