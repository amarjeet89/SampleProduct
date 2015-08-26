package com.prafull.product.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import com.prafull.product.R;
import com.prafull.product.activity.ConfirmOrderActivity;
import com.prafull.product.pojo.Plate;

import java.util.ArrayList;

public class ConfirmAdapter extends ArrayAdapter<Plate> implements View.OnClickListener {
    private static LayoutInflater inflater = null;
    Context context;

    ArrayList<Plate> objects;

    public ConfirmAdapter(Context context, ArrayList<Plate> plateListArray) {
        super(context, 0, plateListArray);
        this.context = context;
        objects = plateListArray;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {

        View vi = view;
        final ViewHolder holder;
        if (vi == null) {
            vi = inflater.inflate(R.layout.item_confirm, null);
            holder = new ViewHolder();
            holder.titleTextView = (TextView) vi.findViewById(R.id.title);
            holder.cookingTime = (TextView) vi.findViewById(R.id.cooking_time);
            holder.price = (TextView) vi.findViewById(R.id.price);
            holder.cross = (ImageButton) vi.findViewById(R.id.cross);
            holder.cross.setTag(position);
            holder.cross.setOnClickListener(this);
            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }
        Plate plateData = getItem(position);
        Plate p = getProduct(position);
        holder.titleTextView.setText(plateData.getTitle());
        holder.price.setText(plateData.getQty() + " * " + plateData.getPrice() + "/-");
        return vi;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.cross){
            int position = (Integer)v.getTag();
            ((ConfirmOrderActivity)context).updateSelectedOrder(position);
        }

    }

    public void refresh(ArrayList<Plate> plates) {
        this.objects = plates;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView titleTextView;
        TextView cookingTime;
        TextView price;
        ImageButton cross;
    }

    Plate getProduct(int position) {
        return ((Plate) getItem(position));
    }


}