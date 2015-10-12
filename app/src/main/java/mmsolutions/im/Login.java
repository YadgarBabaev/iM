//package mmsolutions.im;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.FragmentActivity;
//import com.facebook.CallbackManager;
//import com.facebook.FacebookCallback;
//import com.facebook.FacebookException;
//import com.facebook.FacebookSdk;
//import com.facebook.appevents.AppEventsLogger;
//import com.facebook.login.LoginManager;
//import com.facebook.login.LoginResult;
//import com.facebook.login.widget.LoginButton;
//
//public class Login extends FragmentActivity{
//
//    CallbackManager callbackManager;
//    LoginButton loginButton;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        setContentView(R.layout.fb_login);
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        callbackManager = CallbackManager.Factory.create();
//        loginButton = (LoginButton) findViewById(R.id.login_button);
//        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                startActivity(new Intent(getApplicationContext(), AddShop.class));
//                finish();
//            }
//            @Override
//            public void onCancel() {}
//            @Override
//            public void onError(FacebookException e) {}
//        });
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        // Logs 'install' and 'app activate' App Events.
//        AppEventsLogger.activateApp(this);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        // Logs 'app deactivate' App Event.
//        AppEventsLogger.deactivateApp(this);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//    }
//}