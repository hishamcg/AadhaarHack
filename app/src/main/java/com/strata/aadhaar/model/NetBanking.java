package com.strata.aadhaar.model;

/**
 * Created by hisham on 6/5/15.
 */
public class NetBanking {
    String issuerCode;
    String bankName;

    @Override
    public String toString() {
        return bankName;
    }

    public String getIssuerCode() {
        return issuerCode;
    }

    public void setIssuerCode(String issuerCode) {
        this.issuerCode = issuerCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
