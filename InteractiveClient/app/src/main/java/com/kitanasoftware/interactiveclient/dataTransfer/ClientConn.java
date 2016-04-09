package com.kitanasoftware.interactiveclient.dataTransfer;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import com.kitanasoftware.interactiveclient.MainScreen_4;
import com.kitanasoftware.interactiveclient.db.WorkWithDb;
import com.kitanasoftware.interactiveclient.information.GuideInform;
import com.kitanasoftware.interactiveclient.map.Geopoint;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Chudo on 24.02.2016.
 */
public class ClientConn extends Thread{
    String name;
    String phone;
    String ip;
    ObjectInputStream objectInputStream;
    String serverIp;
    SharedPreferences sp;
    private static boolean STATUS=true;

    public static boolean isSTATUS() {
        return STATUS;
    }

    public static void setSTATUS(boolean STATUS) {
        ClientConn.STATUS = STATUS;
    }

    public ClientConn(String name, String phone, String ip) {
        this.name = name;
        this.phone = phone;
        this.ip = ip;
    }

    public ClientConn(String ip, String serverIp) {
        this.ip = ip;
        this.serverIp = serverIp;

    }

    @Override
    public void run() {
        super.run();

        Socket socket = null;
        try {
            System.out.println("Try to connect to Guide with ip:" + serverIp);
            // connects to Guide on port 5010, where Guide sent ip
            socket = new Socket(serverIp, 5010);

            // send to Guide Ip
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            pw.println(ip);
            pw.flush();
            System.out.println("Getting db from Guide");
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            String resGeo;

            //get JSON object from Guide
            resGeo = (String) objectInputStream.readObject();
            JSONObject jsonObject = new JSONObject(resGeo);
            System.out.println("Got JASON: " + jsonObject);

            // parsing JSON
            // get schedule
            JSONArray jsonArraySchedual = jsonObject.getJSONArray("schedule");
            System.out.println("Schedule  JASON: " + jsonArraySchedual);

            // get geopoints
            JSONArray jsonArrayGeo = jsonObject.getJSONArray("geo");
            System.out.println("Geopoints  JASON: " + jsonArrayGeo);

            //get information
            JSONObject jsonInf = jsonObject.getJSONObject("inf");
            System.out.println("information JASON: " + jsonInf);
            objectInputStream.close();

            //putting Guide's DB to Client
            WorkWithDb.getWorkWithDb().putGeopointsToDb(jsonArrayGeo);
            WorkWithDb.getWorkWithDb().putScheduleToDb(jsonArraySchedual);
            WorkWithDb.getWorkWithDb().putInformationToDb(jsonInf);

            // STATUS allows to start sending again
            STATUS=true;

        } catch (Exception e) {
            e.printStackTrace();


        }
    }
}
