package com.prafull.product.adapter;

import android.content.Context;

import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Gallery;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.prafull.product.R;
import com.prafull.product.pojo.ProductImage;
import com.prafull.product.util.LruBitmapCache;

import java.util.ArrayList;


public class ProductImagesAdapter extends ArrayAdapter<ProductImage> {
    private static LayoutInflater inflater = null;
    private final int itemBackground;
    Context context;
    ImageLoader mImageLoader;
    RequestQueue mRequestQueue;


    public ProductImagesAdapter(Context context, ArrayList<ProductImage> productListArray) {
        super(context, 0, productListArray);
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRequestQueue = Volley.newRequestQueue(context);
        mImageLoader = new ImageLoader(mRequestQueue, new LruBitmapCache(LruBitmapCache.getCacheSize(context)));
        TypedArray a =context.obtainStyledAttributes(R.styleable.MyGallery);
        itemBackground = a.getResourceId(R.styleable.MyGallery_android_galleryItemBackground, 0);
        a.recycle();
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ProductImage productImages=getItem(position);
        NetworkImageView imageView = new NetworkImageView(context);
        imageView.setImageUrl(productImages.getImageUrl(), mImageLoader);
        imageView.setLayoutParams(new Gallery.LayoutParams(600, 500));
        imageView.setBackgroundResource(itemBackground);

       /* View vi = view;
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
*/
        return imageView;
    }

    static class ViewHolder{
        TextView titleTextView;
        TextView descriptionTextView;
        NetworkImageView productImageView;
    }

}