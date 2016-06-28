package network.kekejl.com.mvpdemo;

import android.support.v4.widget.SwipeRefreshLayout;

import java.util.ArrayList;

/**
 * 作者：tzh on 2016/6/12 11:55
 * <p/>
 * 类描述:
 * <p/>
 * 修改描述:
 */
public interface LoadDataFinishListener {
   void onFinishedLoadData(ArrayList<String> mList);
   void onRefresh(ListViewAdapter listViewAdapter, SwipeRefreshLayout swipeRefreshLayout);
}
