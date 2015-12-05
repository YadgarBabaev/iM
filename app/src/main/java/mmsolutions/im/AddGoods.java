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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.EditText;
//import android.widget.Spinner;
//import android.widget.Switch;
//
//import java.util.ArrayList;
//
public class AddGoods extends Activity{

    Spinner category, subcat, subsubcat;
    ImageButton dltButton; ImageView image;
    private static int RESULT_LOAD_IMAGE = 11;
    DatabaseHandler mdb = new DatabaseHandler(this);
    JSONParser jParser = new JSONParser();
    int category_child, category_grandchild = 0;
    String shop_id, token, picturePath;
    ProgressDialog pDialog;
    int show_price = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_goods);
        pDialog = new ProgressDialog(this);

        Intent i = getIntent();
        token = i.getStringExtra("token");
        shop_id = i.getStringExtra("id");

        ToggleButton tb = (ToggleButton) findViewById(R.id.show_price);
        tb.setChecked(true);
        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(show_price == 0) show_price = 1;
                else show_price = 0;
            }
        });

        category = (Spinner)findViewById(R.id.categories); loadSpinnerData();
        subcat = (Spinner)findViewById(R.id.sub_categories);
        subsubcat = (Spinner)findViewById(R.id.sub_sub_categories);
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String label = parent.getItemAtPosition(position).toString();
                loadSubSpinnerData(subcat, "sub_category", mdb.getId("category", label));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        subcat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String label = parent.getItemAtPosition(position).toString();
                category_child =  mdb.getId("sub_category", label);
                loadSubSpinnerData(subsubcat, "sub_sub_category", category_child);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        subsubcat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String label = parent.getItemAtPosition(position).toString();
                category_grandchild =  mdb.getId("sub_sub_category", label);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        ImageButton addButton = (ImageButton)findViewById(R.id.addImage);
        dltButton = (ImageButton)findViewById(R.id.deleteImage);
        image = (ImageView)findViewById(R.id.image);
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
                image.setImageResource(android.R.color.transparent);
                image.setImageResource(0);
                dltButton.setVisibility(View.INVISIBLE);
                image.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        });

        findViewById(R.id.add_goods).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
//        Spinner forPrice = (Spinner)findViewById(R.id.currency_spinner);
//        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[] {getString(R.string.som), getString(R.string.dollar)});
//        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        forPrice.setAdapter(adapter1);
//
//        Spinner CategorySpinner = (Spinner)findViewById(R.id.categories);
//        ArrayAdapter<?> category_adapter = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
//        category_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        CategorySpinner.setAdapter(category_adapter);
//        CategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Object item = parent.getItemAtPosition(position);
//                category = item.toString();
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
    }

    class NewGoods extends AsyncTask<String, String, String> {

        boolean success = false;
        String response = "";
        String title = ((EditText)findViewById(R.id.goods_name)).getText().toString();
        String description = ((EditText)findViewById(R.id.goods_description)).getText().toString();
        String price = ((EditText)findViewById(R.id.goods_price)).getText().toString();
        String discount = ((EditText)findViewById(R.id.discount)).getText().toString();
        String sizes = ((EditText)findViewById(R.id.sizes)).getText().toString();

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
                String url = "http://85.113.17.196:92/api/v1/goods/add"; //url to send
                MultipartEntity mpEntity = new MultipartEntity();
                Charset charset = Charset.forName("UTF-8");
                mpEntity.addPart("title", new StringBody(title, charset));
                mpEntity.addPart("description", new StringBody(description, charset));
                mpEntity.addPart("price", new StringBody(price));
                mpEntity.addPart("price_control", new StringBody(String.valueOf(show_price)));
                mpEntity.addPart("size", new StringBody(sizes, charset));
                mpEntity.addPart("discount", new StringBody(discount));
                mpEntity.addPart("shop", new StringBody(shop_id));
                mpEntity.addPart("sub_category", new StringBody(String.valueOf(category_child)));
                if(category_grandchild != 0)
                    mpEntity.addPart("sub_sub_category", new StringBody(String.valueOf(category_grandchild)));
                File photo = new File(picturePath);
                mpEntity.addPart("photo", new FileBody(photo));

                Log.d("show_price", String.valueOf(show_price));

                JSONObject json = jParser.makeHttpRequest(url, "POST", mpEntity, token);
                response = String.valueOf(json);
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

            image.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            dltButton.setVisibility(View.VISIBLE);
        }
    }
    public void uploadImage() {
        // When Image is selected from Gallery
        if (picturePath != null && !picturePath.isEmpty()) {
            new NewGoods().execute();
        } else {
            Toast.makeText(getApplicationContext(), "You must select image from gallery before you try to upload", Toast.LENGTH_LONG).show();
        }
    }
    private void loadSpinnerData() {
        // database handler
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        // Spinner Drop down elements
        List<String> lables = db.getAllLabels();
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lables);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        category.setAdapter(dataAdapter);
    }
    private void loadSubSpinnerData(Spinner spinner, String table, int parent) {
        // database handler
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        // Spinner Drop down elements
        List<String> lables = db.getAllLabelsByParent(table, parent);
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lables);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }
}
