package mmsolutions.im;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.Serializable;

import mmsolutions.im.SignIn.AuthenticatorActivity;

import static mmsolutions.im.SignIn.AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS;

public class test extends Activity{

    String authToken, mAuthTokenType;
    ProgressDialog pDialog;
    AccountManager am;
    Account[] accounts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pDialog = new ProgressDialog(this);
        mAuthTokenType = getIntent().getStringExtra("AUTH_TYPE");
        am = AccountManager.get(getBaseContext());
        accounts = am.getAccountsByType(mAuthTokenType);
        getExistingAccountAuthToken();
        new Test().execute();
//        != () @
    }

    class Test extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... args) {
            try {
                String url = "http://85.113.17.196:92/api/"; //url
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.addHeader("TOKEN", authToken);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                String responseString = EntityUtils.toString(httpResponse.getEntity());
                Log.d("RESPONSE", responseString);
                if(httpResponse.getStatusLine().getStatusCode() != 200){
                    Log.d("Error", "code != 200");
                    updateToken();
//                    ParseComError error = new Gson().fromJson(responseString, ParseComError.class);
//                    throw new Exception("Error signing-in ["+error.code+"] - " + error.error);
//                    addNewAccount(AccountGeneral.ACCOUNT_TYPE, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
        }
    }

    private void addNewAccount(String accountType, String authTokenType) {
        final AccountManagerFuture<Bundle> future = am.addAccount(accountType, authTokenType, null, null, this, new AccountManagerCallback<Bundle>() {
            @Override
            public void run(AccountManagerFuture<Bundle> future) {
                try {
                    Bundle bnd = future.getResult();
                    showMessage("Account was created");
                    Log.d("udinic", "AddNewAccount Bundle is " + bnd);

                } catch (Exception e) {
                    e.printStackTrace();
                    showMessage(e.getMessage());
                }
            }
        }, null);
    }

    private void getExistingAccountAuthToken() {
        final AccountManagerFuture<Bundle> future = am.getAuthToken(accounts[0], AUTHTOKEN_TYPE_FULL_ACCESS, null, this, null, null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bundle bnd = future.getResult();
                    authToken = bnd.getString(AccountManager.KEY_AUTHTOKEN);
                    runOnUiThread(new Runnable() {@Override public void run() {Log.d("TOKEN", authToken);}});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void updateToken(){
        final String password = am.getPassword(accounts[0]);
        String accountName = getIntent().getStringExtra("username");
        AuthenticatorActivity authenticatorActivity = new AuthenticatorActivity();
//        authenticatorActivity.submit(accountName, password);
        new Test().execute();
    }

    private void showMessage(final String msg) {
        if (TextUtils.isEmpty(msg))
            return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class ParseComError implements Serializable {int code; String error;}
}