package com.fansfunding.fan.project.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fansfunding.fan.R;
import com.fansfunding.fan.utils.MyGridView;
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
        final View rootView;
        final ViewHolder viewHolder;
        if(convertView == null) {
            rootView = LayoutInflater.from(context).inflate(R.layout.item_project_detail_reward, null);
            viewHolder = new ViewHolder();

            viewHolder.tv_project_detail_reward_support=(TextView)rootView.findViewById(R.id.tv_project_detail_reward_support);
            viewHolder.tv_project_detail_reward_information=(TextView)rootView.findViewById(R.id.tv_project_detail_reward_information);
            viewHolder.gv_project_detail_reward_photo_list=(MyGridView)rootView.findViewById(R.id.gv_project_detail_reward_photo_list);
            viewHolder.ll_project_detail_reward_support_limit=(LinearLayout)rootView.findViewById(R.id.ll_project_detail_reward_support_limit);
            viewHolder.tv_project_detail_reward_support_times=(TextView)rootView.findViewById(R.id.tv_project_detail_reward_support_times);
            viewHolder.tv_project_detail_reward_support_ceiling=(TextView)rootView.findViewById(R.id.tv_project_detail_reward_support_ceiling);
            rootView.setTag(viewHolder);

        }else {
            rootView = convertView;
            viewHolder = (ViewHolder) rootView.getTag();
        }

        if(list.get(position)==null) {
            return null;
        }
        else {

            ProjectDetailDynamicPhotoAdapter photoAdapter=new ProjectDetailDynamicPhotoAdapter(context);

            viewHolder.tv_project_detail_reward_support.setText(list.get(position).getLimitation().toString());
            viewHolder.tv_project_detail_reward_information.setText(list.get(position).getDescription());
            viewHolder.gv_project_detail_reward_photo_list.setAdapter(photoAdapter);
            if(list.get(position).getCeiling()==-1){
                viewHolder.ll_project_detail_reward_support_limit.setVisibility(View.GONE);
            }else {
                viewHolder.ll_project_detail_reward_support_limit.setVisibility(View.VISIBLE);
                viewHolder.tv_project_detail_reward_support_ceiling.setText(""+list.get(position).getCeiling());
                viewHolder.tv_project_detail_reward_support_times.setText(""+list.get(position).getSupportTimes());
            }


            if(list.get(position).getImages()!=null&&list.get(position).getImages().size()>0){
                for(int i=0;i<list.get(position).getImages().size();i++){
                    //大于四张图则剩下的都不展示
                    if(i>=4){
                        break;
                    }
                    if(list.get(position).getImages().get(i)!=null&&list.get(position).getImages().get(i).equals("")==false){
                        photoAdapter.addItem(list.get(position).getImages().get(i));
                    }
                }

                switch (photoAdapter.getCount()){
                    case 1:
                        viewHolder.gv_project_detail_reward_photo_list.setNumColumns(1);
                        break;
                    case 2:
                        viewHolder.gv_project_detail_reward_photo_list.setNumColumns(2);
                        break;
                    case 3:
                        viewHolder.gv_project_detail_reward_photo_list.setNumColumns(3);
                        break;
                    case 4:
                        viewHolder.gv_project_detail_reward_photo_list.setNumColumns(2);
                        break;
                }
                photoAdapter.notifyDataSetChanged();
            }

        }

        /*View rootView=View.inflate(context,R.layout.item_project_detail_reward,null);

        //回报图片
        //ImageView iv_project_detail_reward_image=(ImageView)rootView.findViewById(R.id.iv_project_detail_reward_image);
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
*/
        return rootView;
    }

    private class ViewHolder{
        //回报标题
        TextView tv_project_detail_reward_support;
        //回报内容
        TextView tv_project_detail_reward_information;
        //图片展示
        MyGridView gv_project_detail_reward_photo_list;

        //回报已支持次数
        TextView tv_project_detail_reward_support_times;

        //回报限制
        TextView tv_project_detail_reward_support_ceiling;

        //回报限制显示区域
        LinearLayout ll_project_detail_reward_support_limit;
    }


}
