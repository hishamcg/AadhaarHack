package com.strata.aadhaar.aadhaarhack;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.dd.processbutton.iml.ActionProcessButton;
import com.google.gson.JsonElement;
import com.strata.aadhaar.Config;
import com.strata.aadhaar.R;
import com.strata.aadhaar.model.Transaction;
import com.strata.aadhaar.payment_gateway.PaymentActivity;
import com.strata.aadhaar.rest.RestClient;
import com.strata.aadhaar.utils.FontsOverride;

import java.util.Arrays;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class TransactionDetails extends Activity {
    private Transaction transaction = new Transaction();
    private ImageView statusImg;
    private String txn_id;
    private ActionProcessButton btnPay;
    private  TextView custName,aadhaarNum,status,date,transactionId,amount;

    @SuppressLint("NewApi")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_details);

        txn_id = getIntent().getStringExtra("txn_id");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/style1.ttf");
        custName = (TextView) findViewById(R.id.cust_name);
        aadhaarNum = (TextView) findViewById(R.id.aadhar_no);
        status = (TextView) findViewById(R.id.status_txt);
        amount = (TextView) findViewById(R.id.amount);
        date = (TextView) findViewById(R.id.date);
        transactionId = (TextView) findViewById(R.id.transaction_num);
        Button btnCancel = (Button) findViewById(R.id.btn_trans_cancel);
        statusImg = (ImageView) findViewById(R.id.status_img);
        btnPay = (ActionProcessButton) findViewById(R.id.btn_pay);
        btnPay.setMode(ActionProcessButton.Mode.ENDLESS);

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPay.setProgress(1);
                RestClient.getPayDetailService().settleAmount(transaction.getId(),
                        amount.getText().toString(), new Callback<JsonElement>() {
                            @Override
                            public void success(JsonElement jsonElement, Response response) {
                                btnPay.setProgress(100);
                                Bundle b = new Bundle();
                                b.putString("billString", jsonElement.toString());
                                b.putString("amount", amount.getText().toString());
                                Intent in = new Intent(TransactionDetails.this, PaymentActivity.class);
                                in.putExtras(b);
                                startActivity(in);
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                btnPay.setProgress(-1);
                            }
                        });
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestClient.getFeedService().cancelTransaction(transaction.getId(), callbackCancleTransaction);
            }
        });
    }

    private Callback<Transaction> callback = new Callback<Transaction>() {
        @Override
        public void success(Transaction tra, Response response) {
            transaction = tra;
            custName.setText(transaction.getName());
            aadhaarNum.setText(transaction.getAadhaar());
            transactionId.setText(transaction.getId());
            status.setText(transaction.getStatus());
            date.setText(transaction.getId());

            if(transaction.getHas_paid())
                btnPay.setVisibility(View.GONE);

            if(transaction.getStatus().equals("Completed")) {
                statusImg.setBackgroundResource(R.drawable.icon_up_green);
            }else if(Arrays.asList(Config.failed_states).contains(transaction.getStatus()))
                statusImg.setBackgroundResource(R.drawable.icon_red_down);
            else
                statusImg.setBackgroundResource(R.drawable.icon_gray_up);
        }

        @Override
        public void failure(RetrofitError error) {
            Toast.makeText(TransactionDetails.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
        }
    };


    private Callback<String> callbackCancleTransaction = new Callback<String>() {
        @Override
        public void success(String str_response, Response response) {
            Toast.makeText(TransactionDetails.this, str_response, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void failure(RetrofitError error) {
            Toast.makeText(TransactionDetails.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onResume(){
        super.onResume();
        RestClient.getFeedService().getTransactionDetail(txn_id,callback);
    }
}
