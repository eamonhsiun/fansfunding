package com.fansfunding.fan.social.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fansfunding.fan.R;
import com.fansfunding.fan.utils.StartHomepage;
import com.fansfunding.internal.social.MomentPraise;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by 13616 on 2016/8/28.
 */
public class MomentPraiseAdapter extends BaseAdapter {

    private List<MomentPraise.DataBean.ListBean> praiseList;

    private Activity context;


    public MomentPraiseAdapter(Activity activity){
        context=activity;
        praiseList=new ArrayList<>();
    }

    public void addItem(MomentPraise.DataBean.ListBean praise){
        if(praise==null){
            return;
        }
        praiseList.add(praise);

    }

    public void clear(){
        praiseList=new ArrayList<>();
    }

    @Override
    public int getCount() {
        return praiseList.size();
    }

    @Override
    public MomentPraise.DataBean.ListBean getItem(int position) {
        if(position<0||position>=praiseList.size()){
            return null;
        }
        return praiseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(position<0||position>=praiseList.size()){
            return null;
        }
        MomentPraise.DataBean.ListBean praise=praiseList.get(position);
        final View rootView;
        final ViewHolder viewHodler;
        if(convertView==null){
            rootView= LayoutInflater.from(context).inflate(R.layout.item_praise_people,null);
            viewHodler=new ViewHolder();
            viewHodler.iv_people_head=(CircleImageView) rootView.findViewById(R.id.iv_people_head);
            viewHodler.tv_people_nickname=(TextView)rootView.findViewById(R.id.tv_people_nickname);

            rootView.setTag(viewHodler);
        }else {
            rootView=convertView;
            viewHodler=(ViewHolder)rootView.getTag();
        }

        //设置赞的用户的头像
        if(praise.getHead()!=null&&praise.getHead().equals("")==false){
            Picasso.with(context).load(context.getString(R.string.url_resources)+praise.getHead()).memoryPolicy(MemoryPolicy.NO_CACHE).into(viewHodler.iv_people_head);
        }
        //设置昵称
        viewHodler.tv_people_nickname.setText(praise.getNickname());

        rootView.setOnClickListener(new StartHomepage(context,praise.getId()));
        return rootView;
    }

    class ViewHolder{
        //赞的人的头像
        CircleImageView iv_people_head;

        //赞的人的昵称
        TextView tv_people_nickname;
    }
}
