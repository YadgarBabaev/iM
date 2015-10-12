//package mmsolutions.im;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.TextView;
//
//import mmsolutions.im.R;
//
//class ListViewAdapter extends BaseAdapter {
//
//    Context context;
//    long[] ids;
//    String[] name, adrs;
//    private static LayoutInflater inflater = null;
//
//    public ListViewAdapter(Context context, long[] IDs, String[] name, String[] adrs) {
//        this.context = context;
//        this.name = name;
//        this.adrs = adrs;
//        this.ids = IDs;
//        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//    }
//
//    @Override
//    public int getCount() { return ids.length; }
//
//    @Override
//    public Object getItem(int position) { return ids[position]; }
//
//    @Override
//    public long getItemId(int position) { return position; }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View vi = convertView;
//        if (vi == null)
//            vi = inflater.inflate(R.layout.row_layout, null);
//
//        TextView id = (TextView) vi.findViewById(R.id.tvID);
//        TextView title = (TextView) vi.findViewById(R.id.shopName);
//        TextView adr = (TextView) vi.findViewById(R.id.shopAddress);
////        ImageView imageView = (ImageView) vi.findViewById(R.id.itemImg);
//
//        id.setText(String.valueOf(ids[position]));
//        title.setText(name[position]);
//        adr.setText(adrs[position]);
////        imageView.setImageResource(img[position%5]);
//        return vi;
//    }
//}