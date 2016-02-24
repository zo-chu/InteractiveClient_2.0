package com.kitanasoftware.interactiveclient.map;

import java.io.File;
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
import android.widget.FrameLayout;


public class HybridMap extends FrameLayout {

    public MapView mv;
    private ResourceProxy resourceProxy;

    public HybridMap(Context context, AttributeSet attrs) {
        super(context, attrs);

        resourceProxy = new DefaultResourceProxyImpl(this.getContext());
        useMapsforgeTiles(Environment.getExternalStorageDirectory().getPath() + File.separator + "israel.map");
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

    public void useMapsforgeTiles(String mapFilePath) {

        if (mv != null) {
            this.removeView(mv);
            mv = null;
        }

        File mapFile = new File(mapFilePath);
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
