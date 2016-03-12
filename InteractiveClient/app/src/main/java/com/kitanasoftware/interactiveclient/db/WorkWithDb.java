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

    private ArrayList<Information> informList;
    private ArrayList<Schedule> scheduleList;
    private ArrayList<Geopoint> geopointList;

    private HashSet<String> ipList;

    private JSONObject jsonObjectInform;

    private JSONArray jsonArrayGeo;

    private JSONArray jsonArraySchedule;
    private ArrayList<MyNotification> notificationList;

    public ArrayList<MyNotification> getNotificationList() {
        if (notificationList.size() == 0) {
            getNotifications();
        }
        return notificationList;
    }

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

    public ArrayList<Information> getInformList() {
        if (informList.size() == 0) {
            getInformation();
        }
        return informList;
    }

    public HashSet<String> getIpList() {
        if (ipList.size() == 0) {
            getClientIp();
        }
        return ipList;
    }

    public ArrayList<Geopoint> getGeopointList() {
        if (geopointList.size() == 0) {
            getGeopoints();
        }
        return geopointList;
    }

    public ArrayList<Schedule> getScheduleList() {
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

    private ArrayList<Information> getInformation() {

        GuideInform guideInform;
        TourInform tourInform;
        AdditionalInform additionalInform;

        cursor = db.rawQuery("SELECT * FROM information", null);
        int size = cursor.getCount();
        if (size > 0) {
            try {
                cursor.moveToFirst();
                guideInform = new GuideInform(Information.InformType.GUIDE,
                        cursor.getString(0), cursor.getString(1));

                tourInform = new TourInform(Information.InformType.TOUR,
                        cursor.getString(2), cursor.getString(3));

                additionalInform = new AdditionalInform(Information.InformType.ADD, cursor.getString(4));

                informList.add(0, guideInform);
                informList.add(1, tourInform);
                informList.add(2, additionalInform);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        return informList;
    }

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

    public void updateSchedualByIndex(int index, String time, String description) {
        Schedule schedule = scheduleList.get(index);
        db.execSQL("UPDATE schedule set time='" + time + "',description='" + description +
                " WHERE id=" + index + "");

        schedule.setTime(time);
        schedule.setDescription(description);
    }

    public void updateInformationByIndex(String guideName, String guidePhone, String tour, String goal) {

        ((GuideInform) informList.get(0)).setFull_name(guideName);
        ((GuideInform) informList.get(0)).setPhone(guidePhone);
        ((TourInform) informList.get(1)).setName(tour);
        ((TourInform) informList.get(1)).setGoal(goal);

        db.execSQL("UPDATE information set guide_name='" + guideName + "', " +
                "guide_phone='" + guidePhone + "', tour='" + tour + "', goal='" + goal + "'");

    }

    public void addGeopiont(String name, String type, int color, double[] coordinates) {
        int index = getGeopointList().size();
        getGeopointList().add(new Geopoint(name, type, color, coordinates));
        db.execSQL("INSERT INTO geopoints VALUES (" + index + ", '" + name + "', '" + type + "', " +
                color + ", " + coordinates[0] + ", " + coordinates[1] + ")");

    }

    public void addSchedule(String time, String description) {
        int index = getScheduleList().size();
        getScheduleList().add(new Schedule(time, description));
        db.execSQL("INSERT INTO schedule VALUES (" + index + ", '" + time + "', '" + description + "')");

    }

    public void addInformation(String guideName, String guidePhone, String tour, String goal, String company) {
        int inf_id = getInformList().size();
        db.execSQL("INSERT INTO information VALUES ('" + guideName + "', '" + guidePhone + "', '" + tour + "','" + goal + "','" + company + "')");

    }

    public void addNotification(String sentTo, String text) {
        int index = getNotificationList().size();
        getScheduleList().add(new Schedule(sentTo, text));
        db.execSQL("INSERT INTO notifications VALUES (" + index + ", '" + sentTo + "', '" + text + "')");

    }

    public void addIp(String ip) {
        int inf_id = getIpList().size();
        db.execSQL("INSERT INTO mygroup VALUES ('" + inf_id + "','" + ip + "')");
    }

    public void putGeopointsToDb(JSONArray jsonArray) {
        Geopoint geopoint;
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                geopoint = Geopoint.createFromJson(jsonArray.getJSONObject(i));
                //geopointList.add(geopoint); - am i right ? need to chek dounloading
                addGeopiont(geopoint.getName(), geopoint.getType(), geopoint.getColor(), geopoint.getCoordinates());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void putScheduleToDb(JSONArray jsonArray) {
        Schedule schedule;
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

    public void putInformationToDb(JSONObject jsonObject) {
        try {
            addInformation(jsonObject.getString("guide_name"), jsonObject.getString("guide_phone"), jsonObject.getString("tour"),
                    jsonObject.getString("goal"), jsonObject.getString("company"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
