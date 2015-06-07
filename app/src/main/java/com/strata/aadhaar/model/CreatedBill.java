package com.strata.aadhaar.model;

import com.google.gson.JsonElement;

public class CreatedBill {
    JsonElement bill;
    Boolean success;

    public JsonElement getBill() {
        return bill;
    }

    public void setBill(JsonElement bill) {
        this.bill = bill;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
