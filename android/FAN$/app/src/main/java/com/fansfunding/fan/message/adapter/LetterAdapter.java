package com.fansfunding.fan.message.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fansfunding.fan.R;
import com.fansfunding.fan.message.entity.PrivateLetter;
import com.fansfunding.fan.message.model.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by RJzz on 2016/9/1.
 */

public class LetterAdapter extends ArrayAdapter<Message> {
    private int resourceId;


    private Context context;




    public LetterAdapter(Context context, int resource, List<Message> objects) {
        super(context, resource, objects);
        this.resourceId = resource;
        this.context = context;
    }


    @Override
    public int getCount() {
        return super.getCount();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message message = getItem(position);


        ViewHolder viewHolder = null;
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.circleImageView = (CircleImageView) convertView.findViewById(R.id.iv_message_letter_head);
            viewHolder.name = (TextView) convertView.findViewById(R.id.tv_message_letter_name);
            viewHolder.info = (TextView) convertView.findViewById(R.id.tv_message_letter_info);
            viewHolder.time = (TextView) convertView.findViewById(R.id.tv_message_letter_time);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        if(message.getRead()) {
            convertView.setBackgroundResource(R.color.colorDividerGrey);
        }else{
            convertView.setBackgroundResource(R.color.white);
        }
        Gson gson = new GsonBuilder().create();
        PrivateLetter privateLetter = new PrivateLetter();
        privateLetter = gson.fromJson(message.getJson(), privateLetter.getClass());

        viewHolder.info.setText("给您发送了私信");


        //加载头像
        Picasso.with(context).load(context.getString(R.string.url_resources) + privateLetter.getSender().getHead()).into(viewHolder.circleImageView);
        viewHolder.name.setText(privateLetter.getSender().getNickname());
        viewHolder.time.setText(new SimpleDateFormat("MM-dd hh-mm").format(new Date(privateLetter.getSendTime())));


        return convertView;
    }
    class ViewHolder {

        de.hdodenhof.circleimageview.CircleImageView circleImageView;
        TextView name;
        TextView time;
        TextView info;
    }
}
