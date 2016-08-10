package com.fansfunding.fan.project.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fansfunding.fan.R;
import com.fansfunding.fan.utils.MyGridView;
import com.fansfunding.internal.ProjectDetailReward;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by 13616 on 2016/7/24.
 */
public class ProjectSupportAdapter extends BaseAdapter{




    private Activity context;

    //数据
    private List<ProjectDetailReward.ProjectReward> list;

    private ArrayList<Boolean> select;

    public ProjectSupportAdapter(Activity context){
        this.context=context;
        list=new LinkedList<ProjectDetailReward.ProjectReward>();
        select=new ArrayList<Boolean>();
    }

    //添加数据
    public void addItem(ProjectDetailReward.ProjectReward reward){
        if(reward==null){
            return;
        }
        if(list==null) {
            list = new LinkedList<ProjectDetailReward.ProjectReward>();
            select = new ArrayList<Boolean>();
        }
        list.add(reward);
        select.add(false);

    }

    //获取到被选择的一项
    public ProjectDetailReward.ProjectReward getSelectedItem(){
        for(int i=0;i<select.size();i++){
            if(i<list.size()&&select.get(i)==true&&list.get(i)!=null){
                return list.get(i);
            }
        }
        return null;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(position<0||position>=list.size()){
            return null;
        }
        View rootView=View.inflate(context, R.layout.item_project_detail_reward_select,null);
        //回报图片
        //ImageView iv_project_detail_reward_image=(ImageView)rootView.findViewById(R.id.iv_project_detail_reward_image);
        //回报标题
        TextView tv_project_detail_reward_support=(TextView)rootView.findViewById(R.id.tv_project_detail_reward_support);
        //回报内容
        TextView tv_project_detail_reward_information=(TextView)rootView.findViewById(R.id.tv_project_detail_reward_information);

        //图片展示适配器
        ProjectDetailDynamicPhotoAdapter photoAdapter=new ProjectDetailDynamicPhotoAdapter(context);

        MyGridView gv_project_detail_reward_photo_list=(MyGridView)rootView.findViewById(R.id.gv_project_detail_reward_photo_list);
        gv_project_detail_reward_photo_list.setAdapter(photoAdapter);


        //选择框
        final CheckBox iv_project_detail_reward_select=(CheckBox)rootView.findViewById(R.id.iv_project_detail_reward_select);

        if(list.get(position)==null) {
            return null;
        }
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
            //Picasso.with(context).load(context.getString(R.string.url_resources)+list.get(position).getImages().get(0)).into(iv_project_detail_reward_image);
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
                    gv_project_detail_reward_photo_list.setNumColumns(1);
                    break;
                case 2:
                    gv_project_detail_reward_photo_list.setNumColumns(2);
                    break;
                case 3:
                    gv_project_detail_reward_photo_list.setNumColumns(3);
                    break;
                case 4:
                    gv_project_detail_reward_photo_list.setNumColumns(2);
                    break;
            }
            photoAdapter.notifyDataSetChanged();
        }


        //设置checkBox
        if(select.get(position)!=null){
            if(select.get(position)==true){
                iv_project_detail_reward_select.setChecked(true);
            }else{
                iv_project_detail_reward_select.setChecked(false);
            }
        }

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(iv_project_detail_reward_select.isChecked()==true){
                    iv_project_detail_reward_select.setChecked(false);
                    if(select.get(position)!=null){
                        select.set(position,false);
                    }
                }else{
                    iv_project_detail_reward_select.setChecked(true);
                    for(int i=0;i<select.size();i++){
                        select.set(i,false);
                    }
                    if(select.get(position)!=null){
                        select.set(position,true);
                    }
                }
                ProjectSupportAdapter.this.notifyDataSetChanged();
            }
        });
        //使得checkbox只有一个有效
        iv_project_detail_reward_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(iv_project_detail_reward_select.isChecked()==false){
                    iv_project_detail_reward_select.setChecked(false);
                    if(select.get(position)!=null){
                        select.set(position,false);
                    }
                }else{
                    iv_project_detail_reward_select.setChecked(true);
                    for(int i=0;i<select.size();i++){
                        select.set(i,false);
                    }
                    if(select.get(position)!=null){
                        select.set(position,true);
                    }
                }
                ProjectSupportAdapter.this.notifyDataSetChanged();
            }
        });
        return rootView;
    }
}
