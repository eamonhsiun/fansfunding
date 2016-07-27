package com.fansfunding.fan;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fansfunding.internal.ProjectInfo;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 13616 on 2016/7/18.
 */
public class ListProjectAdapter extends BaseAdapter{

    List<ProjectInfo> listProjectDetail;

    private Activity context;
    public ListProjectAdapter(Activity context){
        listProjectDetail=new LinkedList<ProjectInfo>();
        this.context=context;

    }

    public void addItemAtHead(ProjectInfo detail){
        if(detail==null){
            return;
        }
        if(listProjectDetail!=null){
            //如果已经存在该项目，则返回
            for(int i=0;i<listProjectDetail.size();i++){
                //判断项目id和所属类别是否重复
                if(listProjectDetail.get(i).getId()==detail.getId()
                        &&listProjectDetail.get(i).getCategoryId()==detail.getCategoryId()){
                    listProjectDetail.remove(i);
                    listProjectDetail.add(i,detail);
                    return;
                }
            }
            listProjectDetail.add(detail);
            Collections.sort(listProjectDetail);


        }else{
            listProjectDetail=new LinkedList<ProjectInfo>();
            listProjectDetail.add(detail);
        }


    }

    public void Clear(){
        listProjectDetail=new LinkedList<ProjectInfo>();
    }

    public void addItemAtFoot(ProjectInfo detail){
        if(detail==null){
            return;
        }
        if(listProjectDetail!=null){
            //如果已经存在该项目，则返回
            for(int i=0;i<listProjectDetail.size();i++){
                //判断项目id和所属类别是否重复
                if(listProjectDetail.get(i).getId()==detail.getId()
                        &&listProjectDetail.get(i).getCategoryId()==detail.getCategoryId()){
                    listProjectDetail.remove(i);
                    listProjectDetail.add(detail);
                    return;
                }
            }
            listProjectDetail.add(0,detail);
            Collections.sort(listProjectDetail);

        }else{
            listProjectDetail=new LinkedList<ProjectInfo>();
            listProjectDetail.add(detail);
        }

    }
    @Override
    public int getCount() {
        return listProjectDetail.size();
    }

    @Override
    public Object getItem(int position) {
        if(position>=0&&position<listProjectDetail.size()){
            return listProjectDetail.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(position<0||position>=listProjectDetail.size()){
            return null;
        }
        ProjectInfo detail=listProjectDetail.get(position);
        View rootView=View.inflate(context,R.layout.item_project,null);

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
            Picasso.with(context).load(context.getString(R.string.url_resources)+detail.getCover()).resize(720,400).centerCrop().memoryPolicy(MemoryPolicy.NO_CACHE).into(iv_PJ_image_1);
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
            Picasso.with(context).load(context.getString(R.string.url_resources)+detail.getSponsorHead()).memoryPolicy(MemoryPolicy.NO_CACHE).into(iv_PJ_publish_head);
        }



        //开始时间
        TextView tv_PJ_time_start=(TextView)rootView.findViewById(R.id.tv_PJ_time_start);
        tv_PJ_time_start.setText(getStartTime(detail.getCreateTime()));


        //截止日期
        TextView tv_PJ_time_end=(TextView)rootView.findViewById(R.id.tv_PJ_time_end);
        tv_PJ_time_end.setText(getEndTime(detail.getTargetDeadline()));
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
}
