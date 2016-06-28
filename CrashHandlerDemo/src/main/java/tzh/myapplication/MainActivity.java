package tzh.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 创建这个测试项目的目的 实现App 异常退出重启App 的异常处理
 * 实现将异常写到本地 并上传到我们自己的服务器
 */
public class MainActivity extends AppCompatActivity {


//    @BindView(R.id.btn_next)
//    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        btnNext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this,SecondActivity.class));
//            }
//        });
    }
    // 使用注解 自动生成点击事件 如果在布局文件中写了onclick 事件那么就是自动生成一个方法名
//    @OnClick(R.id.btn_next) void setBtnNext() {
//        startActivity(new Intent(MainActivity.this,SecondActivity.class));
//    }
    @OnClick(R.id.btn_next)
    public void setBtnNext(View view){
        startActivity(new Intent(MainActivity.this,SecondActivity.class));
    }
}
