package com.fansfunding.fan.user.order.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.fansfunding.PullListView.XListView;
import com.fansfunding.fan.R;
import com.fansfunding.fan.user.order.adapter.MyOrderAdapter;
import com.fansfunding.internal.ErrorCode;
import com.fansfunding.internal.UserOrder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MyOrderActivity extends AppCompatActivity {

    //成功获取到订单
    private static final int GET_ORDER_SUCCESS=100;

    //获取订单失败
    private static final int GET_ORDER_FAILURE=101;


    //是否已经完成了项目数据获取的请求
    private boolean isFinishRequest=true;

    //httpclient
    //private OkHttpClient httpClient;
    //热门项目列表
    private XListView lv_order_list;

    //每次获取的数量
    private  final int rows=10;

    //获取的页数
    private int page=1;

    //用户id
    private int userId;

    //用户token
    private String token;

    //adapter
    private MyOrderAdapter adapter;

    //获取到的数据
    private UserOrder order;


    //handler
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GET_ORDER_FAILURE:
                    endRefresh();
                    if(MyOrderActivity.this.isFinishing()==true){
                        break;
                    }
                    Toast.makeText(MyOrderActivity.this,"获取订单失败",Toast.LENGTH_LONG).show();
                    break;
                case GET_ORDER_SUCCESS:
                    if(order.getData().getList().size()<rows){
                        //已结获取到最后一页，再从第一页开始获取
                        page=1;
                        lv_order_list.setPullLoadEnable(false);
                        lv_order_list.setAutoLoadEnable(false);
                    }else {
                        //获取完本页后，获取下一页的内容
                        lv_order_list.setPullLoadEnable(true);
                        lv_order_list.setAutoLoadEnable(true);
                        page++;
                    }
                    for(int i=0;i<order.getData().getList().size();i++){
                        adapter.addItem(order.getData().getList().get(i));
                    }
                    adapter.notifyDataSetChanged();
                    endRefresh();
                    break;
                case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                    endRefresh();
                    if(MyOrderActivity.this.isFinishing()==true){
                        break;
                    }
                    Toast.makeText(MyOrderActivity.this,"请求过于频繁",Toast.LENGTH_LONG).show();

                    break;
                case ErrorCode.PARAMETER_ERROR:
                    endRefresh();
                    if(MyOrderActivity.this.isFinishing()==true){
                        break;
                    }
                    Toast.makeText(MyOrderActivity.this,"参数错误",Toast.LENGTH_LONG).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        final Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_order);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("我的订单");
        actionBar.setDisplayHomeAsUpEnabled(true);

        //初始化
        //httpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
        adapter=new MyOrderAdapter(this);

        //获取用户信息
        SharedPreferences share=getSharedPreferences(getString(R.string.sharepreference_login_by_phone),MODE_PRIVATE);
        userId=share.getInt("id",0);
        token=share.getString("token"," ");

        //初始化列表
        lv_order_list=(XListView)findViewById(R.id.lv_order_list);
        lv_order_list.setPullRefreshEnable(false);
        lv_order_list.setPullLoadEnable(false);
        lv_order_list.setAutoLoadEnable(false);
        lv_order_list.setRefreshTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        lv_order_list.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                if(isFinishRequest==true){
                    getOrder(userId,token,page,rows);
                }
            }

            @Override
            public void onLoadMore() {
                if(isFinishRequest==true){
                    getOrder(userId,token,page,rows);
                }
            }
        });

        lv_order_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                UserOrder.OrderDetail detail=(UserOrder.OrderDetail)lv_order_list.getAdapter().getItem(position);
                int categoryId=detail.getCategoryId();
                int projectId=detail.getProjectId();
                intent.putExtra("categoryId",categoryId);
                intent.putExtra("projectId",projectId);
                intent.setAction(getString(R.string.activity_project_detail));
                startActivity(intent);
            }
        });

        lv_order_list.setAdapter(adapter);

        //获取用户订单信息
        getOrder(userId,token,page,rows);
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


    //获取订单信息
    private void getOrder(final int userId,final String token,final int page,final int rows){

        OkHttpClient httpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();

        Request request=new Request.Builder()
                .url(getString(R.string.url_user)+userId+"/orders?token="+token+"&page="+page+"&rows="+rows+"&userId="+userId)
                .get()
                .build();

        Call call=httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(GET_ORDER_FAILURE);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //服务器响应失败
                if(response==null||response.isSuccessful()==false){
                    handler.sendEmptyMessage(GET_ORDER_FAILURE);
                    return;
                }
                Gson gson=new GsonBuilder().create();
                String str_response=response.body().string();
                Log.i("TAG","order:"+str_response);
                order=new UserOrder();
                try {

                    //用Gson进行解析，并判断结果是否为空
                    if((order = gson.fromJson(str_response, order.getClass()))==null){
                        handler.sendEmptyMessage(GET_ORDER_FAILURE);
                        return;
                    }
                    //搜索用户订单失败
                    if(order.isResult()==false){
                        switch (order.getErrCode()){
                            case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                                handler.sendEmptyMessage(ErrorCode.REQUEST_TOO_FRENQUENTLY);
                                break;
                            case ErrorCode.PARAMETER_ERROR:
                                handler.sendEmptyMessage(ErrorCode.PARAMETER_ERROR);
                                break;
                            default:
                                handler.sendEmptyMessage(GET_ORDER_FAILURE);
                                break;
                        }
                        return;
                    }

                    //搜索用户订单成功
                    handler.sendEmptyMessage(GET_ORDER_SUCCESS);
                }catch (IllegalStateException e){
                    handler.sendEmptyMessage(GET_ORDER_FAILURE);
                    e.printStackTrace();
                }catch (JsonSyntaxException e){
                    handler.sendEmptyMessage(GET_ORDER_FAILURE);
                    e.printStackTrace();
                }
            }
        });
    }

    //停止更新的动画
    private void endRefresh(){
        isFinishRequest=true;
        lv_order_list.stopRefresh();
        lv_order_list.stopLoadMore();
        lv_order_list.setRefreshTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
    }
}
