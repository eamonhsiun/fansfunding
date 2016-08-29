package com.fansfunding.fan.message.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.activeandroid.query.Select;
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
    private ListView listView;

    //适配器
    private NotificationAdapter notificationAdapter;

    //通知数据
    private List<Notifications> notificationses = new Select().from(Notifications.class).orderBy("id desc").execute();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notification, container, false);
        listView = (ListView) rootView.findViewById(R.id.lv_message_notification);
        notificationAdapter = new NotificationAdapter(getContext(), R.layout.item_notification, notificationses);
        listView.setAdapter(notificationAdapter);
        notificationAdapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Notifications notifications = notificationses.get(position);
                Gson gson = new GsonBuilder().create();
                NotificationProject notificationProject = new NotificationProject();
                notificationProject = gson.fromJson(notifications.getJson(), notificationProject.getClass());
                switch (notificationProject.getType()) {
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
                        Intent intent = new Intent();
                        ProjectInfo detail = notificationProject.getReference();
                        intent.setAction(getString(R.string.activity_project_detail));
                        intent.putExtra("detail",detail);
                        startActivity(intent);
                        break;
                        //用户关注
                    case 5:


                }
            }
        });
        return rootView;
    }
}
