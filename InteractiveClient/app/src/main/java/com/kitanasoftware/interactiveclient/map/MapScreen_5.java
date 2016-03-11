package com.kitanasoftware.interactiveclient.map;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.kitanasoftware.interactiveclient.DrawerAppCompatActivity;
import com.kitanasoftware.interactiveclient.R;
import com.kitanasoftware.interactiveclient.db.WorkWithDb;

import org.osmdroid.tileprovider.IRegisterReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;

public class MapScreen_5 extends DrawerAppCompatActivity
        implements IRegisterReceiver
{
    private HybridMap mapView;
    private ArrayList<OverlayItem> items;
    private MyItemizedOverlay myItemizedOverlay = null;
    private GeoPoint myLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#127e83"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        //map View
        mapView = (HybridMap) findViewById(R.id.hybridMap);

        Intent intent = getIntent();
        if (intent.getStringExtra("location") != null) {
            String strLocation = intent.getStringExtra("location");
            String[] arrStrLocation = strLocation.split(",");
            double guideLat = Double.parseDouble(arrStrLocation[0]);
            double guideLong = Double.parseDouble(arrStrLocation[1]);
            mapView.center(guideLat, guideLong);

            WorkWithDb.getWorkWithDb().updateGeopointByIndex(0, "My Guide", "Guide",
                    GeopointsData.getInstance().getCOLORS().get(0),
                    new double[]{guideLat,guideLong});
            createOverlay();
        } else {
            myLocation = getLocation();
            mapView.center(myLocation.getLatitude(), myLocation.getLongitude());
        }


        //add geopoints on map
        createOverlay();
    }


    public void createOverlay() {
        items = new ArrayList<>();
        ArrayList<Geopoint> geopoints;
        if(WorkWithDb.getWorkWithDb().getGeopointList() != null) {
             geopoints = WorkWithDb.getWorkWithDb().getGeopointList();
        }
        else geopoints = new ArrayList<>();

        if (geopoints.size() != 0) {
            for (int i = 0; i < geopoints.size(); i++) {
                OverlayItem newItem = new OverlayItem(geopoints.get(i).getName(),
                        geopoints.get(i).getType(),
                        new GeoPoint(geopoints.get(i).getCoordinates()[0],
                                geopoints.get(i).getCoordinates()[1]));
                Drawable marker = getResources().getDrawable(
                        geopoints.get(i).getColor());
                newItem.setMarker(marker);
                items.add(newItem);
            }
            myItemizedOverlay = new MyItemizedOverlay(this, items);
            mapView.setOverlay(myItemizedOverlay);
        }
    }

    public GeoPoint getLocation() {

        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getApplicationContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            return myLocation = new GeoPoint(0, 0);
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {

            myLocation = new GeoPoint(location.getLatitude(), location.getLongitude());
        } else {
            myLocation = new GeoPoint(0, 0);
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 10, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mapView.center(myLocation.getLatitude(), myLocation.getLongitude());
                WorkWithDb.getWorkWithDb().updateGeopointByIndex(1, "My location", "Me",
                        GeopointsData.getInstance().getCOLORS().get(2),
                        new double[]{myLocation.getLatitude(), myLocation.getLongitude()});
                createOverlay();
                System.out.println("Location updated!");
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });


        return myLocation;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.allPoints) {
            EditingDialog editDialog = new EditingDialog();
            Bundle args = new Bundle();
            args.putString("clicked", "allPoints");
            editDialog.setArguments(args);
            editDialog.show(getFragmentManager(), "allPoints");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View getContentView() {
        return getLayoutInflater().inflate(R.layout.map_screen_5, null);
    }
}
