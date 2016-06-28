package network.kekejl.com.mvpdemo;

import android.os.SystemClock;
import android.support.v4.widget.SwipeRefreshLayout;
import java.util.ArrayList;

/**
 * 作者：tzh on 2016/6/12 11:48
 * <p/>
 * 类描述:
 * <p/>
 * 修改描述:
 */
public class ListViewIteratorImpl implements ListViewIteatore {

    ArrayList<String> mList = new ArrayList<>();
    //业务模型层
    @Override
    public void loadData(final LoadDataFinishListener loadDataFinishListener) {
        // 加载数据的操作
        new Thread(){
            @Override
            public void run() {
                SystemClock.sleep(2000);
                for(int i = 0;i < 50; i++){
                    mList.add("中正" + i);
                }
                loadDataFinishListener.onFinishedLoadData(mList);
            }
        }.start();
    }
    //ListViewAdapter lvAdapter,SwipeRefreshLayout sw,
    @Override
    public void pullDownToRefresh(final ListViewAdapter listViewAdapter, final SwipeRefreshLayout swipeRefreshLayout, final LoadDataFinishListener loadDataFinishListener) {
        new Thread(){
            @Override
            public void run() {
                // 模仿耗时操作
                SystemClock.sleep(2000);
                mList.add(0,"这是下拉刷新出来的数据");
                //刷新界面
                loadDataFinishListener.onRefresh(listViewAdapter,swipeRefreshLayout);
            }
        }.start();
    }
}
