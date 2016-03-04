package com.kitanasoftware.interactiveclient.map;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.tileprovider.IRegisterReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import android.content.Context;
import android.location.Location;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import com.kitanasoftware.interactiveclient.R;


public class HybridMap extends FrameLayout {
    private Context context;
    public MapView mv;
    private ResourceProxy resourceProxy;

    public HybridMap(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        resourceProxy = new DefaultResourceProxyImpl(this.getContext());
//        useMapsforgeTiles(Environment.getExternalStorageDirectory().getPath() + File.separator + "israel.map");
        useMapsforgeTiles(R.raw.israel);
        mv.getController().setZoom(15);
//        center(31.915091, 34.768182);
    }

//    public void zoomIn() {
//        mv.getController().zoomIn();
//    }
//
//    public void zoomOut() {
//        mv.getController().zoomOut();
//    }

    public void setOverlay(MyItemizedOverlay myItemizedOverlay) {
        mv.getOverlays().clear();
        mv.getOverlays().add(myItemizedOverlay);
        mv.invalidate();
    }

    public void useMapsforgeTiles(int mapResourceId) {
//		Log.d("--->useMapsforgeTiles", "startMethod");

        if (mv != null) {
            this.removeView(mv);
            mv = null;
        }
        InputStream input = getResources().openRawResource(mapResourceId);
//		Log.d("--->useMapsforgeTiles",getResources().getResourceEntryName(mapResourceId));
        File mapFile = new File(context.getCacheDir(), getResources().getResourceEntryName(mapResourceId));
        OutputStream output = null;
        try {
            output = new FileOutputStream(mapFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            byte[] buffer = new byte[input.available() / 2]; // or other buffer size
            int read;
            while ((read = input.read(buffer)) != -1) {
                Log.d("--->", "readed");
                output.write(buffer, 0, read);
            }
            output.flush();
        } catch (Exception e) {
            e.printStackTrace(); // handle exception, define IOException and others
        } finally {
            try {
                input.close();
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        MFTileProvider provider = new MFTileProvider((IRegisterReceiver) this.getContext(), mapFile);
        mv = new MapView(this.getContext(), provider.getTileSource().getTileSizePixels(), resourceProxy, provider);
        mv.setBuiltInZoomControls(false);
        mv.setMultiTouchControls(true);
        this.addView(mv);
    }

    public void center(double latitude, double longitude) {
        GeoPoint p = new GeoPoint(latitude, longitude);
        mv.getController().setCenter(p);
        mv.getController().setZoom(13);
    }

}
