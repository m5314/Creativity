package com.moj.warehouse.customUI.bezier;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.moj.warehouse.util.ScreenUtil;

/**
 * 二阶贝塞尔曲线
 */
public class BezierView extends View {
    /**
     * 坐标轴间隔（单位：px）
     */
    private static final int axis_unit = 50;
    /**
     * 本View宽度
     */
    private int myWidth;
    /**
     * 本View高度
     */
    private int myHeight;
    /**
     * 本View一半宽度
     */
    private int myHalfWidth;
    /**
     * 本View一半高度
     */
    private int myHalfHeight;

    /**
     * 网格画笔
     */
    private Paint axisPaint = new Paint();
    /**
     * 坐标主轴画笔
     */
    private Paint gridPaint = new Paint();
    /**
     * 贝塞尔曲线画笔
     */
    private Paint pathPaint = new Paint();
    /**
     * 辅助线画笔（绿）
     */
    private Paint assistPaint = new Paint();
    /**
     * 辅助线画笔（黄）
     */
    private Paint assistPaint2 = new Paint();
    /**
     * 点画笔
     */
    private Paint pointPaint = new Paint();

    /**
     * 路径
     */
    private Path path = new Path();

    /**
     * 三个基点
     */
    private PointF startP = new PointF(-250f,0f);
    private PointF middleP = new PointF(0f,-250f);
    private PointF endP = new PointF(250f, 0f);

    /**
     * 二阶控制点
     */
    private PointF a = new PointF();
    private PointF b = new PointF();

    /**
     * 一阶控制点
     */
    private PointF c = new PointF();

    /**
     * 是否正在演示动画
     */
    private boolean isAniming = false;

    public BezierView(Context context) {
        this(context, null);
    }

    public BezierView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BezierView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint(context);
    }

    /**
     * 初始化画笔
     * @param context
     */
    private void initPaint(Context context){
        axisPaint.setColor(Color.parseColor("#000000"));
        axisPaint.setStrokeWidth(ScreenUtil.dp2px(context, 1));

        gridPaint.setColor(Color.parseColor("#aaaaaa"));
        gridPaint.setStrokeWidth(ScreenUtil.dp2px(context, 1));
        gridPaint.setAlpha(63);

        pathPaint.setColor(Color.parseColor("#ff0000"));
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setStrokeWidth(ScreenUtil.dp2px(context, 2));

        assistPaint.setColor(Color.parseColor("#00ff00"));
        assistPaint.setStrokeWidth(ScreenUtil.dp2px(context, 2));

        assistPaint2.setColor(Color.parseColor("#ffff00"));
        assistPaint2.setStrokeWidth(ScreenUtil.dp2px(context, 2));

        pointPaint.setColor(Color.parseColor("#0000ff"));
        pointPaint.setStrokeWidth(ScreenUtil.dp2px(context, 5));
        pointPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        pointPaint.setStrokeCap(Paint.Cap.ROUND);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取大小信息
        myWidth = getMeasuredWidth();
        myHeight = getMeasuredHeight();
        myHalfWidth = myWidth / 2;
        myHalfHeight = myHeight / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //移动画布，使得坐标原点在中央
        canvas.translate(myHalfWidth, myHalfHeight);

        //==========================绘制坐标网格=================================
        for(int temp = axis_unit; temp < myHalfWidth; temp+=axis_unit){
            canvas.drawLine(temp, -myHalfHeight, temp, myHalfHeight, gridPaint);
        }

        for(int temp = -axis_unit; temp > -myHalfWidth; temp-=axis_unit){
            canvas.drawLine(temp, -myHalfHeight, temp, myHalfHeight, gridPaint);
        }

        for(int temp = axis_unit; temp < myHalfHeight; temp+=axis_unit){
            canvas.drawLine(-myHalfWidth, temp, myHalfWidth, temp, gridPaint);
        }

        for(int temp = -axis_unit; temp > -myHalfHeight; temp-=axis_unit){
            canvas.drawLine(-myHalfWidth, temp, myHalfWidth, temp, gridPaint);
        }
        //======================================================================

        //绘制两条坐标主轴（x轴、 y轴）
        canvas.drawLine(-myHalfWidth, 0, myHalfWidth, 0, axisPaint);
        canvas.drawLine(0, -myHalfHeight, 0, myHalfHeight, axisPaint);

        if(isAniming) {
            //绘制一阶基线
            canvas.drawLine(a.x, a.y, b.x, b.y, assistPaint2);
        }

        //绘制由3个基点形成的两条基线
        canvas.drawLine(startP.x,startP.y,middleP.x,middleP.y, assistPaint);
        canvas.drawLine(middleP.x,middleP.y,endP.x,endP.y, assistPaint);

        //绘制二阶贝塞尔曲线
        path.moveTo(startP.x, startP.y);
        path.quadTo(middleP.x, middleP.y, endP.x, endP.y);
        canvas.drawPath(path, pathPaint);

        //绘制三个基点
        canvas.drawPoint(startP.x, startP.y , pointPaint);
        canvas.drawPoint(middleP.x, middleP.y , pointPaint);
        canvas.drawPoint(endP.x, endP.y , pointPaint);

        if(isAniming) {
            //绘制二阶控制点
            canvas.drawPoint(a.x, a.y, pointPaint);
            canvas.drawPoint(b.x, b.y, pointPaint);

            //绘制一阶控制点
            canvas.drawPoint(c.x, c.y, pointPaint);
        }

    }

    /**
     * 开始动画（演示贝塞尔曲线的绘制过程）
     */
    public void startAnim(){
        if(isAniming){
            return;
        }

        ValueAnimator animator = ValueAnimator.ofFloat(0,1);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                a.x = (Float)animation.getAnimatedValue() * (middleP.x - startP.x) + startP.x;
                a.y = (Float)animation.getAnimatedValue() * (middleP.y - startP.y) + startP.y;

                b.x = (Float)animation.getAnimatedValue() * (endP.x - middleP.x) + middleP.x;
                b.y = (Float)animation.getAnimatedValue() * (endP.y - middleP.y) + middleP.y;

                c.x = (Float)animation.getAnimatedValue() * (b.x - a.x) + a.x;
                c.y = (Float)animation.getAnimatedValue() * (b.y - a.y) + a.y;

                invalidate();
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAniming = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAniming = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isAniming = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.setDuration(5000);
        animator.start();
    }

    /**
     * 重置
     */
    public void reset(){
        a = new PointF(startP.x, startP.y);
        b = new PointF(middleP.x, middleP.y);
        c = new PointF(startP.x, startP.y);
        invalidate();
    }
}
