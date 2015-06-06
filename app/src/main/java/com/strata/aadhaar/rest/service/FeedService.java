package com.strata.aadhaar.rest.service;



import com.strata.aadhaar.model.Transaction;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;


public interface FeedService {
    @GET("/transaction/transaction.json")
    public void getTransactions(Callback<ArrayList<Transaction>> callback);

    @GET("/transaction/transaction_detail.json")
    public void getTransactionDetail(@Query("transaction_id") String transaction_id, Callback<Transaction> callback);

    @GET("/transaction/transaction_cancel.json")
    public void cancelTransaction(@Query("transaction_id") String transaction_id, Callback<String> callback);

    @GET("/transaction/check_otp.json")
    public void checkOtp(@Query("otp") String otp, Callback<Boolean> callback);


}