package mmsolutions.examples;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

public class BackgroundUploading extends Activity{

    private LinearLayout mLoadingFooter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLoadingFooter = (LinearLayout) layoutInflater.inflate(R.layout.loading, null);

        ListView mList = (ListView) findViewById(R.id.list);
        mList.addFooterView(mLoadingFooter);

        mList.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (visibleItemCount > 0 && firstVisibleItem + visibleItemCount == totalItemCount) {
//                    loadNextPage();
                }
            }
        });
    }
}
