//package mmsolutions.im;
//
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.loopj.android.http.AsyncHttpClient;
//import com.loopj.android.http.AsyncHttpResponseHandler;
//import com.loopj.android.http.RequestParams;
//
//import org.apache.http.Header;
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class AllShopListActivity extends Activity {
//
//    JSONParser jsonParser = new JSONParser();
//    private ProgressDialog pDialog;
//    ListView listView;
//
//    String[] title = new String[]{};
//    String[] address = new String[]{};
//    String[] phone = new String[]{};
//    String[] coords = new String[]{};
//    long[] id = new long[]{};
//
//    int[] img = {R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4, R.drawable.img5, R.drawable.img6, R.drawable.img7};
//
//    RequestParams request = new RequestParams();
//    ArrayList<String> shopList;
//    boolean enable = true;
//
//    // url to get all products list
//    private static String url_all_shops = "http://192.168.128.166/im/get_all_shops.php";
//
//    // JSON Node names
//    private static final String TAG_SUCCESS = "success";
//    private static final String TAG_SHOPS = "shops";
//    private static final String TAG_ID = "id";
//    private static final String TAG_NAME = "title";
//    private static final String TAG_ADDRESS = "address";
//    private static final String TAG_COORDS = "coordinates";
//    private static final String TAG_PHONE = "phone";
//    // products JSONArray
//    JSONArray shops = null;
//    int owner = 0;
//    LinearLayout layout;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        setContentView(R.layout.list_view);
//
//        listView = (ListView) findViewById(android.R.id.list);
//        layout = (LinearLayout) findViewById(R.id.LinearLayout);
//        makeHTTPCall();
//
////        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////            @Override
////            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                String shop_id = ((TextView) view.findViewById(R.id.shopID)).getText().toString();
////                Intent in = new Intent(getApplicationContext(), AllGoods.class);
////                in.putExtra(TAG_ID, shop_id);
////                startActivity(in);
////            }
////        });
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu items for use in the action bar
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_main, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onPrepareOptionsMenu (Menu menu) {
//        if (!enable)
//            menu.getItem(1).setEnabled(false);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        switch (id) {
//            case R.id.action_add_shop:
//                Intent addShop = new Intent(this, AddShop.class);
//                addShop.putExtra("owner", owner);
//                startActivity(addShop);
//                finish();
//                startActivity(getIntent());
//                break;
//            case R.id.action_add_goods:
//                Intent i = new Intent(this, AddGoods.class);
//                i.putStringArrayListExtra("shops", shopList);
//                startActivity(i);
//                break;
//            case R.id.action_reload:
//                finish();
//                startActivity(getIntent());
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    class LoadAllShops extends AsyncTask<String, String, String> {
//
//        int success;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pDialog = new ProgressDialog(AllShopListActivity.this);
//            pDialog.setMessage("Loading products. Please wait...");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(false);
//            pDialog.show();
//        }
//
//        protected String doInBackground(String... args) {
//            // Building Parameters
//            List<NameValuePair> params = new ArrayList<>();
//            params.add(new BasicNameValuePair("owner", String.valueOf(owner)));
//
//            // getting JSON string from URL
//            JSONObject json = jsonParser.makeHttpRequest(url_all_shops, "GET", params);
//
//            // Check your log cat for JSON response
//            Log.d("All Products: ", json.toString());
//            try {
//                // Checking for SUCCESS TAG
//                success = json.getInt(TAG_SUCCESS);
//                if (success == 1) {
//                    // products found
//                    // Getting Array of Products
//                    shops = json.getJSONArray(TAG_SHOPS);
//                    // looping through All Products
//                    for (int i = 0; i < shops.length(); i++) {
//                        JSONObject c = shops.getJSONObject(i);
//                        String name = c.getString(TAG_NAME);
//                        shopList.add(name);
//
//                        id[i] = c.getLong(TAG_ID);
//                        title[i] = c.getString(TAG_NAME);
//                        address[i] = c.getString(TAG_ADDRESS);
//                        phone[i] = c.getString(TAG_PHONE);
//                        coords[i] = c.getString(TAG_COORDS);
//                    }
//                } else { Log.d("MY_LOG", "No Data"); }
//            } catch (JSONException e) {e.printStackTrace();}
//            return null;
//        }
//
//        protected void onPostExecute(String file_url) {
//            pDialog.dismiss();
//            if (success != 1) {
//                TextView textView = new TextView(AllShopListActivity.this);
//                textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//                textView.setText(getString(R.string.no_shop));
//                textView.setGravity(Gravity.CENTER);
//                layout.addView(textView);
//                enable = false;
////                Toast.makeText(AllShops.this, "Shop list is empty", Toast.LENGTH_LONG).show();
//            }
//
//            // updating UI from Background Thread
//            runOnUiThread(new Runnable() {
//                public void run() {
//                    listView.setAdapter(new MyAdapter(AllShopListActivity.this, id, title, address, phone, img));
//                }
//            });
//        }
//    }
//
//    public void makeHTTPCall() {
//        pDialog = new ProgressDialog(AllShopListActivity.this);
//        pDialog.setMessage("Please wait...");
//        pDialog.show();
//        AsyncHttpClient client = new AsyncHttpClient();
//        client.post(url_all_shops, request, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                pDialog.dismiss();
//                new LoadAllShops().execute();
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                TextView textView = new TextView(AllShopListActivity.this);
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
//}
