package com.strata.aadhaar.rest.service;



import com.strata.aadhaar.model.Feed;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;


public interface FeedService {
    @GET("/feed/transaction.json")
    public void getTransactions(Callback<ArrayList<Feed>> callback);
}
