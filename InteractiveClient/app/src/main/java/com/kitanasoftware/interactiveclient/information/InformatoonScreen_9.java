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

    InformationAdapter adapter;
    ListView listView;
    ArrayList<Information> informList;

    Bitmap bitPhoto;
    String photoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#fdc68a"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        if(WorkWithDb.getWorkWithDb().getInformList() != null) {
            informList = WorkWithDb.getWorkWithDb().getInformList();
            adapter = new InformationAdapter(getApplicationContext(), informList);
            listView = (ListView) findViewById(R.id.lvInform);
            listView.setAdapter(adapter);
        }
    }

    @Override
    public View getContentView() {
        return getLayoutInflater().inflate(R.layout.informatoon_screen_9, null);
    }


    public void invalidateLv() {
        adapter.setList(informList);
        listView.setAdapter(adapter);
        invalidateOptionsMenu();
        listView.invalidate();
    }

    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    // save foto to SD card
    public void savePhoto() {

        photoPath = Environment.getExternalStorageDirectory() + "/myPhoto" + System.currentTimeMillis() + ".jpg";
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(photoPath);
            bitPhoto.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public Bitmap getPhotoFromGallery(){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeFile(photoPath, options);
    }
}




