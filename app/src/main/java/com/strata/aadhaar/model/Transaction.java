package com.strata.aadhaar.model;

import java.io.Serializable;

public class Transaction implements Serializable {
	private String name,email,phone_no,aadhaar,id,status,date,error;
    private float amount;
    private Boolean success,isCustomerPresent,has_account,has_paid;

    public Boolean getHas_paid() {
        return has_paid;
    }

    public void setHas_paid(Boolean has_paid) {
        this.has_paid = has_paid;
    }

    public Boolean getHas_account() {
        return has_account;
    }

    public void setHas_account(Boolean has_account) {
        this.has_account = has_account;
    }

    public Boolean getIsCustomerPresent() {
        return isCustomerPresent;
    }

    public void setIsCustomerPresent(Boolean isCustomerPresent) {
        this.isCustomerPresent = isCustomerPresent;
    }

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getAadhaar() {
        return aadhaar;
    }

    public void setAadhaar(String aadhaar) {
        this.aadhaar = aadhaar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
