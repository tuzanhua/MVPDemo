package network.kekejl.com.myapplication;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import network.kekejl.com.myapplication.service.CallActivityService;

public class MainActivity extends Activity {
    private static TextView tv;

    public static  Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
//            tv.setText((String)msg.obj);
        }
    };

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CallActivityService.MyBinder myBinder = (CallActivityService.MyBinder) service;
            myBinder.show();

            CallActivityService service1 = myBinder.getService();
            service1.start();
            service1.setCallBack(new CallActivityService.Callback() {
                @Override
                public void update(final String msg) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv.setText(msg);
                        }
                    });
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv);
        bindService(new Intent(this, CallActivityService.class), serviceConnection, BIND_AUTO_CREATE);
    }

    public void update(String msg){
        tv.setText(msg);
    }
}
