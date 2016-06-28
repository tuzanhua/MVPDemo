package tzh.pulltorefreshlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by 001 on 2016/5/18.
 */
public class PullToRefreshListView extends ListView implements AbsListView.OnScrollListener{
    /**
     * 定义状态的常量  0 下拉刷新   1 松开刷新   2 正在刷新
     */
    private  static  final int  PULLTOREFRESH = 0;
    private static final int RELEASETOREFRESH = 1;
    private static final int LOADING = 2;

    private ProgressBar pb;
    private TextView tv_des;
    private float startY;


    private int mState = PULLTOREFRESH;
    private View view;
    private int headerHeight;
    private View footerView;

    public PullToRefreshListView(Context context) {
        super(context);
        init(context);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    /**
     *  自定义view 的初始化操作
     */
    public void init(Context contenxt){
        initHeader(contenxt);

        initFooter(contenxt);

    }

    private void initFooter(Context contenxt) {
        footerView = View.inflate(contenxt, R.layout.item_header, null);
        pb = (ProgressBar) footerView.findViewById(R.id.pb);
        tv_des = (TextView) footerView.findViewById(R.id.tv_des);

        footerView.measure(0,0);
        headerHeight = footerView.getMeasuredHeight();
        footerView.setPadding(0,-headerHeight,0,0);
        this.addFooterView(footerView);
    }

    private void initHeader(Context contenxt) {
        // 初始化头布局
        view = View.inflate(contenxt, R.layout.item_header, null);
        pb = (ProgressBar) view.findViewById(R.id.pb);
        tv_des = (TextView) view.findViewById(R.id.tv_des);
        //将布局添加到 listview 里面
        // 初始化头布局的时候 将padding 设置为 负的 header 的高度 即隐藏header
        view.measure(0,0);
        headerHeight = view.getMeasuredHeight();
        view.setPadding(0,-headerHeight,0,0);
        this.addHeaderView(view);
    }

    /**
     *  重写onTouchEvent 方法根据不同的操作 做对应的处理
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if(mState == LOADING){
                    break;  // 这里return 的话必须有返回值 break 直接跳出去
                }
                float moveY; moveY = ev.getY();
                float dex = moveY - startY;
                int paddIngTop = (int) (dex + ( -headerHeight));
                // 在这里 移动的距离设置给padding  如果是下拉刷新  可见条目为第一条
                // 如果是第一个条条目 可见  并且 paddingtop  > - 高度  就是header 可见
                if(getFirstVisiblePosition() == 0 && paddIngTop > (- headerHeight)){
                    view.setPadding(0,paddIngTop,0,0);
                    // 如果paddingtop > 0 变成松开刷新
                    if(paddIngTop >0 && mState == PULLTOREFRESH){
                        mState = RELEASETOREFRESH;
                        // 更新头部 ui

                    }else if(paddIngTop <0 ){
                        //否则变成下拉刷新
                        mState = PULLTOREFRESH;
                        // 更新头部 ui

                    }
                    updateHeaderView();
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if(mState == RELEASETOREFRESH){ //如果是松开刷新的话 那么就变成loading
                    mState = LOADING;
                    onloading.loading();
                }else if(mState == PULLTOREFRESH){

                }
                updateHeaderView();
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void  updateHeaderView(){
        switch (mState){
            case PULLTOREFRESH:
                pb.setVisibility(View.INVISIBLE);
                tv_des.setText("下拉刷新");
            break;
            case RELEASETOREFRESH:
                pb.setVisibility(View.INVISIBLE);
                tv_des.setText("松开刷新");
            break;
            case LOADING:
                view.setPadding(0,0,0,0);
                pb.setVisibility(View.VISIBLE);
                tv_des.setText("正在刷新");
            break;
        }
    }

    /**
     * 如果是刷新的话 那么要用到接口回调 不需要关心 回调所用的数据 只要关心什么 时间回调
     *
     */
    private Onloading onloading;

    public void setOnRefreshing(Onloading onloading){
        this.onloading = onloading;
    }


    public interface Onloading{
        void loading();
        void LoadMore();
    }

    /**
     *  完成刷新的方法
     */
    public void completedRefreshing(){

    }


    /**
     * @param view
     * @param scrollState
     *
     *  跟随状态的改变 做出对应的操作
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState == OnScrollListener.SCROLL_STATE_IDLE && getLastVisiblePosition() == (getCount() - 1)){
            // 上拉加载更多
            if(onloading != null){
                onloading.LoadMore();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
