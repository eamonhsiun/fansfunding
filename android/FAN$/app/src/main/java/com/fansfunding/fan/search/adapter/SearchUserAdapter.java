package com.fansfunding.fan.search.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fansfunding.fan.R;
import com.fansfunding.internal.SearchProject;
import com.fansfunding.internal.SearchUser;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 13616 on 2016/7/22.
 */
public class SearchUserAdapter extends BaseAdapter {

    List<SearchUser.ProjectDetail> list;

    private Activity context;

    public SearchUserAdapter(Activity context){
        list=new LinkedList<SearchUser.ProjectDetail>();
        this.context=context;
    }


    //添加item
    public void addItem(SearchUser.ProjectDetail user){
        if(user==null){
            return;
        }
        if(list!=null){
            //如果已经存在该用户，则返回
            for(int i=0;i<list.size();i++){
                //判断用户id是否会有重复
                if(list.get(i).getId()==user.getId()){
                    list.remove(i);
                    list.add(i,user);
                    return;
                }
            }
            list.add(user);
            Collections.sort(list);


        }else{
            list=new LinkedList<SearchUser.ProjectDetail>();
            list.add(user);
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
        View rootView=View.inflate(context, R.layout.item_search_people,null);

        //用户头像
        CircleImageView iv_search_people_head=(CircleImageView)rootView.findViewById(R.id.iv_search_people_head);

        //用户昵称
        TextView tv_search_people_name=(TextView)rootView.findViewById(R.id.tv_search_people_name);

        //用户简介
        TextView tv_search_people_signature=(TextView)rootView.findViewById(R.id.tv_search_people_signature);

        if(list.get(position)==null){
            return null;
        }
        SearchUser.ProjectDetail detail=list.get(position);

        //用户头像设置
        if(context!=null&&detail.getHead()!=null&&detail.getHead().equals("")==false){
            Picasso.with(context).load(context.getString(R.string.url_resources)+detail.getHead()).memoryPolicy(MemoryPolicy.NO_CACHE).into(iv_search_people_head);
        }

        //用户昵称设置
        if(detail.getNickname()!=null){
            tv_search_people_name.setText(detail.getNickname());
        }

        //用户简介设置
        if(detail.getIntro()!=null){
            tv_search_people_signature.setText(detail.getIntro());
        }
        return rootView;
    }
}
