package com.fansfunding.fan.project.utils;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Eamon on 2016/7/19.
 */
public class MotionLessViewPager extends ViewPager {

    public MotionLessViewPager(Context context) {
        super(context);
    }

    public MotionLessViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
