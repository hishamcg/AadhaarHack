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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.strata.aadhaar.Config;
import com.strata.aadhaar.R;
import com.strata.aadhaar.model.CreatedBill;
import com.strata.aadhaar.model.Transaction;
import com.strata.aadhaar.payment_gateway.PaymentActivity;
import com.strata.aadhaar.rest.RestClient;
import com.strata.aadhaar.utils.FontsOverride;
import com.strata.aadhaar.utils.ShowToast;

import java.util.Arrays;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class TransactionDetails extends Activity {
    private Transaction transaction = new Transaction();
    private ImageView statusImg;
    private String txn_id;
    private ActionProcessButton btnPay;
    private FormEditText ifscCode,accNum;
    private TextView custName,aadhaarNum,status,date,transactionId,amount;
    private LinearLayout accLayout;
    private Button btnCancel;

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
        btnCancel = (Button) findViewById(R.id.btn_trans_cancel);
        statusImg = (ImageView) findViewById(R.id.status_img);
        btnPay = (ActionProcessButton) findViewById(R.id.btn_pay);
        btnPay.setMode(ActionProcessButton.Mode.ENDLESS);
        ifscCode = (FormEditText) findViewById(R.id.id_ifsc);
        accNum = (FormEditText) findViewById(R.id.id_acc_no);
        accLayout = (LinearLayout) findViewById(R.id.acc_layout);

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(accLayout.getVisibility() == View.VISIBLE){
                    btnPay.setProgress(1);
                    RestClient.getFeedService().postManualTransaction(txn_id, ifscCode.getText().toString(),
                            accNum.getText().toString(), new Callback<Transaction>() {
                                @Override
                                public void success(Transaction tra, Response response) {
                                    if (tra.getSuccess()) {
                                        btnPay.setProgress(100);
                                        RestClient.getFeedService().getTransactionDetail(txn_id, callback);

                                    } else {
                                        btnPay.setProgress(-1);
                                        ShowToast.setText("Failed. Please try again later");
                                    }
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    btnPay.setProgress(-1);
                                    ShowToast.setText(error.toString());
                                }
                            });
                }else {
                    btnPay.setProgress(1);
                    RestClient.getFeedService().settleAmount(transaction.getId(), new Callback<CreatedBill>() {
                        @Override
                        public void success(CreatedBill cBill, Response response) {
                            if (cBill.getSuccess()) {
                                btnPay.setProgress(100);
                                Bundle b = new Bundle();
                                b.putString("billString", cBill.getBill().toString());
                                b.putString("amount", amount.getText().toString());
                                Intent in = new Intent(TransactionDetails.this, PaymentActivity.class);
                                in.putExtras(b);
                                startActivity(in);
                            } else {
                                btnPay.setProgress(-1);
                                ShowToast.setText("Some thing went wrong. please try again later");
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            btnPay.setProgress(-1);
                            ShowToast.setText("please try again later");
                        }
                    });
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestClient.getFeedService().cancelTransaction(transaction.getId(), callbackCancelTransaction);
            }
        });
    }

    private Callback<Transaction> callback = new Callback<Transaction>() {
        @Override
        public void success(Transaction tra, Response response) {
            btnPay.setProgress(0);
            transaction = tra;
            custName.setText((transaction.getName()==null || transaction.getName().isEmpty())?"Customer":transaction.getName());
            aadhaarNum.setText(transaction.getAadhaar());
            transactionId.setText("txn id : "+transaction.getId());
            status.setText(transaction.getStatus());
            date.setText(transaction.getDate());
            amount.setText("Rs "+String.valueOf(transaction.getAmount()));

            if(transaction.getHas_paid()) {
                btnPay.setVisibility(View.GONE);
                accLayout.setVisibility(View.GONE);
            }

            if(!transaction.getHas_account()){
                accLayout.setVisibility(View.VISIBLE);
                btnPay.setText("Enter");
            }else{
                accLayout.setVisibility(View.GONE);
                btnPay.setText("Pay Now");
            }

            if("Completed".equals(transaction.getStatus())) {
                statusImg.setBackgroundResource(R.drawable.icon_up_green);
                HideAllView();
            }else if(Arrays.asList(Config.failed_states).contains(transaction.getStatus())) {
                statusImg.setBackgroundResource(R.drawable.icon_red_down);
                HideAllView();
            }else
                statusImg.setBackgroundResource(R.drawable.icon_gray_up);
        }

        @Override
        public void failure(RetrofitError error) {
            ShowToast.setText(error.toString());
        }
    };

    private void HideAllView(){
        btnPay.setVisibility(View.GONE);
        accLayout.setVisibility(View.GONE);
        btnCancel.setVisibility(View.GONE);
    }


    private Callback<Transaction> callbackCancelTransaction = new Callback<Transaction>() {
        @Override
        public void success(Transaction tra, Response response) {
            if(tra.getSuccess()) {
                ShowToast.setText("Cancelled the transaction");
                RestClient.getFeedService().getTransactionDetail(txn_id, callback);
            }else
                ShowToast.setText(tra.getError());
        }

        @Override
        public void failure(RetrofitError error) {
            ShowToast.setText(error.toString());
        }
    };

    @Override
    public void onResume(){
        super.onResume();
        btnPay.setProgress(0);
        RestClient.getFeedService().getTransactionDetail(txn_id,callback);
    }
}
