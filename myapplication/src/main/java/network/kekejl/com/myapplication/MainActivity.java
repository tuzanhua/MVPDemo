package network.kekejl.com.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<String> mDatas;
    private HomeAdapter homeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
        //设置布局管理器
       mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        homeAdapter = new MainActivity.HomeAdapter();
        //设置Adapter
        mRecyclerView.setAdapter(homeAdapter);
    }

    private void initData() {
        mDatas = new ArrayList<>();
        for(int i = 'A';i<'Z';i++){
            mDatas.add("" +(char)i);
        }
    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>{

        /**
         *  创建对应的viewHolder
         */
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder myViewHolder = new MyViewHolder(View.inflate(MainActivity.this,R.layout.item_home,null));
            return myViewHolder;
        }


        /**
         * @param holder
         * @param position
         *  数据显示的方法
         */
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
                holder.tv.setText(mDatas.get(position));
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        /**
         * 实现的viewHolder 必须实现recyleview 的ViewHolder
         */
        public class MyViewHolder extends RecyclerView.ViewHolder{

            private  TextView tv;

            public MyViewHolder(View itemView) {
                super(itemView);
                 tv =  (TextView) itemView.findViewById(R.id.id_num);
            }
        }
    }
}
