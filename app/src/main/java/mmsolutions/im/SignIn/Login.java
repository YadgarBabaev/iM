//package mmsolutions.im.SignIn;
//
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import mmsolutions.im.AllShops;
//import mmsolutions.im.JSONParser;
//import mmsolutions.im.R;
//
//
//public class Login extends Activity{
//
//    JSONParser jsonParser = new JSONParser();
//    EditText etLogin, etPass;
//    ProgressDialog pDialog;
//    TextView error;
//    private static final String TAG_SUCCESS = "success";
//    private static final String TAG_MSG = "message";
//    String LOG_TAG = "MY_LOG";
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.login);
//
//        etLogin = (EditText)findViewById(R.id.etLogin);
//        etPass = (EditText)findViewById(R.id.etPass);
//        error = (TextView) findViewById(R.id.errorMSG);
//
//        TextView link = (TextView)findViewById(R.id.link);
//        link.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), ForgottenPassword.class));
//            }
//        });
//
//        TextView save = (TextView)findViewById(R.id.btnOK);
//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new LogIn().execute();
//            }
//        });
//    }
//
//    class LogIn extends AsyncTask<String, String, String> {
//
//        int success;
//        String message;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pDialog = new ProgressDialog(Login.this, R.style.MyColorDialogTheme);
//            pDialog.setMessage("Processing request. Please wait...");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(true);
//            pDialog.show();
//        }
//
//        @Override
//        protected String doInBackground(String... args) {
//
//            String login = String.valueOf(etLogin.getText());
//            String pass = String.valueOf(etPass.getText());
//
//            List<NameValuePair> params = new ArrayList<>();
//
//            params.add(new BasicNameValuePair("login", login));
//            params.add(new BasicNameValuePair("password", pass));
//
//
////            params.add(new BasicNameValuePair("_username", login));
////            params.add(new BasicNameValuePair("_password", pass));
////            params.add(new BasicNameValuePair("_submit", "true"));
//
//            Log.d("MY_LOG", params.toString());
//
//            String url = "http://192.168.88.143/im/login.php";
////            String url = "http://85.113.17.196:92/login";
//
//            JSONObject json = jsonParser.makeHttpRequest(url, "POST", params);
//
//            Log.d(LOG_TAG, "jsonParser: " + jsonParser.toString());
//            Log.d(LOG_TAG, "JSON: " + json);
//
//            if(json != null){
//                try {
//                    success = json.getInt(TAG_SUCCESS);
//                    message = json.getString(TAG_MSG);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            Log.d(LOG_TAG, message);
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String file_url) {
//            pDialog.dismiss();
//            if (success != 1) {
//                error.setVisibility(View.VISIBLE);
//                error.setText(message);
//            } else {
//                Intent i = new Intent(getApplicationContext(), AllShops.class);
//                i.putExtra("owner", String.valueOf(message));
//                startActivity(i);
//                finish();
//            }
//        }
//    }
//}