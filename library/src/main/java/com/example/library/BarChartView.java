package com.example.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cuieney on 16/6/13.
 */
public class BarChartView extends View {
    private Paint axisPaint;
    private Paint txtYPaint;
    private Paint txtXPaint;
    private Paint yPaint;
    private Paint rectPaint;
    private float[] progress = {};
    private Context context;


    //xy轴的颜色
    private int mAxisColor = Color.parseColor("#A4A4A4");
    //xy轴的宽度
    private int mAxisWidth = 2;

    //xy字体的颜色
    private int mXAxisTxtColor = Color.parseColor("#ff000000");
    private int mYAxisTxtColor = Color.parseColor("#ff000000");

    //柱状图的颜色
    private int mBarColor = Color.parseColor("#A4A4A4");
    private int mBarPressColor = Color.parseColor("#ff0000");
    private boolean mBarPressEnable = false;
    private boolean isHideGirdLine = true;

    //Y轴最大值
    private int mYMaxValue = 200;


    private int mXGirdLine = Color.parseColor("#55A4A4A4");
    private int mXGirdLineWidth = sp2px(1);
    private float mTxtSize = sp2px(10);

    private int mRadius = dp2px(3);


    private OnClickListener mListener;
    private onViewClick mViewClick;
    private int clickIndex = -1;
    private int startRawX;
    private int startRawY;


    private List<Rect> rects;
    private List<Rect> circulars;

    private int mType;

    public BarChartView(Context context) {
        super(context);
        this.context = context;
        init();

    }

