package com.fansfunding.fan.search.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fansfunding.fan.R;
import com.fansfunding.fan.utils.StartHomepage;
import com.fansfunding.internal.AllProjectInCategory;
import com.fansfunding.internal.ProjectInfo;
import com.fansfunding.internal.SearchProject;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by 13616 on 2016/7/22.
 */
public class SearchProjectAdapter extends BaseAdapter {
    private List<ProjectInfo> list;

    private Activity context;
    public SearchProjectAdapter(Activity context){
        list=new LinkedList<ProjectInfo>();
        this.context=context;

    }

    public void addItemAtHead(ProjectInfo detail){
        if(detail==null){
            return;
        }
        if(list!=null){
            //如果已经存在该项目，则返回
            for(int i=0;i<list.size();i++){
                //判断项目id和所属类别是否重复
                if(list.get(i).getId()==detail.getId()
                        &&list.get(i).getCategoryId()==detail.getCategoryId()){
                    list.remove(i);
                    list.add(i,detail);
                    return;
                }
            }
            list.add(detail);
            Collections.sort(list);


        }else{
            list=new LinkedList<ProjectInfo>();
            list.add(detail);
        }


    }

    public void cleanItem(){
        list=new LinkedList<ProjectInfo>();
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
        ProjectInfo detail=list.get(position);

        final View rootView;
        final ViewHolder viewHolder;
        if(convertView == null) {
            rootView  = LayoutInflater.from(context).inflate(R.layout.item_project, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_PJ_publish_nickname=(TextView)rootView.findViewById(R.id.tv_PJ_publish_nickname);
            viewHolder.tv_PJ_name=(TextView)rootView.findViewById(R.id.tv_PJ_name);
            viewHolder.tv_PJ_intro=(TextView)rootView.findViewById(R.id.tv_PJ_intro);
            viewHolder.iv_PJ_image_1=(ImageView)rootView.findViewById(R.id.iv_PJ_image_1);
            viewHolder.tv_PJ_get_money=(TextView)rootView.findViewById(R.id.tv_PJ_get_money);
            viewHolder.tv_PJ_target_money=(TextView)rootView.findViewById(R.id.tv_PJ_target_money);
            viewHolder.tv_PJ_rate=(TextView)rootView.findViewById(R.id.tv_PJ_rate);
            viewHolder.progressBar_project_detail=(ProgressBar)rootView.findViewById(R.id.progressBar_project_detail);
            viewHolder.iv_PJ_publish_head=(CircleImageView)rootView.findViewById(R.id.iv_PJ_publish_head);
            viewHolder.tv_PJ_time_start=(TextView)rootView.findViewById(R.id.tv_PJ_time_start);
            viewHolder.tv_PJ_time_end=(TextView)rootView.findViewById(R.id.tv_PJ_time_end);
            rootView.setTag(viewHolder);
        }else  {
            rootView = convertView;
            viewHolder = (ViewHolder) rootView.getTag();
        }

        //设置控件的值
        viewHolder.tv_PJ_publish_nickname.setText(detail.getSponsorNickname());
        viewHolder.tv_PJ_publish_nickname.setOnClickListener(new StartHomepage(context,detail.getSponsor()));

        viewHolder.tv_PJ_name.setText(detail.getName());
        viewHolder.tv_PJ_intro.setText(detail.getDescription());
        if(context!=null&&detail.getCover()!=null&&detail.getCover().equals("")==false){
            Picasso.with(context).load(context.getString(R.string.url_resources)+detail.getCover()).resize(720,400).centerCrop().memoryPolicy(MemoryPolicy.NO_CACHE).into(viewHolder.iv_PJ_image_1);
        }
        viewHolder.tv_PJ_get_money.setText(new java.text.DecimalFormat("0.00").format(detail.getSum()));
        viewHolder. tv_PJ_target_money.setText(detail.getTargetMoney().toString());
        viewHolder.tv_PJ_rate.setText(String.valueOf((int)(100*(detail.getSum().doubleValue()/detail.getTargetMoney().doubleValue()))));
        viewHolder.progressBar_project_detail.setProgress((int)(100*(detail.getSum().doubleValue()/detail.getTargetMoney().doubleValue())));
        if(context!=null&&detail.getSponsorHead()!=null&&detail.getSponsorHead().equals("")==false){
            Picasso.with(context).load(context.getString(R.string.url_resources)+detail.getSponsorHead()).memoryPolicy(MemoryPolicy.NO_CACHE).into(viewHolder.iv_PJ_publish_head);
        }
        viewHolder.iv_PJ_publish_head.setOnClickListener(new StartHomepage(context,detail.getSponsor()));
        viewHolder.tv_PJ_time_start.setText(getStartTime(detail.getCreateTime()));
        viewHolder.tv_PJ_time_end.setText(getEndTime(detail.getTargetDeadline()));
        /*View rootView=View.inflate(context, R.layout.item_project,null);

        //发起人名称
        TextView tv_PJ_publish_nickname=(TextView)rootView.findViewById(R.id.tv_PJ_publish_nickname);
        tv_PJ_publish_nickname.setText(detail.getSponsorNickname());

        //项目名称
        TextView tv_PJ_name=(TextView)rootView.findViewById(R.id.tv_PJ_name);
        tv_PJ_name.setText(detail.getName());

        //项目介绍
        TextView tv_PJ_intro=(TextView)rootView.findViewById(R.id.tv_PJ_intro);
        tv_PJ_intro.setText(detail.getDescription());

        //项目图片
        ImageView iv_PJ_image_1=(ImageView)rootView.findViewById(R.id.iv_PJ_image_1);
        if(context!=null&&detail.getCover()!=null&&detail.getCover().equals("")==false){
            Picasso.with(context).load(context.getString(R.string.url_resources)+detail.getCover()).resize(720,400).centerCrop().into(iv_PJ_image_1);
        }
        //已筹金额
        TextView tv_PJ_get_money=(TextView)rootView.findViewById(R.id.tv_PJ_get_money);
        tv_PJ_get_money.setText(new java.text.DecimalFormat("0.00").format(detail.getSum()));

        //目标金额
        TextView tv_PJ_target_money=(TextView)rootView.findViewById(R.id.tv_PJ_target_money);
        tv_PJ_target_money.setText(detail.getTargetMoney().toString());

        //进度
        TextView tv_PJ_rate=(TextView)rootView.findViewById(R.id.tv_PJ_rate);
        tv_PJ_rate.setText(String.valueOf((int)(100*(detail.getSum().doubleValue()/detail.getTargetMoney().doubleValue()))));

        //进度条
        ProgressBar progressBar_project_detail=(ProgressBar)rootView.findViewById(R.id.progressBar_project_detail);
        progressBar_project_detail.setProgress((int)(100*(detail.getSum().doubleValue()/detail.getTargetMoney().doubleValue())));

        //发起人头像
        CircleImageView iv_PJ_publish_head=(CircleImageView)rootView.findViewById(R.id.iv_PJ_publish_head);
        if(context!=null&&detail.getSponsorHead()!=null&&detail.getSponsorHead().equals("")==false){
            Picasso.with(context).load(context.getString(R.string.url_resources)+detail.getSponsorHead()).into(iv_PJ_publish_head);
        }

        //开始时间
        TextView tv_PJ_time_start=(TextView)rootView.findViewById(R.id.tv_PJ_time_start);
        tv_PJ_time_start.setText(getStartTime(detail.getCreateTime()));


        //截止日期
        TextView tv_PJ_time_end=(TextView)rootView.findViewById(R.id.tv_PJ_time_end);
        tv_PJ_time_end.setText(getEndTime(detail.getTargetDeadline()));

*/
        return rootView;
    }

    //转化时间,获取开始的时间格式
    private String getStartTime(long milliscond){
        String time="";
        Date startDate=new Date(milliscond);
        Date now=new Date();

        int differ=(int)(now.getTime()/(1000*3600*24))-(int)(startDate.getTime()/(1000*3600*24));
        if(differ<0){
            time="已完成";
        }
        else if(differ==0){
            time="今天";
        }else if(differ==1){
            time="昨天";
        }else if(differ==2){
            time="前天";
        }else if(differ>2&&differ<7){
            time= new SimpleDateFormat("E").format(startDate);
        }else if(differ>=7){
            time=new SimpleDateFormat("MM-dd").format(startDate);
        }

        return time;
    }

    //获取截止时间格式
    private String getEndTime(long milliscond){
        String time="";
        Date endtDate=new Date(milliscond);
        Date now=new Date();
        int differ=(int)(endtDate.getTime()/(1000*3600*24))-(int)(now.getTime()/(1000*3600*24));
        if(differ<0){
            time="已完成";
        }else {
            time="还剩"+(differ+1)+"天";
        }
        return time;
    }
    class ViewHolder{
        //发起人名称
        TextView tv_PJ_publish_nickname;
        //项目名称
        TextView tv_PJ_name;
        //项目介绍
        TextView tv_PJ_intro;
        //项目图片
        ImageView iv_PJ_image_1;
        //已筹金额
        TextView tv_PJ_get_money;
        //目标金额
        TextView tv_PJ_target_money;
        //进度
        TextView tv_PJ_rate;
        //进度条
        ProgressBar progressBar_project_detail;
        //发起人头像
        CircleImageView iv_PJ_publish_head;
        //开始时间
        TextView tv_PJ_time_start;
        //截止日期
        TextView tv_PJ_time_end;
    }
}
