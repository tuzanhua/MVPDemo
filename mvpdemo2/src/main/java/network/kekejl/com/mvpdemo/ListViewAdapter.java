package network.kekejl.com.mvpdemo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：tzh on 2016/6/12 11:25
 * <p/>
 * 类描述:
 * <p/>
 * 修改描述:
 */
public class ListViewAdapter extends BaseAdapter {
   public ArrayList<String> mList;
   public Context mContext;

    public ListViewAdapter(Context context, ArrayList<String> mlist) {
        this.mList = mlist;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_main, null);
        }

        ViewHolder viewHolder = ViewHolder.getViewHolder(convertView);
        viewHolder.tvContent.setText(mList.get(position));
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_content)
        TextView tvContent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public static ViewHolder getViewHolder(View view) {
            ViewHolder viewHolder = (ViewHolder) view.getTag();
            if (viewHolder == null) {
                viewHolder = new ViewHolder(view);
            }
            return viewHolder;
        }
    }
}
