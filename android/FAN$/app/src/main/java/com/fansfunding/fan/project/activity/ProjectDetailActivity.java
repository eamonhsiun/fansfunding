package com.fansfunding.fan.project.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.fansfunding.fan.project.adapter.ProjectDetailAdapter;
import com.fansfunding.fan.project.fragment.*;
import com.fansfunding.fan.R;
import com.fansfunding.internal.EnsureUserFollowMessage;
import com.fansfunding.internal.ErrorCode;
import com.fansfunding.internal.FanRequest.ProjectMessage;
import com.fansfunding.internal.FeedbackCode;
import com.fansfunding.internal.ProjectInfo;
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

public class ProjectDetailActivity extends AppCompatActivity {

    //关注项目成功
    private static final int FOLLOW_PROJECT_SUCCESS=100;

    //关注项目失败
    private static final int FOLLOW_PROJECT_FAILURE=101;

    //取消关注项目成功
    private static final int CANCEL_FOLLOW_PROJECT_SUCCESS=102;

    //取消关注项目失败
    private static final int CANCEL_FOLLOW_PROJECT_FAILURE=103;

    //请求用户是否关注该项目成功
    private static final int ENSURE_USER_FOLLOW_PROJECT_SUCCESS=104;

    //请求用户是否关注该项目成功
    private static final int ENSURE_USER_FOLLOW_PROJECT_FAILURE=105;

    //正在请求用户是否关注该项目
    private static final int ENSUREING_USER_FOLLOW_INFO=106;

    //获取项目信息成功
    private static final int GET_PROJECT_DETAIL_SUCCESS=107;

    //获取项目信息失败
    private static final int GET_PROJECT_DETAIL_FAILURE=108;

    //登录按钮启动码
    private static final int REQUEST_CODE_LOGIN=109;



    private ViewPager vp_project_detail;
    private ProjectDetailAdapter adapter;
    private TabLayout tabLayout;

    private Fragment fragment_first;
    private Fragment fragment_second;

    //关注按钮
    private MenuItem menuItem_follow;
    private ImageView iv_project_detail_follow_project;
    //是否正在请求关注项目
    private boolean isFinishFollow=true;

    //是否已经关注了项目
    private boolean isFollow=false;

    //是否正在请求 用户是否已经关注了该项目
    private boolean isGetUserFollowMessage=false;

    //通过查找分类下所有项目所获取的数据
    private ProjectInfo detail;

    //用户关注信息
    private EnsureUserFollowMessage feedbackCode;

    //用户是否登录
    private boolean isLogin;

    //用户id
    private int userId;

    //用户token
    private String token;

    //项目分类id
    private int categoryId;

    //项目id
    private int projectId;

    //httpclient
    //private OkHttpClient httpClient;

