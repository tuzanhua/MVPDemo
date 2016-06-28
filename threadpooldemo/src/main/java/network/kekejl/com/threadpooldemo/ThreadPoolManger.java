package network.kekejl.com.threadpooldemo;

import android.util.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 作者：tzh on 2016/6/26 18:06
 * <p>
 * 类描述:
 * <p>
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
public class ThreadPoolManger {
    //线程池的管理 线程的管理要是单例的
    private static ThreadPoolManger mInstance = new ThreadPoolManger();

    private int corePoolSize;
    private int maximumPoolSize;
    private long keepAliveTime = 1l;
    private final ThreadPoolExecutor executor;

    private ThreadPoolManger() {
//        int corePoolSize,  //核心线程数
//        int maximumPoolSize, //线程池最大线程数
//        long keepAliveTime,   //存活时间
//        TimeUnit unit,         //枚举
//        BlockingQueue<Runnable> workQueue, //缓存队列
//        ThreadFactory threadFactory,       //线程工厂
//        RejectedExecutionHandler handler //拒绝执行
        corePoolSize = Runtime.getRuntime().availableProcessors() * 2 + 1;//计算核心线程池的数量
        Log.e("线程池的模拟","手机核心线程个数" + corePoolSize);
        maximumPoolSize = corePoolSize;
        //核心线程池
// 不写参数就默认没有最大的限制 可以等待无限个
        executor = new ThreadPoolExecutor(
                corePoolSize,//核心线程池
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<Runnable>(),// 不写参数就默认没有最大的限制 可以等待无限个
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());


    }

    public static ThreadPoolManger getmInstance() {
        return mInstance;
    }

    /**
     * @param runnable
     *执行的方法
     */
    public void executor(Runnable runnable){
        if(runnable != null){
            executor.execute(runnable);
        }

    }

    /**
     * 移除线程的方法 移除之后释放资源
     * @param runnable
     */
    public void remove(Runnable runnable){
        if(runnable != null){
            executor.remove(runnable);
        }
    }

}
