package com.kitanasoftware.interactiveclient.Schedule;

import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by dasha on 23/02/16.
 */
public class Schedule {
    private int id;
    private String time;
    private String description;
    Schedule schedule;

    public Schedule( String time, String description) {
        this.time = time;
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static Schedule createFromJson(JSONObject object){
        Gson gson = new Gson();
        Schedule schedule=  gson.fromJson(object.toString(),Schedule.class);
        return schedule;
    }
}
