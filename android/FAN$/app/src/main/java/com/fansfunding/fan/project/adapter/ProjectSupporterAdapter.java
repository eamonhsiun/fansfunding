package com.fansfunding.fan.project.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fansfunding.fan.R;
import com.fansfunding.internal.ProjectDetailReward;
import com.fansfunding.internal.ProjectInfo;
import com.fansfunding.internal.project.ProjectSupportsInfo;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 13616 on 2016/8/10.
 */
public class ProjectSupporterAdapter extends BaseAdapter {

    private List<ProjectSupportsInfo.SupportInfo> list;

    private Activity context;

    public ProjectSupporterAdapter(Activity context){
        this.context=context;
        list=new ArrayList<ProjectSupportsInfo.SupportInfo>();
    }

    public void addItem(ProjectSupportsInfo.SupportInfo info){
        if(info==null){
            return;
        }
        list.add(info);
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
        ProjectSupportsInfo.SupportInfo info=list.get(position);
        if(info==null){
            return null;
        }

        final View rootView;
        final ViewHolder viewHolder;

        if(convertView==null){
            rootView  = LayoutInflater.from(context).inflate(R.layout.item_project_supporter_info, null);
            viewHolder = new ViewHolder();
            viewHolder.iv_project_supporter_head=(CircleImageView)rootView.findViewById(R.id.iv_project_supporter_head);
            viewHolder.tv_project_supporter_nickname=(TextView)rootView.findViewById(R.id.tv_project_supporter_nickname);
            viewHolder.tv_project_supporter_money=(TextView)rootView.findViewById(R.id.tv_project_supporter_money);

            rootView.setTag(viewHolder);
        }else {
            rootView=convertView;
            viewHolder = (ViewHolder) rootView.getTag();
        }

        if(info.getHead()!=null&&info.getHead().equals("")==false){
            Picasso.with(context).load(context.getString(R.string.url_resources)+info.getHead()).memoryPolicy(MemoryPolicy.NO_CACHE).into(viewHolder.iv_project_supporter_head);
        }
        viewHolder.tv_project_supporter_nickname.setText(info.getNickname());



        return rootView;
    }


    private class ViewHolder{
        //支持者头像
        CircleImageView iv_project_supporter_head;

        //支持者昵称
        TextView tv_project_supporter_nickname;

        //支持者支持金额
        TextView tv_project_supporter_money;
    }
}
