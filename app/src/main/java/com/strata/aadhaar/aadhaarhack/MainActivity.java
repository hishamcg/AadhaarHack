package com.strata.aadhaar.aadhaarhack;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.andreabaccega.widget.FormEditText;
import com.dd.processbutton.iml.ActionProcessButton;
import com.strata.aadhaar.R;
import com.strata.aadhaar.rest.RestClient;
import com.strata.aadhaar.signin.ProfileEntryPage;
import com.strata.aadhaar.signin.SigninActivity;
import com.strata.aadhaar.utils.SharedPref;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionProcessButton btn = (ActionProcessButton)findViewById(R.id.btn);
        final FormEditText ip = (FormEditText)findViewById(R.id.ip);
        final FormEditText port = (FormEditText)findViewById(R.id.port);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ip.testValidity() && port.testValidity()){
                    SharedPref.setStringValue("SERVER_BASE_URL",ip.getText().toString()+":"+port.getText().toString());
                    RestClient.init(SharedPref.getStringValue("AUTH_TOKEN"));
                    Validate();
                }
            }
        });
        if(!SharedPref.getStringValue("SERVER_BASE_URL").isEmpty()) {
            Validate();
        }
    }

    private void Validate(){
        if (SharedPref.getStringValue("AUTH_TOKEN").isEmpty()) {
            startHome(SigninActivity.class);
        }else if(SharedPref.getStringValue("NAME").isEmpty()) {
            startHome(ProfileEntryPage.class);
        }else{
            startHome(HomeActivity.class);
        }
    }

    private void startHome(Class cls) {
        Intent in = new Intent(MainActivity.this, cls);
        startActivity(in);
        finish();
    }

}
