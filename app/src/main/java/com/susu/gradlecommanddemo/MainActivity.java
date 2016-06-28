package com.susu.gradlecommanddemo;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.susu.gradlecommanddemo.main.SuccessActivity;

public class MainActivity extends AppCompatActivity implements LoginView{

    private ProgressBar progressBar;
    private EditText username;
    private EditText password;
    private Button button;
    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginPresenter = new PresenterImpl(this);
        initViews();


    }

    private void initViews() {
        progressBar = (ProgressBar) findViewById(R.id.progress);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        button = (Button)  findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginPresenter.validateUser(username.getText().toString().trim(),password.getText().toString().trim());
            }
        });
    }

    @Override
    public void showProgress() {
       progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void setUsernameError() {
        username.setError(getResources().getText(R.string.username_error));
    }

    @Override
    public void setPwdError() {
        password.setError(getResources().getText(R.string.password_error));
    }

    @Override
    public void navigateToHome() {
        startActivity(new Intent(this,SuccessActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //回收对象
        loginPresenter.onDestroy();
    }
}
