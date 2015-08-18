package com.prafull.product.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.prafull.product.R;

public class OTPActivity extends AppCompatActivity implements View.OnClickListener {
    Button otp_varify,send_otp;
    EditText et_otp,et_contact_info;
    LinearLayout otp_info,contact_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar_color)));
        otp_varify=(Button)findViewById(R.id.varify_otp);
        send_otp=(Button)findViewById(R.id.send_otp);
        et_otp=(EditText)findViewById(R.id.tv_otp);
        et_contact_info=(EditText)findViewById(R.id.tv_mobile_no);
        otp_varify.setOnClickListener(this);
        send_otp.setOnClickListener(this);
        otp_info=(LinearLayout)findViewById(R.id.otp_info);
        contact_info=(LinearLayout)findViewById(R.id.contact_info);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ot, menu);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.varify_otp:
                startActivity(new Intent(OTPActivity.this,MapActivity.class));

                break;
            case R.id.send_otp:
                contact_info.setVisibility(View.GONE);
                otp_info.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }

    }
}
