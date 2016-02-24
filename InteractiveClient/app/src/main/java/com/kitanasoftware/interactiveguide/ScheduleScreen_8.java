package com.kitanasoftware.interactiveguide;

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

import com.kitanasoftware.interactiveguide.Schedule.AdapterForSchedule;
import com.kitanasoftware.interactiveguide.Schedule.ScheduleSingleton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScheduleScreen_8 extends DrawerAppCompatActivity {

    ArrayList<String> arrTime;
    ArrayList<String> arrDes;

    SharedPreferences sp;

    ListView lvMain;
    AdapterForSchedule adapterForSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#c9e4ba"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);


        lvMain = (ListView) findViewById(R.id.listView);

        adapterForSchedule = new AdapterForSchedule(getApplicationContext());

        lvMain.invalidateViews();

        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {


            ParseQuery<ParseObject> query = ParseQuery.getQuery("Schedule");
            query.selectKeys(Arrays.asList("Time", "Destination"));
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {

                    arrTime = new ArrayList<String>();
                    arrDes = new ArrayList<String>();
                    for (int i = 0; i < list.size(); i++) {

                        arrTime.add(i, list.get(i).getString("Time"));
                        arrDes.add(i, list.get(i).getString("Destination"));

                    }

                    ScheduleSingleton.getInstance().setArrTime(arrTime);
                    ScheduleSingleton.getInstance().setArrDestination(arrDes);

                    sp = getSharedPreferences("editor", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("arrTime", arrTime.toString());
                    editor.putString("arrDestination", arrDes.toString());
                    editor.commit();

                    lvMain.setAdapter(adapterForSchedule);

                }
            });
        } else {
            try {
                sp = getSharedPreferences("editor", MODE_PRIVATE);
                String timeString = sp.getString("arrTime", "");
                String destinationString = sp.getString("arrDestination", "");

                timeString = timeString.replace("[", "").replace("]", "");
                destinationString = destinationString.replace("[", "").replace("]", "");

                ScheduleSingleton.getInstance().setArrTime(new ArrayList<>(Arrays.asList(timeString.split("\\s*,\\s*"))));
                ScheduleSingleton.getInstance().setArrDestination(new ArrayList<>(Arrays.asList(destinationString.split("\\s*,\\s*"))));

                lvMain.setAdapter(adapterForSchedule);


            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "You need internet to donload schedule", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public View getContentView() {
        return getLayoutInflater().inflate(R.layout.schedule_screen_8, null);
    }


}



