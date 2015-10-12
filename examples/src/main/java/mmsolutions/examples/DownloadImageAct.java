package mmsolutions.examples;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadImageAct extends Activity{

    ImageView imView;
    Bitmap bmImg;
    String imageUrl="http://192.168.88.143/im/uploadedimages/hhjgbyjkhby";

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.app_layout);

        imView = (ImageView)findViewById(R.id.imageID);
        downloadFile(imageUrl + ".png");
        Log.i("im url", imageUrl + ".png");
    }

    URL myFileUrl = null;
    void downloadFile(String fileUrl){
        try {
            myFileUrl = new URL(fileUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Thread myThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection conn= (HttpURLConnection)myFileUrl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
//                    int length = conn.getContentLength();
//                    int[] bitmapData = new int[length];
//                    byte[] bitmapData2 = new byte[length];
                    InputStream is = conn.getInputStream();

                    bmImg = BitmapFactory.decodeStream(is);
                    imView.setImageBitmap(bmImg);
                } catch (IOException e) {
                    e.printStackTrace();
                }            }
        });
        myThread.start();
    }
}