package mmsolutions.im;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

public class MyShopsList extends Activity {

    ProgressDialog pDialog;
    String token;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        Intent i = getIntent();
        token = i.getStringExtra("token");

        pDialog = new ProgressDialog(this);
        new LoadMyShops().execute();
    }

    class LoadMyShops extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Request processing\nPlease wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                String url = "http://85.113.17.196:92/api/v1/my/shop/list"; //url to send
                HttpClient httpclient = new DefaultHttpClient();
                httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
                httpclient.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8");
                HttpPost httppost = new HttpPost(url);
                httppost.addHeader("TOKEN", token);
                httppost.setHeader("enctype", "application/x-www-form-urlencoded");
                MultipartEntity mpEntity = new MultipartEntity();

                httppost.setEntity(mpEntity);
                Log.d("Entity", httppost.toString());
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity resEntity = response.getEntity();
                String responseString = EntityUtils.toString(resEntity);

                JSONArray j = new JSONArray(responseString);
                Log.d("RESPONSE", String.valueOf(j.length()));
                for(int i=0; i<j.length(); i++){
                    Log.d("RESPONSE", j.getJSONObject(i).toString());
                }

                httpclient.getConnectionManager().shutdown();
            } catch (IOException | JSONException e) {e.printStackTrace();}
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            finish();
        }
    }
}
