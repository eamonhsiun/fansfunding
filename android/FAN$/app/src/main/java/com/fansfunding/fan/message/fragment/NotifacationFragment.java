package com.fansfunding.fan.message.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;
import com.fansfunding.app.App;
import com.fansfunding.fan.R;
import com.fansfunding.fan.message.adapter.NotificationAdapter;
import com.fansfunding.fan.message.entity.NotificationDynamic;
import com.fansfunding.fan.message.entity.NotificationProject;
import com.fansfunding.fan.message.model.Notifications;
import com.fansfunding.fan.social.activity.MomentActivity;
import com.fansfunding.internal.ProjectInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RJzz on 2016/8/26.
 */

public class NotifacationFragment extends Fragment {


    //userId
    private int userId;

    private App app;

    //更新ui
    public static final int UPDATE_UI = 100;

    private ListView listView;

    //适配器
    public static NotificationAdapter notificationAdapter;

    //通知数据
    public static List<Notifications> notificationses = new ArrayList<>();

    //当前未读的通知
    public static TextView unread;

    //全部标记为已读
    private ImageButton imageButton;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_UI:
//                    ActiveAndroid.beginTransaction();
//                    try {
//                        notificationses = new Select().from(Notifications.class).orderBy("id desc").where("isRead = ?", 0).execute();
//                    }finally {
//                        ActiveAndroid.endTransaction();
//                    }
                    int i  = new Select().from(Notifications.class).where("isRead = ? and userId  = ?", 0, userId).count();
                    unread.setText(i + "");
                    notificationAdapter.notifyDataSetChanged();
                    Log.d("PushService", "fucking");
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notification, container, false);
        unread = (TextView) rootView.findViewById(R.id.tv_message_notification_not_read);

        listView = (ListView) rootView.findViewById(R.id.lv_message_notification);
        notificationAdapter = new NotificationAdapter(getContext(), R.layout.item_notification, notificationses);
        listView.setAdapter(notificationAdapter);
        app = (App)getActivity().getApplication();
        //设置未读push的数量
        int i  = new Select().from(Notifications.class).where("isRead = ? and userId = ?", 0, userId).count();
        unread.setText(i + "");
        notificationAdapter.notifyDataSetChanged();
        imageButton = (ImageButton) rootView.findViewById(R.id.ib_message_notification);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("是否清空通知消息");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int d = 0;
                        ActiveAndroid.beginTransaction();
                        try {
                            d  = new Select().from(Notifications.class).where("isRead = ? and userId = ?", 0, userId).count();
                            for(int i = 0; i < notificationses.size(); ++i) {

                                //更新数据库
                                Notifications nb = Notifications.load(Notifications.class, notificationses.get(i).getId());
                                nb.setRead(true);
                                nb.setWillDelete(true);
                                nb.save();
                            }

                            ActiveAndroid.setTransactionSuccessful();
                        } finally {

                            app.getBadgeView().decrementBadgeCount(d);
                            notificationses.clear();
                            handler.sendEmptyMessage(UPDATE_UI);
                            ActiveAndroid.endTransaction();
                        }
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Notifications notifications = notificationses.get(position);
                Gson gson = new GsonBuilder().create();
                NotificationProject notificationProject = new NotificationProject();
                NotificationDynamic notificationDynamic = new NotificationDynamic();
                if(notifications.getType() == 1 || notifications.getType() == 3) {
                    notificationDynamic = gson.fromJson(notifications.getJson(), notificationDynamic.getClass());
                } else {
                    notificationProject = gson.fromJson(notifications.getJson(), notificationProject.getClass());
                }
                switch (notifications.getType()) {
                    //动态相关
                    //1 用户动态点赞
                    case 1:
                        Intent intentD = new Intent();
                        intentD.putExtra(MomentActivity.MOMENTID, notificationDynamic.getReference().getMomentId());
                        intentD.setAction(app.getApplicationContext().getString(R.string.activity_moment));
                        startActivityForResult(intentD, 1003);
                        //删除当前这条数据
//                        notificationses.remove(position);
                        //如果没有读过小红点数量减1
                        if(!notifications.getRead()) {
                            app.getBadgeView().decrementBadgeCount(1);
                            view.setBackgroundResource(R.color.colorDividerGrey);
                            //更新数据库
                            Notifications nb = Notifications.load(Notifications.class, notifications.getId());
                            nb.setRead(true);
                            nb.save();
                            handler.sendEmptyMessage(UPDATE_UI);
                        }
                        break;
                    case 3:
                        break;
                    //跳转到
                    case 2:
                        break;
                    //项目关注和项目动态更新
                    case 4:
                    case 6:

                        //跳转到相关的项目详情页
                        Intent intent = new Intent();
                        ProjectInfo detail = notificationProject.getReference();
                        intent.setAction(getString(R.string.activity_project_detail));
                        intent.putExtra("detail",detail);
                        intent.putExtra("page", 2);
                        startActivityForResult(intent, 1003);
//                        //删除当前这条数据
//                        notificationses.remove(position);

                        //如果没有读过小红点数量减1
                        if(!notifications.getRead()) {
                            app.getBadgeView().decrementBadgeCount(1);
                            view.setBackgroundResource(R.color.colorDividerGrey);
                            //更新数据库
                            Notifications nb = Notifications.load(Notifications.class, notifications.getId());
                            nb.setRead(true);
                            nb.save();
                            handler.sendEmptyMessage(UPDATE_UI);
                        }


                        break;
                        //用户关注.跳转个人主页
                    case 5:

                        break;

                    default:
                        break;
                }
                //将此通知标记为已读
//                Notifications comments = (Notifications) new Select().from(Notifications.class).where("id = ?", notifications.getId()).execute();
//                comments.setRead(true);
//                comments.save();

//                new Update(Notifications.class).set("isRead = 1").where("id = ?", notifications.getId()).execute();

            }
        });
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1003) {
            super.onActivityResult(requestCode, resultCode, data);
            handler.sendEmptyMessage(UPDATE_UI);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        handler.sendEmptyMessage(UPDATE_UI);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences s = getActivity().getSharedPreferences(getString(R.string.sharepreference_login_by_phone), Context.MODE_PRIVATE);
        userId = s.getInt("id", 0);
    }

    public void updateDb() {
        ActiveAndroid.beginTransaction();
        try {
            for(Notifications delete : notificationses) {
                new Update(Notifications.class).set("willDelete = ? and isRead = ?", 1, 1).where("id = ?", delete.getId()).execute();
            }
        } finally {
            app.getBadgeView().decrementBadgeCount(notificationses.size());
            notificationses.clear();
            handler.sendEmptyMessage(UPDATE_UI);
            ActiveAndroid.endTransaction();
        }
    }
}
