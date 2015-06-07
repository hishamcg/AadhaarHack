package com.strata.aadhaar.payment_gateway;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.citrus.asynch.Binduser;
import com.citrus.asynch.GetWallet;
import com.citrus.asynch.PaymentOptions;
import com.citrus.asynch.Savecard;
import com.citrus.card.Card;
import com.citrus.mobile.Callback;
import com.citrus.netbank.Bank;
import com.citrus.payment.Bill;
import com.citrus.payment.PG;
import com.citrus.payment.UserDetails;
import com.citrus.widgets.CardNumberEditText;
import com.dd.processbutton.iml.ActionProcessButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.strata.aadhaar.R;
import com.strata.aadhaar.model.NetBanking;
import com.strata.aadhaar.model.PayOption;
import com.strata.aadhaar.model.SavedCards;
import com.strata.aadhaar.model.UserWallet;
import com.strata.aadhaar.utils.SharedPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;


public class PaymentActivity extends Activity {

    private ArrayList<NetBanking> netBankList = new ArrayList<>();
    private String bankcode,feed_id;
    private ArrayAdapter<NetBanking> modeAdapter;
    private ListView banks_list;
    ActionProcessButton credit_pay,bank_pay;
    private LinearLayout saved_cards;
    private Activity activity;
    private boolean bindingStatus = false;
    Bill bill;
    ScrollView scrollView;
    RelativeLayout paymentLayout;
    EditText ccvEdit;
    String[] BANK_CODES = {"CID005","CID010","CID001","CID002"};
    String[] BANK_NAMES = {"SBI","HDFC","ICICI","AXIS"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_select_layout);
        activity = this;
        bill = new Bill(getIntent().getStringExtra("billString"));
        feed_id = getIntent().getStringExtra("feed_id");
        paymentOptions();
        scrollView = (ScrollView)findViewById(R.id.scroll_view);
        paymentLayout = (RelativeLayout)findViewById(R.id.payment_layout);
        TextView debitCard = (TextView)findViewById(R.id.debit_card);
        TextView creditCard = (TextView)findViewById(R.id.credit_card);
        final TextView netBanking = (TextView)findViewById(R.id.net_banking);
        TextView amount = (TextView)findViewById(R.id.amount);
        saved_cards = (LinearLayout)findViewById(R.id.saved_cards);
        amount.setText(getIntent().getStringExtra("amount"));

        new Binduser(this, new Callback() {
            @Override
            public void onTaskexecuted(String success, String error) {
                if (error.isEmpty()) {
                    new GetWallet(activity, new Callback() {
                        @Override
                        public void onTaskexecuted(String success, String error) {
                            System.out.println(success);
                            getCardToken(success,error);
                        }
                    }).execute();
                }
            }
        }).execute(SharedPref.getStringValue("EMAIL"), SharedPref.getStringValue("PHONE"));

