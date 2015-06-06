package com.strata.aadhaar.rest.service;



import com.strata.aadhaar.model.Transaction;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;


public interface FeedService {
    @GET("/merchants/get_transfers")
    public void getTransactions(Callback<ArrayList<Transaction>> callback);

    @GET("/feed/transaction_detail.json")
    public void getTransactionDetail(@Query("transaction_id") String merchant_id, Callback<Transaction> callback);

    @GET("/feed/transaction_cancel.json")
    public void cancelTransaction(@Query("transaction_id") String merchant_id, Callback<String> callback);

}