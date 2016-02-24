package com.kitanasoftware.interactiveguide.Schedule;

import java.util.ArrayList;

/**
 * Created by metkinskiioleg on 14.02.16.
 */
public class ScheduleSingleton {
    ArrayList<String> arrTime;
    ArrayList<String> arrDestination;

    public void setArrTime(ArrayList<String> arrTime) {
        this.arrTime = arrTime;
    }

    public void setArrDestination(ArrayList<String> arrDestination) {
        this.arrDestination = arrDestination;
    }

    private static ScheduleSingleton ourInstance = new ScheduleSingleton();


    public static ScheduleSingleton getInstance() {
        if (ourInstance == null){
            ourInstance = new ScheduleSingleton();
        }
        return ourInstance;
    }



    private ScheduleSingleton() {
        arrTime = new ArrayList<>();
        arrDestination = new ArrayList<>();
    }

    public ArrayList<String> getArrTime() {
        return arrTime;
    }

    public ArrayList<String> getArrDestination() {
        return arrDestination;
    }
}


