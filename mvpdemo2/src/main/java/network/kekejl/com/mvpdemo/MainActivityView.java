package network.kekejl.com.mvpdemo;

import android.view.View;
import android.widget.BaseAdapter;

import java.util.ArrayList;
/**
 * 作者：tzh on 2016/6/12 11:16
 * <p/>
 * 类描述:  UI 更新和展示的接口
 * <p/>
 * 修改描述:
 */
public interface MainActivityView {

    /**
     * 显示
     */
    void showProgress();

    /**
     * 隐藏
     */
    void hideProgress();

    /**
     * 加载数据
     * @param mList
     */
    void setListViewData(ArrayList<String> mList);

}
