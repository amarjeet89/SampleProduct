package com.prafull.product.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.prafull.product.R;
import com.prafull.product.services.BaseSync;
import com.prafull.product.util.CommonUtil;
import com.prafull.product.util.ProductPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Amar on 05-08-2015.
 */
public class CustomPlate extends AppCompatActivity{
    private String[] mPrices;
    private String[] mDishes;
    Spinner price_spinner,dish_spinner;
    String mCurrency;
    String mDishtype;
    EditText mTitle,mDesc,mPrice,mCooktime;
    private ProgressDialog loadingProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_plate);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar_color)));
        // Spinner element
        mTitle=(EditText)findViewById(R.id.plate_title);
        mDesc=(EditText)findViewById(R.id.plate_desc);
        mPrice=(EditText)findViewById(R.id.plate_price);
        mCooktime=(EditText)findViewById(R.id.plate_cookingtime);
        price_spinner = (Spinner) findViewById(R.id.price_spinnerlist);
        dish_spinner = (Spinner) findViewById(R.id.dishtype_spinner);
        mPrices = getResources().getStringArray(R.array.currency_list);
        mDishes = getResources().getStringArray(R.array.dish_list);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, mPrices);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        price_spinner.setAdapter(dataAdapter);
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, mDishes);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dish_spinner.setAdapter(dataAdapter1);
        price_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mCurrency = mPrices[i];
                Toast.makeText(getApplicationContext(), mCurrency, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        dish_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mDishtype=mDishes[i];
                Toast.makeText(getApplicationContext(),mDishtype,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        loadingProgress = new ProgressDialog(CustomPlate.this,
                ProgressDialog.THEME_HOLO_LIGHT);
        loadingProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loadingProgress.setTitle(getResources().getString(R.string.app_name));
        loadingProgress.setMessage("Loading..");
    }
    public void sendPlateData(View v){
        if (!mTitle.getText().toString().isEmpty() && !mDesc.getText().toString().isEmpty()  && !mPrice.getText().toString().isEmpty()
                && !mCooktime.getText().toString().isEmpty()) {
            loadingProgress.show();
            JSONObject obj = new JSONObject();
            String loginUrl=getString(R.string.base_url)+getString(R.string.Create_Plate_Url);
            try {
                obj.put("cooking_time", mCooktime.getText().toString());
                obj.put("currency", mCurrency);
                obj.put("description", mDesc.getText().toString());
                obj.put("dish_type", mDishtype);
                obj.put("price",mPrice.getText().toString());
                obj.put("title",mTitle.getText().toString());
                obj.put("token", new ProductPreferences(getApplicationContext()).getAccessToken());
                new BaseSync(redrawListener,loginUrl, obj, CommonUtil.HTTP_POST).execute();
            } catch (JSONException e) {
                e.printStackTrace();
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

                        if(obj.getString("status").equals("success")){
                            Toast.makeText(getApplicationContext(),obj.getString("status"),Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(),obj.getString("status")+obj.getString("error_msg"),Toast.LENGTH_SHORT).show();
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
