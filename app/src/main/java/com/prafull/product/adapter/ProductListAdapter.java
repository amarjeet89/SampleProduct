package com.prafull.product.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.prafull.product.R;
import com.prafull.product.pojo.Product;
import com.prafull.product.util.LruBitmapCache;

import java.util.ArrayList;

public class ProductListAdapter extends ArrayAdapter<Product> {
    private static LayoutInflater inflater = null;
    Context context;
    ImageLoader mImageLoader;
    RequestQueue mRequestQueue;


    public ProductListAdapter(Context context, ArrayList<Product> productListArray) {
        super(context, 0, productListArray);
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRequestQueue = Volley.newRequestQueue(context);
        mImageLoader = new ImageLoader(mRequestQueue, new LruBitmapCache(LruBitmapCache.getCacheSize(context)));
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {

        View vi = view;
        final ViewHolder holder;
        if (vi == null) {
            vi = inflater.inflate(R.layout.product_item, null);
            holder = new ViewHolder();
            holder.titleTextView = (TextView)vi.findViewById(R.id.product_title);
            holder.descriptionTextView = (TextView)vi.findViewById(R.id.product_description);
            holder.productImageView = (NetworkImageView)vi.findViewById(R.id.networkImageView);
            vi.setTag(holder);
        }else{
            holder = (ViewHolder)vi.getTag();
        }

        Product productData=getItem(position);
        holder.titleTextView.setText(productData.getTitle());
        holder.descriptionTextView.setText(productData.getDescription());
        if(context!=null){
            holder.productImageView.setImageUrl(productData.getIcon_url(), mImageLoader);
        }

        return vi;
    }

    static class ViewHolder{
        TextView titleTextView;
        TextView descriptionTextView;
        NetworkImageView productImageView;
    }

}