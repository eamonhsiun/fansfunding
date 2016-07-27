package com.fansfunding.fan.project.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fansfunding.fan.R;
import com.fansfunding.internal.ProjectDetailReward;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by 13616 on 2016/7/19.
 */
public class ProjectDetailRewardAdapter extends BaseAdapter {

    //容器
    private Activity context;

    //数据
    private List<ProjectDetailReward.ProjectReward> list;

    public ProjectDetailRewardAdapter(Activity context){
        this.context=context;
        list=new LinkedList<ProjectDetailReward.ProjectReward>();
    }

    //将数据添加到头部
    public void addItem(ProjectDetailReward.ProjectReward reward){
        if(reward==null){
            return;
        }
        if(list!=null){
            //如果已经存在该评论，则返回
            for(int i=0;i<list.size();i++){
                if(list.get(i).getId()==reward.getId()){
                    return;
                }
            }
            list.add(reward);

        }else{
            list=new LinkedList<ProjectDetailReward.ProjectReward>();
            list.add(reward);
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
        View rootView=View.inflate(context,R.layout.item_project_detail_reward,null);
        //回报图片
        ImageView iv_project_detail_reward_image=(ImageView)rootView.findViewById(R.id.iv_project_detail_reward_image);
        //回报标题
        TextView tv_project_detail_reward_support=(TextView)rootView.findViewById(R.id.tv_project_detail_reward_support);
        //回报内容
        TextView tv_project_detail_reward_information=(TextView)rootView.findViewById(R.id.tv_project_detail_reward_information);

        if(list.get(position)==null) {
            return null;
        }
        else {
            //设置回报标题
            if(list.get(position).getTitle()!=null){
                tv_project_detail_reward_support.setText(list.get(position).getLimitation().toString());
            }
            //设置回报描述
            if(list.get(position).getDescription()!=null){
                tv_project_detail_reward_information.setText(list.get(position).getDescription());
            }
            //设置回报图像
            if(list.get(position).getImages()!=null&&list.get(position).getImages().size()>0&&list.get(position).getImages().get(0).equals("")==false){
                Picasso.with(context).load(context.getString(R.string.url_resources)+list.get(position).getImages().get(0)).into(iv_project_detail_reward_image);
            }
        }

        return rootView;


    }
}
