package com.kitanasoftware.interactiveclient;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by metkinskiioleg on 14.02.16.
 */
public class ParseAdd extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "Pra048bLmFvpZoHdxdwrMGBijHFoImKG4WklFoFa", "XIlWJhgnIKzB0cci6BSm6DG3vQBwUihuYYkzdswQ");
    }
}
