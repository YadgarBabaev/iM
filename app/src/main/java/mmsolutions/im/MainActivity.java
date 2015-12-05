package mmsolutions.im;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mmsolutions.im.SignIn.AccountGeneral;
import mmsolutions.im.SignIn.MainPage;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String authToken;
    ProgressDialog pDialog;
    TextView text;
    ListView list;
    Activity activity;
    JSONObject json;
    JSONParser jParser = new JSONParser();
    private AccountManager mAccountManager;
    LazyImageLoadAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        if (intent.getStringExtra("token") != null)
            authToken = intent.getStringExtra("token");

        activity = this;
        pDialog = new ProgressDialog(this);
        mAccountManager = AccountManager.get(this);

        View view = findViewById(R.id.app_bar_main).findViewById(R.id.content_main);
        text = (TextView)view.findViewById(R.id.text_message);
        list = (ListView)view.findViewById(R.id.list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    JSONArray jsonArray = json.getJSONArray("goods");
                    JSONObject object = jsonArray.getJSONObject(position);
                    startActivity(new Intent("GoodsView").putExtra("goods", object.toString()));
                    Log.d("GOODS_SELECTION", object.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

//        List<String> list = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            list.add("TEXT #" + i);
//        }
//        RollView roll = (RollView) findViewById(R.id.roll_a);
//        roll.setList(list);
        new LoadAllGoods().execute();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.shop_list) {
            startActivity(new Intent(this, AllShops.class).putExtra("token", authToken));
        } else if (id == R.id.my_shop_list) {
            accountPicker(AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, false, MyShopsList.class);
//        } else if (id == R.id.goods_list) {

        } else if (id == R.id.add_shop) {
            accountPicker(AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, false, AddShop.class);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    class LoadAllGoods extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Loading... Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        boolean success = false;
        List<Integer> id = new ArrayList<>();
        List<String> image = new ArrayList<>();
        List<String> logo = new ArrayList<>();
        List<String> title = new ArrayList<>();
        List<String> desc = new ArrayList<>();
        List<String> price = new ArrayList<>();
        List<Boolean> ctrl = new ArrayList<>();
        List<String> sizes = new ArrayList<>();

        protected String doInBackground(String... args) {
            String url = "http://85.113.17.196:92/rest/goods/list";
            json = jParser.makeHttpRequest(url, "POST", new MultipartEntity(), "");
            try {
                JSONArray jArray = json.getJSONArray("goods");
                if(jArray.length() > 0) success = true;
                for(int i=0; i<jArray.length(); i++)
                {
                    JSONObject object = jArray.getJSONObject(i);
                    Log.d("All GOODS: ", object.toString());
                    JSONObject shop = object.getJSONObject("shop");
                    id.add(object.getInt("id"));
                    image.add(object.getJSONObject("photo").getString("providerReference"));
                    logo.add(shop.getJSONObject("logo").getString("providerReference"));
                    title.add(object.getString("title"));
                    desc.add(object.getString("text"));
                    price.add(object.getString("price"));
                    ctrl.add(!object.getBoolean("controlPrice"));
                    sizes.add(object.getString("size"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            if (!success) {text.setVisibility(View.VISIBLE);}
            else {
                adapter = new LazyImageLoadAdapter(activity, id, image, logo, title, desc, price, ctrl, sizes);
                list.setAdapter(adapter);
            }
        }
    }

    public void accountPicker(final String authTokenType, final boolean invalidate, final Class c) {
        final Account availableAccounts[] = mAccountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);
        if (availableAccounts.length == 0) {
            Toast.makeText(this, "No accounts", Toast.LENGTH_SHORT).show();
            mAccountManager.addAccount(AccountGeneral.ACCOUNT_TYPE, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, null, null, MainActivity.this, new AccountManagerCallback<Bundle>() {
                @Override
                public void run(AccountManagerFuture<Bundle> future) {
                    try {
                        Bundle bnd = future.getResult();
                        showMessage("Account was created");
                        Log.d("addNewAccount", "Bundle is " + bnd);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, null);
        }
        else if (availableAccounts.length == 1) {
            getExistingAccountAuthToken(availableAccounts[0], authTokenType, c);
        }
        else {
            String name[] = new String[availableAccounts.length];
            for (int i = 0; i < availableAccounts.length; i++) {
                name[i] = availableAccounts[i].name;
            }

            // Account picker
            AlertDialog mAlertDialog = new AlertDialog.Builder(this).setTitle("Pick Account").setAdapter(new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, name), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (invalidate)
                        invalidateAuthToken(availableAccounts[which], authTokenType);
                    else
                        getExistingAccountAuthToken(availableAccounts[which], authTokenType, c);
                }
            }).create();
            mAlertDialog.show();
        }
    }
    private void getExistingAccountAuthToken(Account account, String authTokenType, final Class c) {
        final AccountManagerFuture<Bundle> future = mAccountManager.getAuthToken(account, authTokenType, null, this, null, null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bundle bnd = future.getResult();
                    String authToken = bnd.getString(AccountManager.KEY_AUTHTOKEN);
                    Log.d("TOKEN", "GetToken " + authToken);
                    checkToken(authToken, c);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
    private void invalidateAuthToken(final Account account, String authTokenType) {
        final AccountManagerFuture<Bundle> future = mAccountManager.getAuthToken(account, authTokenType, null, this, null, null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bundle bnd = future.getResult();
                    String authToken = bnd.getString(AccountManager.KEY_AUTHTOKEN);
                    mAccountManager.invalidateAuthToken(account.type, authToken);
                    showMessage(account.name + " invalidated");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
    void checkToken(final String token, final Class c){
        new AsyncTask<String, Void, Intent>() {
            @Override
            protected Intent doInBackground(String... params) {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost http = new HttpPost("http://85.113.17.196:92/api/v1/test");
                http.addHeader("TOKEN", token);
                try {
                    HttpResponse response = httpclient.execute(http);
                    if (response.getStatusLine().getStatusCode() == 200)
                        startActivity(new Intent(getApplicationContext(), c).putExtra("token", token));
                    else startActivity(new Intent(getApplicationContext(), MainPage.class));
                    Log.d("Server Response", response.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }
}
