package com.fansfunding.fan;

import android.app.Activity;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fansfunding.internal.CategoryInfo;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 13616 on 2016/9/6.
 */
public class CrowdFundingAdapter extends BaseAdapter{

    private List<CategoryInfo.DataBean> categotyList;

    private Activity context;

    public CrowdFundingAdapter(Activity context){
        this.context=context;
        categotyList=new ArrayList<>();
    }


    public void addItem(CategoryInfo.DataBean category){
        if(category==null){
            return;
        }
        if(categotyList==null){
            categotyList=new ArrayList<>();
        }
        categotyList.add(category);
    }

    @Override
    public int getCount() {
        return categotyList.size();
    }

    @Override
    public CategoryInfo.DataBean getItem(int position) {
        if(position<0||position>=categotyList.size()){
            return null;
        }
        return categotyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(position<0||position>=categotyList.size()){
            return null;
        }
        final CategoryInfo.DataBean category=categotyList.get(position);
        if (category==null){
            return null;
        }
        View rootView= LayoutInflater.from(context).inflate(R.layout.item_category_info,null);

        //分类头像
        CircleImageView iv_category_icon=(CircleImageView)rootView.findViewById(R.id.iv_category_icon);

        //分类名称
        TextView tv_category_name=(TextView)rootView.findViewById(R.id.tv_category_name);

        //设置分类图片
        if(category.getIcon()!=null&&category.getIcon().equals("")==false){
            Picasso.with(context).load(context.getString(R.string.url_resources)+category.getIcon()).memoryPolicy(MemoryPolicy.NO_CACHE).into(iv_category_icon);
        }

        //设置分类名称
        tv_category_name.setText(category.getName());
        return rootView;
    }
}
