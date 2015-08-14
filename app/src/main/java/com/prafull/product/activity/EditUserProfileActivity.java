package com.prafull.product.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.prafull.product.R;
import com.prafull.product.services.BaseSync;
import com.prafull.product.util.CommonUtil;
import com.prafull.product.util.ProductPreferences;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by SHUBHANSU on 7/30/2015.
 */
public class EditUserProfileActivity extends Activity implements View.OnClickListener{

    private TextView firstName;
    private TextView lastName;
    private TextView email;
    private TextView mobileNo;
    private TextView address;
    private TextView country;
    private TextView aboutMe;
    private Button save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);
       /* firstName = (TextView)findViewById(R.id.f_name);
        lastName = (TextView)findViewById(R.id.l_name);
        email = (TextView)findViewById(R.id.email);
        mobileNo = (TextView)findViewById(R.id.mobile);
        address = (TextView)findViewById(R.id.address);
        country = (TextView)findViewById(R.id.country);
        aboutMe = (TextView)findViewById(R.id.about_me);
        save = (Button)findViewById(R.id.save);
        save.setOnClickListener(this);
        getProfileData();*/
    }

    private void getProfileData() {
        String profileURL = getString(R.string.base_url)+getString(R.string.user_Url)+
                ProductPreferences.getInstance(getApplicationContext()).getUserId()+"?"+getString(R.string.token_Url)+
                ProductPreferences.getInstance(getApplicationContext()).getAccessToken();

        new BaseSync(redrawListener,profileURL, null , CommonUtil.HTTP_GET).execute();
    }

    BaseSync.OnTaskCompleted redrawListener = new BaseSync.OnTaskCompleted() {

        @Override
        public void onTaskCompleted(final String str) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject obj = new JSONObject(str);
                        if(obj.getString("status").equals("success")){
                            JSONObject profileObject = obj.getJSONObject("data").getJSONObject("data");
                            email.setText(profileObject.getString("email"));
                            firstName.setText(profileObject.getString("fname"));
                            lastName.setText(profileObject.getString("lname"));
                            mobileNo.setText(profileObject.getString("mobile"));
                            address.setText(profileObject.getString("address"));
                            country.setText(profileObject.getString("country"));
                            aboutMe.setText(profileObject.getString("about_me"));
                        }else{
                            Toast.makeText(getApplicationContext(), obj.getString("status"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
        @Override
        public void onTaskFailure(final String str) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                }
            });
        }

    };

    @Override
    public void onClick(View v) {
        if(v.getId() == save.getId()){
            updateProfileData();
        }

    }

    private void updateProfileData() {
        String updateProfileURL = getString(R.string.base_url)+getString(R.string.user_Url)+
                ProductPreferences.getInstance(getApplicationContext()).getUserId();

        JSONObject obj = new JSONObject();
        try {
            obj.put("fname", firstName.getText().toString());
            obj.put("email", email.getText().toString());
            obj.put("lname", lastName.getText().toString());
            obj.put("mobile", mobileNo.getText().toString());
            obj.put("address", address.getText().toString());
            obj.put("country", country.getText().toString());
            obj.put("about_me", aboutMe.getText().toString());
            obj.put("token", ProductPreferences.getInstance(getApplicationContext()).getAccessToken());
            new BaseSync(updateListener,updateProfileURL, obj, CommonUtil.HTTP_PUT).execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    BaseSync.OnTaskCompleted updateListener = new BaseSync.OnTaskCompleted() {

        @Override
        public void onTaskCompleted(final String str) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject obj = new JSONObject(str);
                        if(obj.getString("status").equals("success")){
                            Toast.makeText(getApplicationContext(), obj.getString("Updated"), Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(), obj.getString("status"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
        @Override
        public void onTaskFailure(final String str) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                }
            });
        }

    };
}
