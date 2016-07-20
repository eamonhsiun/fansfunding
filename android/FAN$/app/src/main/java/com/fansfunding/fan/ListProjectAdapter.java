package com.fansfunding.fan;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fansfunding.internal.AllProjectInCategory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by 13616 on 2016/7/18.
 */
public class ListProjectAdapter extends BaseAdapter{

    List<AllProjectInCategory.ProjectDetail> listProjectDetail;

    private Activity context;
    public ListProjectAdapter(Activity context){
        listProjectDetail=new LinkedList<AllProjectInCategory.ProjectDetail>();
        this.context=context;
    }

    public void addItemAtHead(AllProjectInCategory.ProjectDetail detail){
        if(detail==null){
            return;
        }
        if(listProjectDetail!=null){
            //如果已经存在该项目，则返回
            for(int i=0;i<listProjectDetail.size();i++){
                //判断项目id和所属类别是否重复
                if(listProjectDetail.get(i).getId()==detail.getId()
                        &&listProjectDetail.get(i).getCategoryId()==detail.getCategoryId()){
                    return;
                }
            }
            listProjectDetail.add(0,detail);

        }else{
            listProjectDetail=new LinkedList<AllProjectInCategory.ProjectDetail>();
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
        AllProjectInCategory.ProjectDetail detail=listProjectDetail.get(position);
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

        ImageView iv_PJ_image_1=(ImageView)rootView.findViewById(R.id.iv_PJ_image_1);
        Picasso.with(context).load(context.getString(R.string.url_resources)+detail.getCover()).into(iv_PJ_image_1);
        return rootView;
    }
}
