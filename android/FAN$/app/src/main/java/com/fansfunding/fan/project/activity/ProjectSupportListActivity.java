package com.fansfunding.fan.project.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.fansfunding.PullListView.XListView;
import com.fansfunding.fan.R;
import com.fansfunding.fan.project.adapter.ProjectSupporterAdapter;
import com.fansfunding.fan.request.RequestProjectSupportInfo;
import com.fansfunding.fan.request.RequestProjectWeb;
import com.fansfunding.fan.utils.ErrorHandler;
import com.fansfunding.fan.utils.FANRequestCode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class ProjectSupportListActivity extends AppCompatActivity {

    //项目分类
    private int categoryId;

    //项目Id
    private int projectId;

    //是否已经完成了支持者数据获取的请求
    private boolean isFinishRequest=true;

    //httpclient
    private OkHttpClient httpClient;

    //获取支持者信息
    private RequestProjectSupportInfo requestProjectSupportInfo;

    //listview的适配器
    private ProjectSupporterAdapter adapter;

    //显示支持者的列表
    private XListView lv_project_supporter;

    //handler
    private ErrorHandler handler=new ErrorHandler(this){
        @Override
        public void handleMessage(Message msg) {
            endRefresh();
            switch (msg.what){
                case FANRequestCode.GET_PROJECT_DETAIL_SUPPORTER_SUCCESS:
                    if(requestProjectSupportInfo.getSupportsInfo().getData().getList().size()< requestProjectSupportInfo.getRows()){
                        requestProjectSupportInfo.setPage(1);
                        lv_project_supporter.setPullLoadEnable(false);
                        lv_project_supporter.setAutoLoadEnable(false);
                    }else{
                        requestProjectSupportInfo.setPage(requestProjectSupportInfo.getPage()+1);
                        lv_project_supporter.setPullLoadEnable(true);
                        lv_project_supporter.setAutoLoadEnable(true);
                    }
                    for(int i = 0; i< requestProjectSupportInfo.getSupportsInfo().getData().getList().size(); i++){
                        adapter.addItem(requestProjectSupportInfo.getSupportsInfo().getData().getList().get(i));
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case FANRequestCode.GET_PROJECT_DETAIL_SUPPORTER_FAILURE:
                    if(ProjectSupportListActivity.this.isFinishing()==true){
                        break;
                    }
                    Toast.makeText(ProjectSupportListActivity.this,"获取项目支持者信息失败",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    super.handleMessage(msg);
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_support_list);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_project_supporter_list);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("支持者列表");
        actionBar.setDisplayHomeAsUpEnabled(true);

        initVariables();
        initViews();
        loadData();
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

    //初始化数据
    private void initVariables(){
        Intent intent=getIntent();
        categoryId=intent.getIntExtra("categoryId",0);
        projectId=intent.getIntExtra("projectId",0);
        httpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
        requestProjectSupportInfo=new RequestProjectSupportInfo();
        adapter=new ProjectSupporterAdapter(this);
    }

    //初始化控件
    private void initViews(){
        lv_project_supporter=(XListView)findViewById(R.id.lv_project_supporter);
        lv_project_supporter.setAutoLoadEnable(false);
        lv_project_supporter.setPullLoadEnable(false);
        lv_project_supporter.setPullRefreshEnable(false);
        lv_project_supporter.setRefreshTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        lv_project_supporter.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                if(isFinishRequest==false){
                    return;
                }
                isFinishRequest=false;
                requestProjectSupportInfo.getProjectSupportsInfo(ProjectSupportListActivity.this,handler,httpClient,categoryId,projectId);
            }
        });
        lv_project_supporter.setAdapter(adapter);
    }

    //加载数据
    private void loadData(){
        requestProjectSupportInfo.getProjectSupportsInfo(this,handler,httpClient,categoryId,projectId);
    }

    private void endRefresh(){
        isFinishRequest=true;
        lv_project_supporter.stopRefresh();
        lv_project_supporter.stopLoadMore();
        lv_project_supporter.setRefreshTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
    }
}
