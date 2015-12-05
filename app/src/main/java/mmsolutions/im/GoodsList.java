package mmsolutions.im;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.entity.mime.MultipartEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GoodsList extends AppCompatActivity {

    ProgressDialog pDialog; TextView text;
    String shop_id, title, address, phone;
    String lat, lng, description, token;
    boolean owner;
    ListView list;
    Activity activity;
    JSONObject json;
    LazyImageLoadAdapter adapter;
    JSONParser jParser = new JSONParser();

    private static final String TAG_ID = "id";
    private static final String TAG_TITLE = "title";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_PHONE = "phone_number";
    private static final String TAG_LAT = "latitude";
    private static final String TAG_LNG = "longitude";
    private static final String TAG_DESCRIPTION = "description";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pDialog = new ProgressDialog(this);
        activity = this;

        if(getActionBar() != null)
            getActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton like = (FloatingActionButton) findViewById(R.id.like);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with like action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with comments action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Intent i = getIntent();
        shop_id = i.getStringExtra(TAG_ID);
        title = i.getStringExtra(TAG_TITLE);
        description = i.getStringExtra(TAG_DESCRIPTION);
        phone = i.getStringExtra(TAG_PHONE);
        address = i.getStringExtra(TAG_ADDRESS);
        lat = i.getStringExtra(TAG_LAT);
        lng = i.getStringExtra(TAG_LNG);
        if(i.getStringExtra("token") != null)
            token = i.getStringExtra("token");
        if(description.length() > 200){description = description.substring(0, 199) + "...";}
        owner = i.getBooleanExtra("owner", false);

        View view = findViewById(R.id.goods_listview);
        ((TextView) view.findViewById(R.id.title)).setText(title);
        ((TextView) view.findViewById(R.id.description)).setText(description);
        ((TextView) view.findViewById(R.id.contacts)).setText(phone);
        ((TextView) view.findViewById(R.id.address)).setText(address);
        text = (TextView) findViewById(R.id.content_scrolling).findViewById(R.id.text_message);


        FloatingActionButton show_map = (FloatingActionButton) findViewById(R.id.show_map);
        show_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ShowWay.class);
                i.putExtra(TAG_LAT, lat).putExtra(TAG_LNG, lng);
                startActivity(i);
            }
        });

        // Loading products in Background Thread. Get ListView
        list = (ListView) findViewById(R.id.content_scrolling).findViewById(R.id.list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    JSONArray jsonArray = json.getJSONArray("goods");
                    JSONObject object = jsonArray.getJSONObject(position);
                    startActivity(new Intent("GoodsView").putExtra("goods", object.toString()));
                    Log.d("GOODS_SELECTION", object.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        new LoadAllGoods().execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (owner) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.shop_menu, menu);
        } return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.edit_shop:
                Intent in = new Intent(getApplicationContext(), EditShop.class);
                in.putExtra(TAG_ID, shop_id);
                in.putExtra(TAG_TITLE, title);
                in.putExtra(TAG_ADDRESS, address);
                in.putExtra(TAG_PHONE, phone);
                in.putExtra(TAG_DESCRIPTION, description);
                in.putExtra(TAG_LAT, lat);
                in.putExtra(TAG_LNG, lng);
                in.putExtra("token", token);
                startActivityForResult(in, 1);
                finish();
                break;
            case R.id.add_goods:
                startActivityForResult(new Intent(this, AddGoods.class).putExtra("token", token).putExtra(TAG_ID, shop_id), 1);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            finish();
            startActivity(getIntent());
        }
    }
