package mmsolutions.im;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/*upload image to server
http://programmerguru.com/android-tutorial/how-to-upload-image-to-php-server/ */

public class AddShop extends Activity {
    private static int RESULT_LOAD_IMAGE = 11;
    private static int RESULT_LOAD_LOGO = 12;
    String picturePath, logoPath, authToken;
    ImageButton dltButton;
    ProgressDialog pDialog;
    ImageView cover, logo;
    LinearLayout layout;
    double lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_shop);
        pDialog = new ProgressDialog(this);

        Button add_shop_btn = (Button) findViewById(R.id.btnOK);
        ImageButton getLatLng = (ImageButton) findViewById(R.id.getLatLng);
        ImageButton addButton = (ImageButton)findViewById(R.id.addImage);
        dltButton = (ImageButton)findViewById(R.id.deleteImage);
        cover = (ImageView)findViewById(R.id.cover);
        logo = (ImageView)findViewById(R.id.shopLogo);
        layout = (LinearLayout) findViewById(R.id.LinearLayout);

        Intent i = getIntent();
        authToken = i.getStringExtra("token");

        getLatLng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddShop.this, Map.class);
                i.putExtra("lat", lat);
                i.putExtra("lng", lng);
                startActivityForResult(i, 10);
            }
        });

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_LOGO);
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
                cover.setImageResource(android.R.color.transparent);
                cover.setImageResource(0);
                dltButton.setVisibility(View.INVISIBLE);
                cover.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        });

        add_shop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
    }
    class NewShop extends AsyncTask<String, String, String> {

        String title = ((EditText)findViewById(R.id.shopTitle)).getText().toString();
        String address = ((EditText)findViewById(R.id.shopAddress)).getText().toString();
        String phone = ((EditText)findViewById(R.id.shopPhone)).getText().toString();
        String description = ((EditText)findViewById(R.id.shopDescription)).getText().toString();

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
                String url = "http://85.113.17.196:92/api/v1/shop/add"; //url to send
                HttpClient httpclient = new DefaultHttpClient();
                httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
                HttpPost httppost = new HttpPost(url);
                httppost.addHeader("TOKEN", authToken);
                httppost.setHeader("enctype", "application/x-www-form-urlencoded");

                MultipartEntity mpEntity = new MultipartEntity();
                Charset charset = Charset.forName("UTF-8");

                mpEntity.addPart("title", new StringBody(title, charset));
                mpEntity.addPart("address", new StringBody(address, charset));
                mpEntity.addPart("phone_number", new StringBody(phone, charset));
                mpEntity.addPart("description", new StringBody(description, charset));
                mpEntity.addPart("latitude", new StringBody(String.valueOf(lat)));
                mpEntity.addPart("longitude", new StringBody(String.valueOf(lng)));

                File coverFile = new File(picturePath);
                mpEntity.addPart("cover", new FileBody(coverFile));

                File logoFile = new File(logoPath);
                mpEntity.addPart("logo", new FileBody(logoFile));

                httppost.setEntity(mpEntity);
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity resEntity = response.getEntity();
                String responseString = EntityUtils.toString(resEntity);
                JSONArray j = new JSONArray(responseString);
                Log.d("RESPONSE", j.toString());

                httpclient.getConnectionManager().shutdown();
            } catch (IOException | JSONException e) {e.printStackTrace();}
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            startActivity(new Intent(getApplicationContext(), MyShopsList.class).putExtra("token", authToken));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10 && resultCode == RESULT_OK && data != null) {
            lat = data.getDoubleExtra("Lat", 0);
            lng = data.getDoubleExtra("Lng", 0);
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();

            cover.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            dltButton.setVisibility(View.VISIBLE);
        }
        if (requestCode == RESULT_LOAD_LOGO && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            logoPath = cursor.getString(columnIndex);
            cursor.close();
            logo.setImageBitmap(BitmapFactory.decodeFile(logoPath));
        }
    }

    public void uploadImage() {
        // When Image is selected from Gallery
        if (picturePath != null && !picturePath.isEmpty() && logoPath != null && !logoPath.isEmpty()) {
//            pDialog.setMessage("Converting Image to Binary Data");
//            pDialog.show();
            new NewShop().execute();
        } else {
            Toast.makeText(getApplicationContext(), "You must select images from gallery before you try to upload", Toast.LENGTH_LONG).show();
        }
    }

