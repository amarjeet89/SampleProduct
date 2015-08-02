package com.prafull.product.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Gallery;
import android.widget.TextView;

import com.prafull.product.R;
import com.prafull.product.adapter.ProductImagesAdapter;
import com.prafull.product.pojo.Product;
import com.prafull.product.pojo.ProductImage;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class ProductDetailsActivity extends AppCompatActivity {
    ArrayList<String> images=new ArrayList<>();
    TextView descriptionTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        // Fetching data from a parcelable object passed from MainActivity
        Bundle b=getIntent().getExtras();
        images=b.getStringArrayList("pimage");
        descriptionTv=(TextView)findViewById(R.id.product_description);
        descriptionTv.setText(b.getString("desc"));
        Gallery gallery=(Gallery)findViewById(R.id.product_images);
       if (images.size()!=0){
            try {
                ArrayList<ProductImage> pimages = new ArrayList<>();
                for(int i=0;i<images.size();i++){

                    pimages.add(new ProductImage(images.get(i)));
                }
                ProductImagesAdapter productImageAdapter=new ProductImagesAdapter(getApplicationContext(),pimages);
                if(gallery!=null)
                    gallery.setAdapter(productImageAdapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
      }


    }

}
