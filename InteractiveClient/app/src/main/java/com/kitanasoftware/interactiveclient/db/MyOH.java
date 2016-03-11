package com.kitanasoftware.interactiveclient.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kitanasoftware.interactiveclient.map.GeopointsData;

/**
 * Created by dasha on 19/02/16.
 */

public class MyOH extends SQLiteOpenHelper {

    public MyOH(Context context) {
        super(context, "mydb", null, 1);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {

        db.execSQL("CREATE TABLE schedule (id INT, time TEXT NOT NULL, description TEXT NOT NULL )");// здесь мы создали ДБ и таблицу в ней
        db.execSQL("CREATE TABLE geopoints (id INT, name TEXT NOT NULL, type TEXT NOT NULL, color INT, lat DOUBLE, lon DOUBLE)");
        db.execSQL("CREATE TABLE information (guide_name TEXT NOT NULL, guide_phone TEXT NOT NULL ,tour TEXT NOT NULL, goal TEXT NOT NULL, company TEXT NOT NULL )");
        db.execSQL("CREATE TABLE notifications (id INT,sentTo TEXT NOT NULL, text TEXT NOT NULL)");
        db.execSQL("INSERT INTO geopoints VALUES ( 0, 'My Guide', 'Guide', " +
                GeopointsData.getInstance().getCOLORS().get(0) + ", " + 0 + ", " + 0 + ")");
        db.execSQL("INSERT INTO geopoints VALUES (" + 1 + ", 'My Location', 'Me', " +
                GeopointsData.getInstance().getCOLORS().get(2) + ", " + 0 + ", " + 0 + ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
