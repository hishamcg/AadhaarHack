package com.strata.aadhaar.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.strata.aadhaar.R;
import com.strata.aadhaar.model.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class FeedAdapter extends ArrayAdapter<Transaction> {
	private final Activity context;
	private final ArrayList<Transaction> feed_list;
    private Typeface font_reg;

	public FeedAdapter(Activity context, ArrayList<Transaction> feed_list) {
		super(context, R.layout.feed_list_item, feed_list);
		this.context = context;
		this.feed_list = feed_list;
        this.font_reg = Typeface.createFromAsset(context.getAssets(), "fonts/vegur_reg.ttf");

	}

    static class ViewHolder {
        public TextView id_aadhaar;
        public TextView id_txn;
        public TextView id_txn_date;
        public TextView id_amount;
        public TextView id_status;
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
		Transaction feed = feed_list.get(position);
        ViewHolder viewHolder = new ViewHolder();
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.feed_list_item, parent,
                    false);

            viewHolder.id_aadhaar = (TextView) rowView.findViewById(R.id.id_aadhaar);
            viewHolder.id_txn = (TextView) rowView.findViewById(R.id.id_txn);
            viewHolder.id_txn_date = (TextView) rowView.findViewById(R.id.id_txn_date);
            viewHolder.id_amount = (TextView) rowView.findViewById(R.id.id_amount);
            viewHolder.id_status = (TextView) rowView.findViewById(R.id.id_status);
            viewHolder.id_status.setTypeface(font_reg);
            viewHolder.id_aadhaar.setTypeface(font_reg);
            viewHolder.id_txn.setTypeface(font_reg);
            viewHolder.id_txn_date.setTypeface(font_reg);
            viewHolder.id_amount.setTypeface(font_reg);
            rowView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        viewHolder.id_aadhaar.setText(feed.getAadhaar());
        viewHolder.id_txn.setText("Txn : "+feed.getTransanction_no());
        viewHolder.id_txn_date.setText(feed.getDate());
        viewHolder.id_amount.setText("Rs "+String.valueOf(feed.getAmount()));

		return rowView;
	}

    private static long getDateDiff(Date date1, Date date2) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

}
