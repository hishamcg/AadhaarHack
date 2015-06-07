package com.strata.aadhaar.payment_gateway;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.strata.aadhaar.R;
import com.strata.aadhaar.aadhaarhack.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;


public class WebPage extends Activity {
    WebView webView;
    String feed_id;
    AlertDialog alert;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_page);
        alert = new AlertDialog.Builder(this).create();
        alert.setMessage("Redirecting to Payment Gateway. Please wait ...");
        alert.show();
        String url = getIntent().getStringExtra("url");
        feed_id = getIntent().getStringExtra("feed_id");
        webView = (WebView) this.findViewById(R.id.webview);

        webView.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.addJavascriptInterface(new JsInterface(this), "CitrusResponse");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                alert.dismiss();
            }
        });

        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(url);

    }


    private class JsInterface {
        private Activity activity;
        public JsInterface(Activity activity) {
            this.activity = activity;
        }
        @JavascriptInterface
        public void pgResponse(String response) {
            Log.d("response",response);
            try {
                JSONObject response_json = new JSONObject(response);
                String status = response_json.getString("TxStatus");
                String feedId = response_json.getString("feedId");
                if (status.equals("SUCCESS")){
                    Toast.makeText(activity, "Payment is Successful.",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(activity,response_json.getString("reason"),Toast.LENGTH_LONG).show();
                }
            }catch (JSONException e) {
                Toast.makeText(activity.getApplicationContext(),"Payment Failed",Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            //finish();  //can be used if ChaMainTabActivity onResume adapter notify is managed

            Bundle b = new Bundle();
            b.putString("feed_id", feed_id);
            Intent in = new Intent(getApplicationContext(), HomeActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(in);
            finish();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        webView.removeJavascriptInterface("CitrusResponse");
        alert.dismiss();
    }
}
