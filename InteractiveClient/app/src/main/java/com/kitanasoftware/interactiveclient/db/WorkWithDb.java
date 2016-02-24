package com.kitanasoftware.interactiveclient.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.kitanasoftware.interactiveclient.Schedule.Schedule;
import com.kitanasoftware.interactiveclient.information.AdditionalInform;
import com.kitanasoftware.interactiveclient.information.GuideInform;
import com.kitanasoftware.interactiveclient.information.Information;
import com.kitanasoftware.interactiveclient.information.TourInform;
import com.kitanasoftware.interactiveclient.map.Geopoint;

import java.util.ArrayList;

/**
 * Created by dasha on 23/02/16.
 */
public class WorkWithDb {

    private MyOH myOH;
    private SQLiteDatabase db;
    private Cursor cursor;
    private static WorkWithDb workWithDb;
    private ArrayList<Information> informList;
    private ArrayList<Schedule> scheduleList;
    private ArrayList<Geopoint> geopointList;

    public ArrayList<Schedule> getScheduleList() {
        if(scheduleList.size() == 0 ){
            getSchedule();
        }
        return scheduleList;
    }

    public ArrayList<Information> getInformList() {
        if(informList.size() == 0 ){
            getInformation();
        }
        return informList;
    }

    public ArrayList<Geopoint> getGeopointList() {
        if(geopointList.size() == 0 ){
            getGeopoints();
        }
        return geopointList;
    }

    private WorkWithDb(Context context) {
        myOH = new MyOH(context);
        db = myOH.getWritableDatabase();
        informList = new ArrayList<>();
        scheduleList = new ArrayList<>();
        geopointList = new ArrayList<>();
    }

    public static WorkWithDb getWorkWithDb(Context context){
        if(workWithDb==null){
            workWithDb = new WorkWithDb(context);
        }
        return workWithDb;
    }

    private ArrayList<Information> getInformation(){
        GuideInform guideInform;
        TourInform tourInform;
        AdditionalInform additionalInform;

        cursor = db.rawQuery("SELECT * FROM information", null);
        int size = cursor.getCount();
        if (size > 0){
            cursor.moveToFirst();

            guideInform = new GuideInform(Information.InformType.GUIDE,
                    cursor.getString(1), cursor.getString(2));
            tourInform = new TourInform(Information.InformType.TOUR,
                    cursor.getString(3), cursor.getString(4));
            additionalInform = new AdditionalInform(Information.InformType.ADD, cursor.getString(5));
            informList.add(0, guideInform);
            informList.add(1, tourInform);
            informList.add(2, additionalInform);

        }
        return informList;
    }

    private ArrayList<Schedule> getSchedule(){
        int id;
        String time;
        String description;
        cursor = db.rawQuery("SELECT * FROM schedule", null);
        int size = cursor.getCount();
        if (size > 0){
            cursor.moveToFirst();
            for (int i = 0; i < size; i++) {
                id = cursor.getInt(0);
                time = cursor.getString(1);
                description = cursor.getString(1);
                scheduleList.add(new Schedule(id, time, description));
                cursor.moveToNext();
            }
        }

        return scheduleList;
    }

    private ArrayList<Geopoint> getGeopoints(){
        int id; // ! add to Geopoint
        String name;
        String type;
        int color;
        double[] coordinates = new double[2];

        cursor = db.rawQuery("SELECT * FROM geopoints", null);

        int size = cursor.getCount();
        if (size > 0){
            cursor.moveToFirst();
            for (int i = 0; i < size; i++) {
                id = cursor.getInt(0);
                name = cursor.getString(1);
                type = cursor.getString(2);
                color = cursor.getInt(3);
                coordinates[0] = cursor.getDouble(4);
                coordinates[1] = cursor.getDouble(5);
                //geopointList.add(new Geopoint(id, name, type,color,coordinates));
                cursor.moveToNext();
            }
        }

        return geopointList;
    }

    public void updateGeopointByIndex(int index, String name, String type, int color, double[] coordinates){
        Geopoint geopoint = geopointList.get(index);
        db.execSQL("UPDATE geopoints set name='" + name + "',type='" + type+"',color= " +
                color + "lan=" + coordinates[0] +", lon=" + coordinates[1] +" WHERE id=" + index + "");

        geopoint.setName(name);
        geopoint.setType(type);
        geopoint.setColor(color);
        geopoint.setCoordinates(coordinates);
    }

    public void updateSchedualByIndex(int index, String time, String description){
        Schedule schedule = scheduleList.get(index);
        db.execSQL("UPDATE schedule set time='" + time + "',description='" + description +
                " WHERE id=" + index + "");

        schedule.setTime(time);
        schedule.setDescription(description);
    }

    public void updateInformationByIndex(String guideName, String guidePhone, String tour, String goal){

        ((GuideInform)informList.get(0)).setFull_name(guideName);
        ((GuideInform)informList.get(0)).setPhone(guidePhone);
        ((TourInform)informList.get(1)).setName(tour);
        ((TourInform)informList.get(1)).setGoal(goal);

        db.execSQL("UPDATE information set guide_name='" + guideName + "', " +
                "guide_phone='" + guidePhone + "', tour='" + tour + "', goal='" + goal + "'");

    }

    public void addGeopiont(String name, String type, int color, double[] coordinates){
        int index =getGeopoints().size();
        db.execSQL("INSERT INTO geopoints VALUES ("+index+", '" + name + "', '" + type+"', " +
                color + ", " + coordinates[0] +", " + coordinates[1] +")");
        //getGeopoints().add(new Geopoint(index, name, type, color, coordinates));
    }

    public void addSchedule(String time, String description){
        int index =getSchedule().size();
        db.execSQL("INSERT INTO schedule VALUES ("+ index +", '" + time + "', '" + description + "')");
        getScheduleList().add(new Schedule(index,time,description));
    }

    public void addInformation(String guideName, String guidePhone, String tour, String goal, String company ){
        int inf_id = getInformation().size();
        db.execSQL("INSERT INTO information VALUES ('" + inf_id + "','" + guideName + "', '" + guidePhone + "', '" + tour + "','" + goal + "','" + company + "')");
    }
}
