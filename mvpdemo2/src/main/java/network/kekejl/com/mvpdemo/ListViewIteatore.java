package network.kekejl.com.mvpdemo;

import android.support.v4.widget.SwipeRefreshLayout;

/**
 * 作者：tzh on 2016/6/12 11:48
 * <p/>
 * 类描述:
 * <p/>
 * 修改描述:
 */
public interface ListViewIteatore {
    void loadData(LoadDataFinishListener loadDataFinishListener);
    //ListViewAdapter listViewAdapter, SwipeRefreshLayout swipeRefreshLayout,
    void  pullDownToRefresh(ListViewAdapter listViewAdapter, SwipeRefreshLayout swipeRefreshLayout,final LoadDataFinishListener loadDataFinishListener);

}
