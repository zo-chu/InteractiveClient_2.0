package com.kitanasoftware.interactiveclient;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.kitanasoftware.interactiveclient.information.InformatoonScreen_9;
import com.kitanasoftware.interactiveclient.map.MapScreen_5;


public class  MainScreen_4 extends AppCompatActivity {


    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen_4);

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#127e83"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

    }

    public void MAPclick(View view) {
        intent = new Intent(getApplicationContext(),MapScreen_5.class);
        startActivity(intent);//
    }

    public void AUDIOclick(View view) {
        intent = new Intent(getApplicationContext(),BroadCastScreen_6.class);
        startActivity(intent);
    }

    public void SCHEDULEclick(View view) {
        intent = new Intent(getApplicationContext(),ScheduleScreen_8.class);
        startActivity(intent);
    }

    public void NOTIFICATIONSclick(View view) {
        intent = new Intent(getApplicationContext(),NotificationScreen_7.class);
        startActivity(intent);
    }

    public void INFORMATIIONclick(View view) {
        intent = new Intent(getApplicationContext(),InformatoonScreen_9.class);
        startActivity(intent);
    }
}
