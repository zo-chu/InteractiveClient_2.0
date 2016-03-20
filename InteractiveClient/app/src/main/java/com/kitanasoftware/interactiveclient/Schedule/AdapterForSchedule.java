package com.kitanasoftware.interactiveclient.Schedule;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kitanasoftware.interactiveclient.R;
import com.kitanasoftware.interactiveclient.db.WorkWithDb;


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
        return WorkWithDb.getWorkWithDb().getScheduleList().size();
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

        Schedule schedule = WorkWithDb.getWorkWithDb().getScheduleList().get(position);

        TextView tvTime = (TextView) r1.findViewById(R.id.tvTime);
        TextView tvDestination = (TextView) r1.findViewById(R.id.tvDesc);

        String time= schedule.getTime();
        String destination = schedule.getDescription();

        tvTime.setText(time);
        tvDestination.setText(destination);
        tvDestination.setTextColor(Color.BLACK);
        tvTime.setTextColor(Color.BLACK);

        return r1;
    }
}