//    class GetShopDetails extends AsyncTask<String, String, String> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pDialog.setMessage("Loading... Please wait...");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(false);
//            pDialog.show();
//        }
//
//        int success;
//        protected String doInBackground(String... args) {
//            // Building Parameters
//            List<NameValuePair> params = new ArrayList<>();
//            String sql = "SELECT * FROM shop WHERE id = " + shop_id;
//
//            params.add(new BasicNameValuePair("sql", sql));
//
//            JSONObject json = jParser.makeHttpRequest(url, "POST", params);
//            // Check your log cat for JSON response
//            Log.d("All Data: ", json.toString());
//
//            try {
//                success = json.getInt(TAG_SUCCESS);
//                if (success == 1) {
//                    // products found
//                    // Getting Array of Products
//                    data = json.getJSONArray(TAG_GOODS);
//                    JSONObject c = data.getJSONObject(0);
//
//                    title = c.getString(TAG_TITLE);
//                    address = c.getString(TAG_ADDRESS);
//                    phone = c.getString(TAG_PHONE);
//                    description = c.getString(TAG_DESCRIPTION);
//                    lat = c.getString(TAG_LAT);
//                    lng = c.getString(TAG_LNG);
//                } else {Log.d("MESSAGE", json.getString("message"));}
//            } catch (JSONException e) {e.printStackTrace();}
//            return null;
//        }
//
//        protected void onPostExecute(String file_url) {
//            // dismiss the dialog after getting all products
//            pDialog.dismiss();
//            if (success == 1) {
//                if(description.length()>250){
//                    description = description.substring(0,250) + "...";
//                }
//                ((TextView)findViewById(R.id.title)).setText(title);
//                ((TextView)findViewById(R.id.description)).setText(description);
//                ((TextView)findViewById(R.id.contacts)).setText(phone);
//                ((TextView)findViewById(R.id.address)).setText(address);
//                new LoadAllGoods().execute();
//            }
//        }
//    }

    class LoadAllGoods extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Loading... Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        boolean success = false;
        List<Integer> id = new ArrayList<>();
        List<String> logo = new ArrayList<>();
        List<String> image = new ArrayList<>();
        List<String> title = new ArrayList<>();
        List<String> desc = new ArrayList<>();
        List<String> price = new ArrayList<>();
        List<Boolean> ctrl = new ArrayList<>();
        List<String> sizes = new ArrayList<>();

        protected String doInBackground(String... args) {
            String url = "http://85.113.17.196:92/rest/goods/list";
            json = jParser.makeHttpRequest(url, "POST", new MultipartEntity(), token);
            try {
                JSONArray jArray = json.getJSONArray("goods");
                if(jArray.length() > 0) success = true;
                for(int i=0; i<jArray.length(); i++)
                {
                    JSONObject object = jArray.getJSONObject(i);
                    Log.d("All GOODS: ", object.toString());
                    JSONObject shop = object.getJSONObject("shop");
                    id.add(object.getInt("id"));
                    image.add(object.getJSONObject("photo").getString("providerReference"));
                    logo.add(shop.getJSONObject("logo").getString("providerReference"));
                    title.add(object.getString("title"));
                    desc.add(object.getString("text"));
                    price.add(object.getString("price"));
                    ctrl.add(!object.getBoolean("controlPrice"));
                    sizes.add(object.getString("size"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            if (!success) {text.setVisibility(View.VISIBLE);}
            else {
                adapter = new LazyImageLoadAdapter(activity, id, image, logo, title, desc, price, ctrl, sizes);
                list.setAdapter(adapter);
            }
        }
    }
//    class DeleteShop extends AsyncTask<String, String, String> {
//
//        int success;
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pDialog.setMessage("Request processing... Please wait...");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(false);
//            pDialog.show();
//        }
//
//        @Override
//        protected String doInBackground(String... args) {
//            List<NameValuePair> params = new ArrayList<>();
//            String sql = "DELETE FROM shop WHERE id = " + shop_id;
//
//            params.add(new BasicNameValuePair("sql", sql));
//
//            JSONObject json = jParser.makeHttpRequest(url, "POST", params);
//            Log.d("All Data: ", json.toString());
//
//            try {success = json.getInt(TAG_SUCCESS);
//            } catch (JSONException e) {e.printStackTrace();}
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String file_url) {
//            pDialog.dismiss();
//            if (success == 1) {
//                startActivity(new Intent(AllGoods.this, AllShops.class));
//                finish();
//            }
//        }
//    }
//
////    public boolean isConnectedToServer(String url, int timeout) {
////        try{
////            URL myUrl = new URL(url);
////            URLConnection connection = myUrl.openConnection();
////            connection.setConnectTimeout(timeout);
////            connection.connect();
////            return true;
////        } catch (Exception e) {
////            // Handle your exceptions
////            return false;
////        }
////    }
//
//    public void deleteShop(){
//        AlertDialog.Builder builder = new AlertDialog.Builder(AllGoods.this);
//        builder.setTitle("Удалить магазин?")
//                .setMessage("Все товары тоже будут удалены!")
//                .setIcon(android.R.drawable.ic_menu_delete)
//                .setCancelable(false)
//                .setPositiveButton("I\'m agree", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) { new DeleteShop().execute(); dialog.cancel();}
//                        })
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {dialog.cancel();
//                    }
//                });
//        AlertDialog alert = builder.create();
//        alert.show();
//    }
//
//    public void makeHTTPCall() {
//        pDialog.setMessage("Connecting to server...");
//        pDialog.show();
//        AsyncHttpClient client = new AsyncHttpClient();
//        client.post(url, request, new AsyncHttpResponseHandler() {
//
//            @Override
//            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
//                pDialog.dismiss();
////                new GetShopDetails().execute();
//                new LoadAllGoods().execute();
//            }
//
//            @Override
//            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
//                text.setText(getString(R.string.no_connection));
//                text.setVisibility(View.VISIBLE);
//                pDialog.dismiss();
//
//                // When Http response code is '404'
//                if (statusCode == 404) {
//                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
//                }
//                // When Http response code is '500'
//                else if (statusCode == 500) {
//                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
//                }
//                // When Http response code other than 404, 500
//                else {
//                    Toast.makeText(getApplicationContext(), "Error Occurred \n Most Common Error: \n1. Device not connected to Internet\n" +
//                            "2. Web App is not deployed in App server\n3. App server is not running\n HTTP Status code : " + statusCode, Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//    }
}