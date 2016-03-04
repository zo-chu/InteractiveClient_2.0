package com.kitanasoftware.interactiveclient.dataTransfer;

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

    public ClientConn(String name, String phone, String ip) {
        this.name = name;
        this.phone = phone;
        this.ip = ip;
    }

    public ClientConn(String ip) {
        this.ip = ip;
    }

    @Override
    public void run() {
        super.run();

        Socket socket = null;
        try {
            socket = new Socket("172.22.23.43", 5002);
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            pw.println(ip);
            pw.flush();

            objectInputStream = new ObjectInputStream(socket.getInputStream());
            String resGeo;
            String resSchedual;
            String resInfo;

            resGeo = (String) objectInputStream.readObject();
            JSONObject jsonObject = new JSONObject(resGeo);
            System.out.println("ff "+jsonObject);

            JSONArray jsonArraySchedual = jsonObject.getJSONArray("schedule");
            JSONArray jsonArrayGeo = jsonObject.getJSONArray("geo");
            JSONObject jsonInf = jsonObject.getJSONObject("inf");
            objectInputStream.close();

            WorkWithDb.getWorkWithDb().putGeopointsToDb(jsonArrayGeo);
            WorkWithDb.getWorkWithDb().putScheduleToDb(jsonArraySchedual);
            WorkWithDb.getWorkWithDb().putInformationToDb(jsonInf);

            System.out.println("geo" +  WorkWithDb.getWorkWithDb().getGeopointList().size());
            System.out.println("s" +  WorkWithDb.getWorkWithDb().getScheduleList().size());
            System.out.println("geo" +  WorkWithDb.getWorkWithDb().getInformList().get(0).toString());


        } catch (Exception e) {
            e.printStackTrace();


        }
    }
}
