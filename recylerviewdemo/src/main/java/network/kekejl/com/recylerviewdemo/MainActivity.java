package network.kekejl.com.recylerviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView mRecyclerView;
    private List<String> mDatas;
    private HomeAdapter homeAdapter;
    private Button btn_add;
    private Button btn_remove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        btn_add = (Button) findViewById(R.id.btn_add);
        btn_remove = (Button) findViewById(R.id.btn_remove);
        btn_add.setOnClickListener(this);
        btn_remove.setOnClickListener(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
        //设置布局管理器
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL));
        //添加分割线
//        mRecyclerView.addItemDecoration(new RecycleViewDivider(MainActivity.this));
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(MainActivity.this));
        homeAdapter = new HomeAdapter(MainActivity.this,mDatas);
        //设置Adapter
        mRecyclerView.setAdapter(homeAdapter);
        //设置删除和添加的动画效果
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        homeAdapter.setOnItemClickListener(new HomeAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                Toast.makeText(getApplicationContext(),"当前位置 : "+ position,Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void initData() {
        mDatas = new ArrayList<String>();
        for (int i = 'A'; i < 'z'; i++) {
            mDatas.add("" + (char) i);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add:
                homeAdapter.addData(1);
            break;
            case R.id.btn_remove:
                homeAdapter.removeData(2);
            break;
        }
    }
}
