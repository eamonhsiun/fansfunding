package com.fansfunding.fan.user.info.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fansfunding.fan.R;
import com.fansfunding.fan.utils.StartHomepage;
import com.fansfunding.internal.user.UserList;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 13616 on 2016/8/30.
 */
public class UserFollowingAdapter extends BaseAdapter{

    private Activity context;

    private List<UserList.DataBean.ListBean> followinglist;

    public UserFollowingAdapter(Activity context){
        this.context=context;
        followinglist=new ArrayList<>();
    }


    public void addItem(UserList.DataBean.ListBean following){
        if(following==null){
            return;
        }
        followinglist.add(following);
    }

    @Override
    public int getCount() {
        return followinglist.size();
    }

    @Override
    public UserList.DataBean.ListBean getItem(int position) {
        if(position>=0&&position<followinglist.size()){
            return followinglist.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(position<0||position>=followinglist.size()){
            return null;
        }
        UserList.DataBean.ListBean following=followinglist.get(position);
        final View rootView;
        final ViewHolder viewHodler;
        if(convertView==null){
            rootView= LayoutInflater.from(context).inflate(R.layout.item_search_people,null);
            viewHodler=new ViewHolder();
            viewHodler.iv_search_people_head=(CircleImageView)rootView.findViewById(R.id.iv_search_people_head) ;
            viewHodler.tv_search_people_name=(TextView)rootView.findViewById(R.id.tv_search_people_name);
            viewHodler.tv_search_people_signature=(TextView)rootView.findViewById(R.id.tv_search_people_signature);

            rootView.setTag(viewHodler);
        }else {
            rootView=convertView;
            viewHodler=(ViewHolder)rootView.getTag();
        }

        if(following.getHead()!=null&&following.getHead().equals("")==false){
            Picasso.with(context).load(context.getString(R.string.url_resources)+following.getHead()).memoryPolicy(MemoryPolicy.NO_CACHE).into(viewHodler.iv_search_people_head);
        }else {
        }

        viewHodler.tv_search_people_name.setText(following.getNickname());
        viewHodler.tv_search_people_signature.setText(following.getIntro());


        rootView.setOnClickListener(new StartHomepage(context,following.getId()));
        return rootView;
    }

    class ViewHolder{
        //用户头像
        CircleImageView iv_search_people_head;

        //用户昵称
        TextView tv_search_people_name;

        //用户签名
        TextView tv_search_people_signature;
    }
}
