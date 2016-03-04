package com.kitanasoftware.interactiveclient.notification;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.kitanasoftware.interactiveclient.DrawerAppCompatActivity;
import com.kitanasoftware.interactiveclient.R;

public class  NotificationScreen_7 extends DrawerAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#d7afd2"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
    }

    @Override
    public View getContentView() {
        return getLayoutInflater().inflate(R.layout.notification_screen_7,null);


    }
}
