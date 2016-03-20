package com.kitanasoftware.interactiveclient.dataTransfer;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Toast;

public class StartConn extends Service {
    public StartConn() {
    }

    SharedPreferences sp;

    @Override
    public void onCreate() {
        super.onCreate();
        //Toast.makeText(getApplicationContext(), "Service start", Toast.LENGTH_LONG).show();


//        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
//        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
//
//        sp = getSharedPreferences("editor", MODE_PRIVATE);
//        String ipServer = sp.getString("ip","");

//        GetIp getIp = new GetIp(getApplicationContext());
//        getIp.start();

//        ClientConn clientConn = new ClientConn(ip, ipServer);
//        clientConn.start();

//
//        Intent intent2 = new Intent(getApplicationContext(), StartConn.class);
//        startService(intent2);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
