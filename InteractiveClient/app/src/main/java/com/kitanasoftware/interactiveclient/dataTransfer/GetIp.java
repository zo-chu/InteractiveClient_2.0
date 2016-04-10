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

            //connects to port 5003
            socket = new DatagramSocket(IP_PORT, InetAddress.getByName("0.0.0.0"));
            System.out.println("connecting to  " + InetAddress.getByName("0.0.0.0").toString());
            socket.setBroadcast(true);

            while (flag) {

                //Receive a packet
                //creates byte array for packet,witch will receive
                byte[] buffer = new byte[15000];

                //packet for  receiving
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                System.out.println("received packet: " + packet);
                // get Guide IP from packet
                sender_ip = packet.getAddress().getHostAddress();

                // get Client IP
                device_ip= WifiUtility.getIpAddress();
                socket.close();

                System.out.println(sender_ip + " - GUIDE IP");
                System.out.println(device_ip + " - CLIENT IP");

                //save IPs to SharedPreferences
                sp = context.getSharedPreferences("editor", context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("ip", sender_ip);
                editor.putString("device_ip", device_ip);
                editor.commit();

                System.out.println("Try to connect to Guide with ip:" + sender_ip);
                // connects to Guide on port 5010, where Guide sent ip
                socket2 = new Socket(sender_ip, DB_PORT);

                // send to Guide Ip
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket2.getOutputStream()));
                pw.println(device_ip);
                pw.flush();

                System.out.println("Getting db from Guide");
                objectInputStream = new ObjectInputStream(socket2.getInputStream());
                String resGeo;

                //get JSON object from Guide
                resGeo = (String) objectInputStream.readObject();
                socket2.close();

                JSONObject jsonObject = new JSONObject(resGeo);
                System.out.println("Got JSON: " + jsonObject);

                // parsing JSON
                // get schedule
                JSONArray jsonArraySchedual = jsonObject.getJSONArray("schedule");
                System.out.println("Schedule  JSON: " + jsonArraySchedual);

                // get geopoints
                JSONArray jsonArrayGeo = jsonObject.getJSONArray("geo");
                System.out.println("Geopoints  JSON: " + jsonArrayGeo);

                //get information
                JSONObject jsonInf = jsonObject.getJSONObject("inf");
                System.out.println("information JSON: " + jsonInf);
                objectInputStream.close();

                //putting Guide's DB to Client
                WorkWithDb.getWorkWithDb().putGeopointsToDb(jsonArrayGeo);
                WorkWithDb.getWorkWithDb().putScheduleToDb(jsonArraySchedual);
                WorkWithDb.getWorkWithDb().putInformationToDb(jsonInf);

                // STATUS allows to start sending again
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
