package mmsolutions.im;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MyAdapter extends BaseAdapter {
    Context context;
    long[] ids;
    int[] logo;
    String[] title, phone, address;
    private static LayoutInflater inflater = null;

    public MyAdapter(Context context, long[] IDs, String[] title, String[] address, String[] phone, int[] img) {
        this.context = context;
        this.ids = IDs;
        this.logo = img;
        this.title = title;
        this.address = address;
        this.phone = phone;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() { return ids.length; }

    @Override
    public Object getItem(int position) { return ids[position]; }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.list_item_for_shops, null);

        TextView id = (TextView) vi.findViewById(R.id.shopID);
        TextView name = (TextView) vi.findViewById(R.id.shopTitle);
        TextView adr = (TextView) vi.findViewById(R.id.shopAddress);
        TextView tel = (TextView) vi.findViewById(R.id.shopPhone);
        ImageView imageView = (ImageView) vi.findViewById(R.id.shop_logo);

        id.setText(String.valueOf(ids[position]));
        name.setText(title[position]);
        adr.setText(address[position]);
        tel.setText(phone[position]);
        imageView.setImageResource(logo[new Random().nextInt() % 7]);
        return vi;
    }
}
