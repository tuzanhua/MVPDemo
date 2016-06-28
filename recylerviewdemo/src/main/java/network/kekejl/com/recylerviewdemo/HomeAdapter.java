package network.kekejl.com.recylerviewdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

/**
 * 作者：tzh on 2016/6/20 17:45
 * <p/>
 * 类描述:
 * <p/>
 * 修改描述:
 */
class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
    private Context mContext;
    private List<String> mList;

    public HomeAdapter(Context context, List<String> list) {
        this.mContext = context;
        this.mList = list;
    }

    /**
     * 创建对应的viewHolder
     */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            MyViewHolder myViewHolder = new MyViewHolder(View.inflate(MainActivity.this,R.layout.item_home,null));
        //使用上面的会显示不全 使用layoutInflater 显示上则没有问题
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_home, parent, false);
//        FrameLayout.LayoutParams params =
//                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,(int)(Math.random() * 500));
//        view.setLayoutParams(params);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }


    /**
     * @param holder
     * @param position 数据显示的方法
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tv.setText(mList.get(position));
        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnItemClick(v,position);
            }
        });
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void OnItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    /**
     * 实现的viewHolder 必须实现recyleview 的ViewHolder
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.id_num);
        }
    }

    public void addData(int position){
        mList.add(position,"这是添加进来的数据");
        notifyItemInserted(position);
    }

    public void removeData(int position){
        mList.remove(position);
        notifyItemRemoved(position);
    }
}
