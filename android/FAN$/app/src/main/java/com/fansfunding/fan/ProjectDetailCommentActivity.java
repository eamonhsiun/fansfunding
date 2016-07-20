package com.fansfunding.fan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.fansfunding.PullListView.XListView;
import com.fansfunding.internal.AllProjectInCategory;
import com.fansfunding.internal.ErrorCode;
import com.fansfunding.internal.ProjectDetailComment;
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

public class ProjectDetailCommentActivity extends AppCompatActivity {

    //发表评论请求码
    private static final int REQUEST_CODE_SEND_COMMENT=300;

    //获取评论成功
    private static final int GET_PROJECT_COMMENT_SUCCESS=102;
    //获取评论失败
    private static final int GET_PROJECT_COMMENT_FAILURE=103;

    private XListView  lv_PJ_detail_comment;

    //项目分类
    private int categoryId;

    //项目Id
    private int projectId;

    //项目描述信息(比如目标金额之类的)
    private AllProjectInCategory.ProjectDetail projectDetail;

    //是否已经完成了项目数据获取的请求
    private boolean isFinishRequest=true;


    //一次获取评论的数量
    private final int rows=20;

    //获取评论的页数
    private int page=1;

    //项目评论
    private  ProjectDetailComment projectDetailComment;


    //xlistview适配器
    private ProjectDetailCommentAdapter adapter;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                //获取评论成功
                case GET_PROJECT_COMMENT_SUCCESS:
                    endRefresh();
                    if(projectDetailComment.getData().getList().size()<rows){
                        page=1;
                    }else{
                        page++;
                    }
                    for(int i=0;i<projectDetailComment.getData().getList().size();i++){
                        adapter.addItem(projectDetailComment.getData().getList().get(i));
                    }
                    adapter.notifyDataSetChanged();

                    break;
                //获取评论失败
                case GET_PROJECT_COMMENT_FAILURE:

                    if(ProjectDetailCommentActivity.this.isFinishing()==true){
                        break;
                    }
                    endRefresh();
                    Toast.makeText(ProjectDetailCommentActivity.this,"获取评论失败",Toast.LENGTH_LONG).show();
                    break;
                case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                    if(ProjectDetailCommentActivity.this.isFinishing()==true){
                        break;
                    }
                    endRefresh();
                    Toast.makeText(ProjectDetailCommentActivity.this,"请求过于频繁",Toast.LENGTH_LONG).show();

