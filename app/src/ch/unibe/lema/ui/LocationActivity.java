package ch.unibe.lema.ui;

import android.os.Bundle;

import ch.unibe.lema.R;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class LocationActivity extends MapActivity {
  
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.location);
        
        MapView mapView = (MapView)findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        
    }
    
    
    @Override
    protected boolean isRouteDisplayed() {
        // TODO Auto-generated method stub
        
        return false;
    }

}
