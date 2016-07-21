package com.fansfunding.fan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.fansfunding.internal.ErrorCode;
import com.fansfunding.internal.ProjectDetail;
import com.fansfunding.internal.SendComment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProjectCommentActivity extends AppCompatActivity {


    //发送评论成功
    private final static int SEND_COMMENT_SUCCESS=100;

    //发送评论失败
    private final static int SEND_COMMENT_FAILURE=101;

    //项目分类
    private int categoryId;

    //项目Id
    private int projectId;

    //指向
    private int pointTo;

    //指向的昵称
    private String pointToNickname;

    //循环等待框，不能取消
    private AlertDialog dialog_waitting;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //去掉循环等待框
            if(ProjectCommentActivity.this.isFinishing()==false){
                if(dialog_waitting.isShowing()==true){
                    dialog_waitting.cancel();
                }
            }
            switch(msg.what){

                //获取第一条评论成功
                case SEND_COMMENT_SUCCESS:
                    if(ProjectCommentActivity.this.isFinishing()==false){
                        setResult(RESULT_OK);
                        ProjectCommentActivity.this.finish();
                    }
                    break;
                //获取第一条评论失败
                case SEND_COMMENT_FAILURE:
                    if(ProjectCommentActivity.this.isFinishing()==true){
                        break;
                    }
                    Toast.makeText(ProjectCommentActivity.this,"发送评论失败",Toast.LENGTH_LONG).show();
                    break;
                case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                    if(ProjectCommentActivity.this.isFinishing()==true){
                        break;
                    }
                    Toast.makeText(ProjectCommentActivity.this,"请求过于频繁",Toast.LENGTH_LONG).show();

                    break;
                case ErrorCode.PARAMETER_ERROR:
                    if(ProjectCommentActivity.this.isFinishing()==true){
                        break;
                    }
                    Toast.makeText(ProjectCommentActivity.this,"参数错误",Toast.LENGTH_LONG).show();
                    break;
            }
            super.handleMessage(msg);

        }
    };

    private TextInputEditText et_PJ_comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_comment);


        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_PJ_comment);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        //设置返回键
        ActionBar actionBar=this.getSupportActionBar();
        actionBar.setTitle("写评论");
        actionBar.setHomeAsUpIndicator(R.drawable.arrow);
        actionBar.setDisplayHomeAsUpEnabled(true);


        //评论输入框
        et_PJ_comment=(TextInputEditText)findViewById(R.id.et_PJ_comment);

        Intent intent=getIntent();
        categoryId=intent.getIntExtra("categoryId",1);
        projectId=intent.getIntExtra("projectId",1);
        pointTo=intent.getIntExtra("pointTo",0);
        pointToNickname=intent.getStringExtra("pointToNickname");

        if(pointToNickname!=null){
            et_PJ_comment.setHint("回复 : "+pointToNickname);
        }else{
            et_PJ_comment.setHint("评论");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            //发送评论按钮
            case R.id.menu_send_comment:
                sendProjectComment();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_project_comment, menu);
        return true;
    }


    private void sendProjectComment(){
        //循环等待框
        dialog_waitting=new AlertDialog.Builder(this)
                .setTitle("数据传输")
                .setView(R.layout.activity_internal_waiting)
                .create();
        dialog_waitting.setCancelable(false);
        //dialog_waitting.show();

        OkHttpClient httpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
        String comment=et_PJ_comment.getText().toString();
        //如果评论为空，则不作处理
        if(comment==null||comment.equals("")==true){
            if(dialog_waitting.isShowing()==true){
                dialog_waitting.cancel();
            }
            return;
        }
        SharedPreferences share=getSharedPreferences(getString(R.string.sharepreference_login_by_phone),MODE_PRIVATE);
        int id=share.getInt("id",0);

        FormBody formBody=new FormBody.Builder()
                .add("userId",String.valueOf(id))
                .add("pointTo",String.valueOf(pointTo))
                .add("content",comment)
                .build();
        Request request=new Request.Builder()
                .post(formBody)
                .url(getString(R.string.url_project)+categoryId+"/"+projectId+"/comments")
                .build();

        Call call=httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg=new Message();
                msg.what=SEND_COMMENT_FAILURE;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //服务器响应失败
                if(response==null||response.isSuccessful()==false){
                    Message msg=new Message();
                    msg.what=SEND_COMMENT_FAILURE;
                    handler.sendMessage(msg);
                    return;
                }
                Gson gson=new GsonBuilder().create();
                String str_response=response.body().string();
                SendComment sendComment=new SendComment();
                try {

                    //用Gson进行解析，并判断结果是否为空
                    if((sendComment = gson.fromJson(str_response, sendComment.getClass()))==null){
                        Message msg=new Message();
                        msg.what=SEND_COMMENT_FAILURE;
                        handler.sendMessage(msg);
                    }

                    //发送评论失败
                    if(sendComment.isResult()==false){
                        Message msg=new Message();
                        switch (sendComment.getErrCode()){
                            case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                                msg.what=ErrorCode.REQUEST_TOO_FRENQUENTLY;
                                break;
                            case ErrorCode.PARAMETER_ERROR:
                                msg.what=ErrorCode.PARAMETER_ERROR;
                                break;
                            default:
                                msg.what=SEND_COMMENT_FAILURE;
                                break;
                        }
                        handler.sendMessage(msg);
                        return;
                    }

                    //获取项目信息成功
                    Message msg=new Message();
                    msg.what=SEND_COMMENT_SUCCESS;
                    handler.sendMessage(msg);
                }catch (IllegalStateException e){
                    Message msg=new Message();
                    msg.what=SEND_COMMENT_FAILURE;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }catch (JsonSyntaxException e){
                    Message msg=new Message();
                    msg.what=SEND_COMMENT_FAILURE;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }

            }
        });
    }
}
