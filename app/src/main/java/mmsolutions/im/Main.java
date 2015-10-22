package mmsolutions.im;
//
import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.os.AsyncTask;
import android.os.Bundle;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListAdapter;
//import android.widget.SimpleAdapter;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.loopj.android.http.AsyncHttpClient;
//import com.loopj.android.http.AsyncHttpResponseHandler;
//import com.loopj.android.http.RequestParams;
//
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import mmsolutions.im.SignIn.MainPage;
//
public class Main extends Activity {
//
//    JSONParser jParser = new JSONParser();
//    RequestParams request = new RequestParams();
//
//    private ProgressDialog pDialog;
//    private static final String TAG_SUCCESS = "success";
//    private static final String TAG_ID = "id";
//    TextView text;
//    String url, shop_id;

    ImageButton imageButton, button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_view);

        imageButton = (ImageButton)findViewById(R.id.menu_open);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.menu).setVisibility(View.VISIBLE);
                imageButton.setVisibility(View.INVISIBLE);
            }
        });
        button = (ImageButton)findViewById(R.id.menu_close);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.menu).setVisibility(View.INVISIBLE);
                imageButton.setVisibility(View.VISIBLE);
            }
        });

//        url = getString(R.string.sql_handler); //url to send
//        pDialog = new ProgressDialog(Main.this);
//
//        text = (TextView)findViewById(R.id.text_message);
//
//        ImageButton imageButton = (ImageButton)findViewById(R.id.show_map);
//        imageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                text.setVisibility(View.VISIBLE);
////                startActivity(new Intent(getApplicationContext(), ShowWay.class));
//            }
//        });
//
//        Intent i = getIntent();
//        shop_id = i.getStringExtra(TAG_ID);
//
////        ImageManager man = new ImageManager();
////
////        ImageView i1 = (ImageView) findViewById(R.id.i1);
////        ImageView i2 = (ImageView) findViewById(R.id.i2);
////        ImageView i3 = (ImageView) findViewById(R.id.i3);
////
////        // 1 week
////        man.fetchImage(this, 3600*24*7, "http://auca.esy.es/images/1.jpg", i1);
////        man.fetchImage(this, 3600*168, "http://habrastorage.org/storage1/9042dd3c/acc1f8b3/782ca380/c05ecaf3.png", i2);
////        man.fetchImage(this, 604800, "http://files4.adme.ru/files/news/part_104/1041660/preview-650x390-650-1442231392.jpg", i3);
//
////        final TextView textMyFont = (TextView)findViewById(R.id.text);
////        textMyFont.setTypeface(Typeface.createFromAsset(getAssets(), "7fonts.ru_eurof56.ttf"));
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu items for use in the action bar
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        switch (id) {
//            case R.id.action_delete_shop:
//                makeHTTPCall();
//                break;
//            default: break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
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
//            params.add(new BasicNameValuePair("type", "set"));
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
//                startActivity(new Intent(Main.this, AllShops.class));
//                finish();
//            }
//        }
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
//                new DeleteShop().execute();
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
//                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();}
//                // When Http response code is '500'
//                else if (statusCode == 500) {Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();}
//                // When Http response code other than 404, 500
//                else {Toast.makeText(getApplicationContext(), "Error Occurred \n Most Common Error: \n1. Device not connected to Internet\n" +
//                        "2. Web App is not deployed in App server\n3. App server is not running\n HTTP Status code : " + statusCode, Toast.LENGTH_LONG).show();}
//            }
//        });
//    }
}