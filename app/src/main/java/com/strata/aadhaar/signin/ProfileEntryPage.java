package com.strata.aadhaar.signin;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.IconTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.dd.processbutton.iml.ActionProcessButton;
import com.squareup.picasso.Callback.EmptyCallback;
import com.squareup.picasso.Picasso;
import com.strata.aadhaar.R;
import com.strata.aadhaar.aadhaarhack.HomeActivity;
import com.strata.aadhaar.model.ProfileDetail;
import com.strata.aadhaar.rest.RestClient;
import com.strata.aadhaar.utils.RoundedTransformation;
import com.strata.aadhaar.utils.SharedPref;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ProfileEntryPage extends Activity {
    private FormEditText biz_name,user_phone,user_tan;
    private TextView user_email;
    private ActionProcessButton update_button;
    private static int RESULT_LOAD_IMG = 1,RESULT_CAPTURE_IMAGE = 2;
    private ImageView user_image;
    private ProgressBar progress_bar;
    private Uri fileUri;
    private String imgPath,email;
    //private int stay_nb_id = 0,work_nb_id = 0,private int other_nb_id = 0,private int dine_nb_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_entry_layout);
        email = getIntent().getStringExtra("email");
        progress_bar = (ProgressBar) findViewById(R.id.progressBar1);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        IconTextView btnCamera = (IconTextView) findViewById(R.id.cameras);
        user_image = (ImageView) findViewById(R.id.user_image);
        biz_name = (FormEditText) findViewById(R.id.biz_name);
        user_phone = (FormEditText) findViewById(R.id.user_phone);
        user_tan = (FormEditText) findViewById(R.id.user_tan);
        user_email = (TextView) findViewById(R.id.user_email);
        user_email.setText(email);
        update_button = (ActionProcessButton) findViewById(R.id.profile_update_button);
        update_button.setMode(ActionProcessButton.Mode.ENDLESS);
        update_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileDetail profile_detail = new ProfileDetail();

                if (biz_name.testValidity() && user_phone.testValidity() && user_tan.testValidity()) {
                    profile_detail.setMerchant_id(SharedPref.getStringValue("MERCHANT_ID"));
                    profile_detail.setPhone_no(user_phone.getText().toString());
                    profile_detail.setEmail(user_email.getText().toString());
                    profile_detail.setBiz_name(biz_name.getText().toString());
                    profile_detail.setBiz_name(user_tan.getText().toString());
                    if (imgPath != null) {
                        profile_detail.setImage(encodeImagetoString());
                    }
                    update_button.setProgress(1);
                    RestClient.getProfileService().updateInfo(profile_detail, new Callback<ProfileDetail>() {
                        @Override
                        public void success(ProfileDetail detail, Response response) {
                            update_button.setProgress(100);
                            updateConsumerInfo(detail);
                        }

                        @Override
                        public void failure(RetrofitError retrofitError) {
                            update_button.setProgress(-1);
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Failed to update information",
                                    Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP, 0, 170);
                            toast.show();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Name and Location can't be blank", Toast.LENGTH_LONG).show();
                }
            }
        });

        user_image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        });

        btnCamera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // give the image a name so we can store it in the phone's default location
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "IMG_" + timeStamp + ".png");
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fileUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values); // store content values
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(cameraIntent, RESULT_CAPTURE_IMAGE);
            }
        });

        RestClient.getProfileService().getInfo(new Callback<ProfileDetail>() {
            @Override
            public void success(ProfileDetail detail, Response response) {
                update_button.setProgress(100);
                setNBDetails(detail);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                update_button.setProgress(-1);
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Failed to update information",
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 170);
                toast.show();
            }
        });
    }

    private void setNBDetails(ProfileDetail profile) {
        biz_name.setText(profile.getBiz_name());
        user_email.setText(profile.getEmail());
        user_phone.setText(profile.getPhone_no());
        user_tan.setText(profile.getTan_no());
        String im = profile.getImage().isEmpty() ? "h" : profile.getImage();
        progress_bar.setVisibility(View.VISIBLE);
        Picasso.with(this)
                .load(im)
                .noFade()
                .fit().centerCrop()
                .transform(new RoundedTransformation(250))
                .into(user_image, new EmptyCallback() {
                    @Override
                    public void onSuccess() {
                        progress_bar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        progress_bar.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if ((requestCode == RESULT_LOAD_IMG || requestCode == RESULT_CAPTURE_IMAGE)
                    && resultCode == RESULT_OK) {

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                if (requestCode == RESULT_CAPTURE_IMAGE) {
                    if (fileUri != null) {

                        Cursor cursor1 = getContentResolver().query(fileUri,
                                filePathColumn, null, null, null);
                        cursor1.moveToFirst();
                        int columnIndex = cursor1.getColumnIndex(filePathColumn[0]);
                        imgPath = cursor1.getString(columnIndex);
                        cursor1.close();
                    }
                } else if (data != null) {
                    Uri selectedImage = data.getData();
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imgPath = cursor.getString(columnIndex);
                    cursor.close();
                }
                try {
                    String image_loc;
                    progress_bar.setVisibility(View.VISIBLE);
                    if (!imgPath.startsWith("/http") && !imgPath.startsWith("content")) {
                        image_loc = "file://" + imgPath;
                    } else {
                        image_loc = imgPath;
                    }

                    Picasso.with(this)
                            .load(image_loc)
                            .fit().centerCrop()
                            .transform(new RoundedTransformation(250))
                            .noFade()
                            .into(user_image, new EmptyCallback() {
                                @Override
                                public void onSuccess() {
                                    progress_bar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError() {
                                    progress_bar.setVisibility(View.GONE);
                                }
                            });

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(this, "You haven't picked Image",Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    String encodeImagetoString() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath, options);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 5, stream);
        byte[] byte_arr = stream.toByteArray();
        return Base64.encodeToString(byte_arr, 0);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void updateConsumerInfo(ProfileDetail detail) {
        SharedPreferences preferences = getSharedPreferences("PREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        //setNBDetails(detail);
        //editor.putString("NUMBER", detail.getPhoneNo());
        editor.putString("EMAIL", detail.getEmail())
                .putString("NAME", detail.getBiz_name())
                .putString("IMAGE", detail.getImage())
                .putString("TAN", detail.getTan_no())
                .putString("PHONE", detail.getPhone_no());

        editor.apply();
        Toast.makeText(getApplicationContext(), "Information successfully updated", Toast.LENGTH_SHORT).show();
        Intent in;
        in = new Intent(getBaseContext(), HomeActivity.class);
        in.putExtra("firstTime", false);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(in);
        finish();
    }
}