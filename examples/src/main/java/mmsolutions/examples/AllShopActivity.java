package mmsolutions.examples;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AllShopActivity extends Activity {
    private static final String TAG_SUCCESS = "success";

    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;
    private static String url = "http://192.168.88.243/list.php";
    ListView listView;
    JSONArray shops = null;

    String[] title = new String[]{};
    String[] address = new String[]{};
    long[] _id = new long[]{};

    int[] img = {android.R.drawable.ic_delete, android.R.drawable.btn_star, android.R.drawable.btn_radio,
            android.R.drawable.picture_frame, android.R.drawable.ic_menu_add};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_activity_main);

        listView = (ListView) findViewById(R.id.listViewSMS);
        new LoadAllShops().execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String pid = ((TextView) view.findViewById(R.id.tvID)).getText().toString();
                Log.d("LOG_TAG", "itemClick: position = " + position + ", id = " + pid);
            }
        });
    }

    class LoadAllShops extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AllShopActivity.this);
            pDialog.setMessage("Loading products. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
//            pDialog.show();
        }

        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jsonParser.makeHttpRequest(url, "GET", params);

            // Check your log cat for JSON response
            Log.d("All Products: ", json.toString());
            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    shops = json.getJSONArray("shop");
                    // looping through All Products
                    for (int i = 0; i < shops.length(); i++) {
                        JSONObject c = shops.getJSONObject(i);

                        // Storing each json item in variable
                        _id[i] = c.getLong("id");
                        title[i] = c.getString("title");
                        address[i] = c.getString("address");
                    }
                } else { Log.d("MY_LOG", "No Data"); }
            } catch (JSONException e) {e.printStackTrace();}
            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    listView.setAdapter(new yourAdapter(AllShopActivity.this, _id, title, address));
                }
            });
        }
    }
}
