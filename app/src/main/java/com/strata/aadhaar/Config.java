package com.strata.aadhaar;

import com.strata.aadhaar.utils.SharedPref;

public interface Config {
    static final String BASE_URL = "http://"+ SharedPref.getStringValue("SERVER_BASE_URL","192.168.2.6:3000")+"/api/v1";
    public static String[] failed_states = {"Failed","Cancelled"};
}

