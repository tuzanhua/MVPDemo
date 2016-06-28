package network.kekejl.com.progressbardemo;

import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private HorizontalProgressBarWithNumber pb1;
     int progress = 0;
    Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pb1 = (HorizontalProgressBarWithNumber) findViewById(R.id.pb1);
        pb1.setMax(100);

        new Thread(){
            @Override
            public void run() {
                for(int i = 0;i<=100;i++){
                    SystemClock.sleep(1000);
                    progress++;
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                        pb1.setProgress(progress);
                        }
                    });
                }
            }
        }.start();
    }
}
