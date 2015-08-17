package com.prafull.product.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    ArrayList<String> images=new ArrayList<>();
    TextView descriptionTv;
    Gallery gallery;
    private ProgressDialog loadingProgress;
    ListView plateListview;
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
        descriptionTv=(TextView)findViewById(R.id.product_description);
        gallery=(Gallery)findViewById(R.id.product_images);
        plateListview=(ListView)findViewById(R.id.platelist);
        Bundle b=getIntent().getExtras();
        if(b!=null){
            if(b.containsKey(CommonUtil.SELLER_DETAILS)){
                populateSellerDetails(b.getString(CommonUtil.SELLER_DETAILS));
            }
        }
    }


private void populateSellerDetails(String jsonStr){


    try {
        JSONObject jsonObject=new JSONObject(jsonStr);
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
            plates.add(new Plate(plateobj.getString("title"), plateobj.getInt("quantity"), plateobj.getInt("price")));
        }
        PlateListAdapter plateListAdapter = new PlateListAdapter(getApplicationContext(), plates);
        if (plateListview != null) {
            plateListview.setAdapter(plateListAdapter);
        }
    } catch (JSONException e) {
        e.printStackTrace();
    }

}



}
