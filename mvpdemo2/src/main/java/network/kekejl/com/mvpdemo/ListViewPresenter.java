package network.kekejl.com.mvpdemo;

import android.support.v4.widget.SwipeRefreshLayout;

/**
 * 作者：tzh on 2016/6/12 11:59
 * <p/>
 * 类描述:
 * <p/>
 * 修改描述:
 */
public interface ListViewPresenter {

   void showData();

   void requestMoreData(ListViewAdapter listViewAdapter, SwipeRefreshLayout swipeRefreshLayout);

}
