package com.susu.gradlecommanddemo.main;

import java.util.List;

/**
 * 作者：suxianming on 2016/5/9 14:07
 */
public class MainPresenterImpl implements MainPresenter,FindItemInterator.onFinishedItemListener {
    private SuccessView successView;
    private FindItemInterator findItemInterator;
    @Override
    public void onResume() {
        //显示UI,请求数据
        if(successView!=null){
            successView.showProgress();
        }
        //请求数据
        findItemInterator.findItems(this);
    }

    @Override
    public void onItemClicked(int position) {
        if(successView!=null){
            successView.showMessage(String.format("Position %d clicked",position+1));
        }
    }

    //Model和Presenter的初始化
    public MainPresenterImpl(SuccessView successView){
        //将view层的对象通过接口的形式传递过来
        this.successView = successView;
        //产生Model层的对象
        findItemInterator = new FindItemInteratorImpl();
    }

    //回收view
    @Override
    public void onDestroy() {
        successView = null;
    }

    //回调显示数据
    @Override
    public void onFinished(List<String> items) {
        if(successView!=null){
            successView.setItems(items);
            successView.hideProgress();
        }
    }
}
