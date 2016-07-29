package com.fansfunding.fan.user.order.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fansfunding.fan.R;
import com.fansfunding.fan.request.RequestOrderDetail;
import com.fansfunding.fan.utils.ErrorHandler;
import com.fansfunding.fan.utils.FANRequestCode;
import com.fansfunding.internal.SingleAddress;
import com.fansfunding.internal.user.OrderDetail;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class OrderDetailActivity extends AppCompatActivity {

    //用户id
    private int userId;

    //用户token
    private String token;

    //订单号
    private String orderNo;

    //用来请求订单
    private RequestOrderDetail requestOrderDetail;

    //获取到的订单信息
    private OrderDetail orderDetail=null;


    //项目内容展示
    private LinearLayout ll_order_detail_project;

    //订单编号展示框
    private TextView tv_order_detail_no;

    //订单图片展示框
    private ImageView iv_order_detail_image;

    //订单项目名字展示框
    private TextView tv_order_detail_name;

    //订单支付价格展示框
    private TextView tv_order_detail_price;

    //收货人名字展示框
    private TextView tv_order_detail_people_name;

    //收货人电话展示框
    private TextView tv_order_detail_people_phone;

    //收货人地址展示框(要加上收货地址:)
    private TextView tv_order_detail_address;

    //交易单号
    private TextView tv_order_detail_trade_no;

    //交易时间
    private TextView tv_order_detail_pay_time;

    private ErrorHandler handler=new ErrorHandler(this){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case FANRequestCode.GET_ORDER_DETAIL_SUCCESS:
                    orderDetail=requestOrderDetail.getOrderDetail();
                    InitOrder();
                    break;
                case FANRequestCode.GET_ORDER_DETAIL_FAILURE:
                    if(OrderDetailActivity.this.isFinishing()==true){
                        break;
                    }
                    Toast.makeText(OrderDetailActivity.this,"获取订单详情失败",Toast.LENGTH_LONG).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_order_detail);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("订单详情");
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(Color.WHITE);


        //获得订单号
        Intent intent=getIntent();
        orderNo=intent.getStringExtra("orderNo");

        //获取用户信息
        SharedPreferences share=getSharedPreferences(getString(R.string.sharepreference_login_by_phone),MODE_PRIVATE);
        userId=share.getInt("id",0);
        token=share.getString("token"," ");

        //初始化
        requestOrderDetail=new RequestOrderDetail();

        ll_order_detail_project=(LinearLayout)findViewById(R.id.ll_order_detail_project);
        tv_order_detail_no=(TextView)findViewById(R.id.tv_order_detail_no);
        iv_order_detail_image=(ImageView)findViewById(R.id.iv_order_detail_image);
        tv_order_detail_name=(TextView)findViewById(R.id.tv_order_detail_name);
        tv_order_detail_price=(TextView)findViewById(R.id.tv_order_detail_price);
        tv_order_detail_people_name=(TextView)findViewById(R.id.tv_order_detail_people_name);
        tv_order_detail_people_phone=(TextView)findViewById(R.id.tv_order_detail_people_phone);
        tv_order_detail_address=(TextView)findViewById(R.id.tv_order_detail_address);
        tv_order_detail_trade_no=(TextView)findViewById(R.id.tv_order_detail_trade_no);
        tv_order_detail_pay_time=(TextView)findViewById(R.id.tv_order_detail_pay_time);

        OkHttpClient httpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();

        //请求订单详情
        requestOrderDetail.getOrderDetail(this,handler,httpClient,orderNo,userId,token);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;

    }

    private void InitOrder(){
        if(orderDetail==null||orderDetail.getData()==null){
            return;
        }
        //初始化订单号
        if(orderDetail.getData().getOrderNo()!=null){
            tv_order_detail_no.setText(orderDetail.getData().getOrderNo());
        }
        //初始化订单的项目图片
        if(orderDetail.getData().getFeedbackImages()!=null
                &&orderDetail.getData().getFeedbackImages().size()>0
                &&orderDetail.getData().getFeedbackImages().get(0)!=null
                &&orderDetail.getData().getFeedbackImages().get(0).equals("")==false){
            Picasso.with(this)
                    .load(getString(R.string.url_resources)+orderDetail.getData().getFeedbackImages().get(0))
                    .resize(70,70).centerCrop()
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(iv_order_detail_image);

        }
        //初始化项目名字
        tv_order_detail_name.setText(orderDetail.getData().getProjectName());

        //初始化项目金额
        if(orderDetail.getData().getTotalFee()!=null){
            tv_order_detail_price.setText(new java.text.DecimalFormat("0.00").format(orderDetail.getData().getTotalFee()));
        }



        if(orderDetail.getData().getAddress()!=null){

            SingleAddress address=orderDetail.getData().getAddress();
            //初始化收货人名字
            tv_order_detail_people_name.setText(orderDetail.getData().getAddress().getName());

            //初始化收货人电话
            tv_order_detail_people_phone.setText(orderDetail.getData().getAddress().getPhone());

            //初始化收货人地址
            tv_order_detail_address.setText("收货地址:"+address.getProvince()+address.getCity()+address.getDistrict()+address.getAddress());

        }

        //初始化支付宝交易单号
        tv_order_detail_trade_no.setText(orderDetail.getData().getTradeNo());

        //初始化交易时间
        tv_order_detail_pay_time.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(orderDetail.getData().getPaidTime())));

        ll_order_detail_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(getString(R.string.activity_project_detail));
                intent.putExtra("projectId",orderDetail.getData().getProjectId());
                intent.putExtra("categoryId",orderDetail.getData().getCategoryId());
                startActivity(intent);
            }
        });

    }

}
