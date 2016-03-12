package com.kitanasoftware.interactiveclient.notification;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.kitanasoftware.interactiveclient.DrawerAppCompatActivity;
import com.kitanasoftware.interactiveclient.R;
import com.kitanasoftware.interactiveclient.db.WorkWithDb;

import java.util.ArrayList;

public class NotificationScreen_7 extends DrawerAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#d7afd2"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        getList();

    }

    @Override
    public View getContentView() {
        return getLayoutInflater().inflate(R.layout.notification_screen_7, null);
    }

    private void getList() {
        if (WorkWithDb.getWorkWithDb().getNotificationList().size() != 0) {
            ArrayList<MyNotification> notifs = WorkWithDb.getWorkWithDb().getNotificationList();
            ArrayList<String> receivedMess = new ArrayList<>();
            String tmp = "";
            for (int i = 0; i < notifs.size(); i++) {
                tmp += "From: " + notifs.get(i).getSentTo() + " Message: " + notifs.get(i).getText();
                receivedMess.add(tmp);
            }
            if (receivedMess.size() != 0) {
                if (receivedMess.size() != 0) {
                    ListView recievedMessList = (ListView) findViewById(R.id.recievedNotifList);
                    ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(getApplicationContext(),
                            R.layout.one_string_list, receivedMess);
                    recievedMessList.setAdapter(listAdapter);
                }

            }
        }
    }
}
