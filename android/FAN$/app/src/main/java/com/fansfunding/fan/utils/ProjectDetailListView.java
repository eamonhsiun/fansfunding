package com.fansfunding.fan.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.fansfunding.PullListView.LoadListView;
import com.fansfunding.PullListView.XListView;

/**
 * Created by 13616 on 2016/8/9.
 */
public class ProjectDetailListView extends XListView {

    private static final int MODE_IDLE = 0;
    private static final int MODE_HORIZONTAL = 1;
    private static final int MODE_VERTICAL = 2;

    private int scrollMode;
    private float downX, downY;

    boolean isAtTop = true; // 如果是true，则允许拖动至底部的下一页
    private int mTouchSlop = 4; // 判定为滑动的阈值，单位是像素

    public ProjectDetailListView(Context context) {
        super(context);
    }

    public ProjectDetailListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProjectDetailListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            downX = ev.getRawX();
            downY = ev.getRawY();
            isAtTop = isAtTop();
            scrollMode = MODE_IDLE;
            getParent().requestDisallowInterceptTouchEvent(true);
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            if (scrollMode == MODE_IDLE) {
                float xDistance = Math.abs(downX - ev.getRawX());
                float yDistance = Math.abs(downY - ev.getRawY());
                if (xDistance > yDistance && xDistance > mTouchSlop) {
                    scrollMode = MODE_HORIZONTAL;
                    getParent().requestDisallowInterceptTouchEvent(false);
                    return false;

                } else if (yDistance > xDistance && yDistance > mTouchSlop) {
                    scrollMode = MODE_VERTICAL;
                    if (downY < ev.getRawY() && isAtTop) {
                        getParent().requestDisallowInterceptTouchEvent(false);
                        return false;
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 判断listView是否在顶部
     *
     * @return 是否在顶部
     */
    public boolean isAtTop() {
        boolean resultValue = false;
        int childNum = getChildCount();
        if (childNum == 0) {
            // 没有child，肯定在顶部
            resultValue = true;
        } else {
            if (getFirstVisiblePosition() == 0) {
                // 根据第一个childView来判定是否在顶部
                View firstView = getChildAt(0);
                if (Math.abs(firstView.getTop()) < 2) {
                    resultValue = true;
                }
            }
        }

        return resultValue;
    }
}
