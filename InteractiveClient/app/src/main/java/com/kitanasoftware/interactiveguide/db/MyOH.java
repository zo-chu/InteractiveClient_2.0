package com.kitanasoftware.interactiveguide.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
        db.execSQL("CREATE TABLE information (id TEXT NOT NULL, guide_name TEXT NOT NULL, guide_phone TEXT NOT NULL ,tour TEXT NOT NULL, goal TEXT NOT NULL, company TEXT NOT NULL )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
