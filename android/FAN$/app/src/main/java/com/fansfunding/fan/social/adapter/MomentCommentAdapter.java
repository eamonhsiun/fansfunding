package com.fansfunding.fan.social.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.fansfunding.fan.R;
import com.fansfunding.fan.utils.StartHomepage;
import com.fansfunding.internal.project.ProjectSupportsInfo;
import com.fansfunding.internal.social.MomentComment;
import com.rockerhieu.emojicon.EmojiconTextView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by 13616 on 2016/8/29.
 */
public class MomentCommentAdapter extends BaseAdapter {

    private List<MomentComment.DataBean.ListBean> commentlist;

    private Activity context;

    public MomentCommentAdapter(Activity context){
        this.context=context;
        commentlist=new ArrayList<MomentComment.DataBean.ListBean>();
    }

    public void addItem(MomentComment.DataBean.ListBean comment){
        if(comment==null){
            return;
        }
        commentlist.add(comment);
    }

    public void clear(){
        commentlist=new ArrayList<>();
    }

    @Override
    public int getCount() {
        return commentlist.size();
    }

    @Override
    public MomentComment.DataBean.ListBean getItem(int position) {
        if(position<0||position>=commentlist.size()){
            return null;
        }
        return commentlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(position<0||position>=commentlist.size()){
            return null;
        }
        MomentComment.DataBean.ListBean comment=commentlist.get(position);
        if(comment==null){
            return null;
        }

        final View rootView;
        final ViewHolder viewHolder;
        if(convertView==null){
            rootView  = LayoutInflater.from(context).inflate(R.layout.item_comment, null);
            viewHolder = new ViewHolder();
            viewHolder.iv_project_detail_dynamic_head=(CircleImageView)rootView.findViewById(R.id.iv_project_detail_dynamic_head) ;
            viewHolder.tv_news_detail_commenter_name=(TextView)rootView.findViewById(R.id.tv_news_detail_commenter_name) ;
            viewHolder.tv_news_detail_comment_time=(TextView)rootView.findViewById(R.id.tv_news_detail_comment_time) ;
            viewHolder.tv_news_detail_comment=(EmojiconTextView)rootView.findViewById(R.id.tv_news_detail_comment) ;
            rootView.setTag(viewHolder);
        }else {
            rootView=convertView;
            viewHolder = (ViewHolder) rootView.getTag();
        }

        //设置评论者头像
        if(comment.getUser().getHead()!=null&&comment.getUser().getHead().equals("")==false){
            Picasso.with(context).load(context.getString(R.string.url_resources)+comment.getUser().getHead()).memoryPolicy(MemoryPolicy.NO_CACHE).into(viewHolder.iv_project_detail_dynamic_head);
        }
        viewHolder.iv_project_detail_dynamic_head.setOnClickListener(new StartHomepage(context,comment.getUser().getId()));
        //设置评论人昵称
        viewHolder.tv_news_detail_commenter_name.setText(comment.getUser().getNickname());
        viewHolder.tv_news_detail_commenter_name.setOnClickListener(new StartHomepage(context,comment.getUser().getId()));

        //设置评论时间
        viewHolder.tv_news_detail_comment_time.setText(new SimpleDateFormat("MM-dd HH:mm").format(new Date(comment.getPostTime())));
        //设置评论的内容
        if(comment.getReplyTo()!=null){
            String content="回复 "+comment.getReplyTo().getNickname()
                    +": "
                    +comment.getContent();
            viewHolder.tv_news_detail_comment.setText(content);
        }else {
            viewHolder.tv_news_detail_comment.setText(comment.getContent());
        }

        return rootView;
    }


    class ViewHolder{
        //评论人头像
        CircleImageView iv_project_detail_dynamic_head;

        //评论人昵称
        TextView tv_news_detail_commenter_name;

        //发表日期
        TextView tv_news_detail_comment_time;

        //评论内容
        EmojiconTextView tv_news_detail_comment;


    }
}
