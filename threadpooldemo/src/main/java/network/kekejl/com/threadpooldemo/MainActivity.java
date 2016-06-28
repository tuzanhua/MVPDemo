package network.kekejl.com.threadpooldemo;

import android.app.Activity;
import android.os.SystemClock;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {
    private static final String TAG = "线程池的模拟";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i = 0; i < 21; i++) {
            ThreadPoolManger.getmInstance().executor(new MyTaskThread(i));
        }

    }

    class MyTaskThread implements Runnable {
        private int num;

        public MyTaskThread(int num) {
            //构造方法里面模拟等待的时间段
            this.num = num;
            Log.e(TAG, "第" + num + "个线程等待执行");
        }

        @Override
        public void run() {
            Log.e(TAG, "第" + num + "个线程开始执行");
            SystemClock.sleep(2000);
            Log.e(TAG, "第" + num + "个线程执行完成");
        }
    }
}
