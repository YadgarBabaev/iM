package mmsolutions.im;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.entity.mime.MultipartEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class sdafafhb extends Activity{

    ProgressDialog pDialog;
    TextView text;
    ListView list;
    Activity activity;
    JSONParser jParser = new JSONParser();
    LazyImageLoadAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        pDialog = new ProgressDialog(this);
        activity = this;
        list = (ListView)findViewById(R.id.list);

        new  LoadAllGoods().execute();
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
        boolean success = false;
        List<Integer> id = new ArrayList<>();
        List<String> img = new ArrayList<>();
        List<String> logo = new ArrayList<>();
        List<String> title = new ArrayList<>();
        List<String> desc = new ArrayList<>();
        List<String> price = new ArrayList<>();
        List<Boolean> ctrl = new ArrayList<>();
        protected String doInBackground(String... args) {
            String url = "http://85.113.17.196:92/rest/goods/list";
            JSONObject json = jParser.makeHttpRequest(url, "POST", new MultipartEntity(), "");
            try {
                JSONArray jArray = json.getJSONArray("goods");
                if(jArray.length() > 0) success = true;
                for(int i=0; i<jArray.length(); i++)
                {
                    JSONObject object = jArray.getJSONObject(i);
                    Log.d("All GOODS: ", object.toString());
                    JSONObject shop = object.getJSONObject("shop");
                    id.add(object.getInt("id"));
                    logo.add(shop.getJSONObject("logo").getString("providerReference"));
                    title.add(object.getString("title"));
                    desc.add(object.getString("text"));
                    price.add(object.getString("price"));
                    ctrl.add(!object.getBoolean("controlPrice"));

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
//                adapter = new LazyImageLoadAdapter(activity, id, img, logo, title, desc, price, ctrl);
                list.setAdapter(adapter);
            }
        }
    }

    @Override
    public void onDestroy() {
        // Remove adapter refference from list
        list.setAdapter(null);
        super.onDestroy();
    }
}