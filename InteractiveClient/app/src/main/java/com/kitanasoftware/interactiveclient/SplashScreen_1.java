package com.kitanasoftware.interactiveclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kitanasoftware.interactiveclient.dataTransfer.StartConn;
import com.kitanasoftware.interactiveclient.db.WorkWithDb;
import com.kitanasoftware.interactiveclient.map.GeopointsData;

public class  SplashScreen_1 extends AppCompatActivity {
    WorkWithDb workWithDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_1);
        workWithDb = WorkWithDb.getWorkWithDb(getApplicationContext());

        //if there is conn !!
        Intent intent = new Intent(getApplicationContext(), StartConn.class);
        startService(intent);

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{

                    Intent intent = new Intent(getApplicationContext(),EnterYourNameScreen_2.class);
                    startActivity(intent);

                }
            }
        };
        timerThread.start();
    }
}

