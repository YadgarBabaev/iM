package mmsolutions.im;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class Goods extends Activity {
    JSONObject shop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_view);

        ImageLoader imageLoader = new ImageLoader(this.getApplicationContext());
        ImageView logo = (ImageView)findViewById(R.id.logo);
        TextView title_field = (TextView)findViewById(R.id.goods_title);
        ImageView likes_image = (ImageView)findViewById(R.id.likes);
        ImageView image = (ImageView)findViewById(R.id.goods_image);
        TextView price_field = (TextView)findViewById(R.id.goods_price);
        TextView text_field = (TextView)findViewById(R.id.text);
        TextView size_field = (TextView)findViewById(R.id.sizes);
        TextView category_field = (TextView)findViewById(R.id.category);
        TextView shop_field = (TextView)findViewById(R.id.shop_name);

        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String goods = getIntent().getStringExtra("goods");
        try {
            JSONObject object = new JSONObject(goods);
            shop = object.getJSONObject("shop");
            String logo_path = shop.getJSONObject("logo").getString("providerReference");
            String image_path = object.getJSONObject("photo").getString("providerReference");
            String shop_title = shop.getString("title");
            String goods_title = object.getString("title");
            final String text = object.getString("text");
            String sizes = object.getString("size");
            int likeCount = object.getInt("likeCount");
            int price = object.getInt("price");
            int discount = object.getInt("discount");
            boolean controlPrice = object.getBoolean("controlPrice");
            JSONObject categories = object.getJSONObject("subCategory");
            JSONObject category = categories.getJSONObject("category");

            imageLoader.DisplayImage("http://imarkets.ast.kg/uploads/media/shop_logo/0001/01/" + logo_path, logo);
            ImageManager manager = new ImageManager();
            manager.fetchImage(this, 3600 * 24 *7, "http://imarkets.ast.kg/uploads/media/goods/0001/01/" + image_path, image);

            title_field.setText(goods_title);
            if(likeCount > 0) {likes_image.setImageResource(R.mipmap.im4);}
            if(!controlPrice) {
                if (discount == 0) price_field.setText(price + "сом");
                else price_field.setText(price + "сом -" + discount + "%");
            }
            text_field.setText(text);
            size_field.setText(sizes);
            category_field.setText(category.getString("title") + ", " + categories.getString("title"));
            shop_field.setText(shop_title);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        FloatingActionButton map = (FloatingActionButton) findViewById(R.id.map);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ShowWay.class);
                i.putExtra("shop", String.valueOf(shop));
                startActivity(i);
            }
        });
    }
}
