package com.kitanasoftware.interactiveclient.dataTransfer;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by metkinskiioleg on 10.03.16.
 */
public class WifiUtility {



        public WifiUtility(){}


        public static void toogleWifi(Context context, boolean on){
            WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            wm.setWifiEnabled(on);
        }



        @SuppressWarnings("deprecation")
        public static boolean isWifiConnected (Context context){
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            return mWifi.isConnected();
        }


        public static String getIpAddress() {

            InetAddress inetAddress = null;
            InetAddress myAddress = null;

            try {
                for (Enumeration< NetworkInterface > networkInterface = NetworkInterface.getNetworkInterfaces();
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
            if(myAddress.getHostAddress() !=null){
                return myAddress.getHostAddress();
            }
            else return null;

        }



        public static InetAddress getInetAddress() {

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
            return myAddress;
        }



        public static InetAddress getBroadcastAddress() {

            NetworkInterface temp;
            InetAddress iAddr = null;
            try {
                temp = NetworkInterface.getByInetAddress(getInetAddress());
                List< InterfaceAddress > addresses = temp.getInterfaceAddresses();

                for (InterfaceAddress inetAddress: addresses)
                    iAddr = inetAddress.getBroadcast();
                return iAddr;

            } catch (SocketException e) {
                e.printStackTrace();
            }
            return null;

        }




    public static boolean isHotSpot(Context context) {

        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        Method method = null;
        try {

            method = wm.getClass().getDeclaredMethod("getWifiApState");
            method.setAccessible(true);
            int WIFI_STATE = (Integer) method.invoke(wm, (Object[]) null);

            //public static int AP_STATE_DISABLING = 10;
            //public static int AP_STATE_DISABLED = 11;
            //public static int AP_STATE_ENABLING = 12;
            //public static int AP_STATE_ENABLED = 13;
            //public static int AP_STATE_FAILED = 14;

            if (WIFI_STATE == 13){
                return true;
            }

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return false;
    }


}
