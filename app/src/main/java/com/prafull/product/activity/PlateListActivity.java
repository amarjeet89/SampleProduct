package com.prafull.product.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.prafull.product.R;
import com.prafull.product.adapter.PlateListViewAdapter;
import com.prafull.product.pojo.PlateItem;
import com.prafull.product.services.BaseSync;
import com.prafull.product.util.CommonUtil;
import com.prafull.product.util.ProductPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlateListActivity extends AppCompatActivity{

    private ProgressDialog loadingProgress;
    private ArrayList<PlateItem> plateData;
    ListView plateListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plate_list);
       getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar_color)));
        plateListView=(ListView)findViewById(R.id.plate_list_view);
        loadPlateList();
    }

    private void loadPlateList() {
        String token = ProductPreferences.getInstance(getApplicationContext()).getAccessToken();
        String sellerListUrl = getString(R.string.base_url) + getString(R.string.get_plate_list_url) + "?token=" + token;
        System.out.println("sellerListUrl : " + sellerListUrl);
        loadingProgress = new ProgressDialog(PlateListActivity.this,
                ProgressDialog.THEME_HOLO_LIGHT);
        loadingProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loadingProgress.setTitle(getResources().getString(R.string.app_name));
        loadingProgress.setMessage("Loading..");
        loadingProgress.show();
        new BaseSync(plateLoadListener, sellerListUrl, null, CommonUtil.HTTP_GET).execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_plate_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement//
        if (id == R.id.add) {
            startActivity(new Intent(PlateListActivity.this,CustomPlate.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    BaseSync.OnTaskCompleted plateLoadListener = new BaseSync.OnTaskCompleted() {

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
                            plateData = parsePlateData(obj);
                            PlateListViewAdapter productDataAdapter=new PlateListViewAdapter(getApplicationContext(),plateData);
                            if(plateListView!=null)
                                plateListView.setAdapter(productDataAdapter);
                        }
                        else {
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
                    loadingProgress.cancel();
                    Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                }
            });
        }

    };


    private ArrayList<PlateItem> parsePlateData(JSONObject obj) {
        ArrayList<PlateItem> plateDataArray = null;
        try {
            if (obj.has("data")) {
                JSONObject dataObj = obj.getJSONObject("data");
                if (dataObj.has("data")) {
                    plateDataArray = new ArrayList<>();
                    JSONArray dataArray = dataObj.getJSONArray("data");
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject itemData = dataArray.getJSONObject(i);

                        plateDataArray.add(new PlateItem(itemData.getString("title"), itemData.getString("cooking_time"),
                                itemData.getString("currency"),itemData.getString("_id"),itemData.getString("description"),
                                itemData.getString("dish_type"),itemData.getString("user_id"),itemData.getInt("quantity"),
                                itemData.getInt("price"),itemData.getString("rating"),itemData.getJSONArray("item_pics")
                                ));
                    }

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return plateDataArray;
    }

}
