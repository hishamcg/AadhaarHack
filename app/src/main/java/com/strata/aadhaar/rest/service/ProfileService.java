package com.strata.aadhaar.rest.service;



import com.strata.aadhaar.model.ProfileDetail;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Query;


public interface ProfileService {
    @PUT("/merchants/profile_update.json")
    public void profileUpdate(@Body ProfileDetail profileDetail, Callback<ProfileDetail> callback);

    @GET("/merchants/get_info.json")
    public void getInfo(Callback<ProfileDetail> callback);
}
