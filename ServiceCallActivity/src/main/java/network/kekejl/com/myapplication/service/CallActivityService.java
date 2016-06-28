package network.kekejl.com.myapplication.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import network.kekejl.com.myapplication.MainActivity;

/**
 * 作者：tzh on 2016/6/20 10:12
 * <p/>
 * 类描述:
 * <p/>
 * 修改描述:
 */
public class CallActivityService extends Service {
    private static String TAG = "CallActivityService";

    // IBinder 中间者对象
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "服务创建了");
//        new Thread(){
//            @Override
//            public void run() {
//                for (int i = 0; i <100; i++){
////                    Message message = Message.obtain();
////                    message.obj = "这是第 : " + i+ "次发送消息";
////                    MainActivity.mHandler.sendMessage(message);
//                    }
//                }
//            }
//        }.start();
    }

    public void start(){
        new Thread(){
            @Override
            public void run() {
                for (int i = 0; i <100; i++){
//                    Message message = Message.obtain();
//                    message.obj = "这是第 : " + i+ "次发送消息";
//                    MainActivity.mHandler.sendMessage(message);
                    String msg = "这是第 : " + i+ "次发送消息";
                    Log.e(TAG,"这是第 : " + i+ "次发送消息");
                    if(callback != null){
                        callback.update(msg);
                        SystemClock.sleep(1000);
                    }
                }
            }
        }.start();
    }

   public  class MyBinder extends Binder {
        public void show() {
            Log.e(TAG, "服务里面的方法");

        }
      public  CallActivityService getService(){
          return CallActivityService.this;
      }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private Callback callback;

    //使用接口回调的方式
    public interface  Callback{
        void update(String msg);
    }

    public void setCallBack(Callback callBack){
        this.callback = callBack;
    }
}
