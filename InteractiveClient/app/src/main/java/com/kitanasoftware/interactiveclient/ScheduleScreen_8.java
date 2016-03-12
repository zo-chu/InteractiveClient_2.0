package com.kitanasoftware.interactiveclient;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.kitanasoftware.interactiveclient.Schedule.AdapterForSchedule;

import com.kitanasoftware.interactiveclient.Schedule.Schedule;
import com.kitanasoftware.interactiveclient.Schedule.ScheduleSingleton;
import com.kitanasoftware.interactiveclient.db.WorkWithDb;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScheduleScreen_8 extends DrawerAppCompatActivity {

    ListView lvMain;
    AdapterForSchedule adapterForSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#c9e4ba"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        lvMain = (ListView) findViewById(R.id.listView);
        adapterForSchedule = new AdapterForSchedule(getApplicationContext());
        try {
             lvMain.setAdapter(adapterForSchedule);
        }catch (Exception e){
            e.printStackTrace();
        }

        lvMain.invalidateViews();
    }


    @Override
    public View getContentView() {
        return getLayoutInflater().inflate(R.layout.schedule_screen_8, null);
    }
}



