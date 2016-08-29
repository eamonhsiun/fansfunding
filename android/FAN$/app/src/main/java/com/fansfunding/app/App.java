package com.fansfunding.app;

import android.app.Application;
import android.content.Context;

import com.activeandroid.ActiveAndroid;
import com.jauker.widget.BadgeView;

/**
 * 全局资源
 * Created by RJzz on 2016/8/27.
 */

public class App extends Application {

    //消息数量的小红点
    private BadgeView badgeView;

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        //初始化,通知开启日志
        ActiveAndroid.initialize(this, true);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }

    public static Context getContext() {
        return context;
    }


    /**
     * 获取到小圆点对象，用于更改未读消息的数量
     *
     * @author RJzz
     * create at 2016/8/27 17:04
     */

    public BadgeView getBadgeView() {
        return this.badgeView;
    }

    /**
      *设置小圆点对象
      *@author RJzz
      *create at 2016/8/27 19:20
      */

    public void setBadgeView(BadgeView badgeView) {
        this.badgeView = badgeView;
    }
}
