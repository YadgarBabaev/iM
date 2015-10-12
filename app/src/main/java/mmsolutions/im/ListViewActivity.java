//package mmsolutions.im;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ListView;
//
//public class ListViewActivity extends Activity {
//
//    ListView listView;
//    String[] title = new String[]{};
//    String[] address = new String[]{};
//    long[] _id = new long[]{};
//
////    int[] img = {android.R.drawable.ic_delete, android.R.drawable.btn_star, android.R.drawable.btn_radio,
////            android.R.drawable.picture_frame, android.R.drawable.ic_menu_add};
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_layout);
//
//        listView = (ListView) findViewById(R.id.listViewLayout);
//        listView.setAdapter(new ListViewAdapter(this, _id, title, address));
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.d("LOG_TAG", "itemClick: position = " + position + ", id = " + id);
//            }
//        });
//    }
//}