                    break;
                case ErrorCode.PARAMETER_ERROR:
                    if(ProjectDetailCommentActivity.this.isFinishing()==true){
                        break;
                    }
                    endRefresh();
                    Toast.makeText(ProjectDetailCommentActivity.this,"参数错误",Toast.LENGTH_LONG).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail_comment);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_PJ_detail_comment);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        //设置返回键
        ActionBar actionBar=this.getSupportActionBar();
        actionBar.setTitle("评论");

        actionBar.setDisplayHomeAsUpEnabled(true);
        adapter=new ProjectDetailCommentAdapter(this);

        lv_PJ_detail_comment=(XListView)findViewById(R.id.lv_PJ_detail_comment);
        lv_PJ_detail_comment.setAutoLoadEnable(false);
        lv_PJ_detail_comment.setPullLoadEnable(false);
        lv_PJ_detail_comment.setPullRefreshEnable(true);
        lv_PJ_detail_comment.setRefreshTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        lv_PJ_detail_comment.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                if(isFinishRequest==true){
                    getProjectDetailComment();
                }

            }

            @Override
            public void onLoadMore() {

            }
        });

        lv_PJ_detail_comment.setAdapter(adapter);

        lv_PJ_detail_comment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences share=getSharedPreferences(getString(R.string.sharepreference_login_by_phone),MODE_PRIVATE);
                boolean isLogin=share.getBoolean("isLogin",false);
                Intent intent=new Intent();
                //如果没有登陆，则先登陆
                if(isLogin==false){
                    intent.setAction(getString(R.string.activity_login));
                    startActivity(intent);
                    return;
                }
                //打开评论页
                ProjectDetailComment.ProjectComment comment=(ProjectDetailComment.ProjectComment)lv_PJ_detail_comment.getAdapter().getItem(position);
                if(comment==null){
                    return;
                }
                intent.setAction(getString(R.string.activity_project_comment));
                intent.putExtra("categoryId",categoryId);
                intent.putExtra("projectId",projectId);
                intent.putExtra("pointTo",comment.getCommenterId());
                startActivityForResult(intent,REQUEST_CODE_SEND_COMMENT);
            }
        });

        Intent intent=getIntent();
        projectDetail= (AllProjectInCategory.ProjectDetail) intent.getSerializableExtra("detail");
        //项目分类
        categoryId=projectDetail.getCategoryId();

        //项目Id
        projectId=projectDetail.getId();

        getProjectDetailComment();

        final FloatingActionButton fab_PJ_detail_comment=(FloatingActionButton)findViewById(R.id.fab_PJ_detail_comment);
        fab_PJ_detail_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences share=getSharedPreferences(getString(R.string.sharepreference_login_by_phone),MODE_PRIVATE);
                boolean isLogin=share.getBoolean("isLogin",false);

                Intent intent=new Intent();
                //如果没有登陆，则先登陆
                if(isLogin==false){
                    intent.setAction(getString(R.string.activity_login));
                    startActivity(intent);
                    return;
                }
                //打开评论页
                intent.setAction(getString(R.string.activity_project_comment));
                intent.putExtra("categoryId",categoryId);
                intent.putExtra("projectId",projectId);
                intent.putExtra("pointTo",0);
                startActivityForResult(intent,REQUEST_CODE_SEND_COMMENT);
            }
        });
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
        lv_PJ_detail_comment.stopRefresh();
        lv_PJ_detail_comment.stopLoadMore();
        lv_PJ_detail_comment.setRefreshTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
    }

    private void getProjectDetailComment(){
        isFinishRequest=false;
        OkHttpClient httpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
        Request request=new Request.Builder()
                .get()
                .url(getString(R.string.url_project)+categoryId+"/"+projectId+"/comments?rows="+rows+"&page="+page)
                .build();
        Call call=httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg=new Message();
                msg.what=GET_PROJECT_COMMENT_FAILURE;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //服务器响应失败
                if(response==null||response.isSuccessful()==false){
                    Message msg=new Message();
                    msg.what=GET_PROJECT_COMMENT_FAILURE;
                    handler.sendMessage(msg);
                    return;
                }
                Gson gson=new GsonBuilder().create();
                String str_response=response.body().string();
                projectDetailComment=new ProjectDetailComment();

                try {

                    //用Gson进行解析，并判断结果是否为空
                    if((projectDetailComment = gson.fromJson(str_response, projectDetailComment.getClass()))==null){
                        Message msg=new Message();
                        msg.what=GET_PROJECT_COMMENT_FAILURE;
                        handler.sendMessage(msg);
                    }
                    //获取评论失败
                    if(projectDetailComment.isResult()==false){
                        Message msg=new Message();
                        switch (projectDetailComment.getErrCode()){
                            case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                                msg.what=ErrorCode.REQUEST_TOO_FRENQUENTLY;
                                break;
                            case ErrorCode.PARAMETER_ERROR:
                                msg.what=ErrorCode.PARAMETER_ERROR;
                                break;
                            default:
                                msg.what=GET_PROJECT_COMMENT_FAILURE;
                                break;
                        }
                        handler.sendMessage(msg);
                        return;
                    }

                    //获取评论成功
                    Message msg=new Message();
                    msg.what=GET_PROJECT_COMMENT_SUCCESS;
                    handler.sendMessage(msg);
                }catch (IllegalStateException e){
                    Message msg=new Message();
                    msg.what=GET_PROJECT_COMMENT_FAILURE;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }catch (JsonSyntaxException e){
                    Message msg=new Message();
                    msg.what=GET_PROJECT_COMMENT_FAILURE;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CODE_SEND_COMMENT:
                if(resultCode==RESULT_OK){
                    getProjectDetailComment();
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
