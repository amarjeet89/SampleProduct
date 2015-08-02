package com.prafull.product.adapter;

import android.widget.BaseAdapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import com.prafull.product.R;
import com.prafull.product.pojo.NavigationMenu;

/**
 * Created by Nivedita.Nilugal on 6/8/2015.
 */
public class NavigationDrawerAdapter extends BaseAdapter{
    private Activity activity;
    private ArrayList<NavigationMenu> items;
    LayoutInflater inflater;

    public NavigationDrawerAdapter(Activity activity, ArrayList<NavigationMenu> items) {
        this.activity = activity;
        this.items = items;
        inflater = (LayoutInflater) activity
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public ImageView imageView;
        public TextView title;
        public ImageView navIcon;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.item_navigation_menu, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            //holder.navIcon = (ImageView) convertView.findViewById(R.id.navicon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //holder.imageView.setImageResource(items.get(position).getIcon());
        holder.title.setText(items.get(position).getTitle());
        /*if (getItemId(position) == 0 || getItemId(position) == 1||getItemId(position)==2||getItemId(position)==3||getItemId(position) == 5) {
            holder.navIcon.setVisibility(View.VISIBLE);
        } else {
            holder.navIcon.setVisibility(View.GONE);
        }*/

        if(position == 0)
        {
            convertView.setSelected(true);
        }
        return convertView;
    }
}
