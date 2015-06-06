package com.strata.aadhaar;

public interface Config {
    // Google Project Number
    static final String GOOGLE_PROJECT_ID = "959617453751";

static final String SERVER_BASE_URL = "letzdine.com";
    //static final String SERVER_BASE_URL = "192.168.2.23:9090";


    static final String BASE_URL = "http://"+SERVER_BASE_URL+"/api/v1";
    static final String GCM_URL = "http://"+SERVER_BASE_URL+"/api/v1/gcm_register";


    public static String[] progress_states = {"Paid","Confirmed","Initiated"};
    public static String[] failed_states = {"Failed","Cancelled"};
}

