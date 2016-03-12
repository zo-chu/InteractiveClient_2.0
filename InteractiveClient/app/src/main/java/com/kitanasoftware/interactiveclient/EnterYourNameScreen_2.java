package com.kitanasoftware.interactiveclient;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.kitanasoftware.interactiveclient.Broadcast.WifiUtility;
import com.kitanasoftware.interactiveclient.dataTransfer.GetIp;
import com.kitanasoftware.interactiveclient.dataTransfer.StartConn;
import com.kitanasoftware.interactiveclient.db.WorkWithDb;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class  EnterYourNameScreen_2 extends AppCompatActivity {

    SharedPreferences sp;
    EditText name;
    EditText number;
    String ip;
    WifiManager wifiMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_your_name_screen_2);

        wifiMgr = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        name = (EditText) findViewById(R.id.editText);
        number = (EditText) findViewById(R.id.editText2);

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#127e83"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        sp = getSharedPreferences("editor", MODE_PRIVATE);
        String nameString = sp.getString("name", "");
        String numberString = sp.getString("number", "");

        name.setText(nameString);
        number.setText(numberString);

        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (!(activeNetworkInfo != null && activeNetworkInfo.isConnected())) {
                WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                 wifi.setWifiEnabled(true);

                final ProgressDialog ringProgressDialog = ProgressDialog.show
                        (EnterYourNameScreen_2.this, "Welcome", "Looking for WIFI", true);
                ringProgressDialog.setCancelable(true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));

                        } catch (Exception e) {
                        }
                        ringProgressDialog.dismiss();
                    }
                }).start();
            }
    }

    public void OKclick(View view) {

        if (wifiMgr.isWifiEnabled()){
            ip = WifiUtility.getIpAddress();
        }

        GetIp getIp = new GetIp(getApplicationContext());
        getIp.start();


        sp = getSharedPreferences("editor", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString("name", name.getText().toString());
        editor.putString("number", number.getText().toString());

        editor.commit();



        Intent intent = new Intent(getApplicationContext(),MainScreen_4.class);
        startActivity(intent);
    }

    public static String getIpAddress() {

        InetAddress inetAddress = null;
        InetAddress myAddress = null;

        try {
            for (Enumeration < NetworkInterface > networkInterface = NetworkInterface.getNetworkInterfaces();
                 networkInterface.hasMoreElements();) {

                NetworkInterface singleInterface = networkInterface.nextElement();

                for (Enumeration < InetAddress > IpAddresses = singleInterface.getInetAddresses();
                     IpAddresses.hasMoreElements();) {

                    inetAddress = IpAddresses.nextElement();

                    if (!inetAddress.isLoopbackAddress() &&
                            (singleInterface.getDisplayName().contains("wlan0") ||
                                    singleInterface.getDisplayName().contains("eth0") ||
                                    singleInterface.getDisplayName().contains("ap0"))) {
                        myAddress = inetAddress;

                    }

                }
            }

        } catch (SocketException ex) {
            ex.printStackTrace();
        }


        return myAddress.getHostAddress();
    }
}
