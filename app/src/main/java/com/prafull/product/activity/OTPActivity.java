package com.prafull.product.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.prafull.product.R;
import com.prafull.product.util.ProductPreferences;

public class OTPActivity extends AppCompatActivity implements View.OnClickListener {
    Button otp_varify,send_otp;
    EditText et_otp,et_contact_info;
    LinearLayout otp_info,contact_info;


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
        }


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.verify_otp:
                ProductPreferences.getInstance(getApplicationContext()).setOTPStatus(true);
                startActivity(new Intent(OTPActivity.this, MapActivity.class));

                break;
            case R.id.send_otp:
                contact_info.setVisibility(View.GONE);
                otp_info.setVisibility(View.VISIBLE);
                et_otp.requestFocus();
                break;
            default:
                break;
        }

    }
}
