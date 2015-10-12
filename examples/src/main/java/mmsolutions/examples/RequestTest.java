package mmsolutions.examples;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RequestTest extends Activity {
    JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";
    String LOG_TAG = "MY_LOG";
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        pDialog = new ProgressDialog(this);
        classExecuter();
    }

    class NewShop extends AsyncTask<String, String, String> {

        String title = "name";
        String phone = "phone";
        int success = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Request processing");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<>();
            String sql = String.format("INSERT INTO shop(title, phone_number) VALUES ('%1$s', '%2$s');", title, phone);

            //"DELETE FROM shop WHERE shop_id = ";
            params.add(new BasicNameValuePair("type", "set"));
            params.add(new BasicNameValuePair("sql", sql));

            String url = "http://192.168.88.149/im/sql_handler.php"; //url to send
            JSONObject json = jsonParser.makeHttpRequest(url, "POST", params);

            Log.d(LOG_TAG, "JSON: " + json);

            // check logcat for response
            if (json != null) {
                try {
                    success = json.getInt(TAG_SUCCESS);
                    Log.d("LOG_TAG", json.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if (success != 1) {
                Log.d(LOG_TAG, "Failed to add shop..." );
            } else Log.d(LOG_TAG, "RequestStatus:200");
        }
    }

    void classExecuter(){new NewShop().execute();}
}