package com.strata.aadhaar.aadhaarhack;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.dd.processbutton.iml.ActionProcessButton;
import com.strata.aadhaar.R;
import com.strata.aadhaar.model.Transaction;
import com.strata.aadhaar.rest.RestClient;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CreateNewTransaction extends Activity{
    private FormEditText user_name,user_phone,user_email,user_aadhaar;
    private ActionProcessButton update_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_transaction_layout);
        user_name = (FormEditText) findViewById(R.id.biz_name);
        user_phone = (FormEditText) findViewById(R.id.user_phone);
        user_aadhaar = (FormEditText) findViewById(R.id.user_tan);
        user_email = (FormEditText) findViewById(R.id.user_email);
        final CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);
        update_button = (ActionProcessButton) findViewById(R.id.profile_update_button);
        update_button.setMode(ActionProcessButton.Mode.ENDLESS);
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Transaction newTrac = new Transaction();
                if (user_aadhaar.testValidity()) {
                    if(!checkBox.isChecked()){

                    }else if(user_phone.testValidity()) {
                        newTrac.setPhone_no(user_phone.getText().toString());
                        newTrac.setEmail(user_email.getText().toString());
                        newTrac.setName(user_name.getText().toString());
                        newTrac.setAadhaar(user_aadhaar.getText().toString());

                        update_button.setProgress(1);
                        RestClient.getPayDetailService().getTransactions(newTrac, new Callback<Transaction>() {
                            @Override
                            public void success(Transaction result, Response response) {
                                update_button.setProgress(100);
                                finish();
                            }

                            @Override
                            public void failure(RetrofitError retrofitError) {
                                update_button.setProgress(-1);
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "Failed to update information",
                                        Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP, 0, 170);
                                toast.show();
                            }
                        });
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Name and Location can't be blank", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
