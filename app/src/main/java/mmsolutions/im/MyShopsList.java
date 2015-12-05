package mmsolutions.im;

import android.accounts.AccountManager;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.Random;

public class MyShopsList extends ListActivity {

    ArrayList<HashMap<String, String>> productsList;
    JSONParser jParser = new JSONParser();
    private AccountManager mAccountManager;
    ProgressDialog pDialog;
    String token;

    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "title";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_PHONE = "phone_number";
    private static final String TAG_LAT = "latitude";
    private static final String TAG_LNG = "longitude";
    private static final String TAG_LOGO = "logo";
    private static final String TAG_DESCRIPTION = "description";

    int[] img = {R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4, R.drawable.img5, R.drawable.img6, R.drawable.img7};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);
        if(getActionBar() != null)
            getActionBar().setDisplayHomeAsUpEnabled(true);

        productsList = new ArrayList<>();
        pDialog = new ProgressDialog(this);

        Intent i = getIntent();
        token = i.getStringExtra("token");

        new LoadMyShops().execute();

        ListView lv = getListView();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // getting values from selected ListItem
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
                in.putExtra(TAG_PHONE, phone);
                in.putExtra(TAG_DESCRIPTION, desc);
                in.putExtra(TAG_LAT, lat);
                in.putExtra(TAG_LNG, lng);
                in.putExtra("token", token);
                in.putExtra("owner", true);
                startActivity(in);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class LoadMyShops extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Request processing\nPlease wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            String url = "http://85.113.17.196:92/api/v1/my/shop/list"; //url to send
            JSONObject json = jParser.makeHttpRequest(url, "POST", new MultipartEntity(), token);
            try {
                JSONArray shops = json.getJSONArray("my_shops");
                for (int i = 0; i < shops.length(); i++) {
                    JSONObject c = shops.getJSONObject(i);
                    // Storing each json item in variable
                    String id = c.getString(TAG_ID);
                    String name = c.getString(TAG_NAME);
                    String address = c.getString(TAG_ADDRESS);
                    String phone = c.getString("phoneNumber");
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

                    map.put(TAG_LOGO, String.valueOf(img[new Random().nextInt(7)]));
//                    map.put(TAG_LOGO, String.valueOf(img[i%7]));

                    // adding HashList to ArrayList
                    productsList.add(map);
                }
            } catch (JSONException e) {e.printStackTrace();}
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    ListAdapter adapter = new SimpleAdapter(
                            MyShopsList.this, productsList,
                            R.layout.list_item_for_shops, new String[]{TAG_ID, TAG_NAME, TAG_ADDRESS, TAG_PHONE, TAG_LOGO, TAG_LAT, TAG_LNG, TAG_DESCRIPTION},
                            new int[]{R.id.shopID, R.id.shopTitle, R.id.shopAddress, R.id.shopPhone, R.id.shop_logo, R.id.lat, R.id.lng, R.id.description});
                    // updating listView
                    setListAdapter(adapter);
                }
            });
        }
    }
}
