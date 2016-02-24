package com.kitanasoftware.interactiveclient.map;

/**
 * Created by Chudo on 26.01.2016.
 */
public class Geopoint {
    private String name;
    private String type;
    private int color;
    private double[] coordinates;

    public Geopoint(String name, String type, int color, double[] coordinates) {
        this.name = name;
        this.type = type;
        this.color = color;
        this.coordinates = coordinates;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }
}
