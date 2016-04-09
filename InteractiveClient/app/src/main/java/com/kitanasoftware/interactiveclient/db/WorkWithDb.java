package com.kitanasoftware.interactiveclient.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.google.gson.Gson;
import com.kitanasoftware.interactiveclient.Schedule.Schedule;
import com.kitanasoftware.interactiveclient.information.AdditionalInform;
import com.kitanasoftware.interactiveclient.information.GuideInform;
import com.kitanasoftware.interactiveclient.information.Information;
import com.kitanasoftware.interactiveclient.information.TourInform;
import com.kitanasoftware.interactiveclient.map.Geopoint;
import com.kitanasoftware.interactiveclient.map.GeopointsData;
import com.kitanasoftware.interactiveclient.notification.MyNotification;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by dasha on 23/02/16.
 */
public class WorkWithDb {

    private MyOH myOH;
    private SQLiteDatabase db;
    private Cursor cursor;
    private static WorkWithDb workWithDb;

    //1. узнать IP server
    //2. create 3 JASONArray from ourArrayList
    //3. create JASONObject and put 3 JASONArray
    //4. put JASONObject to OutPutStream from Serv
    //5. get from Client mes about "getdb"
    //6. set JASON

    //private ArrayList<Information> informList;
    private ArrayList<String> informList;
    private ArrayList<Schedule> scheduleList;
    private ArrayList<Geopoint> geopointList;

    private HashSet<String> ipList;

    private JSONObject jsonObjectInform;

    private JSONArray jsonArrayGeo;

    private JSONArray jsonArraySchedule;
    private ArrayList<MyNotification> notificationList;



    // gets Notifications from db, if it is first time,
    // otherwise works with ArrayList, witch was downloaded before
    public ArrayList<MyNotification> getNotificationList() {
        if(notificationList==null){
            notificationList=new ArrayList<>();
        }

        if (notificationList.size() == 0) {
            getNotifications();
        }
        return notificationList;
    }

    // getting Notifications from db
    private ArrayList<MyNotification> getNotifications() {
        String sentTo;
        String text;
        cursor = db.rawQuery("SELECT * FROM notifications", null);
        int size = cursor.getCount();
        if (size > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < size; i++) {
                sentTo = cursor.getString(1);
                text = cursor.getString(2);
                notificationList.add(new MyNotification(sentTo, text));
                cursor.moveToNext();
            }
        }

