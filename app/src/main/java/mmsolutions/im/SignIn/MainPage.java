package mmsolutions.im.SignIn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import mmsolutions.im.R;

public class MainPage extends Activity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        TextView login = (TextView) findViewById(R.id.btnLogin);
        TextView registr = (TextView) findViewById(R.id.btnRegistration);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AuthenticatorActivity.class));
                finish();
            }
        });

        registr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String url = "http://85.113.17.196:92/register";
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                Uri u = Uri.parse(url);
//                i.setData(u);
//                startActivity(i);
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {super.onDestroy();}
}