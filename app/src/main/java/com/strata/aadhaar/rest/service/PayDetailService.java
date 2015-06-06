package com.strata.aadhaar.rest.service;


import com.google.gson.JsonElement;
import com.strata.aadhaar.model.Feed;
import com.strata.aadhaar.model.NewTrasaction;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Query;

public interface PayDetailService {
    @GET("/payments/bill_generator.json")
    public void getBill(@Query("amount") String amount, Callback<JsonElement> callback);

    @PUT("/payments/bill_generator.json")
    public void getTransactions(@Body NewTrasaction newTrasaction, Callback<NewTrasaction> callback);
}
