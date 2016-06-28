package tzh.myapplication;

import android.app.Application;

import butterknife.ButterKnife;
import tzh.myapplication.crash.CrashHandler;

/**
 * Created by 001 on 2016/5/18.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化 异常收集所需要的条件
        ButterKnife.setDebug(BuildConfig.DEBUG);
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
    }
}
