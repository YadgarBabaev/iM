package mmsolutions.im;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.http.entity.mime.MultipartEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AllShops extends ListActivity {

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> productsList;
    ArrayList<String> shopList;

    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "title";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_PHONE = "phoneNumber";
    private static final String TAG_LAT = "latitude";
    private static final String TAG_LNG = "longitude";
    private static final String TAG_LOGO = "logo";
    private static final String TAG_DESCRIPTION = "description";

    String token;
    LinearLayout layout;
    ProgressDialog pDialog;

//    int[] img = {R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4, R.drawable.img5, R.drawable.img6, R.drawable.img7};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);
        if(getActionBar() != null)
            getActionBar().setDisplayHomeAsUpEnabled(true);

        // HashMap for ListView
        productsList = new ArrayList<>();
        shopList = new ArrayList<>();

        Intent i = getIntent();
        token = i.getStringExtra("token");

        layout = (LinearLayout) findViewById(R.id.LinearLayout);
        pDialog = new ProgressDialog(AllShops.this);

//        makeHTTPCall();
        new LoadAllShops().execute();

        // Get ListView
        ListView lv = getListView();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String shop_id = ((TextView) view.findViewById(R.id.shopID)).getText().toString();
                String title = ((TextView) view.findViewById(R.id.shopTitle)).getText().toString();
                String desc = ((TextView) view.findViewById(R.id.description)).getText().toString();
                String phone = ((TextView) view.findViewById(R.id.shopPhone)).getText().toString();
                String address = ((TextView) view.findViewById(R.id.shopAddress)).getText().toString();
                String lat = ((TextView) view.findViewById(R.id.lat)).getText().toString();
                String lng = ((TextView) view.findViewById(R.id.lng)).getText().toString();

                // Starting new intent
                Intent in = new Intent(getApplicationContext(), GoodsList.class);
                in.putExtra(TAG_ID, shop_id);
                in.putExtra(TAG_NAME, title);
                in.putExtra(TAG_ADDRESS, address);
                in.putExtra("phone_number", phone);
                in.putExtra(TAG_DESCRIPTION, desc);
                in.putExtra(TAG_LAT, lat);
                in.putExtra(TAG_LNG, lng);
                in.putExtra("token", token);
                in.putExtra("owner", false);
                startActivity(in);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

//    @Override
//    public boolean onPrepareOptionsMenu (Menu menu) {
//        if (!enable)
//            menu.getItem(1).setEnabled(false);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
//                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class LoadAllShops extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Loading... Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            String url = "http://85.113.17.196:92/rest/shop/list";
            JSONObject json = jParser.makeHttpRequest(url, "POST", new MultipartEntity(), token);
            Log.d("All Data: ", json.toString());
            try {
                JSONArray shops = json.getJSONArray("shops");

                for (int i = 0; i < shops.length(); i++) {
                    JSONObject c = shops.getJSONObject(i);
                    // Log.d("JSONObject", c.toString());
                    // Storing each json item in variable
                    String id = c.getString(TAG_ID);
                    String name = c.getString(TAG_NAME);
                    String address = c.getString(TAG_ADDRESS);
                    String phone = c.getString(TAG_PHONE);
                    String lat = c.getString(TAG_LAT);
                    String lng = c.getString(TAG_LNG);
                    String desc = c.getString(TAG_DESCRIPTION);

                    // creating new HashMap
                    HashMap<String, String> map = new HashMap<>();

                    // adding each child node to HashMap key => value
                    map.put(TAG_ID, id);
                    map.put(TAG_NAME, name);
                    map.put(TAG_ADDRESS, address);
                    map.put(TAG_PHONE, phone);
                    map.put(TAG_DESCRIPTION, desc);
                    map.put(TAG_LAT, lat);
                    map.put(TAG_LNG, lng);

//                    map.put("image", String.valueOf(img[new Random().nextInt(7)]));
                    map.put(TAG_LOGO, String.valueOf(R.drawable.purple_shop));

                    // adding HashList to ArrayList
                    productsList.add(map);
                    shopList.add(name);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
//            if (success != 1) {
//                TextView textView = new TextView(AllShops.this);
//                textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//                textView.setText(getString(R.string.no_shop));
//                textView.setGravity(Gravity.CENTER);
//                layout.addView(textView);
//                enable = false;
//
//            } else {
//                // updating UI from Background Thread
//            }
            runOnUiThread(new Runnable() {
                public void run() {
                    ListAdapter adapter = new SimpleAdapter(
                            AllShops.this, productsList,
                            R.layout.list_item_for_shops, new String[]{ TAG_ID, TAG_NAME, TAG_ADDRESS, TAG_PHONE, TAG_LOGO, TAG_LAT, TAG_LNG, TAG_DESCRIPTION },
                            new int[]{ R.id.shopID, R.id.shopTitle, R.id.shopAddress, R.id.shopPhone, R.id.shop_logo, R.id.lat, R.id.lng, R.id.description });
                    // updating listView
                    setListAdapter(adapter);
                }
            });
        }
    }

//    public void makeHTTPCall() {
//        pDialog.setMessage("Connecting to server...");
//        pDialog.show();
//        AsyncHttpClient client = new AsyncHttpClient();
//        client.post(url, request, new AsyncHttpResponseHandler() {
//
//            @Override
//            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
//                pDialog.dismiss();
//                new LoadAllShops().execute();
//            }
//
//            @Override
//            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
//                TextView textView = new TextView(AllShops.this);
//                textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//                textView.setText(getString(R.string.no_connection));
//                textView.setGravity(Gravity.CENTER);
//                layout.addView(textView);
//                pDialog.dismiss();
//
//                // When Http response code is '404'
//                if (statusCode == 404) {
//                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();}
//                // When Http response code is '500'
//                else if (statusCode == 500) {Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();}
//                // When Http response code other than 404, 500
//                else {Toast.makeText(getApplicationContext(), "Error Occurred \n Most Common Error: \n1. Device not connected to Internet\n" +
//                        "2. Web App is not deployed in App server\n3. App server is not running\n HTTP Status code : " + statusCode, Toast.LENGTH_LONG).show();}
//            }
//        });
//    }
}