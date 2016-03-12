package com.kitanasoftware.interactiveclient.dataTransfer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

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
       while (flag){

        //Keep socket open to listen to all the UDP traffic that is destined for this port.
        try {

            socket = new DatagramSocket(5001, InetAddress.getByName("0.0.0.0"));
            socket.setBroadcast(true);
            while (true) {

            //Receive a packet
            byte[] buffer = new byte[15000];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);


            sender_ip = packet.getAddress().getHostAddress();
            String device_ip = WifiUtility.getIpAddress();


            System.out.println(sender_ip + "   sender");
            System.out.println(device_ip + "     devi");

            flag=false;



            sp = context.getSharedPreferences("editor", context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("ip", sender_ip);
            editor.commit();
                Intent intent2 = new Intent(context, StartConn.class);
                context.startService(intent2);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    }
}
