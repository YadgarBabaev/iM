package mmsolutions.im.SignIn;

import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ParseComServerAuthenticate implements ServerAuthenticate {
    @Override
    public String userSignUp(String name, String email, String pass, String authType) throws Exception {

        Log.d("REGISTRATION", "userSignUp");
        String url = "http://85.113.17.196:92/api/v1/users/registration";
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("enctype", "application/x-www-form-urlencoded");
        List<NameValuePair> args = new ArrayList<>();
        args.add(new BasicNameValuePair("username", name));
        args.add(new BasicNameValuePair("email", email));
        args.add(new BasicNameValuePair("first_password", pass));
        args.add(new BasicNameValuePair("second_password", pass));
//        String user = "{\"username\":\"" + name + "\",\"email\":\"" + email + "\"," +
//                "\"first_password\":\"" + pass + "\",\"second_password\":\"" + pass + "\"}";
//        HttpEntity entity = new StringEntity(user);
        httpPost.setEntity(new UrlEncodedFormEntity(args));
        String authtoken = null;
        try {
            HttpResponse response = httpClient.execute(httpPost);
            String responseString = EntityUtils.toString(response.getEntity());
            if (response.getStatusLine().getStatusCode() != 200) {
                ParseComError error = new Gson().fromJson(responseString, ParseComError.class);
                throw new Exception("Error creating user["+error.code+"] - " + error.error);
            }
            Log.d("Response", responseString);
            User createdUser = new Gson().fromJson(responseString, User.class);
            authtoken = createdUser.sessionToken;
            Log.d("Response", "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return authtoken;
    }

    @Override
    public String userSignIn(String user, String pass, String authType) throws Exception {

        Log.d("LOGIN", "userSignIn");
        String url = "http://85.113.17.196:92/api/v1/users/login";
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("enctype", "application/x-www-form-urlencoded");
        List<NameValuePair> args = new ArrayList<>();
        args.add(new BasicNameValuePair("username", user));
        args.add(new BasicNameValuePair("password", pass));
        httpPost.setEntity(new UrlEncodedFormEntity(args));
//        httpPost.getParams().setParameter("username", user).setParameter("password", pass);
//        String query = null;
//        try {
//            query = String.format("%s=%s&%s=%s", "username", URLEncoder.encode(user, "UTF-8"), "password", pass);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        url += "?" + query;
//        HttpGet httpGet = new HttpGet(url);
//        HttpParams params = new BasicHttpParams();
//        params.setParameter("username", user);
//        params.setParameter("password", pass);
//        httpGet.setParams(params);
        String authtoken = null;
        try {
            HttpResponse response = httpClient.execute(httpPost);
            String responseString = EntityUtils.toString(response.getEntity());
            if (response.getStatusLine().getStatusCode() != 200) {
                ParseComError error = new Gson().fromJson(responseString, ParseComError.class);
                throw new Exception("Error signing-in ["+error.code+"] - " + error.error);
            }
            User loggedUser = new User();
            loggedUser.setSessionToken(new JSONObject(responseString).getString("token"));
            authtoken = loggedUser.getSessionToken();
            Log.d("TOKEN: ", authtoken);
        } catch (IOException e) { e.printStackTrace(); }
        return authtoken;
    }

    private class ParseComError implements Serializable {
        int code;
        String error;
    }
    private class User implements Serializable {

        private String firstName;
        private String lastName;
        private String username;
        private String phone;
        private String objectId;
        public String sessionToken;
        private String gravatarId;
        private String avatarUrl;

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        public String getSessionToken() {
            return sessionToken;
        }

        public void setSessionToken(String sessionToken) {
            this.sessionToken = sessionToken;
        }

        public String getGravatarId() {
            return gravatarId;
        }

        public void setGravatarId(String gravatarId) {
            this.gravatarId = gravatarId;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }
    }
}
