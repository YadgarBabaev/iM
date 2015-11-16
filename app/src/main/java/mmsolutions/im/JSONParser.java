package mmsolutions.im;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class JSONParser {
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    // constructor
    public JSONParser() {}

    // function get json from url
    // by making HTTP POST or GET method
    public JSONObject makeHttpRequest(String url, String method, List<NameValuePair> args, String token){
        // Making HTTP request
        try {
            // check for request method
            if(method.equals("POST")){
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);

//                httpPost.addHeader("TOKEN", token);
//                MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
//                for (int index = 0; index < args.size(); index++) {
//                    if (args.get(index).getName().equalsIgnoreCase("image")) {
//                        // If the key equals to "image", we use FileBody to transfer the data
//                        entity.addPart(args.get(index).getName(), new FileBody(new File(args.get(index).getValue())));
//                    } else {
//                        // Normal string data
//                        entity.addPart(args.get(index).getName(), new StringBody(args.get(index).getValue()));
//                    }
//                }
//                httpPost.setEntity(entity);
                httpPost.setEntity(new UrlEncodedFormEntity(args));
                HttpResponse httpResponse = httpClient.execute(httpPost);
//                if(httpResponse.getStatusLine().getStatusCode() != 200){return null;}
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }
        } catch (IOException e) {e.printStackTrace();}

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {Log.e("Buffer Error", "Error converting result " + e.toString());}

        // try parse the string to a JSON object
        try {jObj = new JSONObject(json);
        } catch (JSONException e) {Log.e("JSON Parser", "Error parsing data " + e.toString());}
        return jObj;
    }
}