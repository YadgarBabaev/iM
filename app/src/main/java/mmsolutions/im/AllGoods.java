package mmsolutions.im;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
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

public class AllGoods extends ListActivity {

    ProgressDialog pDialog;
    String shop_id, title, address, phone, lat, lng, description, url;
    JSONParser jParser = new JSONParser();
    RequestParams request = new RequestParams();
    ArrayList<HashMap<String, String>> productsList;

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_GOODS = "data";
    private static final String TAG_ID = "id";
    private static final String TAG_TITLE = "title";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_PRICE = "price";
    private static final String TAG_PHONE = "phone_number";
    private static final String TAG_LAT = "latitude";
    private static final String TAG_LNG = "longitude";
    private static final String TAG_DESCRIPTION = "text";
    // products JSONArray
    JSONArray data = null;
    TextView text;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_for_goods);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // HashMap for ListView
        productsList = new ArrayList<>();
        text = (TextView)findViewById(R.id.text_message);
        pDialog = new ProgressDialog(this);
        url = getString(R.string.get_data);


        Intent i = getIntent();
        shop_id = i.getStringExtra(TAG_ID);
        title = i.getStringExtra(TAG_TITLE);

        ((TextView)findViewById(R.id.title)).setText(title);



        ImageButton imageButton = (ImageButton)findViewById(R.id.show_map);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ShowWay.class);
                i.putExtra(TAG_LAT, lat);
                i.putExtra(TAG_LNG, lng);
                startActivity(i);
            }
        });

        // Loading products in Background Thread. Get ListView
        ListView lv = getListView();
        makeHTTPCall();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String _id = ((TextView) view.findViewById(R.id.goodsID)).getText().toString();
                Toast.makeText(getApplicationContext(), _id, Toast.LENGTH_SHORT).show();
                //TODO Goods page activity
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_delete_shop:
                deleteShop();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class GetShopDetails extends AsyncTask<String, String, String> {
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
            String sql = "SELECT * FROM shop WHERE id = " + shop_id;

            params.add(new BasicNameValuePair("sql", sql));

            JSONObject json = jParser.makeHttpRequest(url, "POST", params);
            // Check your log cat for JSON response
            Log.d("All Data: ", json.toString());

            try {
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    data = json.getJSONArray(TAG_GOODS);
                    JSONObject c = data.getJSONObject(0);

                    title = c.getString(TAG_TITLE);
                    address = c.getString(TAG_ADDRESS);
                    phone = c.getString(TAG_PHONE);
                    description = c.getString(TAG_DESCRIPTION);
                    lat = c.getString(TAG_LAT);
                    lng = c.getString(TAG_LNG);
                } else {Log.d("MESSAGE", json.getString("message"));}
            } catch (JSONException e) {e.printStackTrace();}
            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            if (success == 1) {
                if(description.length()>250){
                    description = description.substring(0,250) + "...";
                }
                ((TextView)findViewById(R.id.title)).setText(title);
                ((TextView)findViewById(R.id.description)).setText(description);
                ((TextView)findViewById(R.id.contacts)).setText(phone);
                ((TextView)findViewById(R.id.address)).setText(address);
                new LoadAllGoods().execute();
            }
        }
    }

    class LoadAllGoods extends AsyncTask<String, String, String> {

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
            String sql = "SELECT * FROM goods WHERE shop_id = " + shop_id;

            params.add(new BasicNameValuePair("type", "get"));
            params.add(new BasicNameValuePair("sql", sql));

            JSONObject json = jParser.makeHttpRequest(url, "POST", params);
            // Check your log cat for JSON response
            Log.d("All Data: ", json.toString());

            try {
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    data = json.getJSONArray(TAG_GOODS);

                    // looping through All Products
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject c = data.getJSONObject(i);

                        Log.d("Row", c.toString());
                        // Storing each json item in variable
                        int id = c.getInt(TAG_ID);
                        String name = c.getString(TAG_TITLE);
                        String desc = c.getString(TAG_DESCRIPTION);
                        int price = c.getInt(TAG_PRICE);
                        int ctrl = c.getInt("controlPrice");
                        Log.d("GOODS:", id + ": " + name);
                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_ID, String.valueOf(id));
                        map.put(TAG_TITLE, name);
                        map.put(TAG_DESCRIPTION, desc);
                        if(ctrl == 1) {
                            map.put(TAG_PRICE, String.valueOf(price));
                        }
                        // adding HashList to ArrayList
                        productsList.add(map);
                    }
                } else {Log.d("MESSAGE", json.getString("message"));}
            } catch (JSONException e) {e.printStackTrace();}
            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            if (success != 1) {text.setVisibility(View.VISIBLE);}
            else {
                runOnUiThread(new Runnable() {
                    public void run() {
                        ListAdapter adapter = new SimpleAdapter(
                                AllGoods.this, productsList,
                                R.layout.list_item_for_goods, new String[]{TAG_ID, TAG_TITLE, TAG_DESCRIPTION, TAG_PRICE},
                                new int[]{R.id.goodsID, R.id.goods_name, R.id.goods_description, R.id.goods_price});
                        // updating ListView
                        setListAdapter(adapter);
                    }
                });
            }
        }
    }

    class DeleteShop extends AsyncTask<String, String, String> {

        int success;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Request processing... Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<>();
            String sql = "DELETE FROM shop WHERE id = " + shop_id;

            params.add(new BasicNameValuePair("type", "set"));
            params.add(new BasicNameValuePair("sql", sql));

            JSONObject json = jParser.makeHttpRequest(url, "POST", params);
            Log.d("All Data: ", json.toString());

            try {success = json.getInt(TAG_SUCCESS);
            } catch (JSONException e) {e.printStackTrace();}
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if (success == 1) {
                startActivity(new Intent(AllGoods.this, AllShops.class));
                finish();
            }
        }
    }

//    public boolean isConnectedToServer(String url, int timeout) {
//        try{
//            URL myUrl = new URL(url);
//            URLConnection connection = myUrl.openConnection();
//            connection.setConnectTimeout(timeout);
//            connection.connect();
//            return true;
//        } catch (Exception e) {
//            // Handle your exceptions
//            return false;
//        }
//    }

    public void deleteShop(){
        AlertDialog.Builder builder = new AlertDialog.Builder(AllGoods.this);
        builder.setTitle("Удалить магазин?")
                .setMessage("Все товары тоже будут удалены!")
                .setIcon(android.R.drawable.ic_menu_delete)
                .setCancelable(false)
                .setPositiveButton("I\'m agree", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) { new DeleteShop().execute(); dialog.cancel();}
                        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void makeHTTPCall() {
        pDialog.setMessage("Connecting to server...");
        pDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, request, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                pDialog.dismiss();
//                new GetShopDetails().execute();
                new LoadAllGoods().execute();
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                text.setText(getString(R.string.no_connection));
                text.setVisibility(View.VISIBLE);
                pDialog.dismiss();

                // When Http response code is '404'
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(getApplicationContext(), "Error Occurred \n Most Common Error: \n1. Device not connected to Internet\n" +
                            "2. Web App is not deployed in App server\n3. App server is not running\n HTTP Status code : " + statusCode, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
