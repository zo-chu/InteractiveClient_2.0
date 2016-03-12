package com.kitanasoftware.interactiveclient.map;

import com.kitanasoftware.interactiveclient.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chudo on 27.01.2016.
 */
public class GeopointsData {
    private ArrayList<Geopoint> geopoints;
    private Map<String,Integer> typesOfGeopoints;
    private final ArrayList<Integer> COLORS;
    private static GeopointsData geopointsInstance = new GeopointsData();

    public static GeopointsData getInstance() {
        return geopointsInstance;
    }

    public ArrayList<Geopoint> getGeopoints() {
        return geopoints;
    }

    public void setGeopoints(ArrayList<Geopoint> geopoints) {
        this.geopoints = geopoints;
    }

    public Map<String, Integer> getTypesOfGeopoints() {
        return typesOfGeopoints;
    }

    public void setTypesOfGeopoints(Map<String, Integer> typesOfGeopoints) {
        this.typesOfGeopoints = typesOfGeopoints;
    }

    public ArrayList<Integer> getCOLORS() {
        return COLORS;
    }


    private GeopointsData() {

        geopoints = new ArrayList<Geopoint>();
        typesOfGeopoints = new HashMap<String,Integer>();

        COLORS = new ArrayList<Integer>();
        COLORS.add(R.drawable.point_type1);
        COLORS.add(R.drawable.point_type2);
        COLORS.add(R.drawable.point_type3);
        COLORS.add(R.drawable.point_type4);
        COLORS.add(R.drawable.point_type5);
        COLORS.add(R.drawable.point_type6);
        COLORS.add(R.drawable.point_type7);
        COLORS.add(R.drawable.point_type8);


    }
}
