package com.kitanasoftware.interactiveclient.dataTransfer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

import com.kitanasoftware.interactiveclient.Broadcast.WifiUtility;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by metkinskiioleg on 12.03.16.
 */
public class GetIp extends Thread {

    DatagramSocket socket;
    SharedPreferences sp;
    Context context ;
    String sender_ip;
    String device_ip;

    public GetIp(Context context) {
        super("d");
        this.context = context;
    }

    public String getSender_ip() {
        return sender_ip;
    }

    public void setSender_ip(String sender_ip) {
        this.sender_ip = sender_ip;
    }

    private boolean flag = true;
    @Override
    public void run() {
        super.run();
        //Keep socket open to listen to all the UDP traffic that is destined for this port.
        try {
            System.out.println("Waiting for Ip");
            socket = new DatagramSocket(5001, InetAddress.getByName("0.0.0.0"));
            socket.setBroadcast(true);
            while (true) {
            //Receive a packet
            byte[] buffer = new byte[15000];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                System.out.println("Receiving dataramPocket");

                sender_ip = packet.getAddress().getHostAddress();
                device_ip= WifiUtility.getIpAddress();
                System.out.println("get ip");
            System.out.println(sender_ip + "   sender");
            System.out.println(device_ip + "     devi");

            sp = context.getSharedPreferences("editor", context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("ip", sender_ip);
            editor.putString("device_ip", device_ip);
            editor.commit();
//            Intent intent2 = new Intent(context, StartConn.class);
//            context.startService(intent2);
            flag=false;

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
