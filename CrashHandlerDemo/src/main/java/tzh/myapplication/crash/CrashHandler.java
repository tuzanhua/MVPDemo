package tzh.myapplication.crash;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Process;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by TuZanHua on 2016/5/18.
 * UncaughtExceptionHandler 系统处理异常的类 ,我们自己用CrashHandler 来实现这个接口,当程序发生异常的时候,由该类
 * 接管程序,并记录发送报告.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    /**
     * 当我们在打log的时候需要Tag 每一个类里面定义一个
     */
    public static final String TAG = "CrashHandler";

    //系统默认的UncaughtException 处理类 系统开启一个线程来完成这项工作
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    //创建本类的实例对象
    private static CrashHandler instance = new CrashHandler();

    /**
     * 保证只有一个实例对象  私有化构造方法
     */
    private CrashHandler() {
    }

    /**
     * 对外提供一个实例
     */
    public static CrashHandler getInstance() {
        return instance;
    }

    //初始化本类需要的对象
    public void init(Context context) {
        mContext = context;
        // 获取系统默认的异常处理器 uncaghtException
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler 为程序默认的处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    //程序的上下文
    private Context mContext;
    //用来存储设备的信息和异常
    private Map<String, String> infos = new HashMap<>();
    //用于格式化日期，作为日志文件名的一部分
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    /**
     * 当UncaughtException 发生异常的时候出转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) { //这里说明没有自己处理异常
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            //退出程序
            Process.killProcess(Process.myPid());
            System.exit(1);
        }

    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告均在此完成
     * 如果处理了该异常 return true; 否则返回false
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        //收集 设备参数信息
        collectDeviceInfo(mContext);
        //保存日志文件
        saveCrashInfo2File(ex);
        return true;
    }

    /**
     * 手机设备参数的信息
     */
    public void collectDeviceInfo(Context context) {

        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = (pi.versionName == null) ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
    /**
     *  将日志保存到文件中
     *  返回文件的名称 便于将文件发送到服务器
     */

    private String saveCrashInfo2File(Throwable ex){
        // 保存的文件 路径
        String filename = null;
        StringBuffer sb = new StringBuffer();
        // 遍历错误 的Map
        Set<String> keySets = infos.keySet();

        for(String key : keySets){
            String value = infos.get(key);
            sb.append(key +"=" + value +"\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        // 将异常写到流中
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while(cause != null){
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);

        String fileName = "crash.log";
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            String path = Environment.getExternalStorageDirectory() +"/tzh/";
            File file = new File(path);
            if(!file.exists()){
                file.mkdirs();
            }
            //将sb 写到文件中
            try {
                FileOutputStream fos = new FileOutputStream(new File(path,fileName));
                fos.write(sb.toString().getBytes());
                fos.close();

            } catch (Exception e) {
                Log.e(TAG,"异常出现" + e.getMessage());
                e.printStackTrace();
            }
        }
       return fileName;
    }


}
