package com.fansfunding.fan.project.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.fansfunding.fan.R;
import com.fansfunding.fan.request.RequestSendComment;
import com.fansfunding.fan.utils.ErrorHandler;
import com.fansfunding.fan.utils.FANRequestCode;
import com.fansfunding.internal.ErrorCode;
import com.fansfunding.internal.FeedbackCode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.rockerhieu.emojicon.EmojiconEditText;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProjectCommentActivity extends AppCompatActivity implements EmojiconGridFragment.OnEmojiconClickedListener,EmojiconsFragment.OnEmojiconBackspaceClickedListener{

    //发布项目的评论
    public final static int SEND_PROJECT_COMMENT=1;

    //发布动态的评论
    public final static int SEND_MOMENT_COMMENT=2;

    //用来判断发布什么的评论（比如是发布动态的评论还是项目的评论）
    private int mode;

    //用户id
    private int userId;

    //用户token
    private String token;

    //httpclient
    private OkHttpClient httpClient;

    //项目分类
    private int categoryId;

    //项目Id
    private int projectId;

    //动态id
    private int momentId;
    //指向
    private int pointTo;

    //指向的昵称
    private String pointToNickname;

    //emoji输入栏
    private EmojiconEditText et_PJ_comment;

    //emoji的fragment的tag
    private final String TAG_EMOJICON="EMOJICON";

    //emoji的展示fragment
    private EmojiconsFragment emojiconsFragment;

    private ErrorHandler handler=new ErrorHandler(this){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                //发送项目的评论成功
                case FANRequestCode.SEND_PROJECT_COMMENT_SUCCESS:
                    if(ProjectCommentActivity.this.isFinishing()==false){
                        setResult(RESULT_OK);
                        ProjectCommentActivity.this.finish();
                    }
                    break;
                //发送项目的评论失败
                case FANRequestCode.SEND_PROJECT_COMMENT_FAILURE:
                    if(ProjectCommentActivity.this.isFinishing()==true){
                        break;
                    }
                    Toast.makeText(ProjectCommentActivity.this,"发送评论失败",Toast.LENGTH_LONG).show();
                    break;
                default:
                    super.handleMessage(msg);
            }


        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_comment);

        initValues();

        initViews();

        loadData();
    }

    private void initValues(){
        Intent intent=getIntent();
        categoryId=intent.getIntExtra("categoryId",-1);
        projectId=intent.getIntExtra("projectId",-1);
        pointTo=intent.getIntExtra("pointTo",0);
        mode=intent.getIntExtra("mode",SEND_PROJECT_COMMENT);
        pointToNickname=intent.getStringExtra("pointToNickname");

        httpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();

        SharedPreferences share=getSharedPreferences(getString(R.string.sharepreference_login_by_phone),MODE_PRIVATE);
        userId=share.getInt("id",0);
        token=share.getString("token"," ");

    }

    private void initViews(){
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_PJ_comment);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        //设置返回键
        ActionBar actionBar=this.getSupportActionBar();
        actionBar.setTitle("写评论");
        actionBar.setDisplayHomeAsUpEnabled(true);
        //评论输入框
        et_PJ_comment=(EmojiconEditText)findViewById(R.id.et_PJ_comment);
        if(pointToNickname!=null){
            et_PJ_comment.setHint("回复 : "+pointToNickname);
        }else{
            et_PJ_comment.setHint("评论");
        }

        //emoji的fragment
        emojiconsFragment=new EmojiconsFragment().newInstance(false);
        setEmojiconFragment(false);
    }

    private void loadData(){

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

    private void setEmojiconFragment(boolean useSystemDefault) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_PJ_comment_emojicon, emojiconsFragment,TAG_EMOJICON)
                .commit();
    }

    private void removeEmojiconFragment(){
        getSupportFragmentManager()
                .beginTransaction()
                .remove(getSupportFragmentManager().findFragmentByTag(TAG_EMOJICON))
                .commit();
    }

    //发送项目评论
    private void sendProjectComment(){
        String comment=et_PJ_comment.getText().toString();
        //如果评论为空，则不作处理
        if(comment==null||comment.equals("")==true){
            Toast.makeText(this,"评论不能为空",Toast.LENGTH_LONG).show();
            return;
        }
        if(comment.length()>140){
            Toast.makeText(this,"评论字数应为140字以内",Toast.LENGTH_LONG).show();
            return;
        }
        RequestSendComment.sendProjectComment(this,handler,httpClient,userId,token,categoryId,projectId,comment,pointTo);
    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {
        EmojiconsFragment.backspace(et_PJ_comment);
    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(et_PJ_comment, emojicon);
        removeEmojiconFragment();
    }
}
