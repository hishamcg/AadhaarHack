package com.strata.aadhaar.signin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.dd.processbutton.iml.ActionProcessButton;
import com.strata.aadhaar.R;
import com.strata.aadhaar.aadhaarhack.HomeActivity;
import com.strata.aadhaar.model.ProfileDetail;
import com.strata.aadhaar.rest.RestClient;
import com.strata.aadhaar.utils.SharedPref;
import com.strata.aadhaar.utils.ShowToast;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ProfileEntryPage extends Activity {
    private FormEditText biz_name,user_phone,user_tan;
    private TextView user_email;
    private ActionProcessButton update_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_entry_layout);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        biz_name = (FormEditText) findViewById(R.id.biz_name);
        user_phone = (FormEditText) findViewById(R.id.user_phone);
        user_tan = (FormEditText) findViewById(R.id.user_tan);
        user_email = (TextView) findViewById(R.id.user_email);
        user_email.setText(SharedPref.getStringValue("EMAIL"));
        update_button = (ActionProcessButton) findViewById(R.id.profile_update_button);
        update_button.setMode(ActionProcessButton.Mode.ENDLESS);
        update_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileDetail profile_detail = new ProfileDetail();

                if (biz_name.testValidity() && user_phone.testValidity() && user_tan.testValidity()) {
                    profile_detail.setPhone_no(user_phone.getText().toString());
                    profile_detail.setName(biz_name.getText().toString());
                    profile_detail.setTan_no(user_tan.getText().toString());
                    update_button.setProgress(1);
                    RestClient.getProfileService().profileUpdate(profile_detail, new Callback<ProfileDetail>() {
                        @Override
                        public void success(ProfileDetail detail, Response response) {
                            if(detail.getSuccess()!=null && detail.getSuccess()) {
                                update_button.setProgress(100);
                                updateConsumerInfo(detail);
                            }else{
                                update_button.setProgress(-1);
                                ShowToast.setText(detail.getError());
                            }
                        }

                        @Override
                        public void failure(RetrofitError retrofitError) {
                            update_button.setProgress(-1);
                            ShowToast.setText("Failed to update information\n" +
                                    "please try again.");
                        }
                    });
                }
            }
        });

        RestClient.getProfileService().getInfo(new Callback<ProfileDetail>() {
            @Override
            public void success(ProfileDetail detail, Response response) {
                setNBDetails(detail);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
            }
        });
    }

    private void setNBDetails(ProfileDetail profile) {
        if(profile.getName()!=null)
            biz_name.setText(profile.getName());
        if(profile.getPhone_no()!=null)
            user_phone.setText(profile.getPhone_no());
        if(profile.getTan_no()!=null)
            user_tan.setText(profile.getTan_no());
    }

    private void updateConsumerInfo(ProfileDetail detail) {
        SharedPreferences preferences = getSharedPreferences("PREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("NAME", detail.getName())
                .putString("TAN", detail.getTan_no())
                .putString("PHONE", detail.getPhone_no());

        editor.apply();
        Toast.makeText(getApplicationContext(), "Information successfully updated", Toast.LENGTH_SHORT).show();
        Intent in;
        in = new Intent(getBaseContext(), HomeActivity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(in);
        finish();
    }
}