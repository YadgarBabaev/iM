package mmsolutions.im.SignIn;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mmsolutions.im.JSONParser;
import mmsolutions.im.R;

public class ForgottenPassword extends Activity{

    JSONParser jsonParser = new JSONParser();
    EditText etEmail;
    ProgressDialog pDialog;
    TextView error;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MSG = "message";
    String LOG_TAG = "MY_LOG";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotten_pass);


        etEmail = (EditText)findViewById(R.id.etEmail);
        error = (TextView) findViewById(R.id.errorMSG);

        TextView send = (TextView)findViewById(R.id.btnSend);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Send().execute();
            }
        });
    }

    class Send extends AsyncTask<String, String, String> {

        int success;
        String message;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ForgottenPassword.this, R.style.MyColorDialogTheme);
            pDialog.setMessage("Processing request. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            String email = String.valueOf(etEmail.getText());

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("email", email));
            Log.d("MY_LOG", params.toString());

            String url = "http://192.168.88.143/im/sendme_pass.php";
            JSONObject json = jsonParser.makeHttpRequest(url, "POST", params);

            Log.d(LOG_TAG, "jsonParser: " + jsonParser.toString());
            Log.d(LOG_TAG, "JSON: " + json);

            if(json != null){
                try {
                    success = json.getInt(TAG_SUCCESS);
                    message = json.getString(TAG_MSG);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if (success != 1) {
                error.setVisibility(View.VISIBLE);
                error.setText(message);
            } else {
                Toast.makeText(getApplicationContext(),
                        "We sent Your profile data./n Please check your email",
                        Toast.LENGTH_LONG).show();
                Log.d(LOG_TAG, message);
                finish();
            }
        }
    }
}