    public BarChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.BarChartView);

        mAxisWidth = mTypedArray.getInteger(R.styleable.BarChartView_axisWidth, mAxisWidth);
        mAxisColor = mTypedArray.getColor(R.styleable.BarChartView_axisColor, mAxisColor);
        mBarColor = mTypedArray.getColor(R.styleable.BarChartView_barColor, mBarColor);
        mXAxisTxtColor = mTypedArray.getColor(R.styleable.BarChartView_xAxisTxtColor, mXAxisTxtColor);
        mYAxisTxtColor = mTypedArray.getColor(R.styleable.BarChartView_yAxisTxtColor, mYAxisTxtColor);
        isHideGirdLine = mTypedArray.getBoolean(R.styleable.BarChartView_hideGirdLine, false);
        mYMaxValue = mTypedArray.getInteger(R.styleable.BarChartView_max, 200);
        mBarPressEnable = mTypedArray.getBoolean(R.styleable.BarChartView_barPressEnable, false);
        mType = mTypedArray.getInteger(R.styleable.BarChartView_type, 0);
        mRadius = mTypedArray.getInteger(R.styleable.BarChartView_radius, dp2px(3));
        mTypedArray.recycle();

        init();
    }


    private void init() {
        //轴画笔
        axisPaint = new Paint();
        axisPaint.setStrokeWidth(mAxisWidth);
        axisPaint.setColor(mAxisColor);
        axisPaint.setStyle(Paint.Style.STROKE);
        axisPaint.setAntiAlias(true);

        //y字体画笔
        txtYPaint = new Paint();
        txtYPaint.setColor(mYAxisTxtColor);
        txtYPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        txtYPaint.setAntiAlias(true);
        txtYPaint.setTextAlign(Paint.Align.RIGHT);
        txtYPaint.setTextSize(mTxtSize);

        //yGird
        yPaint = new Paint();
        yPaint.setColor(mXGirdLine);
        yPaint.setStrokeWidth(mXGirdLineWidth);

        //柱状图画笔
        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);
        rectPaint.setTextSize(sp2px(15));
        rectPaint.setColor(mBarColor);

        //x字体画笔
        txtXPaint = new Paint();
        txtXPaint.setColor(mXAxisTxtColor);
        txtXPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        txtXPaint.setAntiAlias(true);
        txtXPaint.setTextAlign(Paint.Align.CENTER);
        txtXPaint.setTextSize(mTxtSize);

        rects = new ArrayList<>();
        circulars = new ArrayList<>();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mType == 0) {
            drawRect(canvas);
        } else {
            drawLineChar(canvas);
        }
        drawAxis(canvas);
        drawGrid(canvas);
        drawYLables(canvas);
    }


    private void drawLineChar(Canvas canvas) {
        int startX = dp2px(50);
        int rectWidth = dp2px(35);
        int widthPixels = context.getResources().getDisplayMetrics().widthPixels - dp2px(35);
        int endX = getWidth();
        int rangeX = endX - startX;

        if (startX + rectWidth * progress.length + rectWidth <= widthPixels) {
            circulars.clear();
            for (int i = 0; i < progress.length; i++) {


                if (mBarPressEnable && clickIndex == i) {
                    rectPaint.setColor(mBarPressColor);
                } else {
                    rectPaint.setColor(mBarColor);
                }

                Rect e = new Rect(
                        startX + rectWidth * i - mRadius+2,
                        caculate(progress[i]) - mRadius+2,
                        startX + rectWidth * i + mRadius+2,
                        caculate(progress[i]) + mRadius+2);
                circulars.add(e);
                canvas.drawCircle(startX + rectWidth * i, caculate(progress[i]), mRadius, rectPaint);
                rectPaint.setColor(mBarColor);
                if (i != progress.length - 1) {
                    canvas.drawLine(
                            startX + rectWidth * i,
                            caculate(progress[i]),
                            startX + rectWidth * (i + 1),
                            caculate(progress[i + 1]),
                            rectPaint
                    );
                }
                canvas.drawText((i + 1) + "", startX + rectWidth * i + sp2px(8), getHeight() - dp2px(15), txtXPaint);

            }
        } else {
            int range = rangeX / progress.length;
            circulars.clear();
            for (int i = 0; i < progress.length; i++) {


                if (mBarPressEnable && clickIndex == i) {
                    rectPaint.setColor(mBarPressColor);
                } else {
                    rectPaint.setColor(mBarColor);
                }

                Rect e = new Rect(
                        startX + range * i - mRadius+2,
                        caculate(progress[i]) - mRadius+2,
                        startX + range * i + mRadius+2,
                        caculate(progress[i]) + mRadius+2);
                circulars.add(e);
                canvas.drawCircle(startX + range * i, caculate(progress[i]), mRadius, rectPaint);
                rectPaint.setColor(mBarColor);
                if (i != progress.length - 1) {
                    canvas.drawLine(
                            startX + range * i,
                            caculate(progress[i]),
                            startX + range * (i + 1),
                            caculate(progress[i + 1]),
                            rectPaint
                    );
                }
                canvas.drawText((i + 1) + "", startX + range * i + (range / 2), getHeight() - dp2px(15), txtXPaint);
            }
        }
    }


    private void drawRect(Canvas canvas) {
        int startX = dp2px(35);
        int padding = dp2px(2);
        int rectWidth = dp2px(16);
        int widthPixels = context.getResources().getDisplayMetrics().widthPixels - dp2px(35);
        int endX = getWidth();
        int rangeX = endX - startX;
        //超过屏幕长度
        if (startX + rectWidth * progress.length + rectWidth <= widthPixels) {
            rects.clear();
            for (int i = 0; i < progress.length; i++) {
                Rect rect = new Rect();
                rect.left = startX + rectWidth * i + padding;
                rect.top = caculate(progress[i]);
                rect.right = startX + rectWidth * i + rectWidth;
                rect.bottom = getHeight() - dp2px(28);

                if (mBarPressEnable && clickIndex == i) {
                    rectPaint.setColor(mBarPressColor);
                } else {
                    rectPaint.setColor(mBarColor);
                }

                if (progress[i] != 0) {
                    canvas.drawRect(rect, rectPaint);
                }
                rects.add(rect);
                canvas.drawText((i + 1) + "", startX + rectWidth * i + sp2px(8), getHeight() - dp2px(15), txtXPaint);
            }
        } else {
            rects.clear();
            int range = rangeX / progress.length;
            for (int i = 0; i < progress.length; i++) {
                Rect rect = new Rect();
                rect.left = startX + range * i + padding;
                rect.top = caculate(progress[i]);
                rect.right = startX + range * i + range;
                rect.bottom = getHeight() - dp2px(28);
                if (mBarPressEnable && clickIndex == i) {
                    rectPaint.setColor(mBarPressColor);
                } else {
                    rectPaint.setColor(mBarColor);
                }

                if (progress[i] != 0) {
                    canvas.drawRect(rect, rectPaint);
                }
                if (i % 3 == 0) {
                    canvas.drawText((i + 1) + "", startX + range * i + (range / 2), getHeight() - dp2px(15), txtXPaint);
                }

                rects.add(rect);
            }
        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();
        int rawX = (int) event.getRawX();
        int rawY = (int) event.getRawY();
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startRawX = rawX;
                startRawY = rawY;
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                if (mType == 1) {

                    for (int i = 0; i < circulars.size(); i++) {
                        Rect rect = circulars.get(i);
                        if (rect.right >= x && x >= rect.left && rect.top <= y && rect.bottom >= y) {
                            if (mViewClick != null) {
                                mViewClick.onClick(i, (int) progress[i]);
                                clickIndex = i;
                                if (mBarPressEnable) {
                                    postInvalidate();
                                }
                            }
                        }
                    }
                } else {

                    for (int i = 0; i < rects.size(); i++) {
                        Rect rect = rects.get(i);
                        if (rect.right >= x && x >= rect.left && rect.top <= y && rect.bottom >= y) {
                            if (mViewClick != null) {
                                mViewClick.onClick(i, (int) progress[i]);
                                clickIndex = i;
                                if (mBarPressEnable) {
                                    postInvalidate();
                                }
                            }
                        }
                    }
                }
                if (x + getLeft() < getRight() && y + getTop() < getBottom()) {
                    if (mListener != null) {
                        mListener.onClick(this);
                    }
                }
                break;
        }
        return true;

    }


    @Override
    public void setOnClickListener(OnClickListener l) {
        mListener = l;
    }

    public void setOnViewClick(onViewClick click) {
        this.mViewClick = click;
    }

    public interface onViewClick {

        void onClick(int index, int value);
    }

    //计算每个点对应的位子
    private int caculate(float value) {
        int totalHeight = getHeight() - dp2px(35);
        double offset = totalHeight / 5;
        double startHeiht = totalHeight - (offset * 4);
        double rangeHeiht = totalHeight - startHeiht;
        return (int) (totalHeight - (rangeHeiht * (value / mYMaxValue)));
    }

    //画y轴的标签和横向表格
    private void drawYLables(Canvas canvas) {
        int height = getHeight() - dp2px(30);
        int offset = height / 5;
        int width = getWidth();
        int value = 50;
        canvas.drawText(0 + "", dp2px(25), height, txtYPaint);
        for (int i = 1; i < 5; i++) {
            canvas.drawText(value + "", dp2px(25), height - (offset * i), txtYPaint);
            if (!isHideGirdLine) {
                canvas.drawLine(dp2px(30), height - (offset * i) - sp2px(4), width, height - (offset * i) - sp2px(4), yPaint);
            }
            value += mYMaxValue / 4;
        }

    }

    private void drawGrid(Canvas canvas) {

    }

    //画轴
    private void drawAxis(Canvas canvas) {
        int width = getWidth();
        int height = getHeight() - dp2px(30);
        // 绘制底部的线条
        canvas.drawLine(dp2px(30), height + dp2px(3), width, height
                + dp2px(3), axisPaint);
        //绘制y轴
        canvas.drawLine(dp2px(30), 0, dp2px(30), height + dp2px(3), axisPaint);
    }


    public void setHideGirdLine(boolean hide) {
        this.isHideGirdLine = hide;
        postInvalidate();
    }

    public void setBarPressEnable(boolean enable) {
        if (!enable) {
            clickIndex = -1;
        }
        this.mBarPressEnable = enable;
    }

    public void setBarChartList(float[] progress) {
        this.progress = progress;
        postInvalidate();
    }


    public void setCharType(int type) {
        this.mType = type;
        postInvalidate();
    }

    private int dp2px(int value) {
        float v = getContext().getResources().getDisplayMetrics().density;
        return (int) (v * value + 0.5f);
    }

    private int sp2px(int value) {
        float v = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (v * value + 0.5f);
    }


}
