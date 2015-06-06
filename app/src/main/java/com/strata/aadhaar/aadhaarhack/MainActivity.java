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

import com.strata.aadhaar.R;
import com.strata.aadhaar.signin.ProfileEntryPage;
import com.strata.aadhaar.signin.SigninActivity;
import com.strata.aadhaar.utils.SharedPref;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String authToken = SharedPref.getStringValue("AUTH_TOKEN");
        String name = SharedPref.getStringValue("NAME");
        if (authToken.isEmpty()) {
            startHome(SigninActivity.class);
        }else if(name.isEmpty()) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
