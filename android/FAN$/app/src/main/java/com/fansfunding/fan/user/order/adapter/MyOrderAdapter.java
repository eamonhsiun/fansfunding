package com.fansfunding.fan.user.order.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fansfunding.fan.R;
import com.fansfunding.internal.ProjectInfo;
import com.fansfunding.internal.UserOrder;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by 13616 on 2016/7/23.
 */
public class MyOrderAdapter extends BaseAdapter {

    //数据
    private List<UserOrder.OrderDetail> list;

    private Activity context;

    public MyOrderAdapter(Activity context){
        list=new LinkedList<UserOrder.OrderDetail>();
        this.context=context;

    }

    //添加Item
    public void addItem(UserOrder.OrderDetail order){
        if(order==null){
            return;
        }
        if(list!=null){
            //如果已经存在该项目，则返回
            for(int i=0;i<list.size();i++){
                //判断订单id是否重复
                if(list.get(i).getOrderNo().equals(order.getOrderNo())){
                    list.remove(i);
                    list.add(i,order);
                    return;
                }
            }
            list.add(order);
            Collections.sort(list);

        }else{
            list=new LinkedList<UserOrder.OrderDetail>();
            list.add(order);
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

        UserOrder.OrderDetail detail=list.get(position);
        if(detail==null){
            return null;
        }

        final View rootView;
        final ViewHolder viewHolder;
        if(convertView == null) {
            rootView= LayoutInflater.from(context).inflate(R.layout.item_order, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_order_project_name=(TextView)rootView.findViewById(R.id.tv_order_project_name);
            viewHolder.iv_order_image=(ImageView)rootView.findViewById(R.id.iv_order_image);
            viewHolder.tv_order_price=(TextView)rootView.findViewById(R.id.tv_order_price);
            viewHolder.tv_order_time=(TextView)rootView.findViewById(R.id.tv_order_time);
            viewHolder.tv_order_number=(TextView)rootView.findViewById(R.id.tv_order_number);
            viewHolder.tv_order_status=(TextView)rootView.findViewById(R.id.tv_order_status);

            rootView.setTag(viewHolder);
        }else  {
            rootView = convertView;
            viewHolder = (ViewHolder) rootView.getTag();
        }
        //设置控件的值

        //设置订单的项目名字
        viewHolder.tv_order_project_name.setText(detail.getProjectName());
        //设置订单项目图片
        if(detail.getFeedbackImages()!=null&&detail.getFeedbackImages().size()>0&&detail.getFeedbackImages().get(0).equals("")==false){
            Picasso.with(context).load(context.getString(R.string.url_resources)+detail.getFeedbackImages().get(0)).into(viewHolder.iv_order_image);
        }
        //设置订单金额
        if(detail.getTotalFee()!=null){
            viewHolder.tv_order_price.setText(detail.getTotalFee().toString());
        }else {
            viewHolder.tv_order_price.setText("");
        }
        //设置交易时间
        viewHolder.tv_order_time.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(detail.getPaidTime())));
        //设置订单编号
        viewHolder.tv_order_number.setText(detail.getOrderNo());
        //设置订单状态
        viewHolder.tv_order_status.setText(detail.getOrderStatus());


        /*View rootView=View.inflate(context, R.layout.item_order,null);

        //项目名字
        TextView tv_order_project_name=(TextView)rootView.findViewById(R.id.tv_order_project_name);

        //项目图片
        ImageView iv_order_image=(ImageView)rootView.findViewById(R.id.iv_order_image);

        //所付金额
        TextView tv_order_price=(TextView)rootView.findViewById(R.id.tv_order_price);

        //交易时间
        TextView tv_order_time=(TextView)rootView.findViewById(R.id.tv_order_time);

        //订单编号
        TextView tv_order_number=(TextView)rootView.findViewById(R.id.tv_order_number);

        //订单状态
        TextView tv_order_status=(TextView)rootView.findViewById(R.id.tv_order_status);

        if(detail==null){
            return null;
        }

        //设置订单的项目名字
        if(detail.getProjectName()!=null){
            tv_order_project_name.setText(detail.getProjectName());
        }

        //设置订单项目图片
        if(detail.getFeedbackImages()!=null&&detail.getFeedbackImages().size()>0&&detail.getFeedbackImages().get(0).equals("")==false){
            Picasso.with(context).load(context.getString(R.string.url_resources)+detail.getFeedbackImages().get(0)).into(iv_order_image);
        }

        //设置订单金额
        if(detail.getTotalFee()!=null){
            tv_order_price.setText(detail.getTotalFee().toString());
        }

        //设置交易时间
        tv_order_time.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(detail.getPaidTime())));

        //设置订单编号
        if(detail.getOrderNo()!=null){
            tv_order_number.setText(detail.getOrderNo());
        }

        //设置订单状态
        if(detail.getOrderStatus()!=null){
            tv_order_status.setText(detail.getOrderStatus());
        }*/
        return rootView;
    }

    class ViewHolder{
        //项目名字
        TextView tv_order_project_name;

        //项目图片
        ImageView iv_order_image;

        //所付金额
        TextView tv_order_price;

        //交易时间
        TextView tv_order_time;

        //订单编号
        TextView tv_order_number;

        //订单状态
        TextView tv_order_status;

    }
}
