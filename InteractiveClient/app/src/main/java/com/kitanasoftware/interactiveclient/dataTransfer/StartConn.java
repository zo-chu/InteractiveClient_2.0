package com.kitanasoftware.interactiveclient.dataTransfer;

import android.app.Service;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.text.format.Formatter;
import android.widget.Toast;

public class StartConn extends Service {
    public StartConn() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(getApplicationContext(), "Service start", Toast.LENGTH_LONG).show();


        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

        ClientConn clientConn = new ClientConn(ip);
        clientConn.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
