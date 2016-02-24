package com.kitanasoftware.interactiveguide.information;

import android.graphics.Bitmap;

/**
 * Created by dasha on 27/01/16.
 */
public class Information {

    private InformType type;

    public enum InformType {
        GUIDE, TOUR, ADD
    }

    public Information(InformType type) {
        this.type =type;
    }

    public InformType getType() {
        return type;
    }

    public void setType(InformType type) {
        this.type = type;
    }
}
