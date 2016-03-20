package com.kitanasoftware.interactiveclient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.kitanasoftware.interactiveclient.dataTransfer.ClientConn;
import com.kitanasoftware.interactiveclient.dataTransfer.GetIp;
import com.kitanasoftware.interactiveclient.dataTransfer.StartConn;
import com.kitanasoftware.interactiveclient.information.InformatoonScreen_9;
import com.kitanasoftware.interactiveclient.map.MapScreen_5;
import com.kitanasoftware.interactiveclient.notification.NotificationScreen_7;



public class  MainScreen_4 extends AppCompatActivity {
    private String device_ip;
    private String server_ip;
    View btn;
    Intent intent;
    boolean s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen_4);
        s=true;
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        btn=menu.getItem(0).getActionView();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //        Intent intent = new Intent(this,StartConn.class);
//        startService(intent);
        //item.setEnabled(false);
        //btn=findViewById(R.id.route);
        //btn.setEnabled(false);

        SharedPreferences sp;
        sp = getApplicationContext().getSharedPreferences("editor", getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        sp.getString("ip", server_ip);
        sp.getString("device_ip",device_ip);
        editor.commit();

        if(ClientConn.isSTATUS()) {
            ClientConn clientConn = new ClientConn(device_ip, server_ip);
            clientConn.start();
            ClientConn.setSTATUS(false);
        }else Toast.makeText(getApplicationContext(), "Downloading has started", Toast.LENGTH_LONG).show();
        return super.onOptionsItemSelected(item);
    }


}
