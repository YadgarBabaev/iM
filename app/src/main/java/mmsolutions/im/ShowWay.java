package mmsolutions.im;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*http://stackoverflow.com/questions/14702621/answer-draw-path-between-two-points-using-google-maps-android-api-v2*/
public class ShowWay extends FragmentActivity {
    SupportMapFragment mapFragment;
    LocationManager locationManager;
    double lat, lng, myLat, myLng;
    String title, address = "", phone_number;
    GoogleMap map;
    Marker marker;
    Polyline line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, mLocationListener);

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

       if(i.getStringExtra("shop") != null){
            try {
                JSONObject obj = new JSONObject(i.getStringExtra("shop"));
                title = obj.getString("title");
                address = "Адресс: " + obj.getString("address");
                phone_number = obj.getString("phoneNumber");
                lat = obj.getDouble("latitude");
                lng = obj.getDouble("longitude");
                Log.d("LatLng", obj.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if (!i.getStringExtra("latitude").equals("null") && i.getStringExtra("latitude") != null
                && !i.getStringExtra("longitude").equals("null") && i.getStringExtra("longitude") != null) {
            lat = Double.parseDouble(i.getStringExtra("latitude"));
            lng = Double.parseDouble(i.getStringExtra("longitude"));
        }

        if (lat == 0  || lng == 0){
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(42.87442305, 74.61158752), 11);
            map.animateCamera(cameraUpdate);
        }
        else {
            marker = map.addMarker(new MarkerOptions().
                    position(new LatLng(lat, lng)).draggable(true).
                    title(title).
                    snippet(address + "; " + phone_number).
                    icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 14);
            map.animateCamera(cameraUpdate);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, mLocationListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(mLocationListener);
    }

    private void location() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER); // getting GPS status
        Boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER); // getting network status
        String LOG_TAG = "LOCATION";
        if (!isGPSEnabled && !isNetworkEnabled) {
            Log.d(LOG_TAG, "No Connection");
        } else {
            Location location;
            if (isGPSEnabled && locationManager != null) {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    myLat = location.getLatitude();
                    myLng = location.getLongitude();
                }
            } else if (isNetworkEnabled && locationManager != null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null) {
                    myLat = location.getLatitude();
                    myLng = location.getLongitude();
                }
            } else Log.d(LOG_TAG, "Could not to find ");
            Log.d(LOG_TAG, myLat + ", " + myLng);
            new connectAsyncTask().execute();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng((myLat + lat) / 2, (myLng + lng) / 2), 12);
            map.animateCamera(cameraUpdate);
        }
    }
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {location();}
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
        @Override
        public void onProviderEnabled(String provider) {}
        @Override
        public void onProviderDisabled(String provider) {}
    };

    public String makeURL (double sourcelat, double sourcelog, double destlat, double destlog ){
        String from = "?origin=" + sourcelat + "," + sourcelog;
        String to = "&destination=" + destlat + "," + destlog;
        return "https://maps.googleapis.com/maps/api/directions/json" + from + to + "&sensor=false&mode=driving&alternatives=true&key=AIzaSyD_NaC_upk2BujKJKdvOjNZ4uZ48tjNwqw";
    }
    public void drawPath(String  result) {
        try {
            if(line != null)
                line.remove();
            //Tranform the string into a json object
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);

//            PolylineOptions options = new PolylineOptions().width(5).color(Color.parseColor("#05b1fb")).geodesic(true);
//            for (int z = 0; z < list.size(); z++) {
//                LatLng point = list.get(z);
//                options.add(point);
//            }
//            line = map.addPolyline(options);

//            map.addPolyline(new PolylineOptions()
//                            .addAll(list)
//                            .width(12)
//                            .color(Color.parseColor("#05b1fb"))//Google maps blue color
//                            .geodesic(true)
//            );

            for(int z = 0; z<list.size()-1;z++){
                LatLng src= list.get(z);
                LatLng dest= list.get(z + 1);

                line = map.addPolyline(new PolylineOptions()
                .add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude,   dest.longitude))
                .width(2)
                .color(Color.parseColor("#05b1fb")).geodesic(true));
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng( (((double) lat / 1E5)),
                    (((double) lng / 1E5) ));
            poly.add(p);
        }
        return poly;
    }
    private class connectAsyncTask extends AsyncTask<Void, Void, String> {

        String response, url = makeURL(myLat, myLng, lat, lng);
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ShowWay.this);
            progressDialog.setMessage("Fetching route, Please wait...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }
        @Override
        protected String doInBackground(Void... params) {
            try {
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                response = EntityUtils.toString(httpEntity);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("DRAW_WAY: ", response);
            return response;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.hide();
            if(result!=null){
                drawPath(result);
            }
        }
    }
}
