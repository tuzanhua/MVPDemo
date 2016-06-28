package network.kekejl.com.progressbardemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.LruCache;
import android.util.TypedValue;
import android.widget.ProgressBar;

/**
 * 作者：tzh on 2016/6/24 09:46
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
public class HorizontalProgressBarWithNumber extends ProgressBar {

    private static final int DEFAULT_TEXT_SIZE = 10;//默认字体大小
    private static final int DEFAULT_TEXT_COLOR = 0XFFFC00D1;//默认字体颜色
    private static final int DEFAULT_COLOR_UNREACHED_COLOR = 0xFFd3d6da;// 默认的进度颜色
    private static final int DEFAULT_HEIGHT_REACHED_PROGRESS_BAR = 2;// 默认的已经完成的进度条高度
    private static final int DEFAULT_HEIGHT_UNREACHED_PROGRESS_BAR = 2;//默认进度条高度
    private static final int DEFAULT_SIZE_TEXT_OFFSET = 10; //默认偏移量

    /**
     * 画笔
     * painter of all drawing things
     */
    protected Paint mPaint = new Paint();
    /**
     * 进度条字体颜色
     * color of progress number
     */
    protected int mTextColor = DEFAULT_TEXT_COLOR;
    /**
     * size of text (sp)
     */
    protected int mTextSize = sp2px(DEFAULT_TEXT_SIZE);

    /**
     * offset of draw progress
     */
    protected int mTextOffset = dp2px(DEFAULT_SIZE_TEXT_OFFSET);

    /**
     * height of reached progress bar
     */
    protected int mReachedProgressBarHeight = dp2px(DEFAULT_HEIGHT_REACHED_PROGRESS_BAR);

    /**
     * color of reached bar
     */
    protected int mReachedBarColor = DEFAULT_TEXT_COLOR;
    /**
     * color of unreached bar
     */
    protected int mUnReachedBarColor = DEFAULT_COLOR_UNREACHED_COLOR;
    /**
     * height of unreached progress bar
     */
    protected int mUnReachedProgressBarHeight = dp2px(DEFAULT_HEIGHT_UNREACHED_PROGRESS_BAR);
    /**
     * view width except padding
     */
    protected int mRealWidth;

    protected boolean mIfDrawText = true;

    protected static final int VISIBLE = 0;

    public HorizontalProgressBarWithNumber(Context context, AttributeSet attrs) {
        //调用下面的方法
        this(context, attrs, 0);
        LruCache
    }

    public HorizontalProgressBarWithNumber(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setHorizontalScrollBarEnabled(true); //设置水平进度条是不是可以draw
        obtainStyledAttributes(attrs);

        mPaint.setTextSize(mTextSize);
        mPaint.setColor(mTextColor);
    }

    /**
     * 获取自定义属性
     *
     * @param attrs
     */
    private void obtainStyledAttributes(AttributeSet attrs) {
        //获取自定义属性的根集合
        TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.HorizontalProgressBarWithNumber);

        mTextColor = attributes.getColor(R.styleable.HorizontalProgressBarWithNumber_progress_text_color, DEFAULT_TEXT_COLOR);

        mTextSize = (int) attributes.getDimension(R.styleable.HorizontalProgressBarWithNumber_progress_text_size, mTextSize);

        mReachedBarColor = attributes.getColor(R.styleable.HorizontalProgressBarWithNumber_progress_reached_color, mTextColor);

        mUnReachedBarColor = attributes.getColor(R.styleable.HorizontalProgressBarWithNumber_progress_unreached_color, DEFAULT_HEIGHT_UNREACHED_PROGRESS_BAR);

        mReachedProgressBarHeight = (int) attributes.getDimension(R.styleable.HorizontalProgressBarWithNumber_progress_reached_bar_height, mReachedProgressBarHeight);

        mUnReachedProgressBarHeight = (int) attributes.getDimension(R.styleable.HorizontalProgressBarWithNumber_progress_unreached_bar_height, mUnReachedProgressBarHeight);

        mTextOffset = (int) attributes.getDimension(R.styleable.HorizontalProgressBarWithNumber_progress_text_offset, mTextOffset);

        int textVisible = attributes.getInt(R.styleable.HorizontalProgressBarWithNumber_progress_text_visibility, VISIBLE);

        if (textVisible != VISIBLE) {
            mIfDrawText = false;
        }
        attributes.recycle();
    }

    /**
     * 因为我们所有的属性都让用户自定义了,所以我们的测量也得稍微改变下
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode != MeasureSpec.EXACTLY) {
            float textHeight = mPaint.descent() + mPaint.ascent();
            //经过判决定当前进度条的高度
            int exceptHeight = (int)(getPaddingTop()
                    + getPaddingBottom() + Math.max(Math.max(mReachedProgressBarHeight, mUnReachedProgressBarHeight), Math.abs(textHeight)));
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 绘制
     * @param canvas
     */
    @Override
    protected synchronized void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        canvas.save();
        //画笔平移到指定的paddingleft ,高度的一半位置,注意以后的坐标都以此为 0,0 点
        canvas.translate(getPaddingLeft(),getHeight() / 2);

        boolean noNeedBg = false;
        //当前进度和总值的比例
        float radio = getProgress() *1.0f / getMax();
        //已到达的宽度
        float progressPosX = (int)(mRealWidth * radio);
        //绘制的文本
        String text = getProgress() +"%";

        //拿到字体的高度和宽度
        float textWidth = mPaint.measureText(text);
        float textHeight = (mPaint.descent() + mPaint.ascent()) / 2;

        //如果到达最后,则未到达的进度条不需要绘制
        if(progressPosX +textWidth > mRealWidth){
            progressPosX = mRealWidth -textWidth;
            noNeedBg = true;
        }

        //绘制已到达的进度
        float endX = progressPosX - mTextOffset / 2;
        if(endX >0){
            mPaint.setColor(mReachedBarColor);
            mPaint.setStrokeWidth(mReachedProgressBarHeight);
            canvas.drawLine(0,0,endX,0,mPaint);
        }

        //绘制文本
        if(mIfDrawText){
            mPaint .setColor(mTextColor);
            canvas.drawText(text,progressPosX,-textHeight,mPaint);
        }

        //绘制未到达的进度条
        if(!noNeedBg){
            float start = progressPosX + mTextOffset / 2 + textWidth;
            mPaint.setColor(mUnReachedBarColor);
            mPaint.setStrokeWidth(mUnReachedProgressBarHeight);
            canvas.drawLine(start,0,mRealWidth,0,mPaint);
        }

        canvas.restore();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRealWidth = w - getPaddingRight() - getPaddingLeft();
    }

    /**
     * dp 2 px
     *
     * @param dpVal
     */
    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    /**
     * sp 2 px
     *
     * @param spVal
     * @return
     */
    protected int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, getResources().getDisplayMetrics());

    }
}
