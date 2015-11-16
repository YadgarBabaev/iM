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
//import android.widget.Toast;
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
//public class Registration extends Activity {
//
//    JSONParser jsonParser = new JSONParser();
//    private static final String TAG_SUCCESS = "success";
//    private static final String TAG_MSG = "message";
//    private static final String TAG_ID = "id";
//    String LOG_TAG = "MY_LOG";
//    ProgressDialog pDialog;
//    EditText etLogin, etEmail, etPass, etPass2;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.registration);
//
//        etLogin = (EditText)findViewById(R.id.etLogin);
//        etEmail = (EditText)findViewById(R.id.etEmail);
//        etPass = (EditText)findViewById(R.id.etPass);
//        etPass2 = (EditText)findViewById(R.id.etPass2);
//
//        TextView textView = (TextView)findViewById(R.id.btnNew);
//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(check()){
//                    new Create().execute();
//                }
//            }
//        });
//    }
//    class Create extends AsyncTask<String, String, String> {
//
//        int success = 0, user_id;
//        String msg;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pDialog = new ProgressDialog(Registration.this, R.style.MyColorDialogTheme);
//            pDialog.setMessage("Processing request. Please wait...");
//            pDialog.setIndeterminate(true);
//            pDialog.setCancelable(true);
//            pDialog.show();
//        }
//
//        @Override
//        protected String doInBackground(String... args) {
//            String login = String.valueOf(etLogin.getText());
//            String email = String.valueOf(etEmail.getText());
//            String pass = String.valueOf(etPass.getText());
//
//            List<NameValuePair> params = new ArrayList<>();
//            params.add(new BasicNameValuePair("login", login));
//            params.add(new BasicNameValuePair("email", email));
//            params.add(new BasicNameValuePair("password", pass));
//
//            Log.d("MY_LOG", params.toString());
//
//            String url = "http://192.168.88.143/im/new_user.php";
//            JSONObject json = jsonParser.makeHttpRequest(url, "POST", params);
//
//            Log.d(LOG_TAG, "jsonParser: " + jsonParser.toString());
//            Log.d(LOG_TAG, "JSON: " + json);
//
//            // check logcat for response
//            if(json != null){
//                try {
//                    user_id = json.getInt(TAG_ID);
//                    success = json.getInt(TAG_SUCCESS);
//                    msg = json.getString(TAG_MSG);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String file_url) {
//            pDialog.dismiss();
//            if (success != 1) {
//                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
//                Log.d(LOG_TAG, "Failed to send data...");}
//            else {
//                Intent i = new Intent(getApplicationContext(), AllShops.class);
//                i.putExtra("owner", String.valueOf(user_id));
//                startActivity(i);
//                finish();
//                Log.d(LOG_TAG, "User created - " + user_id);
//            }
//        }
//    }
//
//    boolean check(){
//        String login = etLogin.getText().toString();
//        String email = etEmail.getText().toString();
//        String pass = etPass.getText().toString();
//        String pass2 = etPass2.getText().toString();
//
//        if(login.equals("") | email.equals("") | pass.equals("")) {
//            Log.d(LOG_TAG, "blank fields");
//            if(login.equals("")){etLogin.setError("????????? ???????????? ????");}
//            if(email.equals("")){etEmail.setError("????????? ???????????? ????");}
//            if(pass.equals("")){etPass.setError("????????? ???????????? ????");}
//            if(pass2.equals("")){etPass2.setError("????????? ???????????? ????");}
//            return false;
//        }
//        else {
//            if(pass.length()<6) {
//                etPass.setError("??????? ???????? ??????");
//                return false;
//            }
//            if(!pass.equals(pass2)){
//                etPass2.setError("?????? ?? ?????????");
//                return false;
//            }
//            else return true;
//        }
//    }
//}