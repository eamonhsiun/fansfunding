package com.fansfunding.fan.project.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.fansfunding.fan.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 13616 on 2016/7/26.
 */
public class ProjectDetailDynamicPhotoAdapter extends BaseAdapter {

    private List<String> paths;

    private Activity context;

    public ProjectDetailDynamicPhotoAdapter(Activity context){
        this.context=context;
        paths=new ArrayList<String>();
    }

    public void addItem(String path){
        if(path==null||path.equals("")==true){
            return;
        }
        paths.add(path);
    }


    @Override
    public int getCount() {
        return paths.size();
    }

    @Override
    public Object getItem(int position) {
        if(position<0||position>paths.size())
            return null;
        return paths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(position<0||position>=paths.size()){
            return null;
        }
        ImageView rootView=(ImageView) LayoutInflater.from(context).inflate(R.layout.adapter_photo_list_item,null);
        setHeight(rootView);
        if(paths.get(position)!=null&&paths.get(position).equals("")==false){
            final String url=context.getString(R.string.url_resources)+paths.get(position);
            Picasso.with(context).load(url)
                    //.resizeDimen(R.dimen.dynamic_photo_one_width,R.dimen.dynamic_photo_one_height).centerCrop()
                    .memoryPolicy(MemoryPolicy.NO_CACHE).into(rootView);
        }
        /*ImageView iv_project_detail_dynamic_image=(ImageView) rootView.findViewById(R.id.iv_project_detail_dynamic_image);

        if(paths.get(position)!=null&&paths.get(position).equals("")==false){
            final String url=context.getString(R.string.url_resources)+paths.get(position);
            switch(paths.size()){
                //当只有一张图的时候
                case 1:
                    rootView=View.inflate(context, R.layout.item_project_detail_dynamic_image_272,null);
                    iv_project_detail_dynamic_image=(ImageView) rootView.findViewById(R.id.iv_project_detail_dynamic_image);
                    Picasso.with(context).load(url)
                            .resizeDimen(R.dimen.dynamic_photo_one_width,R.dimen.dynamic_photo_one_height).centerCrop()
                            .memoryPolicy(MemoryPolicy.NO_CACHE).into(iv_project_detail_dynamic_image);
                    break;

                //当动态有两张图的时候
                case 2:
                    rootView=View.inflate(context, R.layout.item_project_detail_dynamic_image_134,null);
                    iv_project_detail_dynamic_image=(ImageView) rootView.findViewById(R.id.iv_project_detail_dynamic_image);
                    Picasso.with(context).load(url)
                            .resizeDimen(R.dimen.dynamic_photo_two_width,R.dimen.dynamic_photo_two_height).centerCrop()
                            .memoryPolicy(MemoryPolicy.NO_CACHE).into(iv_project_detail_dynamic_image);
                    break;
                //当动态有三张图的时候
                case 3:
                    rootView=View.inflate(context, R.layout.item_project_detail_dynamic_image_88,null);
                    iv_project_detail_dynamic_image=(ImageView) rootView.findViewById(R.id.iv_project_detail_dynamic_image);
                    Picasso.with(context).load(url)
                            .resizeDimen(R.dimen.dynamic_photo_three_width,R.dimen.dynamic_photo_three_height).centerCrop()
                            .memoryPolicy(MemoryPolicy.NO_CACHE).into(iv_project_detail_dynamic_image);
                     break;
                //当动态有四张图的时候
                case 4:
                    rootView=View.inflate(context, R.layout.item_project_detail_dynamic_image_134,null);
                    iv_project_detail_dynamic_image=(ImageView) rootView.findViewById(R.id.iv_project_detail_dynamic_image);
                    Picasso.with(context).load(url)
                            .resizeDimen(R.dimen.dynamic_photo_four_width,R.dimen.dynamic_photo_four_height).centerCrop()
                            .memoryPolicy(MemoryPolicy.NO_CACHE).into(iv_project_detail_dynamic_image);
                    break;
                default:
                    rootView=View.inflate(context, R.layout.item_project_detail_dynamic_image_134,null);
                    iv_project_detail_dynamic_image=(ImageView) rootView.findViewById(R.id.iv_project_detail_dynamic_image);
                    Picasso.with(context).load(url)
                            .resizeDimen(R.dimen.dynamic_photo_four_width,R.dimen.dynamic_photo_four_height).centerCrop()
                            .memoryPolicy(MemoryPolicy.NO_CACHE).into(iv_project_detail_dynamic_image);
            }
            iv_project_detail_dynamic_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent();
                    intent.setAction(context.getString(R.string.activity_big_picture));
                    intent.putExtra("url",url);
                    context.startActivity(intent);
                }
            });
        }*/

        return rootView;
    }

    private void setHeight(final View convertView){
        int height=0;
        switch (paths.size()){
            case 1:
                 height= context.getResources().getDimensionPixelOffset(R.dimen.dynamic_photo_one_height);
                convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
                return;
            case 2:
                height = context.getResources().getDimensionPixelOffset(R.dimen.dynamic_photo_two_height);
                convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
                return;
            case 3:
                height = context.getResources().getDimensionPixelOffset(R.dimen.dynamic_photo_three_height);
                convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
                return;
            case 4:
                height = context.getResources().getDimensionPixelOffset(R.dimen.dynamic_photo_four_height);
                convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
                return;

        }
    }
}
