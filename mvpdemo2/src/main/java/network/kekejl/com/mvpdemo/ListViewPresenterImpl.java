package network.kekejl.com.mvpdemo;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;

import java.util.ArrayList;

/**
 * 作者：tzh on 2016/6/12 12:01
 * <p/>
 * 类描述:
 * <p/>
 * 修改描述:
 */
public class ListViewPresenterImpl implements ListViewPresenter {

    MainActivityView mainActivityView;

    ListViewIteratorImpl listViewIteratorimpl;

    Handler mHandler = new Handler();

    public ListViewPresenterImpl(MainActivityView mainActivityView) {
        this.mainActivityView = mainActivityView;
        listViewIteratorimpl = new ListViewIteratorImpl();
    }

    @Override
    public void showData() {
        mainActivityView.showProgress();
        listViewIteratorimpl.loadData(new LoadDataFinishListener() {
            @Override
            public void onFinishedLoadData(final ArrayList<String> mList) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mainActivityView.setListViewData(mList);
                        mainActivityView.hideProgress();
                    }
                });
            }

            @Override
            public void onRefresh(ListViewAdapter listViewAdapter, SwipeRefreshLayout swipeRefreshLayout) {

            }
        });
    }

    @Override
    public void requestMoreData(final ListViewAdapter listViewAdapter, final SwipeRefreshLayout swipeRefreshLayout) {
        listViewIteratorimpl.pullDownToRefresh(listViewAdapter, swipeRefreshLayout, new LoadDataFinishListener() {
            @Override
            public void onFinishedLoadData(ArrayList<String> mList) {

            }

            @Override
            public void onRefresh(final ListViewAdapter listViewAdapter, final SwipeRefreshLayout swipeRefreshLayout) {
                if (listViewAdapter == null)
                    return;
                    mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        listViewAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

    }
}
