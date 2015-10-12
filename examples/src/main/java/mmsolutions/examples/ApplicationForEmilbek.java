package mmsolutions.examples;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class ApplicationForEmilbek extends Activity {

    ImageButton goButton;
    String url = "http://kdm.kg";
    Intent i = new Intent(Intent.ACTION_VIEW);
    Uri u = Uri.parse(url);
    Context context = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_layout);

        goButton = (ImageButton)findViewById(R.id.goButton);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Start the activity
                    i.setData(u);
                    startActivity(i);
                    finish();
                } catch (ActivityNotFoundException e) {
                    // Raise on activity not found
                    Toast.makeText(context, "Browser not found.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
