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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AllGoods extends ListActivity {

    private ProgressDialog pDialog;
    String shop_id;
    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> productsList;

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_GOODS = "goods";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    // products JSONArray
    JSONArray data = null;
    LinearLayout layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_for_goods);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // HashMap for ListView
        productsList = new ArrayList<HashMap<String, String>>();
        layout = (LinearLayout) findViewById(R.id.LinearLayout);

//        Intent i = getIntent();
//        shop_id = i.getStringExtra(TAG_ID);
        shop_id = "1";

        // Loading products in Background Thread
        // Get ListView
        ListView lv = getListView();
        new LoadAllGoods().execute();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String _id = ((TextView) view.findViewById(R.id.goodsID)).getText().toString();
                Toast.makeText(getApplicationContext(), _id, Toast.LENGTH_SHORT).show();

                // Starting new intent
//                Intent in = new Intent(getApplicationContext(), EditProduct.class);
//                // sending pid to next activity
//                in.putExtra(TAG_PID, pid);
//
//                // starting new activity and expecting some response back
//                startActivity(in);
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

    class LoadAllGoods extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AllGoods.this);
            pDialog.setMessage("Loading... Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        int success;

        protected String doInBackground(String... args) {

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id", shop_id));
            JSONObject json = jParser.makeHttpRequest(getString(R.string.url) + "get_all_goods.php", "GET", params);

            // Check your log cat for JSON reponse
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

                        // Storing each json item in variable
                        int id = c.getInt(TAG_ID);
                        String name = c.getString(TAG_NAME);
                        Log.d("GOODS:", id + ": " + name);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_ID, String.valueOf(id));
                        map.put(TAG_NAME, name);

                        // adding HashList to ArrayList
                        productsList.add(map);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            if (success != 1) {
                TextView textView = new TextView(AllGoods.this);
                textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                textView.setText(getString(R.string.no_goods));
                textView.setGravity(Gravity.CENTER);
                layout.addView(textView);
            }

            runOnUiThread(new Runnable() {
                public void run() {
                    ListAdapter adapter = new SimpleAdapter(
                            AllGoods.this, productsList,
                            R.layout.list_item_for_goods, new String[]{TAG_ID, TAG_NAME},
                            new int[]{R.id.goodsID, R.id.goods_name});
                    // updating ListView
                    setListAdapter(adapter);
                }
            });

        }
    }

    public boolean isConnectedToServer(String url, int timeout) {
        try{
            URL myUrl = new URL(url);
            URLConnection connection = myUrl.openConnection();
            connection.setConnectTimeout(timeout);
            connection.connect();
            return true;
        } catch (Exception e) {
            // Handle your exceptions
            return false;
        }
    }

    public void deleteShop(){
        AlertDialog.Builder builder = new AlertDialog.Builder(AllGoods.this);
        builder.setTitle("Удалить магазин?")
                .setMessage("Все товары тоже будут удалены!")
                .setIcon(android.R.drawable.ic_menu_delete)
                .setCancelable(false)
                .setPositiveButton("I\'m agree", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick (DialogInterface dialog,int id){
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
