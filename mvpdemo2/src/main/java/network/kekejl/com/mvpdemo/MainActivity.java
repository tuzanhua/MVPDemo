package network.kekejl.com.mvpdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainActivityView, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.swipe_refresh_widget)
    SwipeRefreshLayout swipeRefreshWidget;
    @BindView(R.id.progressbar)
    ProgressBar progressbar;
    protected Context mContext;
    private ListViewAdapter listViewAdapter;
    private ListViewPresenterImpl listViewPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
        //首先要展示加载数据的进度条
        //加载数据
        //展示数据
        //下拉刷新数据
        //更新数据展示
        swipeRefreshWidget.setOnRefreshListener(this);
        swipeRefreshWidget.setColorSchemeResources(android.R.color.holo_purple, android.R.color.holo_blue_bright, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        listViewPresenter = new ListViewPresenterImpl(this);
        listViewPresenter.showData();
    }

    @Override
    public void showProgress() {
        progressbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressbar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setListViewData(ArrayList<String> mList) {
        listViewAdapter = new ListViewAdapter(mContext, mList);
        listview.setAdapter(listViewAdapter);
    }

    @Override
    public void onRefresh() {
        listViewPresenter.requestMoreData(listViewAdapter,swipeRefreshWidget);
    }
}
