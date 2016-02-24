package com.kitanasoftware.interactiveclient.map;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kitanasoftware.interactiveclient.R;

import java.util.ArrayList;

/**
 * Created by Chudo on 26.01.2016.
 */
public class GeopointsAdapter extends BaseAdapter {
    ArrayList<Geopoint> geopoints;
    Context context;

    public GeopointsAdapter(Context context, ArrayList<Geopoint> geopoints) {
        this.geopoints = geopoints;
        this.context = context;
    }


    @Override
    public int getCount() {
        return geopoints.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout one_geo_layout =(RelativeLayout)inflater.inflate(R.layout.one_geopoint, parent, false);

        TextView geoName = (TextView) one_geo_layout.findViewById(R.id.name);
        geoName.setText(geopoints.get(position).getName());

        TextView geoType = (TextView) one_geo_layout.findViewById(R.id.type);
        geoType.setText(geopoints.get(position).getType());

        ImageView geoColor = (ImageView) one_geo_layout.findViewById(R.id.color);
        geoColor.setImageResource(geopoints.get(position).getColor());

        return one_geo_layout;
    }
}
