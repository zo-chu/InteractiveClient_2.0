package com.kitanasoftware.interactiveclient;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class    BroadCastScreen_6 extends DrawerAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#f8bfd8"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
    }

    @Override
    public View getContentView() {
        return getLayoutInflater().inflate(R.layout.broad_cast_screen_6,null);
    }
}
