//package mmsolutions.im;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.EditText;
//import android.widget.Spinner;
//import android.widget.Switch;
//
//import java.util.ArrayList;
//
//public class AddGoods extends Activity {
//
//    ArrayList<String> shopList;
//    EditText Gname, Gprice, Gdescription;
//    String category;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.add_goods);
//
//        Gname = (EditText) findViewById(R.id.goods_name);
//        Gprice = (EditText) findViewById(R.id.goods_price);
//        Gdescription = (EditText) findViewById(R.id.goods_description);
//        Switch sw = (Switch) findViewById(R.id.show_price);
//
//        Intent i = getIntent();
//        shopList = i.getStringArrayListExtra("shops");
//
//        Spinner ShopSpinner = (Spinner)findViewById(R.id.shopSpinner);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, shopList);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        ShopSpinner.setAdapter(adapter);
//        ShopSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Object item = parent.getItemAtPosition(position);
//                Log.d("Spinner", item.toString());
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {}
//        });
//
//        Spinner forPrice = (Spinner)findViewById(R.id.currency_spinner);
//        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[] {getString(R.string.som), getString(R.string.dollar)});
//        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        forPrice.setAdapter(adapter1);
//
//        Spinner CategorySpinner = (Spinner)findViewById(R.id.categories);
//        ArrayAdapter<?> category_adapter = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
//        category_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        CategorySpinner.setAdapter(category_adapter);
//        CategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Object item = parent.getItemAtPosition(position);
//                category = item.toString();
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//    }
//}
