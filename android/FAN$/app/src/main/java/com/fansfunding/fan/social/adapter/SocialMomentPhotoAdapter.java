package com.fansfunding.fan.social.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.fansfunding.fan.R;
import com.fansfunding.fan.utils.BigPictureActivity;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.toolsfinal.DeviceUtils;

/**
 * Created by 13616 on 2016/8/26.
 */
public class SocialMomentPhotoAdapter extends BaseAdapter{
    private List<String> paths;

    private Activity context;

    private int mScreenWidth;

    public SocialMomentPhotoAdapter(Activity context){
        this.context=context;
        paths=new ArrayList<String>();
        this.mScreenWidth = DeviceUtils.getScreenPix(context).widthPixels;
    }

    public void addItem(String path){
        //最多九张图
        if(path==null||path.equals("")==true||paths.size()>=9){
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
        final String path=paths.get(position);
        ImageView ivPhoto = (ImageView) LayoutInflater.from(context).inflate(R.layout.adapter_photo_list_item, null);
        setHeight(ivPhoto);

        if(path!=null&&path.equals("")==false){
            Picasso.with(context)
                    .load(context.getString(R.string.url_resources)+path)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(ivPhoto);
        }
        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BigPictureActivity.startThisActivity(context,context.getString(R.string.url_resources)+path);

            }
        });

        return ivPhoto;
    }

    private void setHeight(final View convertView) {
        if(paths.size()==1){
            int height = context.getResources().getDimensionPixelOffset(R.dimen.dynamic_photo_one_height);
            convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
            return;
        }
        int height = mScreenWidth / 3 - 8;
        convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
    }

}
