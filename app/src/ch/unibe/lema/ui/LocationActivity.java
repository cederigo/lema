package ch.unibe.lema.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import ch.unibe.lema.R;
import ch.unibe.lema.provider.Lecture;
import ch.unibe.lema.provider.Lecture.Event;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class LocationActivity extends MapActivity {    
    
    private static final String TAG_NAME = "Location";
    private Lecture mLecture;
    private Geocoder geoCoder;
    
    private class MyItemizedOverlay extends ItemizedOverlay<OverlayItem> {
        
        private List<OverlayItem> overlayItems;        
        private AlertDialog.Builder dialog;
        
        public MyItemizedOverlay(Drawable defaultMarker) {
            super(boundCenterBottom(defaultMarker));
            overlayItems = new ArrayList<OverlayItem>();
            dialog = new AlertDialog.Builder(LocationActivity.this);
        }
        
        private void addOverlay(OverlayItem oItem) {
            overlayItems.add(oItem);
            populate();            
        }
        
        @Override
        protected boolean onTap(int idx) {
            
            if (overlayItems.isEmpty()) {
                return false;
            }
            
            OverlayItem item = overlayItems.get(idx);            
            dialog.setTitle(item.getTitle());
            dialog.setMessage(item.getSnippet());
            dialog.show();
            return true;

        }

        @Override
        protected OverlayItem createItem(int i) {            
            return overlayItems.get(i);
        }

        @Override
        public int size() {
            return overlayItems.size();
        }
        
    }
    
  
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.location);       
        
        Intent i = getIntent();
        mLecture = i.getParcelableExtra("lecture");
        if (mLecture == null) {
            setResult(RESULT_CANCELED);
            finish();
            return;
        }
        
        Log.d(TAG_NAME,"got lecture: " + mLecture.getTitle());
        
        final MapView mapView = (MapView)findViewById(R.id.mapview);
        final MapController mapController = mapView.getController();
        final MyItemizedOverlay itemOverlay = new MyItemizedOverlay(getResources().getDrawable(R.drawable.mappin));        
        
        mapView.setBuiltInZoomControls(true);               
               
        /*overlay with pins*/        
        final List<Event> events = mLecture.getEvents();     
        new Thread() {
            public void run() {
                for(final Event e : events) {
                    /*this takes some time*/
                    final GeoPoint eventCoords = findGeoPoint(e.location);
                    if (eventCoords != null) {
                        itemOverlay.addOverlay(new OverlayItem(eventCoords, 
                                e.toString(), mLecture.getTitle() + "@" + e.location));
                        mapView.postInvalidate();
                        
                    }
                }    
                
                Log.d(TAG_NAME,"showing " + itemOverlay.size() +" items on overlay");
                
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (itemOverlay.size() > 0) {
                            mapView.getOverlays().add(itemOverlay); 
                            mapController.animateTo(itemOverlay.getCenter());            
                        }
                    }
                });
                
            }
        }.start();
    }
    
    
    private GeoPoint findGeoPoint(String locationName) {
        if (geoCoder == null)
            geoCoder = new Geocoder(getBaseContext());

        try {
            List<Address> addresses = geoCoder.getFromLocationName(
                    locationName,1);
            for (Address addr : addresses) {
                Log.d(TAG_NAME, addr.toString());

                GeoPoint geoPoint = new GeoPoint(
                        (int) (addr.getLatitude() * Math.pow(10, 6)),
                        (int) (addr.getLongitude() * Math.pow(10, 6)));

                return geoPoint;
                
            }
        } catch (IOException e) {
            Log.e(TAG_NAME, "error getting address from geocoder. " + e);
        }
        return null;
    }
    
    
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

}
