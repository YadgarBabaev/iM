//package mmsolutions.im;
//
//import android.app.Activity;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Log;
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
//import org.json.JSONException;
//import org.json.JSONObject;
//import java.util.ArrayList;
//import java.util.List;
//
//public class Login extends Activity {
//    String t;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);f(); }
//    @Override
//    protected void onResume() {super.onResume();}
//    @Override
//    protected void onPause() {super.onPause();}
//
//    class Req extends AsyncTask<Void, Integer, String>{
//        @Override
//        protected String doInBackground(Void... args) {
//            // Building Parameters
//            List<NameValuePair> params = new ArrayList<>();
//            String name = "admin";
//            String pass = "admin";
//            String url = "http://85.113.17.196:92/api/v1/users/login";
//
//            params.add(new BasicNameValuePair("username", name));
//            params.add(new BasicNameValuePair("password", pass));
//
//            JSONParser jParser = new JSONParser();
//            JSONObject json = jParser.makeHttpRequest(url, "POST", params);
//            // Check your log cat for JSON response
//            Log.d("All Data: ", json.toString());
//            try {t = json.getString("token");}
//            catch (JSONException e) {e.printStackTrace();}
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String str){ if(t.length()>0) {call(t);} }
//    }
//
//    void f(){new Req().execute();finish();}
//
//    void call(final String token){
//        new AsyncTask<String,String,String>(){
//            @Override
//            protected String doInBackground(String... args) {
//                List<NameValuePair> params = new ArrayList<>();
//                String url = "http://85.113.17.196:92/api/v1/test";
//                params.add(new BasicNameValuePair("token", token));
//                JSONParser jParser = new JSONParser();
//                JSONObject json = jParser.makeHttpRequest(url, "POST", params);
//                // Check your log cat for JSON response
//                Log.d("All Data: ", json.toString());
//                return null;
//            }
//        }.execute();
//    }
//}