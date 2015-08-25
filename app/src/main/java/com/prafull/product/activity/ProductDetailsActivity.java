package com.prafull.product.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.prafull.product.R;
import com.prafull.product.adapter.PlateListAdapter;
import com.prafull.product.adapter.ProductImagesAdapter;
import com.prafull.product.adapter.ProductListAdapter;
import com.prafull.product.pojo.Plate;
import com.prafull.product.pojo.Product;
import com.prafull.product.pojo.ProductImage;
import com.prafull.product.services.BaseSync;
import com.prafull.product.util.CommonUtil;
import com.prafull.product.util.ProductPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductDetailsActivity extends AppCompatActivity {
    ArrayList<String> images = new ArrayList<>();
    TextView descriptionTv;
    Gallery gallery;
    private ProgressDialog loadingProgress;
    ListView plateListview;
    PlateListAdapter plateListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar_color)));
        loadingProgress = new ProgressDialog(ProductDetailsActivity.this,
                ProgressDialog.THEME_HOLO_LIGHT);
        // loadingProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // loadingProgress.setTitle(getResources().getString(R.string.app_name));
        //loadingProgress.setMessage("Loading Product Details..");
        descriptionTv = (TextView) findViewById(R.id.product_description);
        gallery = (Gallery) findViewById(R.id.product_images);
        plateListview = (ListView) findViewById(R.id.platelist);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            if (b.containsKey(CommonUtil.SELLER_DETAILS)) {
                populateSellerDetails(b.getString(CommonUtil.SELLER_DETAILS));
            }
        }
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        if (plateListAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < plateListAdapter.getCount(); i++) {
            View listItem = plateListAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = (totalHeight + (listView.getDividerHeight() * (plateListAdapter.getCount() - 1))) + 60;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    private void populateSellerDetails(String jsonStr) {


        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONObject dataObj = jsonObject.getJSONObject("data").getJSONObject("data");
            descriptionTv.setText(dataObj.getString("description"));
            JSONArray productArray = dataObj.getJSONArray("item_pics");
            ArrayList<ProductImage> pimages = new ArrayList<>();
            for (int i = 0; i < productArray.length(); i++) {
                pimages.add(new ProductImage(productArray.getString(i)));
            }
            ProductImagesAdapter productImageAdapter = new ProductImagesAdapter(getApplicationContext(), pimages);
            if (gallery != null) {
                gallery.setAdapter(productImageAdapter);
            }
            JSONArray plateArray = dataObj.getJSONArray("plate");
            ArrayList<Plate> plates = new ArrayList<>();
            System.out.println();
            for (int j = 0; j < plateArray.length(); j++) {
                JSONObject plateobj = plateArray.getJSONObject(j);
                plates.add(new Plate(plateobj.getString("title"), plateobj.getInt("quantity"), plateobj.getInt("price"), false));
            }
            plateListAdapter = new PlateListAdapter(getApplicationContext(), plates);
            if (plateListview != null) {
                plateListview.setAdapter(plateListAdapter);
                setListViewHeightBasedOnChildren(plateListview);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void selectedItems(View v) {
        String result = "Selected Product are :";
        int totalAmount = 0;
        for (Plate p : plateListAdapter.getBox()) {
            if (p.box) {
                result += "\n" + p.getQty();
                totalAmount += p.getPrice();
            }
        }
        Toast.makeText(this, result + "\n" + "Total Amount:=" + totalAmount, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_order, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_order:
                ArrayList<Plate> selectedPlate = new ArrayList<Plate>();
                for (Plate p : plateListAdapter.getBox()) {
                    if (p.box) {
                        selectedPlate.add(p);
                    }
                }
                if(selectedPlate.size()>0) {
                    Intent intent = new Intent(this, ConfirmOrderActivity.class);
                    intent.putParcelableArrayListExtra(CommonUtil.CONFIRM_ORDER,selectedPlate);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
