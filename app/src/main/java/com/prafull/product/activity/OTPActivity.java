package com.prafull.product.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.prafull.product.R;
import com.prafull.product.services.BaseSync;
import com.prafull.product.util.CommonUtil;
import com.prafull.product.util.ProductPreferences;

import org.json.JSONException;
import org.json.JSONObject;

public class OTPActivity extends AppCompatActivity implements View.OnClickListener {
    Button otp_varify,send_otp;
    EditText et_otp,et_contact_info;
    LinearLayout otp_info,contact_info;
    String mobileNo;
    private ProgressDialog loadingProgress;
    BaseSync.OnTaskCompleted sendOTPListener = new BaseSync.OnTaskCompleted() {

        @Override
        public void onTaskCompleted(final String str) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadingProgress.cancel();
                    try {
                        JSONObject obj = new JSONObject(str);
                        System.out.println(obj);
                        if (obj.getString("status").equals("success")) {
                            contact_info.setVisibility(View.GONE);
                            otp_info.setVisibility(View.VISIBLE);
                            et_otp.requestFocus();
                        } else {
                            Toast.makeText(getApplicationContext(), obj.getString("status"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }

        public void onTaskFailure(final String str) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadingProgress.cancel();
                    Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                }
            });
        }

    };
    BaseSync.OnTaskCompleted verifyOTPListener = new BaseSync.OnTaskCompleted() {

        @Override
        public void onTaskCompleted(final String str) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadingProgress.cancel();
                    try {
                        JSONObject obj = new JSONObject(str);
                        System.out.println(obj);
                        if (obj.getString("status").equals("success")) {
                            ProductPreferences.getInstance(getApplicationContext()).setOTPStatus(true);
                            startActivity(new Intent(OTPActivity.this, MapActivity.class));
                        } else {
                            Toast.makeText(getApplicationContext(), obj.getString("status"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }

        public void onTaskFailure(final String str) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadingProgress.cancel();
                    Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                }
            });
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Boolean otpStatus=ProductPreferences.getInstance(getApplicationContext()).getOTPStatus();
        if(otpStatus){
            finish();
            startActivity(new Intent(OTPActivity.this, MapActivity.class));
        }else{
            setContentView(R.layout.activity_otp);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar_color)));
            otp_varify=(Button)findViewById(R.id.verify_otp);
            send_otp=(Button)findViewById(R.id.send_otp);
            et_otp=(EditText)findViewById(R.id.tv_otp);
            et_contact_info=(EditText)findViewById(R.id.tv_mobile_no);
            otp_varify.setOnClickListener(this);
            send_otp.setOnClickListener(this);
            otp_info=(LinearLayout)findViewById(R.id.otp_info);
            contact_info=(LinearLayout)findViewById(R.id.contact_info);
            loadingProgress = new ProgressDialog(OTPActivity.this,
                    ProgressDialog.THEME_HOLO_LIGHT);
            loadingProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            loadingProgress.setTitle(getResources().getString(R.string.app_name));

        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.verify_otp:
                verifyOTP();
                break;
            case R.id.send_otp:
                getOTPFromServer();
                break;
            default:
                break;
        }

    }

    private void verifyOTP() {
        if (et_otp.getText() != null && !et_otp.getText().toString().isEmpty()) {
            String otp = et_otp.getText().toString();
            String token = ProductPreferences.getInstance(getApplicationContext()).getAccessToken();
            String sellerId = ProductPreferences.getInstance(getApplicationContext()).getUserId();
            String verifyOTPUrl = getString(R.string.base_url) + getString(R.string.SignIn_Url) + "/" + sellerId + getString(R.string.send_otp_url);
            loadingProgress.setMessage(getString(R.string.sending_otp));
            loadingProgress.show();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("contact", mobileNo);
                jsonObject.put("token", token);
                jsonObject.put("otp", otp);
                System.out.println("verifyOTPUrl : " + verifyOTPUrl);
                System.out.println("jsonObject : " + jsonObject);
                new BaseSync(verifyOTPListener, verifyOTPUrl, jsonObject, CommonUtil.HTTP_PUT).execute();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.enter_otp_msg), Toast.LENGTH_SHORT).show();
        }
    }

    private void getOTPFromServer() {
        if (et_contact_info.getText() != null && !et_contact_info.getText().toString().isEmpty()) {
            String contactNo = et_contact_info.getText().toString();
            if (contactNo.length() == 10) {
                mobileNo = contactNo;
                String token = ProductPreferences.getInstance(getApplicationContext()).getAccessToken();
                String sellerId = ProductPreferences.getInstance(getApplicationContext()).getUserId();
                String sendOTPUrl = getString(R.string.base_url) + getString(R.string.SignIn_Url) + "/" + sellerId + getString(R.string.send_otp_url) + "?token=" + token + "&contact=" + contactNo;
                loadingProgress.setMessage(getString(R.string.sending_otp));
                loadingProgress.show();
                System.out.println("sendOTPUrl : " + sendOTPUrl);
                new BaseSync(sendOTPListener, sendOTPUrl, null, CommonUtil.HTTP_GET).execute();
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.enter_correct_no), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.enter_mobile_number), Toast.LENGTH_SHORT).show();
        }

    }
}
