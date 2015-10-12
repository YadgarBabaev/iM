package mmsolutions.examples;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FileActivity extends Activity{

    private static final String LOG_TAG = "MY_LOG";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file);

        final TextView txt = (TextView)findViewById(R.id.txtField);
        Button button = (Button)findViewById(R.id.getBtn);


//        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
//        SharedPreferences.Editor editor = settings.edit();
//        editor.putString("Username", "User");
//        editor.putString("Password", "PASSWORD");
//        editor.apply();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getSharedPreferences("UserInfo", 0);
                txt.setText(settings.getString("Username", "") + " " + settings.getString("Password", ""));
            }
        });

//        SharedPreferences settings = getSharedPreferences("MyFile", 0);
//        boolean silent = settings.getBoolean("silentMode", false);
////        setSilent(silent);
    }
//
//    public File getAlbumStorageDir(String albumName) {
//        // Get the directory for the user's public pictures directory.
//        File file = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES), albumName);
//        if (!file.mkdirs()) {
//            Log.e(LOG_TAG, "Directory not created");
//        }
//        return file;
//    }
//
//    @Override
//    protected void onStop(){
//        super.onStop();
//
//        // We need an Editor object to make preference changes.
//        // All objects are from android.context.Context
//        SharedPreferences settings = getSharedPreferences("MyFile", 0);
//        SharedPreferences.Editor editor = settings.edit();
////        editor.putBoolean("silentMode", SilentMode);
//
//        // Commit the edits!
//        editor.commit();
//    }
}