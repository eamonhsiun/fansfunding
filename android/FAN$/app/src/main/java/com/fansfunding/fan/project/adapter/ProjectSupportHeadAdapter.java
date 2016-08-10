package com.fansfunding.fan.project.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.fansfunding.fan.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 13616 on 2016/8/9.
 */
public class ProjectSupportHeadAdapter extends BaseAdapter{
    private List<String> paths;

    private Activity context;

    public ProjectSupportHeadAdapter(Activity context){
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
        final View rootView;
        final ViewHolder viewHolder;
        if(convertView == null) {
            rootView = LayoutInflater.from(context).inflate(R.layout.item_project_detail_image_32, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView=(CircleImageView)rootView.findViewById(R.id.iv_project_detail_dynamic_image);
            rootView.setTag(viewHolder);
        }else {
            rootView = convertView;
            viewHolder = (ViewHolder) rootView.getTag();
        }

        if(paths.get(position)==null) {
            return null;
        }
        if(paths.get(position)!=null&&paths.get(position).equals("")==false) {
            final String url = context.getString(R.string.url_resources) + paths.get(position);
            Picasso.with(context).load(url)
                    .resizeDimen(R.dimen.project_support_head_width,R.dimen.project_support_head_height).centerCrop()
                    .memoryPolicy(MemoryPolicy.NO_CACHE).into(viewHolder.imageView);
        }


        return rootView;
    }


    private class ViewHolder{
        CircleImageView imageView;
    }
}
