package com.strata.aadhaar.rest;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.strata.aadhaar.Config;
import com.strata.aadhaar.rest.service.FeedService;
import com.strata.aadhaar.utils.SharedPref;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public class RestClient {
    private static FeedService feedService;
    private static SessionRequestInterceptor sessionInterceptor;

    public static void init(String token) {
        Gson gson = new GsonBuilder().create();
        sessionInterceptor = new SessionRequestInterceptor();
        String BASE_URL = "http://"+ SharedPref.getStringValue("SERVER_BASE_URL", "192.168.2.6:3000")+"/api/v1";

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(BASE_URL)
                .setConverter(new GsonConverter(gson))
                .setRequestInterceptor(sessionInterceptor)
                .build();
        sessionInterceptor.setAuthToken(token);
        feedService = restAdapter.create(FeedService.class);
    }

    public static void setAuth_token(String token){
        sessionInterceptor.setAuthToken(token);

    }

    public static FeedService getFeedService() {
        return feedService;
    }

}
