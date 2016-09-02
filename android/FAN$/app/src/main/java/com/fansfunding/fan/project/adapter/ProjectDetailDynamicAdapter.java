package com.fansfunding.fan.project.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.fansfunding.fan.R;
import com.fansfunding.fan.utils.MyGridView;
import com.fansfunding.fan.utils.StartHomepage;
import com.fansfunding.internal.ProjectDetailDynamic;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
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

    //每个item里的图片展示的gridview的适配器

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


        //图片展示适配器
        ProjectDetailDynamicPhotoAdapter photoAdapter=new ProjectDetailDynamicPhotoAdapter(context);

        //动态图片展示
        MyGridView gv_project_detail_dynamic_photo_list=(MyGridView)rootView.findViewById(R.id.gv_project_detail_dynamic_photo_list);
        gv_project_detail_dynamic_photo_list.setAdapter(photoAdapter);

        if(list.get(position)==null) {
            return null;

        }
        else {
            //初始化头像
            if(context!=null&&list.get(position).getSponsorHead()!=null&&list.get(position).getSponsorHead().equals("")==false){
                Picasso.with(context).load(context.getString(R.string.url_resources)+list.get(position).getSponsorHead()).into(iv_project_detail_dynamic_head);

            }
            iv_project_detail_dynamic_head.setOnClickListener(new StartHomepage(context,list.get(position).getSponsor()));
            //初始化发起人昵称
            if(list.get(position).getSponsorNickname()!=null){
                tv_project_detail_dynamic_name.setText(list.get(position).getSponsorNickname());
            }
            tv_project_detail_dynamic_name.setOnClickListener( new StartHomepage(context,list.get(position).getSponsor()));

            //初始化动态发起时间
            tv_project_detail_dynamic_time.setText(new SimpleDateFormat("MM-dd HH:mm").format(new Date(list.get(position).getUpdateTime())));


            //初始化动态内容
            if(list.get(position).getContent()!=null){
                tv_project_detail_dynamic_content.setText((list.get(position).getContent()));
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
                        gv_project_detail_dynamic_photo_list.setNumColumns(1);
                        break;
                    case 2:
                        gv_project_detail_dynamic_photo_list.setNumColumns(2);
                        break;
                    case 3:
                        gv_project_detail_dynamic_photo_list.setNumColumns(3);
                        break;
                    case 4:
                        gv_project_detail_dynamic_photo_list.setNumColumns(2);
                        break;
                }


                photoAdapter.notifyDataSetChanged();
            }
            return rootView;
        }



    }
}
