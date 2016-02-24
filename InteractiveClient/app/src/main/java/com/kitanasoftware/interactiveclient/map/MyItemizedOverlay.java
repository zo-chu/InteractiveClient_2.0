package com.kitanasoftware.interactiveclient.map;

/**
 * Created by Chudo on 31.01.2016.
 */

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.widget.Toast;

import com.kitanasoftware.interactiveclient.R;

public class MyItemizedOverlay extends ItemizedIconOverlay<OverlayItem> {

    protected Context mContext;
    private ArrayList<OverlayItem> items;
    private OverlayItem item;

    @Override
    public void addItem(int location, OverlayItem item) {
        super.addItem(location, item);
    }

    public MyItemizedOverlay(final Context context, final ArrayList<OverlayItem> aList) {
        super(context, aList,
                new OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(final int index,
                                                     final OverlayItem item) {
                        return false;
                    }

                    @Override
                    public boolean onItemLongPress(final int index,
                                                   final OverlayItem item) {
                        return false;
                    }
                });
        // TODO Auto-generated constructor stub
        mContext = context;
        items = aList;
    }

    @Override
    public boolean addItem(OverlayItem item) {
        // TODO Auto-generated method stub
        return super.addItem(item);
    }

    @Override
    protected boolean onSingleTapUpHelper(final int index,
                                          final OverlayItem item, final MapView mapView) {
        if(!item.getTitle().equals("NewPoint")) {
            Toast.makeText(mContext, "" + item.getTitle() + " " + item.getSnippet(),
                    Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e, MapView mapView) {
        float x = e.getX();
        float y = e.getY();

        return super.onSingleTapUp(e, mapView);
    }

}