package com.strata.aadhaar;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import com.strata.aadhaar.rest.RestClient;
import com.strata.aadhaar.utils.NetworkStatus;
import com.strata.aadhaar.utils.SharedPref;
import com.strata.aadhaar.utils.ShowToast;
import com.citrus.mobile.Config;


public class AadhaarHackApp extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        SharedPref.initialize(this);
        SharedPreferences pref = getSharedPreferences("PREF", Context.MODE_PRIVATE);
        RestClient.init(pref.getString("AUTH_TOKEN", ""));
        NetworkStatus.initialize(this);
        ShowToast.initialize(this);
        initPaymentConfig();
    }

    public void initPaymentConfig(){
//        Config.setEnv("sandbox"); // replace it with production when you are ready
//        Config.setupSignupId("mz34j36y57-signup");
//        Config.setupSignupSecret("853653f2dfa70e178fc708ecbb9e93d4");
//        Config.setSigninId("mz34j36y57-signup");
//        Config.setSigninSecret("c8179a6312dd1d55eeacc000545b3249");

        Config.setEnv("production"); // replace it with production when you are ready
        Config.setupSignupId("2xxow4lrt0-signup");
        Config.setupSignupSecret("0c2eaeecee28dbc3b31620e3cd48995a");
        Config.setSigninId("2xxow4lrt0-signin");
        Config.setSigninSecret("11d2b8c0fd6ec3ac2b9772e113dad6f9");
    }

}
