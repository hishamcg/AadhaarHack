package com.strata.aadhaar.model;

import java.util.ArrayList;

public class UserWallet {
    private String type,defaultOption;
    private ArrayList<SavedCards> paymentOptions = new ArrayList<>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDefaultOption() {
        return defaultOption;
    }

    public void setDefaultOption(String defaultOption) {
        this.defaultOption = defaultOption;
    }

    public ArrayList<SavedCards> getPaymentOptions() {
        return paymentOptions;
    }

    public void setPaymentOptions(ArrayList<SavedCards> paymentOptions) {
        this.paymentOptions = paymentOptions;
    }
}
