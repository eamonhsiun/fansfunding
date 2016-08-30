package com.fansfunding.fan.message.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.activeandroid.query.Update;
import com.fansfunding.app.App;
import com.fansfunding.fan.R;
import com.fansfunding.fan.message.adapter.NotificationAdapter;
import com.fansfunding.fan.message.entity.NotificationProject;
import com.fansfunding.fan.message.model.Notifications;
import com.fansfunding.internal.ProjectInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

/**
 * Created by RJzz on 2016/8/26.
 */

public class NotifacationFragment extends Fragment {
    private App app;


    //更新ui
    public static final int UPDATE_UI = 100;

    private ListView listView;

    //适配器
    public static NotificationAdapter notificationAdapter;

    //通知数据
    public static List<Notifications> notificationses;

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
                    unread.setText(notificationses.size() + "");
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
        unread.setText(notificationses.size() + "");
        //设置未读push的数量
        app = (App)getActivity().getApplication();
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
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //全部标记为已读
                                for(Notifications delete : notificationses) {
                                    ActiveAndroid.beginTransaction();
                                    try {
                                        new Update(Notifications.class).set("isRead = ?", 1).where("id = ?", delete.getId()).execute();
                                        app.getBadgeView().decrementBadgeCount(notificationses.size());
                                    } finally {
                                        ActiveAndroid.endTransaction();
                                    }
                                }
                            }
                        }).start();
                        notificationses.clear();
                        handler.sendEmptyMessage(UPDATE_UI);
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
                if(notifications.getType() == 1 || notifications.getType() == 3) {

                } else {
                    notificationProject = gson.fromJson(notifications.getJson(), notificationProject.getClass());
                }
                switch (notifications.getType()) {
                    //动态相关
                    case 1:
                    case 3:
                        break;
                    //跳转到
                    case 2:
                        break;
                    //项目关注和项目动态更新
                    case 4:
                    case 6:
                        //将此通知标记为已读
                        new Update(Notifications.class).set("isRead = ?", 1).where("id =  ? ", notifications.getId()).execute();
                        //跳转到相关的项目详情页
                        Intent intent = new Intent();
                        ProjectInfo detail = notificationProject.getReference();
                        intent.setAction(getString(R.string.activity_project_detail));
                        intent.putExtra("detail",detail);
                        intent.putExtra("page", 2);
                        startActivityForResult(intent, 1003);
                        //删除当前这条数据
                        notificationses.remove(position);
                        //小红点数量减1
                        app.getBadgeView().decrementBadgeCount(1);
                        break;
                        //用户关注
                    case 5:

                        break;

                    default:
                        break;
                }
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
    }
}
