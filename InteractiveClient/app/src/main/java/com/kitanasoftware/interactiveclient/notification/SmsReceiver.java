package com.kitanasoftware.interactiveclient.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.gsm.SmsMessage;
import android.widget.Toast;

import com.kitanasoftware.interactiveclient.map.MapScreen_5;

import java.io.Console;

/**
 * Created by Chudo on 24.02.2016.
 */
public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        String str = "";

        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                str += msgs[i].getMessageBody().toString();
                int ind = str.lastIndexOf("[");
                str = str.substring(ind+1, str.length()-1);

            }
            Intent i = new Intent(context, MapScreen_5.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (!str.equals("")) {
                i.putExtra("location", str);
                context.startActivity(i);
            }

        }

    }
}


