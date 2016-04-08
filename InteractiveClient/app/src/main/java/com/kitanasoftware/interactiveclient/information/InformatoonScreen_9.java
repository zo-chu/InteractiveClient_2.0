package com.kitanasoftware.interactiveclient.information;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kitanasoftware.interactiveclient.DrawerAppCompatActivity;
import com.kitanasoftware.interactiveclient.R;
import com.kitanasoftware.interactiveclient.db.WorkWithDb;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InformatoonScreen_9 extends DrawerAppCompatActivity {

   private InformationAdapter adapter;
    private ListView listView;
    private ArrayList<String> informList;
    private TextView name;
    private TextView phone;
    private TextView tour;
    private TextView goal;
    private TextView company;

    private Bitmap bitPhoto;
    private String photoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#fdc68a"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);


        name = (TextView) findViewById(R.id.guideName);
        phone = (TextView) findViewById(R.id.guidePhone);
        tour = (TextView) findViewById(R.id.tour);
        goal = (TextView) findViewById(R.id.goal);
        company = (TextView) findViewById(R.id.company);

        if(WorkWithDb.getWorkWithDb().getInformList() != null) {
            informList = WorkWithDb.getWorkWithDb().getInformList();
            name.setText(informList.get(0));
            phone.setText(informList.get(1));
            tour.setText(informList.get(2));
            goal.setText(informList.get(3));
            company.setText(informList.get(4));


//            adapter = new InformationAdapter(getApplicationContext(), informList);
//            listView = (ListView) findViewById(R.id.lvInform);
//            listView.setAdapter(adapter);


        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(WorkWithDb.getWorkWithDb().getInformList() != null) {
//            informList = WorkWithDb.getWorkWithDb().getInformList();
//            adapter = new InformationAdapter(getApplicationContext(), informList);
//            listView = (ListView) findViewById(R.id.lvInform);
//            listView.setAdapter(adapter);
            name.setText(informList.get(0));

            phone.setText(informList.get(1));

            tour.setText(informList.get(2));
            goal.setText(informList.get(3));
            company.setText(informList.get(4));
        }
    }

    @Override
    public View getContentView() {
        return getLayoutInflater().inflate(R.layout.informatoon_screen_9, null);
    }

}




