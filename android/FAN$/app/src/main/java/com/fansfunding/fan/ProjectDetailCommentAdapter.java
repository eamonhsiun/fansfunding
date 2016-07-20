package com.fansfunding.fan;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fansfunding.internal.AllProjectInCategory;
import com.fansfunding.internal.ProjectDetailComment;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by 13616 on 2016/7/19.
 */
public class ProjectDetailCommentAdapter extends BaseAdapter {


    private Activity context;
    private List<ProjectDetailComment.ProjectComment> list;

    public ProjectDetailCommentAdapter(Activity context){
        this.context=context;
        list=new LinkedList<ProjectDetailComment.ProjectComment>();
    }


    public void addItem(ProjectDetailComment.ProjectComment comment){
        if(comment==null){
            return;
        }
        if(list!=null){
            //如果已经存在该评论，则返回
            for(int i=0;i<list.size();i++){
                if(list.get(i).getId()==comment.getId()){
                    return;
                }
            }
            list.add(comment);

        }else{
            list=new LinkedList<ProjectDetailComment.ProjectComment>();
            list.add(comment);
        }


    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        if(position>=0&&position<list.size()){
            return list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(position<0||position>=list.size()){
            return null;
        }

        View rootView=View.inflate(context,R.layout.item_comment,null);
        //评论人头像
        de.hdodenhof.circleimageview.CircleImageView iv_project_detail_dynamic_head=(de.hdodenhof.circleimageview.CircleImageView)rootView.findViewById(R.id.iv_project_detail_dynamic_head);
        //评论人昵称
        TextView tv_news_detail_commenter_name=(TextView)rootView.findViewById(R.id.tv_news_detail_commenter_name);
        //评论内容
        TextView tv_news_detail_comment=(TextView)rootView.findViewById(R.id.tv_news_detail_comment);

        //评论时间
        TextView tv_news_detail_comment_tine=(TextView)rootView.findViewById(R.id.tv_news_detail_comment_tine);

        if(list.get(position)==null) {
            return null;
        }
        else {
            if(list.get(position).getCommenterHead()!=null&&list.get(position).getCommenterHead().equals("")==false){
                Picasso.with(context).load(context.getString(R.string.url_resources)+list.get(position).getCommenterHead()).into(iv_project_detail_dynamic_head);
            }
            if(list.get(position).getCommenterNickname()!=null){
                tv_news_detail_commenter_name.setText(list.get(position).getCommenterName());
            }
            if(list.get(position).getContent()!=null){
                if(list.get(position).getPointTo()==0){
                    tv_news_detail_comment.setText(list.get(position).getContent());
                }else{
                    String comment="回复 "+list.get(position).getPointToName()
                            +": "
                            +list.get(position).getContent();
                    tv_news_detail_comment.setText(comment);
                }

            }
            tv_news_detail_comment_tine.setText(new SimpleDateFormat("MM-dd HH:mm").format(new Date(list.get(position).getCommentTime())));

        }
        return rootView;

    }
}
