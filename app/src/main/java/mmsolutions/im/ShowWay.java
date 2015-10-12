package mmsolutions.im;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ShowWay extends FragmentActivity {
    SupportMapFragment mapFragment;
    double lat, lng;
    GoogleMap map;
    Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        findViewById(R.id.getLatLng).setVisibility(View.INVISIBLE);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        map = mapFragment.getMap();
        if (map == null) {
            finish();
            return;
        }

        map.setMyLocationEnabled(true);
        UiSettings settings = map.getUiSettings();
        settings.setAllGesturesEnabled(true);
        settings.setZoomControlsEnabled(true);

        Intent i = getIntent();
        lat = Double.parseDouble(i.getStringExtra("latitude"));
        lng = Double.parseDouble(i.getStringExtra("longitude"));
        Log.d("LatLng", "Coordinates:" + lat + ", " + lng);

        if (lat == 0  || lng == 0){
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(42.87442305, 74.61158752), 11);
            map.animateCamera(cameraUpdate);
        }
        else {
            marker = map.addMarker(new MarkerOptions().
                    position(new LatLng(lat, lng)).draggable(true).
                    icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 14);
            map.animateCamera(cameraUpdate);
        }
    }
}
