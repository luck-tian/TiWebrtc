package com.hhtc.dialer.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.hhtc.dialer.R;

import java.util.Arrays;
import java.util.List;

/**
 * TODO: document your custom view class.
 */
public class DialerContactBarView extends View {

    private static final int DEFULT_SIZE = 16;
    /**
     * 索引字符
     */
    public static String[] INDEX_STRING = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};


    private List<String> indexs;

    private Paint mPaint;

    private int mWidth, mHeight;

    private int mGapHeight;

    private int selectColor;

    private IndexPressedListener listener;

    public DialerContactBarView(Context context) {
        super(context);
        init(null, 0);
    }

    public DialerContactBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public DialerContactBarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.DialerContactBarView, defStyle, 0);
        int textColor = typedArray.getColor(R.styleable.DialerContactBarView_TextColor, Color.RED);
        int textSize = typedArray.getDimensionPixelSize(R.styleable.DialerContactBarView_TextSize, DEFULT_SIZE);
        selectColor = typedArray.getColor(R.styleable.DialerContactBarView_selectBackground, Color.GREEN);
        typedArray.recycle();

        indexs = Arrays.asList(INDEX_STRING);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(textSize);
        mPaint.setColor(textColor);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int measureWidth = 0;
        int measureHeight = 0;

        Rect indexBound = new Rect();

        String index;
        for (int i = 0; i < indexs.size(); i++) {
            index = indexs.get(i);
            mPaint.getTextBounds(index, 0, index.length(), indexBound);
            measureWidth = Math.max(indexBound.width(), measureWidth);
            measureHeight = Math.max(indexBound.height(), measureHeight);
        }

        measureHeight *= indexs.size();

        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                measureWidth = widthSize;
                break;
            case MeasureSpec.AT_MOST:
                measureWidth = Math.min(measureWidth, widthSize);
                break;
            case MeasureSpec.UNSPECIFIED:

                break;
        }

        ViewGroup mViewGroup = (ViewGroup) getParent();
        if (null != mViewGroup) {
            int height = mViewGroup.getHeight();
            if (height == 0) {
                measureHeight = mViewGroup.getMeasuredHeight();
            } else {
                measureHeight = height;
            }
        }
        setMeasuredDimension(measureWidth, measureHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int top = getPaddingTop();
        String index;
        for (int i = 0; i < indexs.size(); i++) {
            index = indexs.get(i);
            Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
            int baseline = (int) ((mGapHeight - fontMetrics.bottom - fontMetrics.top) / 2);
            canvas.drawText(index, mWidth / 2 - mPaint.measureText(index) / 2, top + mGapHeight * i + baseline, mPaint);
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mGapHeight = (mHeight - getPaddingTop() - getPaddingBottom()) / indexs.size();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setBackgroundColor(selectColor);
                break;
            case MotionEvent.ACTION_MOVE:
                float moveY = event.getY();
                int pressI = (int) ((moveY - getPaddingTop()) / mGapHeight);
                if (pressI < 0) {
                    pressI = 0;
                } else if (pressI >= indexs.size()) {
                    pressI = indexs.size() - 1;
                }
                if (null != listener && pressI > -1 && pressI < indexs.size()) {
                    listener.onIndexPressed(pressI, indexs.get(pressI));
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            default:
                setBackgroundResource(android.R.color.transparent);
                if (null != listener) {
                    listener.onMotionEventEnd();
                }
                break;
        }
        return true;
    }


    public interface IndexPressedListener {
        void onIndexPressed(int index, String text);

        void onMotionEventEnd();
    }

    public void setListener(IndexPressedListener listener) {
        this.listener = listener;
    }
}
