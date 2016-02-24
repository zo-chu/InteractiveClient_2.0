package com.kitanasoftware.interactiveclient.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.kitanasoftware.interactiveclient.R;

import java.util.ArrayList;

/**
 * Created by Chudo on 30.01.2016.
 */
public class ColorAdapter extends BaseAdapter {
    private ArrayList<Integer> colors;
    private Context context;

    public ColorAdapter(ArrayList<Integer> colors, Context context) {
        this.colors = colors;
        this.context = context;
    }

    @Override
    public int getCount() {
        return colors.size();
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
        LinearLayout one_color_layout =(LinearLayout)inflater.inflate(R.layout.one_color, parent, false);
        ImageView oneColor = (ImageView)one_color_layout.findViewById(R.id.oneColor);

        oneColor.setImageResource(colors.get(position));


        return one_color_layout;
    }
}
