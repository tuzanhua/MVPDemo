package network.kekejl.com.uploadrefreshrecylerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：tzh on 2016/6/21 13:58
 * <p/>
 * 类描述:
 * <p/>
 * 修改描述:
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<String> mList;
    /**
     * 当前处于打开状态的item
     */
    private List<SwipeItemLayout> mOpenedSil = new ArrayList<>();

    public MyAdapter(Context context, List<String> list) {
        this.mContext = context;
        this.mList = (ArrayList<String>) list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_re, parent, false);
//        View view1 = View.inflate(mContext, R.layout.item_re, null);
//        View view = LayoutInflater.from(mContext).inflate(R.layout.item_home, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tv.setText(mList.get(position));
        closeOpenedSwipeItemLayoutWithAnim();
        SwipeItemLayout swipeItemLayout = holder.item_contact_swipe_root;
        swipeItemLayout.setSwipeAble(true);//侧滑菜单可以滑出来
        swipeItemLayout.setDelegate(new SwipeItemLayout.SwipeItemLayoutDelegate() {//设置侧滑删除的监听
            @Override
            public void onSwipeItemLayoutOpened(SwipeItemLayout swipeItemLayout) {
                //首先
                closeOpenedSwipeItemLayoutWithAnim();
                //添加进对应的swipeItemLayout
                mOpenedSil.add(swipeItemLayout);
            }



            @Override
            public void onSwipeItemLayoutClosed(SwipeItemLayout swipeItemLayout) {
                mOpenedSil.remove(swipeItemLayout);
            }

            @Override
            public void onSwipeItemLayoutStartOpen(SwipeItemLayout swipeItemLayout) {
                closeOpenedSwipeItemLayoutWithAnim();
            }
        });

        holder.item_contact_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这样写会有问题 一会删除的条目就不对了
//                mList.remove(position);
//                notifyItemRemoved(position);
                //这样写很完美
                ((MainActivity) mContext).deleteUser(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    /**
     * 关闭没有关闭的item
     */
    public void closeOpenedSwipeItemLayoutWithAnim() {
        for (SwipeItemLayout sil : mOpenedSil) {
            sil.closeWithAnim();
        }
        mOpenedSil.clear();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv;
        public TextView item_contact_delete;
        public TextView item_contact_like;
        public SwipeItemLayout item_contact_swipe_root;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv);
            item_contact_delete = (TextView) itemView.findViewById(R.id.item_contact_delete);
            item_contact_like = (TextView) itemView.findViewById(R.id.item_contact_like);
            item_contact_swipe_root = (SwipeItemLayout) itemView.findViewById(R.id.item_contact_swipe_root);
        }
    }
}
