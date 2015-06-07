package com.strata.aadhaar.rest.service;


import com.google.gson.JsonElement;
import com.strata.aadhaar.model.Transaction;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Query;

public interface PayDetailService {
    @GET("/payments/bill_generator.json")
    public void getBill(@Query("amount") String amount, Callback<JsonElement> callback);

    @PUT("/merchants/create_refund.json")
    public void getTransactions(@Body Transaction transaction, Callback<Transaction> callback);

    @POST("/payments/bill_generator.json")
    public void settleAmount(@Query("transaction_id") String transaction_id,@Query("amount") String amount,
                             Callback<JsonElement> callback);

    @GET("/merchants/confirm_account.json")
    public void postManualTransaction(@Query("id") String id,@Query("ifsc") String ifsc, @Query("acc_no") String acc_no, Callback<String> callback);
}
