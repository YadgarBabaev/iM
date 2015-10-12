package mmsolutions.im;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map extends FragmentActivity{
    SupportMapFragment mapFragment;
    double markerLat, markerLng, lat, lng;
    GoogleMap map;
    Marker marker;
    String coords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        map = mapFragment.getMap();
        if (map == null) {
            finish();
            return;
        }

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                marker.setPosition(latLng);
                markerLat = latLng.latitude;
                markerLng = latLng.longitude;
            }
        });

        Intent i = getIntent();
        coords = i.getStringExtra("coords");
        Log.d("LatLng", "Coords:" + coords);

        if (!coords.equals("")){
            String[] parts = coords.split(",");
            lat = Double.parseDouble(parts[0]);
            lng = Double.parseDouble(parts[1]);
        }
        else {
            lat = 42.87442305;
            lng = 74.61158752;
        }

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(42.87442305, 74.61158752), 11);
        map.animateCamera(cameraUpdate);

        map.setMyLocationEnabled(true);
        UiSettings settings = map.getUiSettings();
        settings.setAllGesturesEnabled(true);
        settings.setZoomControlsEnabled(true);

        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {}

            @Override
            public void onMarkerDrag(Marker marker) {
                LatLng dragPosition = marker.getPosition();
                markerLat = dragPosition.latitude;
                markerLng = dragPosition.longitude;
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {}
        });

        marker = map.addMarker(new MarkerOptions().
                position(new LatLng(lat, lng)).draggable(true).
                icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        ImageButton show = (ImageButton)findViewById(R.id.getLatLng);
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("LatLng", String.format("%1$.6f,%2$.6f", markerLat, markerLng));
                setResult(RESULT_OK, returnIntent);
                finish();
                Log.d("LatLng", "Lat :" + markerLat + " Long :" + markerLng);
            }
        });
    }
}
