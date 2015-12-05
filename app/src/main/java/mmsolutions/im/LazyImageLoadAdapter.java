package mmsolutions.im;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

//Adapter class extends with BaseAdapter and implements with OnClickListener
public class LazyImageLoadAdapter extends BaseAdapter{

    private List<Integer> id;
    private List<String> image;
    private List<String> logo;
    private List<String> title;
    private List<String> text;
    private List<String> price;
    private List<Boolean> ctrl;
    private List<String> sizes;
    private static LayoutInflater inflater = null;
    public ImageLoader imageLoader;
    Activity activity;

    public LazyImageLoadAdapter(Activity a, List<Integer> id, List<String> image, List<String> logo, List<String> title, List<String> text, List<String> price, List<Boolean> ctrl, List<String> sizes) {
        this.id = id;
        this.image = image;
        this.logo = logo;
        this.title = title;
        this.text = text;
        this.price = price;
        this.ctrl = ctrl;
        this.sizes = sizes;
        activity = a;
        inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Create ImageLoader object to download and show image in list
        // Call ImageLoader constructor to initialize FileCache
        imageLoader = new ImageLoader(a.getApplicationContext());
    }

    public int getCount() {
        return id.size();
    }

    public Object getItem(int position) {return position;}

    public long getItemId(int position) {return position;}

    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{

        public TextView id, title, text, price, sizes;
        public ImageView logo, image;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View vi=convertView;
        ViewHolder holder;

        if(convertView==null){

            /****** Inflate item file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.list_item_for_goods, null);

            /****** View Holder Object to contain item file elements ******/
            holder = new ViewHolder();
            holder.id = (TextView) vi.findViewById(R.id.goodsID);
            holder.title = (TextView)vi.findViewById(R.id.goods_name);
            holder.text = (TextView) vi.findViewById(R.id.goods_description);
            holder.price = (TextView)vi.findViewById(R.id.goods_price);
            holder.sizes = (TextView)vi.findViewById(R.id.goods_sizes);
            holder.image = (ImageView)vi.findViewById(R.id.goods_image);
            holder.logo = (ImageView)vi.findViewById(R.id.logo);


            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else holder=(ViewHolder)vi.getTag();

        holder.id.setText(String.valueOf(id.get(position)));
        holder.title.setText(title.get(position));
        holder.text.setText(text.get(position));
        if(!ctrl.get(position))
            holder.price.setVisibility(View.INVISIBLE);
        holder.price.setText(price.get(position) + " сом");
        holder.sizes.setText("Размеры: " + sizes.get(position));
        ImageView photo = holder.image;
        ImageView logos = holder.logo;

        //DisplayImage function from ImageLoader Class
        imageLoader.DisplayImage("http://imarkets.ast.kg/uploads/media/goods/0001/01/" + image.get(position), photo);
        imageLoader.DisplayImage("http://imarkets.ast.kg/uploads/media/shop_logo/0001/01/" + logo.get(position), logos);
        return vi;
    }
}