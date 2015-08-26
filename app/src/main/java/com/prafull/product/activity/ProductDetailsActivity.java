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
import android.widget.CheckBox;
import android.widget.EditText;
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
    ArrayList<Plate> plates;
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
        plateListview.setItemsCanFocus(true);
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
            plates = new ArrayList<>();
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

    public void selectedItems() {
        CheckBox c;
        EditText e;
        String title;
        View mLayout;
        ArrayList<Plate> selectedPlates=new ArrayList<Plate>();
        for (int i = 0; i < plateListAdapter.getCount(); i++){
            mLayout=plateListview.getChildAt(i);
            if(mLayout!=null){
                c=(CheckBox)mLayout.findViewById(R.id.cbBox);
                if (c!=null){
                    if(c.isChecked()){
                        e=(EditText)mLayout.findViewById(R.id.itemcount);
                        if (e!=null)
                        {
                            title=e.getText().toString();// always get the orignal content : 123 then 345 (not the data user set in the EditText item)
                            int qty=(Integer.parseInt(title));
                            Plate selectPlate=new Plate(plates.get(i).getTitle(),qty,plates.get(i).getPrice(),false);
                            selectedPlates.add(selectPlate);
                        }
                    }
                }
            }
        }
        if(selectedPlates.size()>0) {
            Intent intent = new Intent(this, ConfirmOrderActivity.class);
            intent.putParcelableArrayListExtra(CommonUtil.CONFIRM_ORDER, selectedPlates);
            startActivity(intent);
        }
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
                selectedItems();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