    //handler
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){

                case GET_PROJECT_DETAIL_SUCCESS:

                    //获取用户关注信息
                    if(isLogin==true){
                        isUserFollow(userId,categoryId,projectId);
                        isGetUserFollowMessage=false;
                    }else{
                        isGetUserFollowMessage=true;
                    }
                    InitFragment();
                    break;

                case GET_PROJECT_DETAIL_FAILURE:
                    if(ProjectDetailActivity.this.isFinishing()==true){
                        break;
                    }
                    Toast.makeText(ProjectDetailActivity.this,"获取项目信息失败",Toast.LENGTH_LONG).show();
                    break;
                case FOLLOW_PROJECT_SUCCESS:
                    //关注状态为关注
                    isFollow=true;
                    //已经结束了请求
                    isFinishFollow=true;

                    if(ProjectDetailActivity.this.isFinishing()==false){
                        Toast.makeText(ProjectDetailActivity.this, "关注成功", Toast.LENGTH_LONG).show();
                    }else {
                        break;
                    }
                    //设置menu菜单上的按钮图标
                    //menuItem_follow.setIcon(getResources().getDrawable(R.drawable.like_pressed));
                    iv_project_detail_follow_project.setImageResource(R.drawable.like_pressed);
                    break;
                case FOLLOW_PROJECT_FAILURE:
                    if(ProjectDetailActivity.this.isFinishing()==true){
                        break;
                    }
                    Toast.makeText(ProjectDetailActivity.this, "关注失败", Toast.LENGTH_LONG).show();

                    break;
                case CANCEL_FOLLOW_PROJECT_SUCCESS:

                    //关注状态未关注
                    isFollow=false;
                    //已经结束了请求
                    isFinishFollow=true;
                    if(ProjectDetailActivity.this.isFinishing()==false){
                        Toast.makeText(ProjectDetailActivity.this, "取消关注成功", Toast.LENGTH_LONG).show();
                    }else {
                        break;
                    }
                    //设置menu菜单上的按钮图标
                    //menuItem_follow.setIcon(getResources().getDrawable(R.drawable.like));
                    iv_project_detail_follow_project.setImageResource(R.drawable.like);
                    break;

                case CANCEL_FOLLOW_PROJECT_FAILURE:
                    if(ProjectDetailActivity.this.isFinishing()==true){
                        break;
                    }
                    Toast.makeText(ProjectDetailActivity.this, "取消关注失败", Toast.LENGTH_LONG).show();
                    break;

                case ENSURE_USER_FOLLOW_PROJECT_SUCCESS:
                    isGetUserFollowMessage=true;
                    //设置菜单按钮的图片
                    if(feedbackCode.isData()==true){
                        isFollow=true;
                        /*if(menuItem_follow !=null){
                            menuItem_follow.setIcon(getResources().getDrawable(R.drawable.like_pressed));
                        }*/
                        iv_project_detail_follow_project.setImageResource(R.drawable.like_pressed);

                    }else {
                        isFollow=false;
                        /*if(menuItem_follow !=null){
                            menuItem_follow.setIcon(getResources().getDrawable(R.drawable.like));

                        }*/
                        iv_project_detail_follow_project.setImageResource(R.drawable.like);
                    }

                    break;
                case ENSURE_USER_FOLLOW_PROJECT_FAILURE:
                    isGetUserFollowMessage=true;
                    if(ProjectDetailActivity.this.isFinishing()==true){
                        break;
                    }
                    Toast.makeText(ProjectDetailActivity.this, "请求关注信息失败", Toast.LENGTH_LONG).show();
                    break;

                case ENSUREING_USER_FOLLOW_INFO:
                    if(ProjectDetailActivity.this.isFinishing()==true){
                        break;
                    }
                    Toast.makeText(ProjectDetailActivity.this, "正在请求关注信息，请稍等", Toast.LENGTH_LONG).show();
                    break;
                case ErrorCode.PARAMETER_ERROR:
                    if(ProjectDetailActivity.this.isFinishing()==true){
                        break;
                    }
                    Toast.makeText(ProjectDetailActivity.this, "参数错误", Toast.LENGTH_LONG).show();
                    break;
                case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                    if(ProjectDetailActivity.this.isFinishing()==true){
                        break;
                    }
                    Toast.makeText(ProjectDetailActivity.this, "请求过于频繁", Toast.LENGTH_LONG).show();
                    break;

            }
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detial);

        //httpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_project_detail);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("项目详情");
        actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setHomeAsUpIndicator(R.drawable.arrow_back);
        toolbar.setTitleTextColor(Color.WHITE);
        Intent intent=getIntent();



        //获取登录状态和用户id和token
        SharedPreferences share=getSharedPreferences(getString(R.string.sharepreference_login_by_phone),MODE_PRIVATE);
        isLogin=share.getBoolean("isLogin",false);
        userId=share.getInt("id",0);
        token=share.getString("token"," ");
        detail= (ProjectInfo) intent.getSerializableExtra("detail");

        if(detail!=null){
            //获取项目id和项目分类id
            categoryId=detail.getCategoryId();
            projectId=detail.getId();

            if(isLogin==true){
                isUserFollow(userId,categoryId,projectId);
                isGetUserFollowMessage=false;
            }else{
                //isGetUserFollowMessage=true;
            }

            //初始化其他fragment
            //InitFragment();
        }else {
            categoryId=intent.getIntExtra("categoryId",1);
            projectId=intent.getIntExtra("projectId",1);

        }

        //获取项目信息
        getProjectInfo(categoryId,projectId);



        Button btn_project_detail_support=(Button)findViewById(R.id.btn_project_detail_support);
        btn_project_detail_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //如果没有登陆，就先登陆
                if(isLogin==false){
                    Intent intent=new Intent();
                    intent.setAction(getString(R.string.activity_login));
                    startActivityForResult(intent,REQUEST_CODE_LOGIN);
                    return;
                }

                Intent intent=new Intent();
                intent.setAction(getString(R.string.activity_project_support));
                if(detail==null){
                    Toast.makeText(ProjectDetailActivity.this,"还未获取项目信息，请稍等",Toast.LENGTH_LONG).show();
                    return;
                }
                intent.putExtra("detail",detail);
                startActivity(intent);
            }
        });

        //关注按钮
        iv_project_detail_follow_project=(ImageView)findViewById(R.id.iv_project_detail_follow_project);
        iv_project_detail_follow_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFinishFollow==false){
                    return;
                }
                if(isLogin==true){
                    if(isGetUserFollowMessage==false){
                        Toast.makeText(ProjectDetailActivity.this,"正在获取关注信息，请稍等",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //如果没有关注
                    if(isFollow==false){
                        FollowProject(userId,token,categoryId,projectId);
                    }
                    //如果已经关注
                    else {
                        CancelFollowProject(userId,token,categoryId,projectId);
                    }

                }else{
                    Intent intent=new Intent();
                    intent.setAction(getString(R.string.activity_login));
                    startActivityForResult(intent,REQUEST_CODE_LOGIN);
                }
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
           /* case R.id.menu_project_detail_follow:
                //如果正在请求关注或者取消关注，则不做任何反应
                if(isFinishFollow==false){
                    break;
                }
                if(isLogin==true){
                    //如果没有关注
                    if(isFollow==false){
                        FollowProject(userId,token,categoryId,projectId);
                    }
                    //如果已经关注
                    else {
                        CancelFollowProject(userId,token,categoryId,projectId);
                    }

                }else{
                    Intent intent=new Intent();
                    intent.setAction(getString(R.string.activity_login));
                    startActivityForResult(intent,REQUEST_CODE_LOGIN);
                }
                break;
            case R.id.menu_project_detail_share:

                break;*/


            default:
                return super.onOptionsItemSelected(item);
        }
        return true;

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        /*getMenuInflater().inflate(R.menu.menu_project_detail, menu);
        menuItem_follow =menu.getItem(0);
        if(menuItem_follow !=null){
            if(isFollow==true){
                menuItem_follow.setIcon(getResources().getDrawable(R.drawable.like_pressed));
            }else{
                menuItem_follow.setIcon(getResources().getDrawable(R.drawable.like));
            }
        }*/
        return super.onCreateOptionsMenu(menu);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CODE_LOGIN:
                if(resultCode==RESULT_OK){
                    SharedPreferences share=getSharedPreferences(getString(R.string.sharepreference_login_by_phone),MODE_PRIVATE);
                    isLogin=share.getBoolean("isLogin",false);
                    userId=share.getInt("id",0);
                    token=share.getString("token"," ");
                    isUserFollow(userId,categoryId,projectId);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void InitFragment(){
        fragment_first=ProjectDetailMainFragment.newInstance(detail);
        fragment_second= ProjectDetailViewPaperFragment.newInstance(detail);
        if(ProjectDetailActivity.this.isFinishing()==false){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_project_detail_first, fragment_first).add(R.id.frame_project_detail_second, fragment_second)
                    .commitAllowingStateLoss();
        }

    }



    //关注请求
    private void FollowProject(final int userId,final String token,final int categoryId,final int projectId){
        if(isGetUserFollowMessage==false){
            handler.sendEmptyMessage(ENSUREING_USER_FOLLOW_INFO);
            return;
        }
        isFinishFollow=false;

        OkHttpClient httpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
        FormBody formBody=new FormBody.Builder()
                .add("token",token)
                .build();
        Request request=new Request.Builder()
                .post(formBody)
                .url(getString(R.string.url_user)+userId+"/follow/"+categoryId+"/"+projectId+"?token="+token)
                .build()
                ;

        Call call=httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(FOLLOW_PROJECT_FAILURE);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response==null||response.isSuccessful()==false){
                    handler.sendEmptyMessage(FOLLOW_PROJECT_FAILURE);
                    return;
                }
                Gson gson=new GsonBuilder().create();
                String str_response=response.body().string();
                FeedbackCode feedbackCode=new FeedbackCode();
                try {

                    //用Gson进行解析，并判断结果是否为空
                    if((feedbackCode = gson.fromJson(str_response, feedbackCode.getClass()))==null){
                        handler.sendEmptyMessage(FOLLOW_PROJECT_FAILURE);
                        return;
                    }
                    //关注项目失败
                    if(feedbackCode.isResult()==false){
                        switch (feedbackCode.getErrCode()){
                            case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                                handler.sendEmptyMessage(ErrorCode.REQUEST_TOO_FRENQUENTLY);
                                break;
                            case ErrorCode.PARAMETER_ERROR:
                                handler.sendEmptyMessage(ErrorCode.PARAMETER_ERROR);
                                break;
                            default:
                                handler.sendEmptyMessage(FOLLOW_PROJECT_FAILURE);
                                break;
                        }
                        return;
                    }

                    //关注项目成功
                    handler.sendEmptyMessage(FOLLOW_PROJECT_SUCCESS);
                }catch (IllegalStateException e){
                    handler.sendEmptyMessage(FOLLOW_PROJECT_FAILURE);
                    e.printStackTrace();
                }catch (JsonSyntaxException e){
                    handler.sendEmptyMessage(FOLLOW_PROJECT_FAILURE);
                    e.printStackTrace();
                }
            }
        });


    }

    //取消关注
    private void CancelFollowProject(final int userId,final String token,final int categoryId,final int projectId){
        if(isGetUserFollowMessage==false){
            handler.sendEmptyMessage(ENSUREING_USER_FOLLOW_INFO);
            return;
        }
        isFinishFollow=false;

        OkHttpClient httpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
        FormBody formBody=new FormBody.Builder()
                .add("token",token)
                .build();
        Request request=new Request.Builder()
                .url(getString(R.string.url_user)+userId+"/unfollow/"+categoryId+"/"+projectId)
                .post(formBody)
                .build();
        Call call=httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(CANCEL_FOLLOW_PROJECT_FAILURE);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response==null||response.isSuccessful()==false){
                    handler.sendEmptyMessage(CANCEL_FOLLOW_PROJECT_FAILURE);
                    return;
                }
                Gson gson=new GsonBuilder().create();
                String str_response=response.body().string();

                FeedbackCode feedbackCode=new FeedbackCode();
                try {

                    //用Gson进行解析，并判断结果是否为空
                    if((feedbackCode = gson.fromJson(str_response, feedbackCode.getClass()))==null){
                        handler.sendEmptyMessage(CANCEL_FOLLOW_PROJECT_FAILURE);
                        return;
                    }
                    //关注项目失败
                    if(feedbackCode.isResult()==false){
                        switch (feedbackCode.getErrCode()){
                            case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                                handler.sendEmptyMessage(ErrorCode.REQUEST_TOO_FRENQUENTLY);
                                break;
                            case ErrorCode.PARAMETER_ERROR:
                                handler.sendEmptyMessage(ErrorCode.PARAMETER_ERROR);
                                break;
                            default:
                                handler.sendEmptyMessage(FOLLOW_PROJECT_FAILURE);
                                break;
                        }
                        return;
                    }

                    //关注项目成功
                    handler.sendEmptyMessage(CANCEL_FOLLOW_PROJECT_SUCCESS);
                }catch (IllegalStateException e){
                    handler.sendEmptyMessage(CANCEL_FOLLOW_PROJECT_FAILURE);
                    e.printStackTrace();
                }catch (JsonSyntaxException e){
                    handler.sendEmptyMessage(CANCEL_FOLLOW_PROJECT_FAILURE);
                    e.printStackTrace();
                }
            }
        });
    }


    //判断用户是否关注
    private void isUserFollow(final int userId,final int categoryId,final int projectId){

        OkHttpClient httpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();

        FormBody formBody=new FormBody.Builder()
                .add("userId",String.valueOf(userId))
                .build();

        Request request=new Request.Builder()
                .post(formBody)
                .url(getString(R.string.url_project)+categoryId+"/"+projectId+"/followers")
                .build();

        Call call=httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(ENSURE_USER_FOLLOW_PROJECT_FAILURE);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response==null||response.isSuccessful()==false){
                    handler.sendEmptyMessage(ENSURE_USER_FOLLOW_PROJECT_FAILURE);
                    return;
                }
                Gson gson=new GsonBuilder().create();
                String str_response=response.body().string();
                feedbackCode=new EnsureUserFollowMessage();
                try {

                    //用Gson进行解析，并判断结果是否为空
                    if((feedbackCode = gson.fromJson(str_response, feedbackCode.getClass()))==null){
                        handler.sendEmptyMessage(ENSURE_USER_FOLLOW_PROJECT_FAILURE);
                        return;
                    }
                    //关注项目失败
                    if(feedbackCode.isResult()==false){
                        switch (feedbackCode.getErrCode()){
                            case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                                handler.sendEmptyMessage(ErrorCode.REQUEST_TOO_FRENQUENTLY);
                                break;
                            case ErrorCode.PARAMETER_ERROR:
                                handler.sendEmptyMessage(ErrorCode.PARAMETER_ERROR);
                                break;
                            default:
                                handler.sendEmptyMessage(ENSURE_USER_FOLLOW_PROJECT_FAILURE);
                                break;
                        }
                        return;
                    }


                    //关注项目成功
                    handler.sendEmptyMessage(ENSURE_USER_FOLLOW_PROJECT_SUCCESS);
                }catch (IllegalStateException e){
                    handler.sendEmptyMessage(ENSURE_USER_FOLLOW_PROJECT_FAILURE);
                    e.printStackTrace();
                }catch (JsonSyntaxException e){
                    handler.sendEmptyMessage(ENSURE_USER_FOLLOW_PROJECT_FAILURE);
                    e.printStackTrace();
                }
            }
        });
    }

    //获取项目信息
    private void getProjectInfo(final int categoryId,final int projectId){

        OkHttpClient httpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();

        Request request=new Request.Builder()
                .url(getString(R.string.url_project)+categoryId+"/"+projectId)
                .get()
                .build();

        Call call=httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(GET_PROJECT_DETAIL_FAILURE);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //服务器响应失败
                if(response==null||response.isSuccessful()==false){
                    handler.sendEmptyMessage(GET_PROJECT_DETAIL_FAILURE);
                    return;
                }
                Gson gson=new GsonBuilder().create();
                String str_response=response.body().string();
                Log.i("TAG","project info:"+str_response);
                ProjectMessage project=new ProjectMessage();
                try {

                    //用Gson进行解析，并判断结果是否为空
                    if((project = gson.fromJson(str_response, project.getClass()))==null){
                        handler.sendEmptyMessage(GET_PROJECT_DETAIL_FAILURE);
                        return;
                    }
                    //搜索用户发起项目失败
                    if(project.isResult()==false){
                        switch (project.getErrCode()){
                            case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                                handler.sendEmptyMessage(ErrorCode.REQUEST_TOO_FRENQUENTLY);
                                break;
                            case ErrorCode.PARAMETER_ERROR:
                                handler.sendEmptyMessage(ErrorCode.PARAMETER_ERROR);
                                break;
                            default:
                                handler.sendEmptyMessage(GET_PROJECT_DETAIL_FAILURE);
                                break;
                        }
                        return;
                    }

                    detail=project.getData();

                    //搜索用户发起项目成功
                    handler.sendEmptyMessage(GET_PROJECT_DETAIL_SUCCESS);
                }catch (IllegalStateException e){
                    handler.sendEmptyMessage(GET_PROJECT_DETAIL_FAILURE);
                    e.printStackTrace();
                }catch (JsonSyntaxException e){
                    handler.sendEmptyMessage(GET_PROJECT_DETAIL_FAILURE);
                    e.printStackTrace();
                }

            }
        });
    }

}
