package com.fansfunding.app;

import android.app.Application;
import android.content.Context;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.fansfunding.fan.message.model.Notifications;
import com.jauker.widget.BadgeView;

import static com.fansfunding.fan.message.fragment.NotifacationFragment.notificationses;

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
        //每次程序初始化的时候将通知表中已读的通知删除
        new Delete().from(Notifications.class).where("isRead = 1").execute();
        //初始化通知数据
        notificationses = new Select().from(Notifications.class).orderBy("id desc").where("isRead = 0").execute();
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
