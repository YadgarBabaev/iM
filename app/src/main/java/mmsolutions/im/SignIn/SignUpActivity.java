package mmsolutions.im.SignIn;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import mmsolutions.im.R;

import static mmsolutions.im.SignIn.AccountGeneral.sServerAuthenticate;
import static mmsolutions.im.SignIn.AuthenticatorActivity.ARG_ACCOUNT_TYPE;
import static mmsolutions.im.SignIn.AuthenticatorActivity.KEY_ERROR_MESSAGE;
import static mmsolutions.im.SignIn.AuthenticatorActivity.PARAM_USER_PASS;

public class SignUpActivity extends Activity {

    private String mAccountType;
    private String TAG = getClass().getSimpleName();
    EditText etLogin, etEmail, etPass, etPass2;
    ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        mAccountType = getIntent().getStringExtra(ARG_ACCOUNT_TYPE);

        etLogin = (EditText) findViewById(R.id.etLogin);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etEmail.setText(UserEmailFetcher.getEmail(this));
        etPass = (EditText) findViewById(R.id.etPass);
        etPass2 = (EditText) findViewById(R.id.etPass2);

        findViewById(R.id.btnNew).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check()) {
                    createAccount();
                } // Validation!
            }
        });
    }

    private void createAccount() {
        new AsyncTask<String, Void, Intent>() {

            String name = etLogin.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPass.getText().toString().trim();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(SignUpActivity.this, R.style.MyColorDialogTheme);
                pDialog.setMessage("Processing request. Please wait...");
                pDialog.setIndeterminate(true);
                pDialog.setCancelable(true);
                pDialog.show();
            }

            @Override
            protected Intent doInBackground(String... params) {

                Log.d("SIGN UP", TAG + "> Started authenticating");
                String authtoken;
                Bundle data = new Bundle();
                try {
                    authtoken = sServerAuthenticate.userSignUp(name, email, password, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS);
                    data.putString(AccountManager.KEY_ACCOUNT_NAME, email);
                    data.putString(AccountManager.KEY_ACCOUNT_TYPE, mAccountType);
                    data.putString(AccountManager.KEY_AUTHTOKEN, authtoken);
                    data.putString(PARAM_USER_PASS, password);
                } catch (Exception e) {
                    data.putString(KEY_ERROR_MESSAGE, e.getMessage());
                }
                final Intent res = new Intent();
                res.putExtras(data);
                return res;
            }

            @Override
            protected void onPostExecute(Intent intent) {
                if (intent.hasExtra(KEY_ERROR_MESSAGE)) {
                    pDialog.dismiss();
                    Toast.makeText(getBaseContext(), intent.getStringExtra(KEY_ERROR_MESSAGE), Toast.LENGTH_SHORT).show();
                    Log.d("Authorization", intent.getStringExtra(KEY_ERROR_MESSAGE));
                } else {
                    setResult(RESULT_OK, intent);
                    finish();}
            }
        }.execute();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    boolean check(){
        String login = etLogin.getText().toString();
        String email = etEmail.getText().toString();
        String pass = etPass.getText().toString();
        String pass2 = etPass2.getText().toString();

        if(login.equals("") | email.equals("") | pass.equals("")) {
            Log.d("MY_LOG", "blank fields");
            if(login.equals("")){etLogin.setError("Required field");}
            if(email.equals("")){etEmail.setError("Required field");}
            if(pass.equals("")){etPass.setError("Required field");}
            if(pass2.equals("")){etPass2.setError("Required field");}
            return false;
        }
        else {
            if(pass.length()<6) {
                etPass.setError("Short password");
                return false;
            }
            if(!pass.equals(pass2)){
                etPass2.setError("Different passwords");
                return false;
            }
            else return true;
        }
    }

    @Override
    protected void onDestroy() {super.onDestroy();}
}

class UserEmailFetcher {
    static String getEmail(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account account = getAccount(accountManager);
        if (account == null) {
            return null;
        } else {
            return account.name;
        }
    }

    private static Account getAccount(AccountManager accountManager) {
        Account[] accounts = accountManager.getAccountsByType("com.google");
        Account account;
        if (accounts.length > 0) {
            account = accounts[0];
        } else {
            account = null;
        }
        return account;
    }
}