package com.fansfunding.fan;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.fansfunding.PullListView.XListView;
import com.fansfunding.fan.request.RequestSingleCategoryProject;
import com.fansfunding.fan.utils.ErrorHandler;
import com.fansfunding.fan.utils.FANRequestCode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/*
* 用来显示单个目录下的所有项目，比如美妆下的所有项目
* */
public class CategoryActivity extends AppCompatActivity {

    public final static String CATEGORY_NAME="CATEGORY_NAME";

    public final static String CATEGORY_ID="CATEGORY_ID";

    //是否完成请求
    private boolean isFinishRequest=true;

    //项目分类Id
    private int categoryId;

    private XListView lv_single_category_project;

    private String categoryName;

    private OkHttpClient httpClient;

    private RequestSingleCategoryProject requestSingleCategoryProject;

    private ListProjectAdapter adapter;

    private ErrorHandler handler=new ErrorHandler(this){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case FANRequestCode.GET_PROJECT_IN_CATEGORY_SUCCESS:
                    endRefresh();
                    if(requestSingleCategoryProject.getAllProjectInCategory().getData().getList().size()<requestSingleCategoryProject.getRows()){
                        requestSingleCategoryProject.setPage(1);
                        lv_single_category_project.setPullLoadEnable(false);
                        lv_single_category_project.setAutoLoadEnable(false);
                    }
                    else {
                        requestSingleCategoryProject.setPage(requestSingleCategoryProject.getPage()+1);
                        lv_single_category_project.setPullLoadEnable(true);
                        lv_single_category_project.setAutoLoadEnable(true);
                    }

                    for(int i=0;i<requestSingleCategoryProject.getAllProjectInCategory().getData().getList().size();i++){
                        adapter.addItemAtHead(requestSingleCategoryProject.getAllProjectInCategory().getData().getList().get(i));
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case FANRequestCode.GET_PROJECT_IN_CATEGORY_FAILURE:
                    endRefresh();
                    if(CategoryActivity.this.isFinishing()==false){
                        Toast.makeText(CategoryActivity.this,"获取分类下项目失败",Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        initVariables();
        initViews();
        loadData();

    }



    private void initVariables(){
        Intent intent=getIntent();
        categoryName=intent.getStringExtra(CATEGORY_NAME);
        categoryId=intent.getIntExtra(CATEGORY_ID,-1);

        httpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
        requestSingleCategoryProject=new RequestSingleCategoryProject();
        adapter=new ListProjectAdapter(this);
    }

    private void initViews(){
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_category);
        setSupportActionBar(toolbar);

        ActionBar actionBar=this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        if(categoryName!=null){
            actionBar.setTitle(categoryName);
        }

        lv_single_category_project=(XListView)findViewById(R.id.lv_single_category_project);
        lv_single_category_project.setAutoLoadEnable(false);
        lv_single_category_project.setPullLoadEnable(false);
        lv_single_category_project.setPullRefreshEnable(true);
        lv_single_category_project.setRefreshTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        lv_single_category_project.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                if(isFinishRequest==false){
                    return;
                }
                isFinishRequest=true;
                adapter.Clear();
                lv_single_category_project.setAutoLoadEnable(false);
                lv_single_category_project.setPullLoadEnable(false);
                requestSingleCategoryProject.setPage(1);
                requestSingleCategoryProject.requestSingleCategoryProject(CategoryActivity.this,handler,httpClient,categoryId);
            }

            @Override
            public void onLoadMore() {
                if(isFinishRequest==false){
                    return;
                }
                isFinishRequest=true;
                requestSingleCategoryProject.requestSingleCategoryProject(CategoryActivity.this,handler,httpClient,categoryId);
            }
        });

        lv_single_category_project.setAdapter(adapter);
    }

    private void loadData(){
        isFinishRequest=false;
        requestSingleCategoryProject.requestSingleCategoryProject(this,handler,httpClient,categoryId);
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

    //停止更新的动画
    private void endRefresh(){
        isFinishRequest=true;
        lv_single_category_project.stopRefresh();
        lv_single_category_project.stopLoadMore();
        lv_single_category_project.setRefreshTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
    }
}
