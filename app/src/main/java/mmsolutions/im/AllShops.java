package mmsolutions.im;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mmsolutions.im.SignIn.MainPage;

public class AllShops extends ListActivity {

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    RequestParams request = new RequestParams();
    ArrayList<HashMap<String, String>> productsList;
    ArrayList<String> shopList;
    boolean enable = true;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_SHOPS = "data";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "title";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_PHONE = "phone_number";
    private static final String TAG_LAT = "latitude";
    private static final String TAG_LNG = "longitude";
    private static final String TAG_COORDS = "coordinates";
    private static final String TAG_LOGO = "image";
    private static final String TAG_DESCRIPTION = "description";

    // products JSONArray
    JSONArray shops = null;
    String owner, url;
    LinearLayout layout;
    ProgressDialog pDialog;

    int[] img = {R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4, R.drawable.img5, R.drawable.img6, R.drawable.img7};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

       // HashMap for ListView
        productsList = new ArrayList<>();
        shopList = new ArrayList<>();

//        Intent i = getIntent();
//        owner = i.getStringExtra("owner");
        owner = "18";
        url = getString(R.string.get_data); //url to send
        pDialog = new ProgressDialog(AllShops.this);

        // Get ListView
        ListView lv = getListView();
        layout = (LinearLayout) findViewById(R.id.LinearLayout);

        makeHTTPCall();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // getting values from selected ListItem
                String shop_id = ((TextView) view.findViewById(R.id.shopID)).getText().toString();
                String title = ((TextView) view.findViewById(R.id.shopTitle)).getText().toString();
//                String desc = ((TextView) view.findViewById(R.id.description)).getText().toString();
//                String phone = ((TextView) view.findViewById(R.id.shopPhone)).getText().toString();
//                String address = ((TextView) view.findViewById(R.id.shopAddress)).getText().toString();
//                String coords = ((TextView) view.findViewById(R.id.coordinates)).getText().toString();

                // Starting new intent
                Intent in = new Intent(getApplicationContext(), AllGoods.class);
                in.putExtra(TAG_ID, shop_id);
                in.putExtra(TAG_NAME, title);
//                in.putExtra(TAG_ADDRESS, address);
//                in.putExtra(TAG_PHONE, phone);
//                in.putExtra(TAG_DESCRIPTION, desc);
//                in.putExtra(TAG_COORDS, coords);
                startActivity(in);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        if (!enable)
            menu.getItem(1).setEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_add_shop:
                Intent addShop = new Intent(this, AddShop.class);
                addShop.putExtra("owner", owner);
                startActivityForResult(addShop, 1);
                break;
            case R.id.action_add_goods:
                Intent i = new Intent(this, AddGoods.class);
                i.putStringArrayListExtra("shops", shopList);
                startActivityForResult(i, 1);
                break;
            case R.id.action_reload:
                finish();
                startActivity(getIntent());
                break;
            case R.id.action_logout:
                finish();
                startActivity(new Intent(this, MainPage.class));
                break;
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

        int success;
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<>();
            String sql = "SELECT * FROM shop WHERE user_id = " + owner;

            params.add(new BasicNameValuePair("sql", sql));

            JSONObject json = jParser.makeHttpRequest(url, "POST", params);
            // Check your log cat for JSON response
            Log.d("All Data: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    shops = json.getJSONArray(TAG_SHOPS);

                    // looping through All Products
                    for (int i = 0; i < shops.length(); i++) {
                        JSONObject c = shops.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString(TAG_ID);
                        String name = c.getString(TAG_NAME);
                        String address = c.getString(TAG_ADDRESS);
                        String phone = c.getString(TAG_PHONE);
                        String lat = c.getString(TAG_LAT);
                        String lng = c.getString(TAG_LNG);
                        String coordinates = lat + ", " + lng;
                        String desc = c.getString(TAG_DESCRIPTION);
                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_ID, id);
                        map.put(TAG_NAME, name);
                        map.put(TAG_ADDRESS, address);
                        map.put(TAG_PHONE, phone);
                        map.put(TAG_COORDS, coordinates);
                        map.put(TAG_DESCRIPTION, desc);

//                        map.put("image", String.valueOf(img[new Random().nextInt(7)]));
                        map.put(TAG_LOGO, String.valueOf(img[i%7]));

                        // adding HashList to ArrayList
                        productsList.add(map);
                        shopList.add(name);
                    }
                } else {
                    Log.d("MESSAGE", json.getString("message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            if (success != 1) {
                TextView textView = new TextView(AllShops.this);
                textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                textView.setText(getString(R.string.no_shop));
                textView.setGravity(Gravity.CENTER);
                layout.addView(textView);
                enable = false;

            } else {
                // updating UI from Background Thread
                runOnUiThread(new Runnable() {
                    public void run() {
                        ListAdapter adapter = new SimpleAdapter(
                                AllShops.this, productsList,
                                R.layout.list_item_for_shops, new String[]{TAG_ID, TAG_NAME, TAG_ADDRESS, TAG_PHONE, TAG_LOGO, TAG_COORDS, TAG_DESCRIPTION},
                                new int[]{R.id.shopID, R.id.shopTitle, R.id.shopAddress, R.id.shopPhone, R.id.shop_logo, R.id.coordinates, R.id.description});
                        // updating listView
                        setListAdapter(adapter);
                    }
                });
            }
        }
    }

    public void makeHTTPCall() {
        pDialog.setMessage("Connecting to server...");
        pDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, request, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                pDialog.dismiss();
                new LoadAllShops().execute();
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                TextView textView = new TextView(AllShops.this);
                textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                textView.setText(getString(R.string.no_connection));
                textView.setGravity(Gravity.CENTER);
                layout.addView(textView);
                pDialog.dismiss();

                // When Http response code is '404'
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();}
                // When Http response code is '500'
                else if (statusCode == 500) {Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();}
                // When Http response code other than 404, 500
                else {Toast.makeText(getApplicationContext(), "Error Occurred \n Most Common Error: \n1. Device not connected to Internet\n" +
                        "2. Web App is not deployed in App server\n3. App server is not running\n HTTP Status code : " + statusCode, Toast.LENGTH_LONG).show();}
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            finish();
            startActivity(getIntent());
        }
    }
}