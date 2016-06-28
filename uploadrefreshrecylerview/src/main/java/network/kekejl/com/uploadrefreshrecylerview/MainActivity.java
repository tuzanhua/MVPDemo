package network.kekejl.com.uploadrefreshrecylerview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;

import java.util.ArrayList;

public class MainActivity extends Activity implements SpringView.OnFreshListener {

    private SpringView springview;
    private RecyclerView recyclerview;
    private ArrayList<String> mList;
    private Context mContext;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        mContext = this;
        springview = (SpringView) findViewById(R.id.springview);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
       findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(MainActivity.this,SecondActivity.class));
               MainActivity.this.finish();
           }
       });
        //设置布局管理器
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        //添加分割线
        recyclerview.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        //
        myAdapter = new MyAdapter(mContext, mList);
        recyclerview.setAdapter(myAdapter);

        springview.setHeader(new DefaultHeader(this));
        springview.setFooter(new DefaultFooter(this));
        springview.setListener(this);
        springview.setType(SpringView.Type.FOLLOW);
    }

    private void initData() {
        mList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            mList.add("忘记" + i);
        }
    }

    //下拉刷新
    @Override
    public void onRefresh() {
        new Thread() {
            @Override
            public void run() {
                SystemClock.sleep(2000);
                mList.add(0, "不如不见");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        springview.onFinishFreshAndLoad();
                        myAdapter.notifyDataSetChanged();
                    }
                });
            }
        }.start();

    }

    //上拉加载更多
    @Override
    public void onLoadmore() {
        new Thread() {
            @Override
            public void run() {
                SystemClock.sleep(2000);
                mList.add("如果最后是你晚一点没关系");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        springview.onFinishFreshAndLoad();
                        myAdapter.notifyDataSetChanged();
                        //使用这个方法让加载出来的数据显示出来
                        recyclerview.smoothScrollToPosition(myAdapter.getItemCount() - 1);
                    }
                });
            }
        }.start();
    }

    public void deleteUser(final int position) {
        mList.remove(position);
        myAdapter.notifyDataSetChanged();
        showToast("删除成功");

    }

    public void showToast(String value) {
        Toast.makeText(this, value, Toast.LENGTH_SHORT).show();

    }
}
