package com.strata.aadhaar.rest;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.strata.aadhaar.Config;
import com.strata.aadhaar.rest.service.FeedService;
import com.strata.aadhaar.rest.service.PayDetailService;
import com.strata.aadhaar.rest.service.ProfileService;
import com.strata.aadhaar.rest.service.SigninApiService;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public class RestClient {
    private static ProfileService profileService;
    private static FeedService feedService;
    private static PayDetailService payDetailService;
    private static SigninApiService signinApiService;
    private static SessionRequestInterceptor sessionInterceptor;

    public static void init(String token) {
        Gson gson = new GsonBuilder().create();
        sessionInterceptor = new SessionRequestInterceptor();


        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(Config.BASE_URL)
                .setConverter(new GsonConverter(gson))
                .setRequestInterceptor(sessionInterceptor)
                .build();
        sessionInterceptor.setAuthToken(token);
        profileService = restAdapter.create(ProfileService.class);
        feedService = restAdapter.create(FeedService.class);
        payDetailService = restAdapter.create(PayDetailService.class);
        signinApiService = restAdapter.create(SigninApiService.class);
    }

    public static void setAuth_token(String token){
        sessionInterceptor.setAuthToken(token);

    }

    public static PayDetailService getPayDetailService() {
        return payDetailService;
    }
    public static FeedService getFeedService() {
        return feedService;
    }
    public static ProfileService getProfileService() {
        return profileService;
    }
    public static SigninApiService getSigninApiService() {
        return signinApiService;
    }

}
