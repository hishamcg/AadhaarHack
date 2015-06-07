package com.strata.aadhaar.rest.service;



import com.strata.aadhaar.model.Transaction;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;


public interface FeedService {
    @GET("/merchants/get_transfers")
    public void getTransactions(Callback<ArrayList<Transaction>> callback);

    @GET("/merchants/get_details.json")
    public void getTransactionDetail(@Query("id") String id, Callback<Transaction> callback);

    @GET("/transaction/transaction_cancel.json")
    public void cancelTransaction(@Query("id") String id, Callback<String> callback);

    @GET("/merchants/confirm_otp.json")
    public void confirmOtp(@Query("otp") String otp,@Query("id") String id, Callback<Transaction> callback);


}