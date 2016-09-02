package com.fansfunding.fan.message.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fansfunding.fan.R;
import com.fansfunding.fan.message.entity.PrivateLetter;
import com.fansfunding.fan.message.model.Content;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by RJzz on 2016/9/2.
 */


public class MsgAdapter extends ArrayAdapter<Content> {
    private int resoureID;
    public MsgAdapter(Context context, int textViewResourceId, List<Content> objects) {
        super(context, textViewResourceId, objects);
        resoureID = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Content content = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resoureID, null);
            viewHolder  = new ViewHolder();
            viewHolder.leftLayout = (RelativeLayout) view.findViewById(R.id.left_layout);
            viewHolder.rightLayout = (RelativeLayout) view.findViewById(R.id.right_layout);
            viewHolder.rightMsg = (TextView) view.findViewById(R.id.rigth_msg);
            viewHolder.lefMsg = (TextView) view.findViewById(R.id.left_msg);
            viewHolder.leftHead = (CircleImageView) view.findViewById(R.id.iv_message_left_head);
            viewHolder.rightHead = (CircleImageView) view.findViewById(R.id.iv_message_right_head);

            view.setTag(viewHolder);
        }
        else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        Gson gson = new GsonBuilder().create();
        PrivateLetter privateLetter = new PrivateLetter();
        privateLetter = gson.fromJson(content.getMessage().getJson(), privateLetter.getClass());
        Picasso.with(getContext()).load(getContext().getString(R.string.url_resources) + privateLetter.getSender().getHead()).into(viewHolder.leftHead);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(getContext().getString(R.string.sharepreference_login_by_phone), Context.MODE_PRIVATE);
        String head = sharedPreferences.getString("head", "");
        Picasso.with(getContext()).load(getContext().getString(R.string.url_resources) + head).into(viewHolder.rightHead);
        //接收消息
        if(content.getType() == 2) {
             viewHolder.leftLayout.setVisibility(View.VISIBLE);
            viewHolder.rightLayout.setVisibility(View.GONE);
            viewHolder.leftHead.setVisibility(View.VISIBLE);
            viewHolder.rightHead.setVisibility(View.GONE);
            viewHolder.lefMsg.setText(content.getContent());
        }
        //发送消息
        else if(content.getType() == 1) {
            viewHolder.rightLayout.setVisibility(View.VISIBLE);
            viewHolder.leftLayout.setVisibility(View.GONE);
            viewHolder.rightHead.setVisibility(View.VISIBLE);
            viewHolder.leftHead.setVisibility(View.GONE);
            viewHolder.rightMsg.setText(content.getContent());
        }
        return view;
    }

    class ViewHolder {
        RelativeLayout leftLayout;
        RelativeLayout rightLayout;
        TextView lefMsg;
        TextView rightMsg;
        de.hdodenhof.circleimageview.CircleImageView leftHead;
        de.hdodenhof.circleimageview.CircleImageView rightHead;
    }
}
