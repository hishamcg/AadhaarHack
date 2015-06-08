package com.strata.aadhaar.aadhaarhack;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.andreabaccega.widget.FormEditText;
import com.dd.processbutton.iml.ActionProcessButton;
import com.strata.aadhaar.R;
import com.strata.aadhaar.model.Transaction;
import com.strata.aadhaar.rest.RestClient;
import com.strata.aadhaar.utils.ShowToast;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CreateNewTransaction extends Activity{
    private FormEditText user_email,user_phone, user_amount,user_aadhaar;
    private ActionProcessButton update_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_transaction_layout);
        user_email = (FormEditText) findViewById(R.id.user_email);
        user_phone = (FormEditText) findViewById(R.id.user_phone);
        user_aadhaar = (FormEditText) findViewById(R.id.user_aadhaar);
        user_amount = (FormEditText) findViewById(R.id.user_amount);
        final CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);
        update_button = (ActionProcessButton) findViewById(R.id.profile_update_button);
        update_button.setMode(ActionProcessButton.Mode.ENDLESS);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
               @Override
               public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                   if(isChecked)
                        user_phone.setHint("Phone");
                   else
                       user_phone.setHint("Phone *");
               }
            }
        );
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Transaction newTrac = new Transaction();
                if (user_amount.testValidity() && user_aadhaar.testValidity() && (checkBox.isChecked() || user_phone.testValidity())) {
                    try {
                        int amt = Integer.parseInt(user_amount.getText().toString());
                        if(amt>0) {
                            newTrac.setPhone_no(user_phone.getText().toString());
                            newTrac.setAmount(Float.valueOf(user_amount.getText().toString()));
                            newTrac.setEmail(user_email.getText().toString());
                            newTrac.setAadhaar(user_aadhaar.getText().toString());
                            newTrac.setIsCustomerPresent(checkBox.isChecked());
                            update_button.setProgress(1);
                            RestClient.getFeedService().getTransactions(newTrac, new Callback<Transaction>() {
                                @Override
                                public void success(Transaction result, Response response) {
                                    if (result.getSuccess()) {
                                        ShowToast.setText("Transaction in process");
                                        update_button.setProgress(100);
                                        if (checkBox.isChecked()) {
                                            Intent in = new Intent(getApplicationContext(), ManualTransaction.class);
                                            in.putExtra("txn_id", result.getId());
                                            startActivity(in);
                                        }else{
                                            Intent in = new Intent(getApplicationContext(), TransactionDetails.class);
                                            in.putExtra("txn_id",result.getId());
                                            startActivity(in);
                                        }
                                        finish();
                                    } else {
                                        update_button.setProgress(-1);
                                        ShowToast.setText(result.getError());
                                    }
                                }

                                @Override
                                public void failure(RetrofitError retrofitError) {
                                    update_button.setProgress(-1);
                                    ShowToast.setText(retrofitError.toString());
                                }
                            });
                        }else{
                            ShowToast.setText("please enter amount grater that 0");
                        }
                    } catch (NumberFormatException e) {
                        ShowToast.setText("please enter amount");
                    }
                }
            }
        });
    }

}
