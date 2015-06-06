package com.strata.aadhaar.aadhaarhack;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.strata.aadhaar.Config;
import com.strata.aadhaar.R;
import com.strata.aadhaar.model.Transaction;
import com.strata.aadhaar.rest.RestClient;
import com.strata.aadhaar.utils.FontsOverride;

import java.util.Arrays;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Karthik on 6/6/15.
 */
public class TransactionDetails extends Activity {
    private TextView custName;
    private TextView aadhaarNum;
    private TextView Status;
    private TextView txtTransactionNum;
    private ImageView statusImg;
    private Transaction feed = new Transaction();
    private String transaaction_no;
    private Button btnCancel;

    @SuppressLint("NewApi")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_details);

        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/style1.ttf");
        custName = (TextView) findViewById(R.id.cust_name);
        aadhaarNum = (TextView) findViewById(R.id.aadhar_no);
        Status = (TextView) findViewById(R.id.status_txt);
        txtTransactionNum = (TextView) findViewById(R.id.transaction_num);
        btnCancel = (Button) findViewById(R.id.btn_trans_cancel);
        statusImg = (ImageView) findViewById(R.id.status_img);
        Bundle bund = getIntent().getExtras();
        transaaction_no = bund.getString("transactionNum");
        RestClient.getFeedService().getTransactionDetail(transaaction_no,callback);


        custName.setText(feed.getName());
        aadhaarNum.setText(feed.getAadhaar());
        txtTransactionNum.setText(feed.getTransanction_no());
        Status.setText(feed.getStatus());

        if(feed.getStatus().equals("Completed"))
            statusImg.setBackgroundResource(R.drawable.icon_up_green);
        else if(Arrays.asList(Config.failed_states).contains(feed.getStatus()))
            statusImg.setBackgroundResource(R.drawable.icon_red_down);
        else
            statusImg.setBackgroundResource(R.drawable.icon_gray_up);



        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestClient.getFeedService().cancelTransaction(transaaction_no, callbackCancleTransaction);
            }
        });


    }

    private Callback<Transaction> callback = new Callback<Transaction>() {
        @Override
        public void success(Transaction resp_feed, Response response) {
            feed = resp_feed;
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
}
