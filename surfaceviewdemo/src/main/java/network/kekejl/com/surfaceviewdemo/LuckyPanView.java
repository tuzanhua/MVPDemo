package network.kekejl.com.surfaceviewdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * 作者：tzh on 2016/6/27 13:57
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
public class LuckyPanView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    /**
     * surfaceHolder
     */
    private SurfaceHolder mHolder;


    /**
     * 与surfaceHolder 绑定的canvas
     */
    private Canvas mCancas;


    /**
     * 用于绘制的线程
     */
    private Thread t;

    /**
     * 线程的控制开关
     */
    private boolean isRunning;

    /**
     * 圆盘的直径
     */
    private int mRadius;

    /**
     * padding值
     */
    private int mPadding;

    /**
     * 控件的中心位置
     */
    private int mCenter;

    /**
     * 画笔
     */
    private Paint mArcPaint;


    /**
     * 绘制文字的画笔
     */
    private Paint mTextPaint;

    /**
     * 文字的大小 通过系统自带的工具类来实现TypedValue
     */
    private float mTextSize =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics());


    /**
     * 矩形
     */
    private RectF mRange;

    /**
     * 与文字对应的图片
     */
    private int[] mImgs = new int[]{R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher};

    /**
     * 每个盘块的颜色
     */
    private int[] mColors = new int[] { 0xFFFFC300, 0xFFF17E01, 0xFFFFC300,
            0xFFF17E01, 0xFFFFC300, 0xFFF17E01 };

    /**
     * 盘块个数
     */
    private int mItemCount = 6;


    /**
     * 盘快所对应的图片
     */
    private Bitmap[] mImgsBitmap;

    /**
     * 滚动的速度
     */
    private double mSpeed;
    private volatile float mStartAngle = 0;

    /**
     * 是否点击了停止
     */
    private boolean isShouldEnd;

    /**
     * 抽奖的文字
     */
    private String[] mStrs = new String[] { "单反相机", "IPAD", "恭喜发财", "IPHONE",
            "妹子一只", "恭喜发财" };

    public LuckyPanView(Context context) {
//        super(context);
        this(context, null);
    }


    public LuckyPanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //surfaceView  获取对应的Holder
        mHolder = getHolder();
        mHolder.addCallback(this);
        setZOrderOnTop(true); //设置画布 背景透明
        mHolder.setFormat(PixelFormat.TRANSLUCENT);

        setFocusable(true);//设置可以获取焦点
        setFocusableInTouchMode(true); //设置可触摸
        this.setKeepScreenOn(true); //设置屏幕常亮
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取控件的宽高 然后拿小的值在里面创建 圆盘
        int width = Math.min(getMeasuredWidth(), getMeasuredHeight());
        //获取圆形的直径
        mRadius = width - getPaddingLeft() - getPaddingRight();
        //padding 的值
        mPadding = getPaddingLeft();
        //中心点
        mCenter = width / 2;
        setMeasuredDimension(width, width);
    }

    /**
     * @param holder surfaceView 创建
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //这是callBack 的回调在创建surfaceView 的时候回调这个方法
        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true); //抗锯齿效果,平滑
        mArcPaint.setDither(true);  //渐变

        //初始化画质文字的画笔
        mTextPaint = new Paint();
        mTextPaint.setColor(0xFFffffff);
        mTextPaint.setTextSize(mTextSize);
        //圆弧的绘制范围
        mRange = new RectF(getPaddingLeft(), getPaddingLeft(), mRadius + getPaddingLeft(), mRadius + getPaddingLeft());

        //初始化图片
        mImgsBitmap = new Bitmap[mItemCount];
        for (int i = 0; i < mItemCount; i++) {
            mImgsBitmap[i] = BitmapFactory.decodeResource(getResources(), mImgs[i]);
        }

        //开启线程
        isRunning = true;
        t = new Thread(this);
        t.start();
    }

    /**
     * @param holder
     * @param format
     * @param width
     * @param height surfaceView 改变
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * @param holder surfaceView 销毁
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //通知关闭线程
        isRunning = false;
    }

    @Override
    public void run() {
        //不断的进行Draw
        while (isRunning) {
            long start = System.currentTimeMillis();
            draw();
            long end = System.currentTimeMillis();
            try {
                if (end - start < 50) {
                    Thread.sleep(50 - (end - start));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void draw() {
        //获得canvas
        mCancas = mHolder.lockCanvas();
        if (mCancas != null) {
            //绘制背景图
            drawBg();

            /**
             * 绘制每个块,每个块上的文本,每个块上的图片
             */
            float tmAngle = mStartAngle;
            //每一块所占的角度
            float sweepAngle = (float) (360 / mItemCount);
            for(int i = 0;i<mItemCount; i++){
                //绘制块
                mArcPaint.setColor(mColors[i]);
                mCancas.drawArc(mRange,tmAngle,sweepAngle,true,mArcPaint);
                //绘制文本
                drawText(tmAngle,sweepAngle,mStrs[i]);
                //绘制icon
                drawIcon(tmAngle,i);

                //改变当前的角度
                tmAngle += sweepAngle;
            }

            //如果mSpeed不等于0,则相当于在滚动
            mStartAngle += mSpeed;
            //点击停止的时候设置mSpeed为递减,为0值盘停止
            if(isShouldEnd){
                mSpeed -= -1;
            }
            if(mSpeed <=0){
                mSpeed = 0;
                isShouldEnd = false;
            }
            //根据当前旋转的mStartAngle 计算当前滚动到的区域
            calInExactArea(mStartAngle);
        }
    }

    private void calInExactArea(float mStartAngle) {

    }

    /**
     * 绘制每个块的icon
     * @param tmAngle
     * @param i
     */
    private void drawIcon(float startAngle, int i) {
        //设置图片的宽度为直径的1/8
        int imgWidth = mRadius /8;
        float angle = (float) ((30 + startAngle) * (Math.PI /180));

        int x = (int)(mCenter +mRadius /2/2 * Math.cos(angle));
        int y = (int)(mCenter +mRadius/2/2 *Math.sin(angle));

        //确定绘制图片的位置
        Rect rect = new Rect(x - imgWidth /2,y- imgWidth /2,x + imgWidth/2,y + imgWidth /2);
        mCancas.drawBitmap(mImgsBitmap[i],null,rect,null);
    }

    /**
     * 绘制文本
     * @param startAngle
     * @param sweepAngle
     * @param s
     */
    private void drawText(float startAngle, float sweepAngle, String s) {
        Path path = new Path();
        path.addArc(mRange,startAngle,sweepAngle);
        float textWidth = mTextPaint.measureText(s);
        //医用水平偏移让蚊子居中
        float hOffset = (float) (mRadius * Math.PI / mItemCount / 2 - textWidth / 2);//水平偏移
        int vOffset = mRadius / 2 / 6;//垂直偏移
        mCancas.drawTextOnPath(s,path,hOffset,vOffset,mTextPaint);
    }

    /**
     * 根据当前旋转的mStartAngle 计算当前滚动到的区域 绘制背景
     */
    private void drawBg() {
        mCancas.drawColor(0xFFFFFFFF);
//        mCancas.drawBitmap(m);
    }
}
