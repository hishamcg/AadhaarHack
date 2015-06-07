package com.strata.aadhaar.aadhaarhack;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.andreabaccega.widget.FormEditText;
import com.dd.processbutton.iml.ActionProcessButton;
import com.strata.aadhaar.R;
import com.strata.aadhaar.model.Transaction;
import com.strata.aadhaar.rest.RestClient;
import com.strata.aadhaar.utils.FontsOverride;
import com.strata.aadhaar.utils.ShowToast;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ManualTransaction extends Activity {
    private FormEditText otp,ifscCode,accNum;
    private LinearLayout accLayout;
    private ActionProcessButton button;
    private String txn_id;

    @SuppressLint("NewApi")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual_transaction);
        txn_id = getIntent().getStringExtra("txn_id");
        otp = (FormEditText) findViewById(R.id.id_otp);
        accLayout = (LinearLayout) findViewById(R.id.acc_layout);
        ifscCode = (FormEditText) findViewById(R.id.id_ifsc);
        accNum = (FormEditText) findViewById(R.id.id_acc_no);
        button = (ActionProcessButton)findViewById(R.id.button);
        button.setMode(ActionProcessButton.Mode.ENDLESS);

        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/style1.ttf");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(otp.getVisibility() == View.VISIBLE) {

                    button.setProgress(1);
                    RestClient.getFeedService().confirmOtp(otp.getText().toString(),txn_id, new Callback<Transaction>() {
                        @Override
                        public void success(Transaction tra, Response response) {
                            if (tra.getSuccess()) {
                                if(tra.getHas_account()){
                                    button.setProgress(100);
                                    ShowActivity();
                                }else {
                                    button.setProgress(0);
                                    accLayout.setVisibility(View.VISIBLE);
                                    otp.setVisibility(View.GONE);
                                }
                            } else {
                                button.setProgress(-1);
                                ShowToast.setText(tra.getError());
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            button.setProgress(-1);
                            ShowToast.setText(error.toString());
                        }
                    });
                }else if(ifscCode.testValidity() && accNum.testValidity()) {
                    button.setProgress(1);
                    RestClient.getFeedService().postManualTransaction(txn_id,ifscCode.getText().toString(),
                            accNum.getText().toString(), new Callback<Transaction>() {
                        @Override
                        public void success(Transaction tra, Response response) {
                            if(tra.getSuccess()) {
                                button.setProgress(100);
                                ShowActivity();
                            }else{
                                button.setProgress(-1);
                                ShowToast.setText("Failed. Please try again later");
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            button.setProgress(-1);
                            ShowToast.setText(error.toString());
                        }
                    });
                }
            }
        });
    }

    private void ShowActivity(){
        ShowToast.setText("Transaction in progress");
        Intent in;
        in = new Intent(ManualTransaction.this,HomeActivity.class);
        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(in);
        in = new Intent(ManualTransaction.this,TransactionDetails.class);
        in.putExtra("txn_id",txn_id);
        startActivity(in);
        finish();
    }

}
