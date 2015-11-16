package mmsolutions.im.SignIn;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

import mmsolutions.im.AddShop;
import mmsolutions.im.R;

import static mmsolutions.im.SignIn.AccountGeneral.sServerAuthenticate;

public class AuthenticatorActivity extends AccountAuthenticatorActivity {

    public final static String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public final static String ARG_AUTH_TYPE = "AUTH_TYPE";
    public final static String ARG_ACCOUNT_NAME = "username";
    public final static String ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT";
    public static final String KEY_ERROR_MESSAGE = "ERR_MSG";
    public final static String PARAM_USER_PASS = "password";
    private final String TAG = this.getClass().getSimpleName();

    private AccountManager mAccountManager;
    private String mAuthTokenType;
    EditText etName, etPass;
    ProgressDialog pDialog;
    TextView error;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mAccountManager = AccountManager.get(getBaseContext());

        etName = (EditText)findViewById(R.id.etLogin);
        etPass = (EditText)findViewById(R.id.etPass);
        error = (TextView) findViewById(R.id.errorMSG);

        String accountName = getIntent().getStringExtra(ARG_ACCOUNT_NAME);
        mAuthTokenType = getIntent().getStringExtra(ARG_AUTH_TYPE);
        if (mAuthTokenType == null) mAuthTokenType = AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS;
        if (accountName != null) {etName.setText(accountName);
        }

        submit();
//        findViewById(R.id.btnOK).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {submit();}
//        });
//        TextView link = (TextView)findViewById(R.id.link);
//        link.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), ForgottenPassword.class));
//            }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // The sign up activity returned that the user has successfully created an account
        int REQ_SIGNUP = 1;
        if (requestCode == REQ_SIGNUP && resultCode == RESULT_OK) {
            finishLogin(data);
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    public void submit() {
//        final String userName = etName.getText().toString();
//        final String userPass = etPass.getText().toString();
//        TODO:
        final String userName = "android";
        final String userPass = "12345";
        final String accountType = getIntent().getStringExtra(ARG_ACCOUNT_TYPE);
        new AsyncTask<String, Void, Intent>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(AuthenticatorActivity.this, R.style.MyColorDialogTheme);
                pDialog.setMessage("Processing request. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
            }

            @Override
            protected Intent doInBackground(String... params) {
                Log.d("SIGN IN", TAG + "> Started authenticating");
                String authtoken;
                Bundle data = new Bundle();
                try {
                    authtoken = sServerAuthenticate.userSignIn(userName, userPass, mAuthTokenType);
                    data.putString(AccountManager.KEY_ACCOUNT_NAME, userName);
                    data.putString(AccountManager.KEY_ACCOUNT_TYPE, accountType);
                    data.putString(AccountManager.KEY_AUTHTOKEN, authtoken);
                    data.putString(PARAM_USER_PASS, userPass);

                } catch (Exception e) {
                    data.putString(KEY_ERROR_MESSAGE, e.getMessage());
                }
                final Intent res = new Intent();
                res.putExtras(data);
                Log.d("Data", data.toString());
                return res;
            }

            @Override
            protected void onPostExecute(Intent intent) {
                if (intent.hasExtra(KEY_ERROR_MESSAGE)) {
                    Toast.makeText(getBaseContext(), "Authorization error", Toast.LENGTH_SHORT).show();
                    Log.d("Authorization", intent.getStringExtra(KEY_ERROR_MESSAGE));
                } else {
                    finishLogin(intent);
                }
            }
        }.execute();
    }

    private void finishLogin(Intent intent) {
        Log.d("SIGN IN", TAG + "> finishLogin");
        String authtoken = null;
        String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        String accountPassword = intent.getStringExtra(PARAM_USER_PASS);
        final Account account = new Account(accountName, intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));
        Log.d("Account:", account.toString());

        if (getIntent().getBooleanExtra(ARG_IS_ADDING_NEW_ACCOUNT, false)) {
            Log.d("MY_LOG", TAG + "> finishLogin > addAccountExplicitly");
            authtoken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
            String authtokenType = mAuthTokenType;

            if(authtoken != null) {checkToken(authtoken);}

            // Creating the account on the device and setting the auth token we got
            // (Not setting the auth token will cause another call to the server to
            // authenticate the user)
            mAccountManager.addAccountExplicitly(account, accountPassword, null);
            mAccountManager.setAuthToken(account, authtokenType, authtoken);
        } else {
            Log.d("MY_LOG", TAG + "> finishLogin > setPassword");
            mAccountManager.setPassword(account, accountPassword);
        }

        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        startActivity(new Intent(this, AddShop.class).putExtra("token", authtoken));
        finish();
//        showAccountPicker(AUTHTOKEN_TYPE_FULL_ACCESS, false);
    }

    void checkToken(final String token){
        new AsyncTask<String, Void, Intent>() {
            @Override
            protected Intent doInBackground(String... params) {

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost http = new HttpPost("http://85.113.17.196:92/api/v1/test");
                http.addHeader("TOKEN", token);
                try {
                    String response = httpclient.execute(http, new BasicResponseHandler());
                    Log.d("Server Response", response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
        Log.d("checkToken", "ENDED");
    }
//    private void showAccountPicker(final String authTokenType, final boolean invalidate) {
//        final Account availableAccounts[] = mAccountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);
//
//        if (availableAccounts.length == 0) {
//            Toast.makeText(this, "No accounts", Toast.LENGTH_SHORT).show();
//        } else {
//            String name[] = new String[availableAccounts.length];
//            for (int i = 0; i < availableAccounts.length; i++) {
//                name[i] = availableAccounts[i].name;
//            }
//
//            // Account picker
//            AlertDialog mAlertDialog = new AlertDialog.Builder(this).setTitle("Pick Account").setAdapter(new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, name), new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    if (invalidate)
//                        invalidateAuthToken(availableAccounts[which], authTokenType);
//                    else
//                        getExistingAccountAuthToken(availableAccounts[which], authTokenType);
//                }
//            }).create();
//            mAlertDialog.show();
//        }
//    }
//    private void getExistingAccountAuthToken(Account account, String authTokenType) {
//        final AccountManagerFuture<Bundle> future = mAccountManager.getAuthToken(account, authTokenType, null, this, null, null);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Bundle bnd = future.getResult();
//                    final String authtoken = bnd.getString(AccountManager.KEY_AUTHTOKEN);
//                    showMessage((authtoken != null) ? "SUCCESS!\ntoken: " + authtoken : "FAIL");
//                    Log.d("TOKEN", "GetToken Bundle is " + bnd);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    showMessage(e.getMessage());
//                }
//            }
//        }).start();
//    }
//    private void invalidateAuthToken(final Account account, String authTokenType) {
//        final AccountManagerFuture<Bundle> future = mAccountManager.getAuthToken(account, authTokenType, null, this, null,null);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Bundle bnd = future.getResult();
//                    final String authtoken = bnd.getString(AccountManager.KEY_AUTHTOKEN);
//                    mAccountManager.invalidateAuthToken(account.type, authtoken);
//                    showMessage(account.name + " invalidated");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    showMessage(e.getMessage());
//                }
//            }
//        }).start();
//    }
//    private void showMessage(final String msg) {
//        if (TextUtils.isEmpty(msg))
//            return;
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}