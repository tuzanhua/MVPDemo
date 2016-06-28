package network.kekejl.com.uploadrefreshrecylerview;

import android.content.Context;

import java.io.File;

/**
 * 作者：tzh on 2016/6/22 15:38
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
public class ClearCacheUtil {

    //计算缓存的大小
//    long fileSize = 0;
//    String cacheSize = "0KB";


    /**
     * 获取data/data/package_name/files 文件夹目录
     * @param context
     * @return
     */
    //getFilesDir/data/data/network.kekejl.com.uploadrefreshrecylerview/files
    public static File getFile(Context context) {
        return context.getFilesDir();
    }

    /**
     * 获取 /data/data/package_name/cache 文件夹目录
     * @param context
     * @return
     */
    //getcachedir/data/data/network.kekejl.com.uploadrefreshrecylerview/cache
    public static File getCache(Context context) {
        return context.getCacheDir();
    }

    /**
     * 获取文件的大小
     * @param dir
     * @return
     */
    public static long getDirSize(File dir) {
        if (dir == null) {
            return 0;
        }

        if (!dir.isDirectory()) {
            return 0;
        }

        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += file.length();
                dirSize += getDirSize(file);//递归调用统计
            }
        }
        return dirSize;
    }

    /**
     * 删除文件
     * @param dir
     */
    public static void clearData(File dir){
        if(!dir.exists()){
            return;
        }

        File[] files = dir.listFiles();
        for(File file : files){
            if(file.isDirectory()){ //如果是文件夹继续遍历
                if(file.length()==0){
                    file.delete();
                }else{
                    clearData(file);
                }
            }else if(file.isFile()){//如果是文件删除
                file.delete();
            }
        }
    }


    /**
     * 转换文件大小
     *
     * @param fileS
     * @return B/KB/MB/GB
     */
    public static String formatFileSize(long fileS) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

}
