package com.strata.aadhaar;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.strata.aadhaar.rest.RestClient;
import com.strata.aadhaar.utils.NetworkStatus;
import com.strata.aadhaar.utils.SharedPref;
import com.strata.aadhaar.utils.ShowToast;


public class AadhaarHackApp extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        SharedPreferences pref = getSharedPreferences("PREF", Context.MODE_PRIVATE);
        RestClient.init(pref.getString("AUTH_TOKEN", ""));
        SharedPref.initialize(this);
        NetworkStatus.initialize(this);
        ShowToast.initialize(this);
    }
}
