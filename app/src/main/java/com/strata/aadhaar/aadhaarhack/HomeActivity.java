package com.strata.aadhaar.aadhaarhack;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.melnykov.fab.FloatingActionButton;
import com.strata.aadhaar.R;
import com.strata.aadhaar.adapters.FeedAdapter;
import com.strata.aadhaar.model.Transaction;
import com.strata.aadhaar.rest.RestClient;
import com.strata.aadhaar.utils.FontsOverride;
import com.strata.aadhaar.utils.NetworkStatus;
import com.strata.aadhaar.utils.ShowToast;

import java.util.ArrayList;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class HomeActivity extends Activity  {
    private TextView no_feeds;
	private ArrayList<Transaction> feedList = new ArrayList<>();
    private FeedAdapter adapter;
    private ProgressDialog dialog;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this,MainActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feed_layout);
        dialog = ProgressDialog.show(HomeActivity.this, "", "Loading. Please wait...", true);
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/style1.ttf");
        final ListView feed_listview = (ListView) findViewById(R.id.feed_list);
		no_feeds = (TextView) findViewById(R.id.no_feeds);
        FloatingActionButton buttonFloat = (FloatingActionButton) findViewById(R.id.create_dine_run);
        buttonFloat.attachToListView(feed_listview);
        adapter = new FeedAdapter(HomeActivity.this, feedList);
        feed_listview.setAdapter(adapter);
		feed_listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in = new Intent(getApplicationContext(), TransactionDetails.class);
                in.putExtra("txn_id",feedList.get(position).getId());
                startActivity(in);
            }
        });

		buttonFloat.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (NetworkStatus.isNetworkAvailable()) {
                    Intent in = new Intent(getApplicationContext(), CreateNewTransaction.class);
                    startActivity(in);
                }else{
                    Toast.makeText(HomeActivity.this,"Please conenct to internet",Toast.LENGTH_SHORT).show();
                }
            }
        });
	}

    private Callback<ArrayList<Transaction>> callback = new Callback<ArrayList<Transaction>>() {
        @Override
        public void success(ArrayList<Transaction> feeds, Response response) {
            if(feeds.isEmpty()){
                no_feeds.setVisibility(View.VISIBLE);
            }else {
                no_feeds.setVisibility(View.GONE);
                feedList.clear();
                feedList.addAll(feeds);
                adapter.notifyDataSetChanged();
            }
            dialog.dismiss();
        }

        @Override
        public void failure(RetrofitError error) {
            if(feedList.isEmpty())
                no_feeds.setVisibility(View.VISIBLE);
            else
                no_feeds.setVisibility(View.GONE);
            ShowToast.setText(error.toString());
            dialog.dismiss();
        }
    };

	@Override
	public void onResume() {
		super.onResume();
        RestClient.getFeedService().getTransactions(callback);
	}
}



