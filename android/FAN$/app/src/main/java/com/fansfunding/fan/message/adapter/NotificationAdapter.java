package com.fansfunding.fan.message.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fansfunding.fan.R;
import com.fansfunding.fan.message.entity.NotificationProject;
import com.fansfunding.fan.message.model.Notifications;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.fansfunding.fan.R.id.iv_message_notification_head;

/**
 * Created by RJzz on 2016/8/28.
 */

public class NotificationAdapter extends ArrayAdapter<Notifications> {
    private Context context;
    List<Notifications> notificationsList;
    private int resourceId;

    public NotificationAdapter(Context context, int resource, List<Notifications> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resourceId = resource;
        notificationsList = objects;

    }


    @Override
    public int getCount() {
        return super.getCount();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Notifications notifications = getItem(position);
        final ViewHolder viewHolder;
        final View view;
        if(convertView == null) {
            view = LayoutInflater.from(context).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.circleImageView = (CircleImageView) view.findViewById(iv_message_notification_head);
            viewHolder.name = (TextView) view.findViewById(R.id.tv_message_notification_name);
            viewHolder.time = (TextView) view.findViewById(R.id.tv_message_notification_time);
            viewHolder.type = (TextView) view.findViewById(R.id.tv_message_notification_type);
            viewHolder.info = (TextView) view.findViewById(R.id.tv_message_notification_info);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        Gson gson = new GsonBuilder().create();
        NotificationProject notificationProject = new NotificationProject();
        notificationProject = gson.fromJson(notifications.getJson(), notificationProject.getClass());
        //头像
        Picasso.with(context).load(context.getString(R.string.url_resources)+notificationProject.getCauser().getHead()).into(viewHolder.circleImageView);
        //昵称
        viewHolder.name.setText(notificationProject.getCauser().getNickname());
        //时间
        viewHolder.time.setText(new SimpleDateFormat("MM-dd").format(new Date(notificationProject.getTime())));
        //设置通知的类型
        switch (notificationProject.getType()) {
            //用户动态关注通知
            case 1:
                viewHolder.type.setText("");
                break;
            //项目支持支付通知
            case 2:
                viewHolder.type.setText("");
                break;
            //用户动态转发通知
            case 3:
                viewHolder.type.setText("");
                break;
            //项目关注通知
            case 4:
                viewHolder.type.setText("关注了你的项目");
                viewHolder.info.setText(notificationProject.getReference().getName());
                break;
            //用户关注通知
            case 5:
                viewHolder.type.setText("关注了你");
                viewHolder.info.setText("显示个人简介");
                break;
            //项目动态通知
            case 6:
                viewHolder.type.setText("更新了项目动态");
                viewHolder.info.setText(notificationProject.getReference().getName());
                break;
        }
        return view;
    }


    class ViewHolder {
        de.hdodenhof.circleimageview.CircleImageView circleImageView;
        TextView name;
        TextView type;
        TextView time;
        TextView info;

    }
}
