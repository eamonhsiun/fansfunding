package com.fansfunding.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.fansfunding.fan.R;
import com.fansfunding.fan.message.model.Comments;
import com.fansfunding.fan.message.model.Content;
import com.fansfunding.fan.message.model.Message;
import com.fansfunding.fan.message.model.Notifications;
import com.jauker.widget.BadgeView;

import java.util.ArrayList;
import java.util.List;

import static com.fansfunding.fan.message.fragment.CommentFragment.commentses;
import static com.fansfunding.fan.message.fragment.NotifacationFragment.notificationses;
import static com.fansfunding.fan.message.fragment.PrivateLetterFragment.messages;

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
        ActiveAndroid.initialize(this);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.sharepreference_login_by_phone), MODE_PRIVATE);
        int id = sharedPreferences.getInt("id", 0);
        boolean isLogin = sharedPreferences.getBoolean("isLogin", false);
        //登陆则去加载数据
        if(isLogin) {
            //每次程序初始化的时候将表中已读的内容删除\
            ActiveAndroid.beginTransaction();
            try {
                new Delete().from(Notifications.class).where("willDelete = ? and userId = ?", 1, id).execute();
                new Delete().from(Comments.class).where("willDelete = ? and userId = ?", 1, id).execute();

                //初始化推送数据
                List<Message> messageList = new Select().from(Message.class).orderBy("time desc").where("userId = ? and willDelete = ?", id, 1).execute();
                List<Content> contentList = new ArrayList<>();
                if(messageList != null) {
                    for(int i = 0; i < messageList.size(); ++i) {
                        contentList = messageList.get(i).contents();
                        if(contentList != null) {
                            for(int j = 0; j < contentList.size(); ++j) {
                                contentList.get(j).delete();

                            }
                        }
                    }

                }

//                new Delete().from(Message.class).where("willDelete = ? and userId = ?", 1, id).execute();
                //首先要把关联的外键给删了，这个才可以删除，。嘿嘿嘿
                new Delete().from(Message.class).where("willDelete = ? and userId = ?", 1, id).execute();

                messages = new Select().from(Message.class).where("userId = ? and willDelete = ?", id, 0).execute();
                commentses = new Select().from(Comments.class).orderBy("id desc").where("userId = ?", id).execute();
                notificationses = new Select().from(Notifications.class).orderBy("id desc").where("userId = ?", id).execute();
                ActiveAndroid.setTransactionSuccessful();
            }finally {
                ActiveAndroid.endTransaction();
            }
        }
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
