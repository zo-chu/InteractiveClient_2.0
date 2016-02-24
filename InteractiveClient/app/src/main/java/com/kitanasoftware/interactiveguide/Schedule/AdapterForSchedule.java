package com.kitanasoftware.interactiveguide.Schedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kitanasoftware.interactiveguide.R;

/**
 * Created by metkinskiioleg on 14.02.16.
 */
public class AdapterForSchedule extends BaseAdapter {

    private Context context;

    public AdapterForSchedule(Context context) {
        this.context = context;
    }


    @Override
    public int getCount() {
        return ScheduleSingleton.getInstance().arrTime.size();
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {


        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout r1 = (RelativeLayout) inflater.inflate(R.layout.schedule_screen_item, viewGroup, false);




        TextView tvTime = (TextView) r1.findViewById(R.id.textView3);
        TextView tvDestination = (TextView) r1.findViewById(R.id.textView4);

        String time= ScheduleSingleton.getInstance().getArrTime().get(position);
        String destination = ScheduleSingleton.getInstance().getArrDestination().get(position);

        tvTime.setText(time);
        tvDestination.setText(destination);


        return r1;
    }
}


