package com.strata.aadhaar.rest.service;

import com.strata.aadhaar.model.AuthToken;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;


public interface SigninApiService {
    @GET("/login_create")
    public void userAuthenticate(@Query("name") String name,@Query("password") String password, Callback<AuthToken> callback);
}
