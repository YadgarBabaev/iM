package mmsolutions.im;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/*upload image to server
http://programmerguru.com/android-tutorial/how-to-upload-image-to-php-server/ */

public class AddShop extends Activity {
    JSONParser jsonParser = new JSONParser();
    RequestParams img = new RequestParams();
    RequestParams request = new RequestParams();
    private static final String TAG_SUCCESS = "success";
    private static int RESULT_LOAD_IMAGE = 11;
    String LOG_TAG = "MY_LOG", picturePath, owner;
    String lat = "", lng = "", encodedString;
    ImageButton LatLng, dltButton, addButton;
    EditText name, phone, address, descrip;
    ProgressDialog pDialog;
    ImageView imageView;
    Button add_shop;
    Bitmap bitmap;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_shop);
        pDialog = new ProgressDialog(this);

        name = (EditText) findViewById(R.id.etShopName);
        phone = (EditText) findViewById(R.id.etPhone);
        address = (EditText) findViewById(R.id.etShopAddress);
        descrip = (EditText) findViewById(R.id.etDescription);
        add_shop = (Button) findViewById(R.id.btnOK);
        LatLng = (ImageButton) findViewById(R.id.fillLatLng);
        imageView = (ImageView)findViewById(R.id.image);
        dltButton = (ImageButton)findViewById(R.id.deleteImage);
        addButton = (ImageButton)findViewById(R.id.addImage);
        layout = (LinearLayout) findViewById(R.id.LinearLayout);

        Intent i = getIntent();
        owner = i.getStringExtra("owner");

        LatLng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddShop.this, Map.class);
                i.putExtra("lat", lat);
                i.putExtra("lng", lng);
                startActivityForResult(i, 10);
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
        dltButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageResource(android.R.color.transparent);
                imageView.setImageResource(0);
                dltButton.setVisibility(View.INVISIBLE);
                imageView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        });
        add_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connection();
            }
        });
    }

    class NewShop extends AsyncTask<String, String, String> {

        String title = name.getText().toString();
        String number = phone.getText().toString();
        String adrs = address.getText().toString();
        String desc = descrip.getText().toString();
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
            String sql = String.format("INSERT INTO shop(title, address, phone_number, description, latitude, longitude, user_id) " +
                    "VALUES ('%1$s', '%2$s', '%3$s', '%4$s', '%5$s', '%6$s', '%7$s');", title, adrs, number, desc, lat, lng, owner);

            //"DELETE FROM shop WHERE id = ";
            params.add(new BasicNameValuePair("type", "set"));
            params.add(new BasicNameValuePair("sql", sql));

            Log.d("MY_LOG", params.toString());

            String url = getString(R.string.sql_handler); //url to send
            JSONObject json = jsonParser.makeHttpRequest(url, "POST", params);

            Log.d(LOG_TAG, "jsonParser: " + jsonParser.toString());
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
            } else encodeImageToString();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10 && resultCode == RESULT_OK && data != null) {
            lat = data.getStringExtra("Lat");
            lng = data.getStringExtra("Lng");
        }

        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                picturePath = cursor.getString(columnIndex);
                cursor.close();

                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                dltButton.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    public void uploadImage() {
        // When Image is selected from Gallery
        if (picturePath != null && !picturePath.isEmpty()) {
            pDialog.setMessage("Converting Image to Binary Data");
            pDialog.show();
            new NewShop().execute();
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "You must select image from gallery before you try to upload",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void encodeImageToString() {
        new AsyncTask<Void, Void, String>() {
            protected void onPreExecute() {}

            @Override
            protected String doInBackground(Void... args) {
                BitmapFactory.Options options;
                options = new BitmapFactory.Options();
                options.inSampleSize = 3;

                bitmap = BitmapFactory.decodeFile(picturePath, options);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload easy
                bitmap.compress(Bitmap.CompressFormat.PNG, 70, stream);
                byte[] byte_arr = stream.toByteArray();
                // Encode Image to String
                encodedString = Base64.encodeToString(byte_arr, 0);
                return "";
            }

            @Override
            protected void onPostExecute(String msg) {
                pDialog.setMessage("Calling Upload");
                // Put converted Image string into Async Http Post param
                img.put("image", encodedString);
                img.put("filename", name.getText().toString() + ".png");
                // Trigger Image upload
                makeHTTPCall();
            }
        }.execute(null, null, null);
    }

    // Make Http call to upload Image to Php server
    public void makeHTTPCall() {
        pDialog.setMessage("Invoking Php");
        AsyncHttpClient client = new AsyncHttpClient();
        // Don't forget to change the IP address to your LAN address. Port no as well.
        client.post(getString(R.string.url) + "upload_images.php", img, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                pDialog.hide();
                Intent returnIntent = new Intent();
                setResult(RESULT_OK, returnIntent);
                finish();
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                pDialog.hide();
                // When Http response code is '404'
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Error Occurred \n Most Common Error: \n1. Device not connected to Internet\n" +
                            "2. Web App is not deployed in App server\n3. App server is not running\n HTTP Status code : " + statusCode, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void connection() {
        pDialog.setMessage("Connecting to server...");
        pDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(getString(R.string.sql_handler), request, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                pDialog.dismiss();
                uploadImage();
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                TextView textView = new TextView(AddShop.this);
                textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                textView.setText(getString(R.string.no_connection));
                textView.setGravity(Gravity.CENTER);
                layout.addView(textView);
                pDialog.dismiss();

                // When Http response code is '404'
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();}
                // When Http response code is '500'
                else if (statusCode == 500) {Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();}
                // When Http response code other than 404, 500
                else {Toast.makeText(getApplicationContext(), "Error Occurred \n Most Common Error: \n1. Device not connected to Internet\n" +
                        "2. Web App is not deployed in App server\n3. App server is not running\n HTTP Status code : " + statusCode, Toast.LENGTH_LONG).show();}
            }
        });
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        // Dismiss the progress bar when application is closed
        if (pDialog != null) {
            pDialog.dismiss();
        }
    }
}