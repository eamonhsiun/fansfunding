package com.fansfunding.fan.message.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fansfunding.fan.R;
import com.fansfunding.fan.message.entity.CommentDynamic;
import com.fansfunding.fan.message.entity.CommentsProject;
import com.fansfunding.fan.message.model.Comments;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by RJzz on 2016/8/29.
 */

public class CommentsAdapter extends ArrayAdapter<Comments> {
    private int resourceID;
    private Context context;
    private List<Comments> commentsList;


    public CommentsAdapter(Context context, int resource, List<Comments> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resourceID = resource;
        commentsList = objects;
    }


    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Comments comments = getItem(position);
        final ViewHolder viewHolder;
        final View view;
        if(convertView == null) {
            view = LayoutInflater.from(context).inflate(resourceID, null);
            if(comments.isRead()) {
                view.setBackgroundResource(R.color.colorDividerGrey);
            }
            viewHolder = new ViewHolder();
            viewHolder.circleImageView = (CircleImageView) view.findViewById(R.id.iv_message_comment_head);
            viewHolder.name = (TextView) view.findViewById(R.id.tv_message_comment_name);
            viewHolder.mine = (TextView) view.findViewById(R.id.tv_message_comment_mine);
            viewHolder.info = (TextView) view.findViewById(R.id.tv_message_comment_info);
            viewHolder.time = (TextView) view.findViewById(R.id.tv_message_comment_time);
            view.setTag(viewHolder);
        } else {
            if(comments.isRead()) {
                convertView.setBackgroundResource(R.color.colorDividerGrey);
            }
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        Gson gson = new GsonBuilder().create();
        CommentsProject commentsProject = new CommentsProject();
        CommentDynamic commentDynamic = new CommentDynamic();
        switch (comments.getType()) {
            case 1:
                commentsProject = gson.fromJson(comments.getJson(), commentsProject.getClass());
                //头像
                Picasso.with(context).load(context.getString(R.string.url_resources)+commentsProject.getCommenter().getHead()).into(viewHolder.circleImageView);
                //昵称
                viewHolder.name.setText(commentsProject.getCommenter().getNickname());
                //时间
                viewHolder.time.setText(new SimpleDateFormat("MM-dd hh:mm").format(new Date(commentsProject.getTime())));
                break;
            case 2:

                commentDynamic = gson.fromJson(comments.getJson(), commentDynamic.getClass());
                //头像
                Picasso.with(context).load(context.getString(R.string.url_resources)+commentDynamic.getCommenter().getHead()).into(viewHolder.circleImageView);
                //昵称
                viewHolder.name.setText(commentDynamic.getCommenter().getNickname());
                //时间
                viewHolder.time.setText(new SimpleDateFormat("MM-dd hh:mm").format(new Date(commentDynamic.getTime())));
                break;
            default:
                break;
        }

        //设置评论的类型
        switch (comments.getType()) {
            //项目相关评论
            case 1:
                viewHolder.info.setText(comments.getComment());
                viewHolder.mine.setText("我发起的项目: " + commentsProject.getPointTo().getName());
                break;
            //动态相关评论
            case 2:
                viewHolder.info.setText("回复我: " + comments.getComment());
                viewHolder.mine.setText("我的动态: " + commentDynamic.getPointTo().getContent());
                break;
        }
        return view;
    }

    class ViewHolder {
        de.hdodenhof.circleimageview.CircleImageView circleImageView;
        TextView name;
        TextView mine;
        TextView time;
        TextView info;

    }
}
