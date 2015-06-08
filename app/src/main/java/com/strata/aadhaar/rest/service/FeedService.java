package com.strata.aadhaar.rest.service;



import com.google.gson.JsonElement;
import com.strata.aadhaar.model.AuthToken;
import com.strata.aadhaar.model.CreatedBill;
import com.strata.aadhaar.model.ProfileDetail;
import com.strata.aadhaar.model.Transaction;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Query;


public interface FeedService {
    @GET("/merchants/get_transfers")
    public void getTransactions(Callback<ArrayList<Transaction>> callback);

    @GET("/merchants/get_details.json")
    public void getTransactionDetail(@Query("id") String id, Callback<Transaction> callback);

    @GET("/merchants/cancel.json")
    public void cancelTransaction(@Query("id") String id, Callback<Transaction> callback);

    @GET("/merchants/confirm_otp.json")
    public void confirmOtp(@Query("otp") String otp,@Query("id") String id, Callback<Transaction> callback);

    @GET("/signups/new")
    public void userAuthenticate(@Query("email") String email,@Query("password") String password, Callback<AuthToken> callback);

    @GET("/signups/reset_password")
    public void resetPassword(@Query("email") String email,Callback<Boolean> callback);

    @PUT("/merchants/profile_update.json")
    public void profileUpdate(@Body ProfileDetail profileDetail, Callback<ProfileDetail> callback);

    @GET("/merchants/get_info.json")
    public void getInfo(Callback<ProfileDetail> callback);

    @GET("/test.json")
    public void testIp(Callback<Transaction> callback);

    @PUT("/merchants/create_refund.json")
    public void getTransactions(@Body Transaction transaction, Callback<Transaction> callback);

    @POST("/transactions/create_txn.json")
    public void settleAmount(@Query("id") String id,Callback<CreatedBill> callback);

    @GET("/merchants/confirm_account.json")
    public void postManualTransaction(@Query("id") String id,@Query("ifsc") String ifsc, @Query("acc_no") String acc_no, Callback<Transaction> callback);


}