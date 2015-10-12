//package mmsolutions.examples;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.FragmentActivity;
//import android.view.View;
//import android.widget.Toast;
//
//import com.facebook.android.DialogError;
//import com.facebook.android.Facebook;
//import com.facebook.android.Facebook.DialogListener;
//import com.facebook.android.FacebookError;
//import com.facebook.login.widget.LoginButton;
//
//public class LoginActivity extends FragmentActivity {
//
//    public final String API_KEY = "API_KEY";
//    public final String[] permissions = {"publish_stream"};
//    Facebook facebook = new Facebook(API_KEY);
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fb_login);
//
//        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                authorizeAndPostMassage();
//            }
//        });
//    }
//
//    public void authorizeAndPostMassage() {
//
//        facebook.authorize(this, permissions, new DialogListener() {
//            @Override
//            public void onComplete(Bundle values) {
//                Toast.makeText(LoginActivity.this, "Authorization successful", Toast.LENGTH_SHORT).show();
//                postMassage();
//            }
//
//            @Override
//            public void onFacebookError(FacebookError e) {
//                Toast.makeText(LoginActivity.this, "Facebook error, try again later", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError(DialogError e) {
//                Toast.makeText(LoginActivity.this, "Error, try again later", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancel() {
//                Toast.makeText(LoginActivity.this, "Authorization canceled", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        facebook.authorizeCallback(requestCode, resultCode, data);
//    }
//
//}