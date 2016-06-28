package com.susu.gradlecommanddemo.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.susu.gradlecommanddemo.R;

import java.util.List;

/**
 * 作者：suxianming on 2016/4/27 15:08
 */

public class SuccessActivity extends AppCompatActivity implements SuccessView,AdapterView.OnItemClickListener{

    private ListView listView;
    private ProgressBar progress;
    private MainPresenter mainPresenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(this);
        progress = (ProgressBar) findViewById(R.id.progress);
        mainPresenter = new MainPresenterImpl(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainPresenter.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPresenter.onDestroy();
    }
    @Override
    public void showProgress() {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progress.setVisibility(View.GONE);
    }

    @Override
    public void setItems(List<String> list) {
        listView.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,list));
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mainPresenter.onItemClicked(position);
    }
}