//    String encodeImage(ImageView iv){
//        iv.buildDrawingCache();
//        Bitmap bm = iv.getDrawingCache();
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);
//        byte[] b = baos.toByteArray();
//        return Base64.encodeToString(b , Base64.DEFAULT);
//    }
//
//    public void encodeImageToString() {
//        new AsyncTask<Void, Void, String>() {
//            protected void onPreExecute() {}
//
//            @Override
//            protected String doInBackground(Void... args) {
//                BitmapFactory.Options options;
//                options = new BitmapFactory.Options();
//                options.inSampleSize = 3;
//
//                bitmap = BitmapFactory.decodeFile(picturePath, options);
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                // Must compress the Image to reduce image size to make upload easy
//                bitmap.compress(Bitmap.CompressFormat.PNG, 70, stream);
//                byte[] byte_arr = stream.toByteArray();
//                // Encode Image to String
//                encodedString = Base64.encodeToString(byte_arr, 0);
//                return "";
//            }
//
//            @Override
//            protected void onPostExecute(String msg) {
//                pDialog.setMessage("Calling Upload");
//                // Put converted Image string into Async Http Post parama
//                img.put("image", encodedString);
//                img.put("filename", name.getText().toString() + ".png");
//                // Trigger Image upload
//                makeHTTPCall();
//            }
//        }.execute(null, null, null);
//    }
//
//    // Make Http call to upload Image to Php server
//    public void makeHTTPCall() {
//        pDialog.setMessage("Invoking Php");
//        AsyncHttpClient client = new AsyncHttpClient();
//        // Don't forget to change the IP address to your LAN address. Port no as well.
//        client.post(getString(R.string.url) + "upload_images.php", img, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
//                pDialog.hide();
//                Intent returnIntent = new Intent();
//                setResult(RESULT_OK, returnIntent);
//                finish();
//            }
//
//            @Override
//            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
//                pDialog.hide();
//                // When Http response code is '404'
//                if (statusCode == 404) {
//                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
//                }
//                // When Http response code is '500'
//                else if (statusCode == 500) {
//                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(getApplicationContext(), "Error Occurred \n Most Common Error: \n1. Device not connected to Internet\n" +
//                            "2. Web App is not deployed in App server\n3. App server is not running\n HTTP Status code : " + statusCode, Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//    }
//
//    public void connection() {
//        pDialog.setMessage("Connecting to server...");
//        pDialog.show();
//        AsyncHttpClient client = new AsyncHttpClient();
//        client.post("http://85.113.17.196:92/api/v1/shop/add", request, new AsyncHttpResponseHandler() {
//
//            @Override
//            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
//                pDialog.dismiss();
//                uploadImage();
//            }
//
//            @Override
//            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
//                TextView textView = new TextView(AddShop.this);
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

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stubmakeText
        super.onDestroy();
        // Dismiss the progress bar when application is closed
        if (pDialog != null) {
            pDialog.dismiss();
        }
    }

//    private void getExistingAccountAuthToken() {
//        String mAuthTokenType = getIntent().getStringExtra("AUTH_TYPE");
//        AccountManager am = AccountManager.get(getBaseContext());
//        Account[] accounts = am.getAccountsByType(mAuthTokenType);
//        final AccountManagerFuture<Bundle> future = am.getAuthToken(accounts[0], AUTHTOKEN_TYPE_FULL_ACCESS, null, this, null, null);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Bundle bnd = future.getResult();
//                    authToken = bnd.getString(AccountManager.KEY_AUTHTOKEN);
//                    runOnUiThread(new Runnable() {@Override public void run() {Log.d("TOKEN", authToken);}});
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }
}
