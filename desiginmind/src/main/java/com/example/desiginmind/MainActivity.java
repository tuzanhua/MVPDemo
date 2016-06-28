package com.example.desiginmind;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.desiginmind.observer.ConcreateObserver1;
import com.example.desiginmind.observer.ConcreateObserver2;
import com.example.desiginmind.observer.Observer;
import com.example.desiginmind.observer.ObserverManger;
import com.example.desiginmind.observer.Subject;

public class MainActivity extends AppCompatActivity  {

    private Button btn_onserver;
    private Subject subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConcreateObserver1 concreateObserver1 = new ConcreateObserver1();
        ConcreateObserver2 concreateObserver2 = new ConcreateObserver2();

        // 注册监听
        subject = new Subject();
        subject.register(concreateObserver1);
        subject.register(concreateObserver2);
        //
        final ObserverManger observerManger = ObserverManger.getInstance();

        observerManger.registerListener(new Observer() {
            @Override
            public void update() {
                Log.e("guanchazhe","先添加的观察者");
            }
        });


        btn_onserver = (Button) findViewById(R.id.btn_onserver);

        btn_onserver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subject.notifyUpdate();
                observerManger.notifyAllListener();
            }
        });
    }
}
