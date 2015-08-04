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
import com.prafull.product.pojo.PlateItem;
import com.prafull.product.util.LruBitmapCache;

import java.util.ArrayList;

public class PlateListViewAdapter extends ArrayAdapter<PlateItem> {
    private static LayoutInflater inflater = null;
    Context context;
    ImageLoader mImageLoader;
    RequestQueue mRequestQueue;


    public PlateListViewAdapter(Context context, ArrayList<PlateItem> plateListArray) {
        super(context, 0, plateListArray);
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
            vi = inflater.inflate(R.layout.plate_list_item, null);
            holder = new ViewHolder();
            holder.titleTextView = (TextView)vi.findViewById(R.id.plate_title);
            holder.ctTextView = (TextView)vi.findViewById(R.id.plate_cooking_time);
            holder.plateImageView = (NetworkImageView)vi.findViewById(R.id.plate_networkImageView);
            holder.priceTextView=(TextView)vi.findViewById(R.id.plate_price);
            vi.setTag(holder);
        }else{
            holder = (ViewHolder)vi.getTag();
        }
        PlateItem plateData=getItem(position);
        holder.titleTextView.setText(plateData.getDescription());
        holder.ctTextView.setText("Cooking time : "+plateData.getCookingTime());
        holder.priceTextView.setText(String.valueOf(plateData.getPrice())+"/-");
        if(context!=null){
            holder.plateImageView.setImageUrl(plateData.getItemPic(), mImageLoader);
        }

        return vi;
    }

    static class ViewHolder{
        TextView titleTextView;
        TextView ctTextView;
        TextView priceTextView;
        NetworkImageView plateImageView;
    }

}