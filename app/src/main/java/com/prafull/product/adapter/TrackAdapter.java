package com.prafull.product.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.prafull.product.R;
import com.prafull.product.activity.ConfirmOrderActivity;
import com.prafull.product.pojo.Track;

import java.util.ArrayList;

public class TrackAdapter extends ArrayAdapter<Track> {
    private static LayoutInflater inflater = null;
    Context context;
    ArrayList<Track> objects;

    public TrackAdapter(Context context, ArrayList<Track> trackArray) {
        super(context, 0, trackArray);
        this.context = context;
        objects = trackArray;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {

        View vi = view;
        final ViewHolder holder;
        if (vi == null) {
            vi = inflater.inflate(R.layout.item_order_track, null);
            holder = new ViewHolder();
            holder.titleTextView = (TextView) vi.findViewById(R.id.title);
            holder.description = (TextView) vi.findViewById(R.id.description);
            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }
        Track track = getItem(position);
        holder.titleTextView.setText(track.getTitle());
        holder.description.setText(Html.fromHtml(track.getPublisher()));
        return vi;
    }

    static class ViewHolder {
        TextView titleTextView;
        TextView description;
    }


}