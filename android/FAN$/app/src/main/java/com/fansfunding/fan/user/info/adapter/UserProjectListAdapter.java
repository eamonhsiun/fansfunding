package com.fansfunding.fan.user.info.adapter;

import android.app.Activity;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fansfunding.fan.R;
import com.fansfunding.internal.PersonalInfo;
import com.fansfunding.internal.ProjectInfo;
import com.nostra13.universalimageloader.utils.L;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 13616 on 2016/8/27.
 */
public class UserProjectListAdapter extends BaseExpandableListAdapter {

    //发起项目的组位置
    public static final int sponsorPosition=0;
    //关注项目的组位置
    public static final int followPosition=1;
    //支持项目的组位置
    public static final int supportPosition=2;

    //支持项目的数量
    private int sponsorProjectNum;

    //关注项目的数量
    private  int followProjectNum;
    //支持项目的数量
    private  int supportProjectNum;



    //发起的项目
    private List<ProjectInfo> sponsorList;

    //关注的项目
    private List<ProjectInfo> followList;

    //支持的项目
    private List<ProjectInfo> supportList;


    Activity activity;

    public UserProjectListAdapter(Activity activity){
        this.activity=activity;
        sponsorList=new ArrayList<>();
        followList=new ArrayList<>();
        supportList=new ArrayList<>();
    }


    public void addSponsorItem(ProjectInfo projectInfo ){
        if(projectInfo==null){
            return;
        }
        if(sponsorList==null){
            sponsorList=new ArrayList<>();
        }
        for(int i=0;i<sponsorList.size();i++){
            if(sponsorList.get(i).getId()==projectInfo.getId()&&sponsorList.get(i).getCategoryId()==projectInfo.getCategoryId()){
                return;
            }
        }
        sponsorList.add(projectInfo);

    }

    //添加关注项目
    public void addFollowItem(ProjectInfo projectInfo){
        if(projectInfo==null){
            return;
        }
        if(followList==null){
            followList=new ArrayList<>();
        }
        for(int i=0;i<followList.size();i++){
            if(followList.get(i).getId()==projectInfo.getId()&&followList.get(i).getCategoryId()==projectInfo.getCategoryId()){
                return;
            }
        }
        followList.add(projectInfo);
    }

    //添加支持项目
    public void addSupportItem(ProjectInfo projectInfo){
        if(projectInfo==null){
            return;
        }
        if(supportList==null){
            supportList=new ArrayList<>();
        }
        for(int i=0;i<supportList.size();i++){
            if(supportList.get(i).getId()==projectInfo.getId()&&supportList.get(i).getCategoryId()==projectInfo.getCategoryId()){
                return;
            }
        }
        supportList.add(projectInfo);
    }


    public void setSponsorProjectNum(int num){
        sponsorProjectNum=num;
    }

    public void setFollowProjectNum(int num){
        followProjectNum=num;
    }

    public void setSupportProjectNum(int num){
        supportProjectNum=num;
    }

    @Override
    public int getGroupCount() {
        return 3;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        switch (groupPosition){
            case sponsorPosition:
                return sponsorList.size()+1;
            case followPosition:
                return followList.size()+1;
            case supportPosition:
                return supportList.size()+1;

        }
        return 0;
    }

    @Override
    public List<ProjectInfo> getGroup(int groupPosition) {
        switch (groupPosition){
            case sponsorPosition:
                return sponsorList;
            case followPosition:
                return followList;
            case supportPosition:
                return supportList;

        }
        return null;
    }

    @Override
    public ProjectInfo getChild(int groupPosition, int childPosition) {
        switch (groupPosition){
            case sponsorPosition:
                if(childPosition<sponsorList.size()){
                    return sponsorList.get(childPosition);
                }
                break;
            case followPosition:
                if(childPosition<followList.size()){
                    return followList.get(childPosition);
                }
                break;

            case supportPosition:
                if(childPosition<supportList.size()){
                    return supportList.get(childPosition);
                }
                break;

        }
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View rootView= LayoutInflater.from(activity).inflate(R.layout.activity_homepage_expandablelist_group,null);
        TextView tv_homepage_group=(TextView)rootView.findViewById(R.id.tv_homepage_group);
        switch (groupPosition){
            case sponsorPosition:
                tv_homepage_group.setText("发起的项目("+sponsorProjectNum+")");
                break;
            case followPosition:
                tv_homepage_group.setText("关注的项目("+followProjectNum+")");
                break;
            case supportPosition:
                tv_homepage_group.setText("支持的项目("+supportProjectNum+")");
                break;
        }

        return rootView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(groupPosition>=3||groupPosition<0){
            return null;
        }
        switch (groupPosition){
            case sponsorPosition:
                if(childPosition<0||childPosition>=getChildrenCount(groupPosition)){
                    return null;
                }
            case followPosition:
                if(childPosition<0||childPosition>=getChildrenCount(groupPosition)){
                    return null;
                }
            case supportPosition:
                if(childPosition<0||childPosition>=getChildrenCount(groupPosition)){
                    return null;
                }

        }
        ProjectInfo project=getChild(groupPosition,childPosition);
        if(project==null){
            View rootView=LayoutInflater.from(activity).inflate(R.layout.item_expandablelist_add_more,null);
            TextView tv_add_more=(TextView) rootView.findViewById(R.id.tv_add_more);
            return rootView;
        }
        View rootView=LayoutInflater.from(activity).inflate(R.layout.item_homepage_project,null);
        //项目封面
        ImageView iv_homepage_project_cover=(ImageView)rootView.findViewById(R.id.iv_homepage_project_cover);
        //项目名称
        TextView tv_homepage_project_name=(TextView)rootView.findViewById(R.id.tv_homepage_project_name);
        //项目发起人
        TextView tv_homepage_project_publisher_nickname=(TextView)rootView.findViewById(R.id.tv_homepage_project_publisher_nickname);

        if(project.getCover()!=null&&project.getCover().equals("")==false){
            Picasso.with(activity).load(activity.getString(R.string.url_resources)+project.getCover()).resize(50,50).centerCrop().into(iv_homepage_project_cover);
        }

        tv_homepage_project_name.setText(project.getName());
        tv_homepage_project_publisher_nickname.setText(project.getSponsorNickname());


        return rootView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {

        return true;
    }
}
