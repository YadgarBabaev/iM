package mmsolutions.im;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class JSONParser {
    static String response = "";
    JSONObject jObj;
    // constructor
    public JSONParser() {}
    // function get json from url
    // by making HTTP POST or GET method
    public JSONObject makeHttpRequest(String url, String method, MultipartEntity entity, String token){
        // Making HTTP request
        try {
            // check for request method
            if(method.equals("POST")){
                HttpClient httpClient = new DefaultHttpClient();
                httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
                httpClient.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8");
                HttpPost httpPost = new HttpPost(url);
                httpPost.addHeader("TOKEN", token);
                httpPost.setHeader("enctype", "application/x-www-form-urlencoded");
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
                httpPost.setEntity(entity);
                HttpResponse httpResponse = httpClient.execute(httpPost);
//                if(httpResponse.getStatusLine().getStatusCode() != 200){return null;}
                HttpEntity httpEntity = httpResponse.getEntity();
                response = EntityUtils.toString(httpEntity);
                jObj = new JSONObject(response);
                httpClient.getConnectionManager().shutdown();
            }
        } catch (IOException | JSONException e) {e.printStackTrace();}
        return jObj;
    }
}