        banks_list = new ListView(this);
        modeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, netBankList);
        banks_list.setAdapter(modeAdapter);
        final AlertDialog dialog_net;
        final AlertDialog.Builder builder_net = new AlertDialog.Builder(this);
        builder_net.setView(banks_list);
        dialog_net = builder_net.create();

        View.OnClickListener loadCardView = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentLayout.setVisibility(View.VISIBLE);
                paymentLayout.removeAllViews();
                final LayoutInflater inflater =(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                View in_view = inflater.inflate(R.layout.credit_card_layout, null);

                ccvEdit  = (EditText)in_view.findViewById(R.id.cvv);
                final CardNumberEditText cardNumber = (CardNumberEditText)in_view.findViewById(R.id.card_number);
                final EditText name = (EditText)in_view.findViewById(R.id.card_name);
                credit_pay = (ActionProcessButton)in_view.findViewById(R.id.pay);
                credit_pay.setMode(ActionProcessButton.Mode.ENDLESS);
                final TextView month = (TextView)in_view.findViewById(R.id.month);
                final TextView year = (TextView)in_view.findViewById(R.id.year);
                final CheckBox checkBoxSave = (CheckBox)in_view.findViewById(R.id.checkBoxSave);
                final String type;
                if(v.getId() == R.id.debit_card){
                    TextView title = (TextView)in_view.findViewById(R.id.title);
                    title.setText("ENTER DEBIT CARD NUMBER");
                    type = "debit";
                }else{
                    type = "credit";
                }

                View.OnClickListener picker = new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {

                        final AlertDialog dialog;
                        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        ListView list = new ListView(activity);

                        final ArrayList<Integer> pick_list = new ArrayList<>();
                        int startValue;
                        int endValue;
                        if(v.getId() == R.id.month){
                            startValue = 1;
                            endValue = 13;
                        }else{
                            startValue = Calendar.getInstance().get(Calendar.YEAR);
                            endValue = 2051;
                        }
                        for(int i=startValue;i<endValue;i++){
                            pick_list.add(i);
                        }

                        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(activity,
                                android.R.layout.simple_list_item_1, android.R.id.text1, pick_list.toArray(new Integer[pick_list.size()]));
                        list.setAdapter(adapter);
                        builder.setView(list);
                        dialog = builder.create();
                        dialog.show();
                        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> arg0, View arg1,
                                                    int pos, long arg3) {
                                if(v.getId() == R.id.month){
                                    month.setText(String.valueOf(pick_list.get(pos)));
                                }else{
                                    String yr = String.valueOf(pick_list.get(pos));
                                    year.setText(yr.substring(Math.max(yr.length() - 2, 0)));
                                }
                                dialog.dismiss();
                            }
                        });
                    }
                };
                month.setOnClickListener(picker);
                year.setOnClickListener(picker);
                paymentLayout.addView(in_view);
                focusOnView();

                credit_pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        credit_pay.setProgress(1);
                        if (checkBoxSave.isChecked() && bindingStatus) {
                            saveCard(cardNumber.getText().toString(), month.getText().toString(),
                                    year.getText().toString(), ccvEdit.getText().toString(),
                                    name.getText().toString(), type);
                        }else {
                            cardpay(cardNumber.getText().toString(), month.getText().toString(),
                                    year.getText().toString(), ccvEdit.getText().toString(),
                                    name.getText().toString(), type);
                        }
                    }
                });
            }
        };
        View.OnClickListener loadNetBankingView = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentLayout.setVisibility(View.VISIBLE);
                paymentLayout.removeAllViews();
                final LayoutInflater inflater =(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                View in_view = inflater.inflate(R.layout.net_banking_layout, null);
                final TextView banks = (TextView)in_view.findViewById(R.id.banks);
                final ImageView sbi = (ImageView)in_view.findViewById(R.id.sbi);
                final ImageView hdfc = (ImageView)in_view.findViewById(R.id.hdfc);
                final ImageView icici = (ImageView)in_view.findViewById(R.id.icici);
                final ImageView axis = (ImageView)in_view.findViewById(R.id.axis);
                bank_pay = (ActionProcessButton)in_view.findViewById(R.id.net_pay);

                View.OnClickListener fastSelect = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sbi.setBackgroundResource(R.drawable.border_black);
                        hdfc.setBackgroundResource(R.drawable.border_black);
                        icici.setBackgroundResource(R.drawable.border_black);
                        axis.setBackgroundResource(R.drawable.border_black);
                        switch (v.getId()) {
                            case R.id.sbi:
                                bankcode = BANK_CODES[0];
                                banks.setText(BANK_NAMES[0]);
                                sbi.setBackgroundResource(R.drawable.border_blue);
                                break;
                            case R.id.hdfc:
                                bankcode = BANK_CODES[1];
                                banks.setText(BANK_NAMES[1]);
                                hdfc.setBackgroundResource(R.drawable.border_blue);
                                break;
                            case R.id.icici:
                                bankcode = BANK_CODES[2];
                                banks.setText(BANK_NAMES[2]);
                                icici.setBackgroundResource(R.drawable.border_blue);
                                break;
                            case R.id.axis:
                                bankcode = BANK_CODES[3];
                                banks.setText(BANK_NAMES[3]);
                                axis.setBackgroundResource(R.drawable.border_blue);
                                break;
                        }
                    }
                };
                sbi.setOnClickListener(fastSelect);
                hdfc.setOnClickListener(fastSelect);
                icici.setOnClickListener(fastSelect);
                axis.setOnClickListener(fastSelect);

                banks.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_net.show();
                        banks_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> arg0, View arg1,int pos, long arg3) {
                                bankcode = netBankList.get(pos).getIssuerCode();
                                banks.setText(netBankList.get(pos).getBankName());
                                dialog_net.dismiss();
                                Log.d("code",bankcode);
                            }
                        });
                    }
                });
                bank_pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(bankcode!=null){
                            bank_pay.setProgress(1);
                            bankPay(bankcode);
                        }

                    }
                });
                paymentLayout.addView(in_view);
                focusOnView();
            }
        };

        debitCard.setOnClickListener(loadCardView);
        creditCard.setOnClickListener(loadCardView);
        netBanking.setOnClickListener(loadNetBankingView);
    }

    private final void focusOnView(){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, paymentLayout.getBottom());
            }
        });
    }

    private void saveCard(final String card_no,final String month,final String year,final String cvv,final String name,final String type){
        Card card = new Card(card_no, month, year, cvv, name, type);
        new Savecard(activity, new Callback() {
            @Override
            public void onTaskexecuted(String success, String error) {
                cardpay(card_no, month, year, cvv, name, type);
                if(!TextUtils.isEmpty(success))
                    Toast.makeText(activity,"saved successfully",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(activity,"failed to save",Toast.LENGTH_SHORT).show();
            }
        }).execute(card);
    }
    private void cardpay(String card_no,String month,String year,String cvv,String name,String type)
    {
        InputMethodManager imm = (InputMethodManager)this.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(ccvEdit.getWindowToken(), 0);

        Card card = new Card(card_no, month, year, cvv, name, type);
        UserDetails userDetails = new UserDetails(getConsumer());
        PG paymentGateway = new PG(card, bill, userDetails);
        paymentGateway.charge(new com.citrus.mobile.Callback() {
            @Override
            public void onTaskexecuted(String success, String error) {
                Log.d("response", success);
                if(!TextUtils.isEmpty(success))
                    credit_pay.setProgress(100);
                else
                    credit_pay.setProgress(-1);
                processresponse(success,error);
            }
        });
    }

    private void bankPay(String bankCode){
        Bank netbank = new Bank(bankCode);
        Log.d("bankcode",bankCode);
        UserDetails userDetails = new UserDetails(getConsumer());
        PG paymentGateway = new PG(netbank, bill, userDetails);
        paymentGateway.charge(new com.citrus.mobile.Callback() {
            @Override
            public void onTaskexecuted(String success, String error) {
                Log.d("response", success);
                if(!TextUtils.isEmpty(success))
                    bank_pay.setProgress(100);
                else
                    bank_pay.setProgress(-1);
                processresponse(success,error);
            }
        });
    }

    private void paymentOptions(){
        com.citrus.mobile.Callback a = new com.citrus.mobile.Callback() {
            @Override
            public void onTaskexecuted(String success, String error) {
                Log.d("pay",success);
                Gson gson = new GsonBuilder().create();
                PayOption payOpts = gson.fromJson(success, PayOption.class);
                netBankList.clear();
                netBankList.addAll(payOpts.getNetBanking());
                modeAdapter.notifyDataSetChanged();
            }
        };
        PaymentOptions p = new PaymentOptions(a,"letzdine");
        p.execute();
    }

    private JSONObject getConsumer(){
        JSONObject customer = new JSONObject();
        try{
            customer.put("firstName", SharedPref.getStringValue("NAME"));
            customer.put("lastName", "LetzDine");
            customer.put("email", SharedPref.getStringValue("EMAIL"));
            customer.put("mobileNo", SharedPref.getStringValue("PHONE"));
            customer.put("street1", "Street 1");
            customer.put("street2", "Street 2");
            customer.put("zip", "400052");
            customer.put("city", "Bangalore");
            customer.put("state", "Karnataka");
            customer.put("country", "India");

        }catch (JSONException e) {
            e.printStackTrace();
        }
        return customer;
    }
    private void processresponse(String response, String error) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject redirect = new JSONObject(response);
                Intent i = new Intent(this, WebPage.class);

                if (!TextUtils.isEmpty(redirect.getString("redirectUrl"))) {
                    //Toast.makeText(getApplicationContext(), "Redirecting to Payment Gateway. Please wait ... ", Toast.LENGTH_LONG).show();
                    i.putExtra("url", redirect.getString("redirectUrl"));
                    i.putExtra("feed_id",feed_id);
                    startActivity(i);
                    finish();
                }
                else {
                    String message =  "Transaction failed! Please verify your payment settings";
                    if(redirect.has("txMsg"))
                        message = redirect.getString("txMsg");
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
        }
    }

    private void getCardToken(String response, String error) {
        if (!TextUtils.isEmpty(response)) {
            bindingStatus = true;
            UserWallet wallets = new Gson().fromJson(response,UserWallet.class);
            if(wallets.getPaymentOptions().isEmpty()){
                saved_cards.setVisibility(View.GONE);
            }else {
                for (final SavedCards mCard : wallets.getPaymentOptions()) {
                    if(mCard.getBank()==null)
                        mCard.setBank("bank");
                    if(mCard.getType()==null)
                        mCard.setType("unknown");

                    TextView t = new TextView(activity);
                    t.setText(mCard.getBank().toUpperCase() + " : " + mCard.getType().toUpperCase() + " : " + mCard.getNumber());
                    t.setPadding(20, 20, 20, 20);
                    t.setGravity(Gravity.CENTER);
                    t.setTextColor(getResources().getColor(R.color.white));
                    t.setBackgroundResource(R.drawable.darkblue_container);
                    t.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LayoutInflater inflater = activity.getLayoutInflater();
                            View vi = inflater.inflate(R.layout.enter_amount_dialog, null);
                            final EditText ed = (EditText) vi.findViewById(R.id.entered_amt);
                            ed.requestFocus();

                            AlertDialog.Builder enter = new AlertDialog.Builder(activity);
                            enter.setView(vi)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            Card card = new Card(mCard.getToken(), ed.getText().toString());
                                            UserDetails userDetails = new UserDetails(getConsumer());
                                            PG paymentgateway = new PG(card, bill, userDetails);
                                            paymentgateway.charge(new Callback() {
                                                @Override
                                                public void onTaskexecuted(String success, String error) {
                                                    processresponse(success, error);
                                                }
                                            });
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                            Dialog d = enter.create();
                            d.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                            d.show();
                        }
                    });
                    saved_cards.addView(t);
                }
            }
        }else {
            saved_cards.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
        }
    }
}
