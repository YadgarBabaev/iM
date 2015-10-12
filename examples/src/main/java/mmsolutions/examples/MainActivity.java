package mmsolutions.examples;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends Activity {

    Button btnViewProducts;
    Button btnNewProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

//        btnViewProducts = (Button) findViewById(R.id.btnViewProducts);
//        btnNewProduct = (Button) findViewById(R.id.btnCreateProduct);
//
//        btnViewProducts.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getApplicationContext(), AllProducts.class);
//                startActivity(i);
//            }
//        });
//
//        btnNewProduct.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), NewProduct.class));
//            }
//        });
    }
}