package com.strata.aadhaar.rest.service;

import com.strata.aadhaar.model.AuthToken;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;


public interface SigninApiService {
    @GET("/signups/new")
    public void userAuthenticate(@Query("email") String email,@Query("password") String password, Callback<AuthToken> callback);

    @GET("/signups/reset_password")
    public void resetPassword(@Query("email") String email,Callback<Boolean> callback);

}
