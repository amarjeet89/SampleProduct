package com.prafull.product.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.prafull.product.R;
import com.prafull.product.services.BaseSync;
import com.prafull.product.util.CommonUtil;

import org.json.JSONException;
import org.json.JSONObject;


public class Signin extends Activity {
    private ProgressDialog loadingProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);
        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar_color)));
       // getActionBar().set
        loadingProgress = new ProgressDialog(Signin.this,
                ProgressDialog.THEME_HOLO_LIGHT);
        loadingProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loadingProgress.setTitle(getResources().getString(R.string.app_name));
        loadingProgress.setMessage("Loading..");
    }

    public void signIn(View v) {
        EditText username_edit = (EditText) findViewById(R.id.email);
        EditText pswd_edit = (EditText) findViewById(R.id.password);
        EditText cfmpswd_edit=(EditText)findViewById(R.id.cfmpassword);
        EditText addrs_edit=(EditText)findViewById(R.id.addrs);
        EditText contact_edit=(EditText)findViewById(R.id.mobile_number);
        if (!username_edit.getText().toString().isEmpty() && !pswd_edit.getText().toString().isEmpty()
                &&!cfmpswd_edit.getText().toString().isEmpty() && !addrs_edit.getText().toString().isEmpty()&& !contact_edit.getText().toString().isEmpty()) {
            if (pswd_edit.getText().toString().trim().equals(cfmpswd_edit.getText().toString().trim()))
            {
                loadingProgress.show();
                JSONObject obj = new JSONObject();
                String signInUrl=getString(R.string.base_url)+getString(R.string.SignIn_Url);
                try {
                    obj.put("email", username_edit.getText().toString());
                    obj.put("password", pswd_edit.getText().toString());
                    obj.put("address", addrs_edit.getText().toString());
                    new BaseSync(redrawListener,signInUrl, obj, CommonUtil.HTTP_POST).execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(getApplicationContext(), "Password did not match.", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getApplicationContext(), "Please fill the fields", Toast.LENGTH_SHORT).show();
        }

    }

    BaseSync.OnTaskCompleted redrawListener = new BaseSync.OnTaskCompleted() {

        @Override
        public void onTaskCompleted(final String str) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadingProgress.cancel();
                    try {
                        JSONObject obj = new JSONObject(str);
                        Toast.makeText(getApplicationContext(),obj.getString("status"),Toast.LENGTH_SHORT).show();
                        finish();
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
                    loadingProgress.cancel();
                    Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                }
            });
        }

    };
}
