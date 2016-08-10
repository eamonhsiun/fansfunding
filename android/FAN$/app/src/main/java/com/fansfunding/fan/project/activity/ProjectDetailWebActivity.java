package com.fansfunding.fan.project.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.fansfunding.fan.R;
import com.fansfunding.fan.request.RequestProjectWeb;
import com.fansfunding.fan.utils.ErrorHandler;
import com.fansfunding.fan.utils.FANRequestCode;
import com.fansfunding.internal.ProjectDetail;
import com.fansfunding.internal.ProjectInfo;
import com.fansfunding.verticalslide.CustWebView;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class ProjectDetailWebActivity extends AppCompatActivity {


    //项目分类
    private int categoryId;

    //项目Id
    private int projectId;

    //获取富文本信息
    private RequestProjectWeb requestProjectWeb;

    //httpclient
    private OkHttpClient httpClient;

    //展示的webview
    private CustWebView web_project_detail;

    //handler
    private ErrorHandler handler=new ErrorHandler(this){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case FANRequestCode.GET_PROJECT_DETAIL_WEB_SUCCESS:
                    if(ProjectDetailWebActivity.this.isFinishing()==true){
                        break;
                    }
                    InitWeb();
                    break;
                case FANRequestCode.GET_PROJECT_DETAIL_WEB_FAILURE:
                    if(ProjectDetailWebActivity.this.isFinishing()==true){
                        break;
                    }
                    Toast.makeText(ProjectDetailWebActivity.this,"获取项目信息失败",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    super.handleMessage(msg);
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail_web);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_project_detail_web);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("项目详情");
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(Color.WHITE);

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
        requestProjectWeb=new RequestProjectWeb();
        httpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
    }

    //初始化控件
    private void initViews(){
        web_project_detail=(CustWebView)findViewById(R.id.web_project_detail);
        //拒绝执行js
        web_project_detail.getSettings().setJavaScriptEnabled(false);
        //设置编码样式
        web_project_detail.getSettings().setDefaultTextEncodingName("UTF-8");
        //设置自适应屏幕
        web_project_detail.getSettings().setUseWideViewPort(true);
        web_project_detail.getSettings().setLoadWithOverviewMode(true);
        // 支持缩放
        web_project_detail.getSettings().setSupportZoom(true);
        //适应屏幕，内容将自动缩放
        web_project_detail.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
    }

    //加载数据
    private void loadData(){
        requestProjectWeb.getProjectWeb(this,handler,httpClient,categoryId,projectId);
    }

    private void InitWeb(){
        if(requestProjectWeb.getProjectDetail().getData().getContent()!=null&&requestProjectWeb.getProjectDetail().getData().getContent().equals("")==false){

            //还需要继续更改
            String web="<html><head> <meta charset=\"utf-8\">"
                    +"  <meta name=\"viewport\" content=\"width=device-width, user-scalable=yes\">"
                    +"</head><body><div class=\"content-style\">"
                    +requestProjectWeb.getProjectDetail().getData().getContent()
                    +"</div></body><html>";
            web_project_detail.loadData(web,"text/html;charset=UTF-8",null);

        }else{
            web_project_detail.loadData("<html><head><meta contentType='text/html;charset=utf-8'><meta charset='utf-8'></head><body><p>" +
                    "无对应的项目详情" +
                    "</p></body></html>","text/html;charset=UTF-8",null);
        }
    }
}
