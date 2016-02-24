package com.kitanasoftware.interactiveguide.map;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kitanasoftware.interactiveguide.DrawerAppCompatActivity;
import com.kitanasoftware.interactiveguide.R;

import org.osmdroid.tileprovider.IRegisterReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;

public class MapScreen_5 extends DrawerAppCompatActivity
        implements IRegisterReceiver
// EditGeo
{

    public static Boolean addingMode = false;
    public static Boolean editingMode = false;

    private Geopoint editedGeo;
    private int editedGeoPosition;

    private HybridMap mapView;
    private Button creationGeoOk;
    private Button creationGeoCancel;
    private Button addGeo;
    private Button editGeo;
    private Button delGeo;
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
        myLocation = getLocation();
        mapView.center(myLocation.getLatitude(),myLocation.getLongitude());

        //add geopoints on map
        createOverlay();
    }


    public void createOverlay() {
        items = new ArrayList<>();
        ArrayList<Geopoint> geopoints = GeopointsData.getInstance().getGeopoints();

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

    public GeoPoint getLocation() {

        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getApplicationContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            return myLocation = new GeoPoint(0,0);
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {

            myLocation = new GeoPoint(location.getLatitude(),location.getLongitude());
        }
        else{
            myLocation = new GeoPoint(0,0);
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 10, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
//                mapView.getController().animateTo(myLocation);
                mapView.center(myLocation.getLatitude(), myLocation.getLongitude());
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
