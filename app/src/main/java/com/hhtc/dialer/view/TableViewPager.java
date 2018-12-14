package com.hhtc.dialer.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import java.util.Objects;


public class TableViewPager extends ViewPager {

    private boolean isPagingEnabled = true;

    public TableViewPager(Context context) {
        super(context);
        addOnPageChangeListener(pageChangeListener);
    }

    public TableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        addOnPageChangeListener(pageChangeListener);
    }

    private float startX;
    private float startY;

    private PagerSelectMove pagerSelectMove;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = true;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:


                if (getChildCount() == getCurrentItem() && startX == 0) {
                    startX = event.getX();
                    startY = event.getY();
                } else if (getChildCount() == getCurrentItem() &&
                        getCurrentItem() > 0) {
                    float endX = event.getX();
                    if ((endX - startX) < 0) {
                        result = false;
                    } else {
                        if (Objects.nonNull(pagerSelectMove)) {
                            pagerSelectMove.onPagerSelectMove();
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            default:
                startX = 0;
                break;
        }
        return result ? this.isPagingEnabled && super.onTouchEvent(event) : result;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onInterceptTouchEvent(event);
    }

    public void setPagingEnabled(boolean enable) {
        this.isPagingEnabled = enable;
    }


    public void setPagerSelectMove(PagerSelectMove pagerSelectMove) {
        this.pagerSelectMove = pagerSelectMove;
    }

    public interface PagerSelectMove {
        void onPagerSelectMove();
    }


    private ViewPager.OnPageChangeListener pageChangeListener = new OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {
        }

        @Override
        public void onPageSelected(int i) {

        }

        @Override
        public void onPageScrollStateChanged(int i) {
        }
    };
}
