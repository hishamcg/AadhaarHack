package com.strata.aadhaar.model;

import java.util.ArrayList;

/**
 * Created by hisham on 6/5/15.
 */
public class PayOption {
    ArrayList<String> creditCard;
    ArrayList<NetBanking> netBanking = new ArrayList<>();

    public ArrayList<String> getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(ArrayList<String> creditCard) {
        this.creditCard = creditCard;
    }

    public ArrayList<NetBanking> getNetBanking() {
        return netBanking;
    }

    public void setNetBanking(ArrayList<NetBanking> netBanking) {
        this.netBanking = netBanking;
    }
}