        return notificationList;
    }

    // gets Information from db, if it is first time,
    // otherwise works with ArrayList, witch was downloaded before
    public ArrayList<String> getInformList() {
        if(informList==null){
            informList=new ArrayList<>();
        }

        if (informList.size() == 0) {
             getInformation();
        }

        return informList;
    }

    // Arrays for IPs, but don't use if yet
    public HashSet<String> getIpList() {
        if(ipList==null){
            ipList=new HashSet<>();
        }

        if (ipList.size() == 0) {
            getClientIp();
        }
        return ipList;
    }

    // gets Geopoints from db, if it is first time,
    // otherwise works with ArrayList, witch was downloaded before
    public ArrayList<Geopoint> getGeopointList() {
        if(geopointList==null){
            geopointList=new ArrayList<>();
        }
        if (geopointList.size() == 0) {
            getGeopoints();
        }
        return geopointList;
    }

    // gets Schedule from db, if it is first time,
    // otherwise works with ArrayList, witch was downloaded before
    public ArrayList<Schedule> getScheduleList() {
        if(geopointList==null){
            geopointList=new ArrayList<>();
        }
        if (scheduleList.size() == 0) {
            getSchedule();
        }
        return scheduleList;
    }


    private WorkWithDb(Context context) {
        myOH = new MyOH(context);
        db = myOH.getWritableDatabase();
        informList = new ArrayList<>();
        scheduleList = new ArrayList<>();
        geopointList = new ArrayList<>();
        notificationList = new ArrayList<>();
        ipList = new HashSet<>();
        jsonObjectInform = new JSONObject();
        jsonArrayGeo = new JSONArray();
        jsonArraySchedule = new JSONArray();
        addInformation("Ask guide to send some information!"," "," "," "," ");
    }

    public static WorkWithDb getWorkWithDb(Context context) {
        if (workWithDb == null) {
            workWithDb = new WorkWithDb(context);

        }
        return workWithDb;
    }

    //!!! need to be created before
    public static WorkWithDb getWorkWithDb() {
        return workWithDb;
    }

    // getting Information from db
    private ArrayList<String> getInformation() {

        cursor = db.rawQuery("SELECT * FROM information", null);
        int size = cursor.getCount();
        if (size > 0) {
            try {
                cursor.moveToFirst();
                informList.add(0, cursor.getString(1));
                informList.add(1, cursor.getString(2));
                informList.add(2, cursor.getString(3));
                informList.add(3, cursor.getString(4));
                informList.add(4, cursor.getString(5));
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        return informList;
    }

    //getting Schedule from db
    private ArrayList<Schedule> getSchedule() {
        String time;
        String description;
        cursor = db.rawQuery("SELECT * FROM schedule", null);
        int size = cursor.getCount();
        if (size > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < size; i++) {
                time = cursor.getString(1);
                description = cursor.getString(1);
                scheduleList.add(new Schedule(time, description));
                cursor.moveToNext();
            }
        }

        return scheduleList;
    }

    public JSONObject getJsonObjectInform() {
        if (jsonObjectInform.length() == 0) {
            getInformation();
        }
        return jsonObjectInform;
    }

    //getting IPs from db
    private HashSet<String> getClientIp() {
        String ip;

        String name;

        cursor = db.rawQuery("SELECT * FROM mygroup", null);
        int size = cursor.getCount();
        if (size > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < size; i++) {
                ip = cursor.getString(1);
                //description = cursor.getString(1);
                ipList.add(ip);
                cursor.moveToNext();
            }
        }

        return ipList;
    }

    //getting Geopoints from db
    private ArrayList<Geopoint> getGeopoints() {
        //int id; // ! add to Geopoint
        String name;
        String type;
        int color;
        double[] coordinates = new double[2];

        cursor = db.rawQuery("SELECT * FROM geopoints", null);

        int size = cursor.getCount();
        if (size > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < size; i++) {
                //id = cursor.getInt(0);
                name = cursor.getString(1);
                type = cursor.getString(2);
                color = cursor.getInt(3);
                coordinates[0] = cursor.getDouble(4);
                coordinates[1] = cursor.getDouble(5);
                geopointList.add(new Geopoint(name, type, color, coordinates));
                cursor.moveToNext();
            }
        }

        return geopointList;
    }

    //updating geopoints in db and in ArraysList,
    // witch contains it, when program works
    public void updateGeopointByIndex(int index, String name, String type, int color, double[] coordinates) {
        if (geopointList.size() != 0) {
            Geopoint geopoint = geopointList.get(index);

            db.execSQL("UPDATE geopoints set name='" + name + "',type='" + type + "',color= " +
                    color + ",lat=" + coordinates[0] + ", lon=" + coordinates[1] + " WHERE id=" + index + "");

            geopoint.setName(name);
            geopoint.setType(type);
            geopoint.setColor(color);
            geopoint.setCoordinates(coordinates);
        }
    }

    //updating schedule in db and in ArraysList,
    // witch contains it, when program works
    public void updateSchedualByIndex(int index, String time, String description) {
        Schedule schedule = scheduleList.get(index);
        db.execSQL("UPDATE schedule set time='" + time + "',description='" + description +
                "' WHERE id=" + index + "");

        schedule.setTime(time);
        schedule.setDescription(description);
    }

    //updating information in db and in ArraysList,
    // witch contains it, when program works
    public void updateInformationByIndex(String guideName, String guidePhone, String tour, String goal, String company) {

        informList.set(0, guideName);
        informList.set(1, guidePhone);
        informList.set(2, tour);
        informList.set(3, goal);
        informList.set(4, company);

        db.execSQL("UPDATE information set guide_name='" + guideName + "', " +
                "guide_phone='" + guidePhone + "', tour='" + tour + "', goal='" + goal + "', company='" + company + "' WHERE id=0");

    }

    //adding geopoints in db and in ArraysList,
    // witch contains it, when program works
    public void addGeopiont(String name, String type, int color, double[] coordinates) {
        int index = getGeopointList().size();
        getGeopointList().add(new Geopoint(name, type, color, coordinates));
        db.execSQL("INSERT INTO geopoints VALUES (" + index + ", '" + name + "', '" + type + "', " +
                color + ", " + coordinates[0] + ", " + coordinates[1] + ")");

    }

    //adding schedule in db and in ArraysList,
    // witch contains it, when program works
    public void addSchedule(String time, String description) {
        int index = getScheduleList().size();
        getScheduleList().add(new Schedule(time, description));
        db.execSQL("INSERT INTO schedule VALUES (" + index + ", '" + time + "', '" + description + "')");

    }

    //adding information in db and in ArraysList,
    // witch contains it, when program works
    private void addInformation(String guideName, String guidePhone, String tour, String goal, String company) {
        informList.add(0, guideName);
        informList.add(1, guidePhone);
        informList.add(2, tour);
        informList.add(3, goal);
        informList.add(4, company);
        db.execSQL("INSERT INTO information VALUES (0,'" + guideName + "', '" + guidePhone + "', '" + tour + "','" + goal + "','" + company + "')");

    }

    //adding notifications in db and in ArraysList,
    // witch contains it, when program works
    public void addNotification(String sentTo, String text) {
        int index = getNotificationList().size();
        getNotificationList().add(new MyNotification(sentTo, text));
        db.execSQL("INSERT INTO notifications VALUES (" + index + ", '" + sentTo + "', '" + text + "')");

    }

    //adding IPs in db and in ArraysList,
    // witch contains it, when program works
    public void addIp(String ip) {
        int inf_id = getIpList().size();
        db.execSQL("INSERT INTO mygroup VALUES ('" + inf_id + "','" + ip + "')");
    }

    //put geopoints from Guide to db and to ArraysList,
    // witch contains it, when program works
    public void putGeopointsToDb(JSONArray jsonArray) {

        Geopoint geopoint;
        Geopoint geopointGuide = null;

        try {
            geopointGuide = Geopoint.createFromJson(jsonArray.getJSONObject(0));

            if (jsonArray.length() + 2 == WorkWithDb.getWorkWithDb().getGeopointList().size()) {
                for (int i = 2; i < jsonArray.length(); i++) {
                    geopoint = Geopoint.createFromJson(jsonArray.getJSONObject(i));
                    updateGeopointByIndex(i, geopoint.getName(), geopoint.getType(), geopoint.getColor(), geopoint.getCoordinates());
                }
                updateGeopointByIndex(0, geopointGuide.getName(), geopointGuide.getType(), geopointGuide.getColor(), geopointGuide.getCoordinates());
            } else if (jsonArray.length() + 2 > WorkWithDb.getWorkWithDb().getGeopointList().size()) {
                for (int i = WorkWithDb.getWorkWithDb().getGeopointList().size() - 1; i < jsonArray.length(); i++) {
                    geopoint = Geopoint.createFromJson(jsonArray.getJSONObject(i));
                    addGeopiont(geopoint.getName(), geopoint.getType(), geopoint.getColor(), geopoint.getCoordinates());
                }
                updateGeopointByIndex(0, geopointGuide.getName(), geopointGuide.getType(), geopointGuide.getColor(), geopointGuide.getCoordinates());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //put schedule from Guide to db and to ArraysList,
    // witch contains it, when program works
    public void putScheduleToDb(JSONArray jsonArray) {
        Schedule schedule;
        if (jsonArray.length() == WorkWithDb.getWorkWithDb().getScheduleList().size()) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    schedule = Schedule.createFromJson(jsonArray.getJSONObject(i));
                    //scheduleList.add(schedule);
                    updateSchedualByIndex(i, schedule.getTime(), schedule.getDescription());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    schedule = Schedule.createFromJson(jsonArray.getJSONObject(i));
                    //scheduleList.add(schedule);
                    addSchedule(schedule.getTime(), schedule.getDescription());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //put information from Guide to db and to ArraysList,
    // witch contains it, when program works
    public void putInformationToDb(JSONObject jsonObject) {
        try {
            updateInformationByIndex(jsonObject.getString("guide_name"), jsonObject.getString("guide_phone"), jsonObject.getString("tour"),
                    jsonObject.getString("goal"), jsonObject.getString("company"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
