package com.prafull.product.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.prafull.product.R;
import com.prafull.product.pojo.Plate;

import java.util.ArrayList;

public class PlateListAdapter extends ArrayAdapter<Plate> {
    private static LayoutInflater inflater = null;
    Context context;


    public PlateListAdapter(Context context, ArrayList<Plate> plateListArray) {
        super(context, 0, plateListArray);
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {

        View vi = view;
        final ViewHolder holder;
        if (vi == null) {
            vi = inflater.inflate(R.layout.platelist_row, null);
            holder = new ViewHolder();
            holder.titleTextView = (TextView)vi.findViewById(R.id.itemdesc);
            vi.setTag(holder);
        }else{
            holder = (ViewHolder)vi.getTag();
        }
        Plate plateData=getItem(position);
        holder.titleTextView.setText(plateData.getTitle()+"-"+plateData.getQty()+"-"+plateData.getPrice()+"/-");
        return vi;
    }

    static class ViewHolder{
        TextView titleTextView;
    }

}