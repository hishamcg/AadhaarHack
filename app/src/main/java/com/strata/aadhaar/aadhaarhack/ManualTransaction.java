package com.strata.aadhaar.aadhaarhack;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.strata.aadhaar.R;
import com.strata.aadhaar.model.Transaction;
import com.strata.aadhaar.rest.RestClient;
import com.strata.aadhaar.utils.FontsOverride;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Karthik on 6/6/15.
 */
public class ManualTransaction extends Activity {
    private EditText otp;
    private ImageView btnOtp;
    private LinearLayout otpLayout;
    private LinearLayout accLayout;
    private EditText ifscCode;
    private EditText accNum;
    private ImageView btnAccSubmit;

    @SuppressLint("NewApi")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual_transaction);

        otp = (EditText) findViewById(R.id.id_otp);
        btnOtp = (ImageView) findViewById(R.id.btn_otp_submit);
        otpLayout = (LinearLayout) findViewById(R.id.otp_layout);
        accLayout = (LinearLayout) findViewById(R.id.acc_layout);
        ifscCode = (EditText) findViewById(R.id.id_ifsc);
        accNum = (EditText) findViewById(R.id.id_acc_no);

        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/style1.ttf");

        btnOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestClient.getFeedService().checkOtp(otp.getText().toString(), new Callback<Boolean>() {
                    @Override
                    public void success(Boolean flagOtp, Response response) {
                        if(flagOtp){
                            otpLayout.setVisibility(View.INVISIBLE);
                            accLayout.setVisibility(View.VISIBLE);
                        } else{
                            accLayout.setVisibility(View.VISIBLE);
                            otpLayout.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(ManualTransaction.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnAccSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestClient.getPayDetailService().postManualTransaction(ifscCode.getText().toString(), accNum.getText().toString(), callback);
            }
        });
        }

    private Callback<String> callback = new Callback<String>() {
        @Override
        public void success(String resp_str, Response response) {
//            feed = resp_feed;
        }

        @Override
        public void failure(RetrofitError error) {
            Toast.makeText(ManualTransaction.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
        }
    };

}
