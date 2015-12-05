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

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class EditShop extends Activity {

    JSONParser jParser = new JSONParser();
    private static final String TAG_ID = "id";
    private static final String TAG_TITLE = "title";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_PHONE = "phone_number";
    private static final String TAG_LAT = "latitude";
    private static final String TAG_LNG = "longitude";
    private static final String TAG_DESCRIPTION = "description";
    private static int RESULT_LOAD_IMAGE = 11;
    private static int RESULT_LOAD_LOGO = 12;

    String picturePath, logoPath, authToken, id;
    ImageButton dltButton;
    ProgressDialog pDialog;
    ImageView cover, logo;
    LinearLayout layout;
    double lat, lng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop);
        pDialog = new ProgressDialog(this);

        Button add_shop_btn = (Button) findViewById(R.id.btnOK);
        ImageButton getLatLng = (ImageButton) findViewById(R.id.getLatLng);
        ImageButton addButton = (ImageButton)findViewById(R.id.addImage);
        dltButton = (ImageButton)findViewById(R.id.deleteImage);
        cover = (ImageView)findViewById(R.id.cover);
        logo = (ImageView)findViewById(R.id.shopLogo);
        layout = (LinearLayout) findViewById(R.id.LinearLayout);

        add_shop_btn.setText("Edit");

        Intent i = getIntent();
        id = i.getStringExtra(TAG_ID);
        ((EditText)findViewById(R.id.shopTitle)).setText(i.getStringExtra(TAG_TITLE));
        ((EditText)findViewById(R.id.shopDescription)).setText(i.getStringExtra(TAG_DESCRIPTION));
        ((EditText)findViewById(R.id.shopPhone)).setText(i.getStringExtra(TAG_PHONE));
        ((EditText)findViewById(R.id.shopAddress)).setText(i.getStringExtra(TAG_ADDRESS));
        lat = Double.parseDouble(i.getStringExtra(TAG_LAT));
        lng = Double.parseDouble(i.getStringExtra(TAG_LNG));
        if(i.getStringExtra("token") != null)
            authToken = i.getStringExtra("token");

        getLatLng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditShop.this, Map.class);
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

    class Edit extends AsyncTask<String, String, String> {

        boolean success = false;
        String response = "";
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
                String url = "http://85.113.17.196:92/api/v1/shop/edit/" + id; //url to send
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

                JSONObject json = jParser.makeHttpRequest(url, "POST", mpEntity, authToken);
                response = String.valueOf(json);
                Log.d("RESPONSE", response);
                if(Boolean.parseBoolean(response)) { success = true; }
            } catch (IOException e) {e.printStackTrace();}
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if(!success) {
                Log.d("RESPONSE", response);
            } else { finish(); }
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
        if (picturePath != null && !picturePath.isEmpty() && logoPath != null && !logoPath.isEmpty()) { new Edit().execute(); }
        else { Toast.makeText(getApplicationContext(), "You must select images from gallery before you try to upload", Toast.LENGTH_LONG).show(); }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pDialog != null) {
            pDialog.dismiss();
        }
    }

}