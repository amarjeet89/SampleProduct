package com.prafull.product.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.prafull.product.R;
import com.prafull.product.pojo.Plate;

import java.util.ArrayList;

public class PlateListAdapter extends ArrayAdapter<Plate> {
    private static LayoutInflater inflater = null;
    Context context;
    ArrayList<Plate>objects;
    public PlateListAdapter(Context context, ArrayList<Plate> plateListArray) {
        super(context, 0, plateListArray);
        this.context = context;
        objects=plateListArray;
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
            holder.itemCount=(EditText)vi.findViewById(R.id.itemcount);
            holder.cb=(CheckBox)vi.findViewById(R.id.cbBox);
            vi.setTag(holder);
        }else{
            holder = (ViewHolder)vi.getTag();
        }
        Plate plateData=getItem(position);
        Plate p = getProduct(position);
        holder.titleTextView.setText(plateData.getTitle() + "-" + plateData.getQty() + "-" + plateData.getPrice() + "/-");
        holder.cb.setOnCheckedChangeListener(myCheckChangList);
        holder.cb.setTag(position);
        holder.cb.setChecked(p.box);
        return vi;
    }

    static class ViewHolder{
        TextView titleTextView;
        EditText itemCount;
        CheckBox cb;
    }
    Plate getProduct(int position) {
        return ((Plate) getItem(position));
    }

    public ArrayList<Plate> getBox() {
        ArrayList<Plate> box = new ArrayList<Plate>();
        for (Plate p : objects) {
            if (p.box) {
                box.add(p);
            }
        }
        return box;
    }
    CompoundButton.OnCheckedChangeListener myCheckChangList = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            getProduct((Integer) buttonView.getTag()).box = isChecked;
        }
    };
}