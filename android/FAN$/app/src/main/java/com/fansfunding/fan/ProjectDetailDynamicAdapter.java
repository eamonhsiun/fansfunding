package com.fansfunding.fan;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fansfunding.internal.ProjectDetailDynamic;
import com.fansfunding.internal.ProjectDetailReward;

import java.util.LinkedList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 13616 on 2016/7/20.
 */
public class ProjectDetailDynamicAdapter extends BaseAdapter {

    //容器
    private Activity context;

    //数据
    private List<ProjectDetailDynamic.ProjectDynamic> list;

    //
    public ProjectDetailDynamicAdapter(Activity context){
        this.context=context;
        list=new LinkedList<ProjectDetailDynamic.ProjectDynamic>();
    }

    public void addItem(ProjectDetailDynamic.ProjectDynamic dynamic){
        if(dynamic==null){
            return;
        }
        if(list!=null){
            list.add(dynamic);

        }else{
            list=new LinkedList<ProjectDetailDynamic.ProjectDynamic>();
            list.add(dynamic);
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
        View rootView =View.inflate(context,R.layout.item_project_detail_dynamic,null);

        //动态发起人头像
        CircleImageView iv_project_detail_dynamic_head=(CircleImageView)rootView.findViewById(R.id.iv_project_detail_dynamic_head);

        //动态发起人的昵称
        TextView tv_project_detail_dynamic_name=(TextView)rootView.findViewById(R.id.tv_project_detail_dynamic_name);

        //动态发起时间
        TextView tv_project_detail_dynamic_time=(TextView)rootView.findViewById(R.id.tv_project_detail_dynamic_time);

        //动态内容
        TextView tv_project_detail_dynamic_content=(TextView)rootView.findViewById(R.id.tv_project_detail_dynamic_content);


        if(list.get(position)==null) {
            return null;
        }
        else {
            //if(list.get(1))

        }


        return null;
    }
}
