package com.prafull.product.activity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.prafull.product.R;
import com.prafull.product.services.BaseSync;
import com.prafull.product.util.CommonUtil;
import com.prafull.product.util.ProductPreferences;

import org.json.JSONException;
import org.json.JSONObject;


public class CustomLogin extends Activity {
    private ProgressDialog loadingProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar_color)));
        loadingProgress = new ProgressDialog(CustomLogin.this,
                ProgressDialog.THEME_HOLO_LIGHT);
        loadingProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loadingProgress.setTitle(getResources().getString(R.string.app_name));
        loadingProgress.setMessage("Authenticating..");
    }

    public  void signUp(View v){
        startActivity(new Intent(CustomLogin.this,Signin.class));
    }
    public void login(View v) {
        EditText username_edit = (EditText) findViewById(R.id.username);
        EditText pswd_edit = (EditText) findViewById(R.id.password);
        if (!username_edit.getText().toString().isEmpty() && !pswd_edit.getText().toString().isEmpty()) {
            loadingProgress.show();
            JSONObject obj = new JSONObject();
            String loginUrl=getString(R.string.base_url)+getString(R.string.Login_Url);
            try {
                obj.put("email", username_edit.getText().toString());
                obj.put("password", pswd_edit.getText().toString());
                new BaseSync(redrawListener,loginUrl, obj, CommonUtil.HTTP_POST).execute();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(getApplicationContext(), "Please fill the fields", Toast.LENGTH_SHORT).show();
        }

    }
  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);//Menu Resource, Menu
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
               startActivity(new Intent(CustomLogin.this,Signin.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    BaseSync.OnTaskCompleted redrawListener = new BaseSync.OnTaskCompleted() {

        @Override
        public void onTaskCompleted(final String str) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadingProgress.cancel();
                    try {
                        JSONObject obj = new JSONObject(str);

                        if(obj.getString("status").equals("success")){
                            String token=obj.getJSONObject("data").getJSONObject("data").getString("token");
                            ProductPreferences.getInstance(getApplicationContext()).setAccessToken(token);
                            startActivity(new Intent(CustomLogin.this,ProductListActivity.class));
                        }else{
                            Toast.makeText(getApplicationContext(),obj.getString("status"),Toast.LENGTH_SHORT).show();
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
                    loadingProgress.cancel();
                    Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                }
            });
        }

    };



}
