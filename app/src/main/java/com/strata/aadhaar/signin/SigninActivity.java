package com.strata.aadhaar.signin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.dd.processbutton.iml.ActionProcessButton;
import com.strata.aadhaar.R;
import com.strata.aadhaar.model.AuthToken;
import com.strata.aadhaar.rest.RestClient;
import com.strata.aadhaar.utils.NetworkStatus;
import com.strata.aadhaar.utils.SharedPref;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SigninActivity extends Activity {
    private ActionProcessButton btnSignIn;
    private FormEditText enter_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_layout);

        btnSignIn = (ActionProcessButton) findViewById(R.id.button1);
        btnSignIn.setMode(ActionProcessButton.Mode.ENDLESS);
        final TextView no_connection = (TextView) findViewById(R.id.no_connection);
        final TextView forgot_password = (TextView) findViewById(R.id.forgot_password);
        enter_email = (FormEditText) findViewById(R.id.enter_email);
        final FormEditText enter_password = (FormEditText) findViewById(R.id.enter_password);

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enter_email.testValidity()){
                    RestClient.getSigninApiService().resetPassword(enter_email.getText().toString(), new Callback<Boolean>() {
                        @Override
                        public void success(Boolean success, Response response) {
                            if(success){
                                toast("Reset password link has been send to your mail");
                            }else{
                                toast("Failed");
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            toast("Failed");
                        }
                    });
                }
            }
        });
        btnSignIn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if (NetworkStatus.isNetworkAvailable()) {

                    if (enter_email.testValidity() && enter_password.testValidity()) {
                        btnSignIn.setProgress(1);
                        RestClient.getSigninApiService().userAuthenticate(enter_email.getText().toString()
                                ,enter_password.getText().toString(), callback);
                    }else
                        toast("invalid number");
                } else {
                    no_connection.setVisibility(View.VISIBLE);
                    toast("NO internet Connection!");
                }
            }
        });
    }
    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private final Callback<AuthToken> callback = new Callback<AuthToken>() {
        @Override
        public void failure(RetrofitError retrofitError) {
            showTimeoutError();
            btnSignIn.setProgress(-1);
        }

        @Override
        public void success(final AuthToken authToken, Response response) {
            if (authToken.getSuccess()) {
                btnSignIn.setProgress(100);
                SharedPref.setStringValue("AUTH_TOKEN","haha");
                SharedPref.setStringValue("EMAIL",enter_email.getText().toString());
                startActivity(new Intent(SigninActivity.this,ProfileEntryPage.class));

            } else {
                btnSignIn.setProgress(-1);
                toast(authToken.getErrors());
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        btnSignIn.setEnabled(true);
    }

    private void showTimeoutError() {
        new AlertDialog.Builder(SigninActivity.this)
                .setTitle("Connection Time Out!")
                .setMessage("We were not able to reach the server. Please try again after some time")
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        setResult(0, null);
                        finish();
                    }
                })
                .setCancelable(true)
                .setIcon(R.drawable.ic_launcher)
                .show();
    }

}
