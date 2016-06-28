package tzh.pulltorefreshlistview;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private PullToRefreshListView pulltirefresh;
    private MyAdapter myAdapter;
   private List<String> mLists = new ArrayList<>();
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pulltirefresh = (PullToRefreshListView) findViewById(R.id.pulltirefresh);
        myAdapter = new MyAdapter();
        mContext = this;
        pulltirefresh.setAdapter(myAdapter);
        pulltirefresh.setOnRefreshing(new PullToRefreshListView.Onloading() {
            @Override
            public void loading() {
                //刷新
            }

            @Override
            public void LoadMore() {
                // 加载更多

            }
        });
        initData();
    }

    private void initData() {
        for (int i = 0;i < 30;i++){
            mLists.add("世界你好"+i);
        }
        myAdapter.notifyDataSetChanged();
    }

    class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return mLists.size();
        }

        @Override
        public Object getItem(int position) {
            return mLists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = new TextView(mContext);
            textView.setText(mLists.get(position));
            return textView;
        }
    }
}
