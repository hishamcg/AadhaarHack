package com.strata.aadhaar.aadhaarhack;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.melnykov.fab.FloatingActionButton;
import com.strata.aadhaar.R;
import com.strata.aadhaar.adapters.FeedAdapter;
import com.strata.aadhaar.model.Feed;
import com.strata.aadhaar.rest.RestClient;
import com.strata.aadhaar.utils.FontsOverride;
import com.strata.aadhaar.utils.NetworkStatus;
import com.strata.aadhaar.utils.SharedPref;
import java.util.ArrayList;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class HomeActivity extends Activity  {
    private TextView no_feeds;
	private ArrayList<Feed> feedList = new ArrayList<>();
    private FeedAdapter adapter;

	
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feed_layout);

        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/style1.ttf");
        ListView feed_listview = (ListView) findViewById(R.id.feed_list);
		no_feeds = (TextView) findViewById(R.id.no_feeds);
        FloatingActionButton buttonFloat = (FloatingActionButton) findViewById(R.id.create_dine_run);
        buttonFloat.attachToListView(feed_listview);
        adapter = new FeedAdapter(HomeActivity.this, feedList);
        feed_listview.setAdapter(adapter);
		feed_listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

		buttonFloat.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (NetworkStatus.isNetworkAvailable()) {
                    Bundle bund = new Bundle();
                    bund.putString("isFromChatPage", "NewDineRun");
                    Intent in = new Intent(getApplicationContext(), CreateNewTransaction.class);
                    in.putExtras(bund);
                    startActivity(in);
                }else{
                    Toast.makeText(HomeActivity.this,"Please conenct to internet",Toast.LENGTH_SHORT).show();
                }
            }
        });

        RestClient.getFeedService().getTransactions(callback);
	}

    private Callback<ArrayList<Feed>> callback = new Callback<ArrayList<Feed>>() {
        @Override
        public void success(ArrayList<Feed> feeds, Response response) {
            if(feeds.isEmpty()){
                no_feeds.setVisibility(View.VISIBLE);
            }else {
                no_feeds.setVisibility(View.GONE);
                feedList.clear();
                feedList.addAll(feeds);
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void failure(RetrofitError error) {
            Toast.makeText(HomeActivity.this,"Failed to fetch data",Toast.LENGTH_SHORT).show();
        }
    };

	@Override
	public void onResume() {
        Log.d("OnResume","HomeActiciyty");
		super.onResume();
		this.registerReceiver(mMessageReceiver, new IntentFilter("com.strata.team_lunch.chat"));
        RestClient.getFeedService().getTransactions(callback);
	}


	public void onPause() {
		super.onPause();
		if(mMessageReceiver != null)
			this.unregisterReceiver(mMessageReceiver);
		//close db on destroy.
		//db.close();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
	    @Override
	    public void onReceive(Context context, Intent intent) {
	    }
	};
}



