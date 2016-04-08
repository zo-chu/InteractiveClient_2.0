package com.kitanasoftware.interactiveclient.dataTransfer;

import android.content.Context;
import android.content.SharedPreferences;

import com.kitanasoftware.interactiveclient.db.WorkWithDb;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by metkinskiioleg on 12.03.16.
 */
public class GetIp extends Thread {

    DatagramSocket socket;
    SharedPreferences sp;
    Context context ;
    String sender_ip;
    String device_ip;
    int IP_PORT=5003;
    int DB_PORT=5010;
    private static boolean STATUS=true;
    Socket socket2;
    ObjectInputStream objectInputStream;

    public GetIp(Context context) {
        super("d");
        this.context = context;
    }


    public static boolean isSTATUS() {
        return STATUS;
    }

    public static void setSTATUS(boolean STATUS) {
        GetIp.STATUS = STATUS;
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
        try {
            //getting ip from Guide
            System.out.println("Waiting for Ip");
            socket = new DatagramSocket(IP_PORT, InetAddress.getByName("0.0.0.0"));
            socket.setBroadcast(true);

            while (flag) {

                //Receive a packet
                byte[] buffer = new byte[15000];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                System.out.println("Receiving dataramPocket");
                sender_ip = packet.getAddress().getHostAddress();
                device_ip= WifiUtility.getIpAddress();
                System.out.println("get ip");
                socket.close();

                System.out.println(sender_ip + "   sender");
                System.out.println(device_ip + "     devi");

//            sp = context.getSharedPreferences("editor", context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sp.edit();
//            editor.putString("ip", sender_ip);
//            editor.putString("device_ip", device_ip);
//            editor.commit();

//                ClientConn clientConn = new ClientConn(device_ip, sender_ip);
//                clientConn.start();
//                ClientConn.setSTATUS(false);
//                flag=false;
//
                //getting db from Guide

                //Socket socket2 = null;
                socket2 = new Socket(sender_ip, DB_PORT);
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket2.getOutputStream()));
                pw.println(device_ip);
                pw.flush();
                System.out.println("Get db");
                objectInputStream = new ObjectInputStream(socket2.getInputStream());
                String resGeo;


                resGeo = (String) objectInputStream.readObject();
                socket2.close();


                JSONObject jsonObject = new JSONObject(resGeo);
                System.out.println("ff " + jsonObject);
                JSONArray jsonArraySchedual = jsonObject.getJSONArray("schedule");
                JSONArray jsonArrayGeo = jsonObject.getJSONArray("geo");
                JSONObject jsonInf = jsonObject.getJSONObject("inf");
                objectInputStream.close();

                WorkWithDb.getWorkWithDb().putGeopointsToDb(jsonArrayGeo);
                WorkWithDb.getWorkWithDb().putScheduleToDb(jsonArraySchedual);
                WorkWithDb.getWorkWithDb().putInformationToDb(jsonInf);

                System.out.println("geo" + WorkWithDb.getWorkWithDb().getGeopointList().size());
                System.out.println("s" + WorkWithDb.getWorkWithDb().getScheduleList().size());
                System.out.println("geo" + WorkWithDb.getWorkWithDb().getInformList().get(0).toString());

                //ready to next
                STATUS=true;

                flag=false;

            }

        } catch (Exception e) {
            e.printStackTrace();
            STATUS=true;

            //socket.close();
            try {
//                socket2.close();
                objectInputStream.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            flag=false;
        }

    }
}
