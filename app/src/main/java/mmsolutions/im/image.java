package mmsolutions.im;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


public class image extends Activity{

    String url = "192.168.88.143/im/uploadedimages/hhjgbyjkhby.png";
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.img);
        imageView = (ImageView) findViewById(R.id.imgView);
    }

    public void uploadImage(View view) {
    }
}