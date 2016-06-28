package network.kekejl.com.uploadrefreshrecylerview;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * 作者：tzh on 2016/6/22 16:35
 * <p/>
 * 类描述:
 * <p/>
 * 修改描述:
 * #                       _oo0oo_                     #
 * #                      o8888888o                    #
 * #                      88" . "88                    #
 * #                      (| -_- |)                    #
 * #                      0\  =  /0                    #
 * #                    ___/`---'\___                  #
 * #                  .' \\|     |# '.                 #
 * #                 / \\|||  :  |||# \                #
 * #                / _||||| -:- |||||- \              #
 * #               |   | \\\  -  #/ |   |              #
 * #               | \_|  ''\---/''  |_/ |             #
 * #               \  .-\__  '-'  ___/-. /             #
 * #             ___'. .'  /--.--\  `. .'___           #
 * #          ."" '<  `.___\_<|>_/___.' >' "".         #
 * #         | | :  `- \`.;`\ _ /`;.`/ - ` : | |       #
 * #         \  \ `_.   \_ __\ /__ _/   .-` /  /       #
 * #     =====`-.____`.___ \_____/___.-`___.-'=====    #
 * #                       `=---='                     #
 * #     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   #
 * #                                                   #
 * #               佛祖保佑         永无BUG              #
 * #                                                   #
 */
public class SecondActivity extends Activity {

    private TextView tv;

    long fileSize = 0;
    private static String TAG = "SecondActivity";

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Log.e(TAG,"fileSize" + fileSize);
//            fileSize += ClearCacheUtil.getDirSize(getCacheDir());
//            fileSize += ClearCacheUtil.getDirSize(getFilesDir());
//
//            //格式化
//            String fileSizeString = ClearCacheUtil.formatFileSize(fileSize);
            tv.setText("0KB");
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        tv = (TextView) findViewById(R.id.tv);
        findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //清除数据 之后更新UI
                new Thread(){
                    @Override
                    public void run() {
                        SecondActivity.this.getApplication();
                        ClearCacheUtil.clearData(getFilesDir());
                        ClearCacheUtil.clearData(getCacheDir());
                        Log.e(TAG,"清除完"+fileSize);
                        fileSize = 0;
                        mHandler.sendEmptyMessage(0);
                    }
                }.start();
            }
        });

        Log.e(TAG,"getcachedir" + getCacheDir());
        Log.e(TAG,"getFilesDir" + getFilesDir());
        fileSize += ClearCacheUtil.getDirSize(getCacheDir());
        fileSize += ClearCacheUtil.getDirSize(getFilesDir());

        //格式化
        String fileSizeString = ClearCacheUtil.formatFileSize(this.fileSize);
        Log.e(TAG,"第一次显示" + fileSizeString);
        tv.setText(fileSizeString);
    }
}
