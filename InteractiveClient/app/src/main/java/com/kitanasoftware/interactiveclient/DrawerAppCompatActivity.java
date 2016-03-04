package com.kitanasoftware.interactiveclient;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.kitanasoftware.interactiveclient.information.InformatoonScreen_9;
import com.kitanasoftware.interactiveclient.map.MapScreen_5;
import com.kitanasoftware.interactiveclient.notification.NotificationScreen_7;


/**
 * Created by Chudo on 23.02.2016.
 */
public abstract class DrawerAppCompatActivity extends AppCompatActivity {
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(toggle);

        //Drawer job with keyboard

//        drawerLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
//            @Override
//            public void onDrawerOpened(View drawerView) {
//                super.onDrawerOpened(drawerView);
//                //how to hide a keyboard
////                InputMethodManager inputMethodManager = (InputMethodManager)
////                        getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
////                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//            }
//        });


    }

    public abstract View getContentView();

    //drawer menu click
    public void onDrawerMenuClick(MenuItem menuItem) {
        Intent intent;
        if (menuItem.getItemId() == R.id.button_audio) {
            intent = new Intent(getApplicationContext(), BroadCastScreen_6.class);
        } else if (menuItem.getItemId() == R.id.button_information) {
            intent = new Intent(getApplicationContext(), InformatoonScreen_9.class);
        } else if (menuItem.getItemId() == R.id.button_map) {
            intent = new Intent(getApplicationContext(), MapScreen_5.class);
        } else if (menuItem.getItemId() == R.id.button_notification) {
            intent = new Intent(getApplicationContext(), NotificationScreen_7.class);
        } else if (menuItem.getItemId() == R.id.button_schedule) {
            intent = new Intent(getApplicationContext(), ScheduleScreen_8.class);
        } else {
            intent = new Intent(getApplicationContext(), MainScreen_4.class);
        }
        startActivity(intent);
        finish();

    }


    // this 3 methods are for Drawer toggle
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

}