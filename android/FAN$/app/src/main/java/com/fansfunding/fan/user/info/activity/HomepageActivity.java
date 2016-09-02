package com.fansfunding.fan.user.info.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fansfunding.fan.R;
import com.fansfunding.fan.UserInfoActivity;
import com.fansfunding.fan.request.user.RequestUserFollowProject;
import com.fansfunding.fan.request.user.RequestUserInfo;
import com.fansfunding.fan.request.user.RequestUserSponsorProject;
import com.fansfunding.fan.request.user.RequestUserSupportProject;
import com.fansfunding.fan.user.info.adapter.UserProjectListAdapter;
import com.fansfunding.fan.utils.ErrorHandler;
import com.fansfunding.fan.utils.FANRequestCode;
import com.fansfunding.internal.PersonalInfo;
import com.fansfunding.internal.ProjectInfo;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;

public class HomepageActivity extends AppCompatActivity {

    //请求该activity所需要的参数

    public final static String TARGET_USERID="TARGET_USERID";


    //是否已经完成了对个人发起项目的请求
    private boolean isFinishRequestSponsorProject=true;

    //是否已经完成了对个人关注项目的请求
    private boolean isFinishRequestFollowProject=true;

    //是否已经完成了对个人支持项目的请求
    private boolean isFinishRequestSupportProject=true;

    //所要展示的用户id
    private int target_userId;

    //app用户的id
    private int userId;

    //请求用户个人信息
    private RequestUserInfo requestUserInfo;

    //请求用户发起项目信息
    RequestUserSponsorProject requestUserSponsorProject;

    //请求用户关注的项目信息
    RequestUserFollowProject requestUserFollowProject;

    //请求用户支持的项目信息
    RequestUserSupportProject requestUserSupportProject;


    private OkHttpClient httpClient;


    //用户头像
    private CircleImageView iv_homepage_user_head;

    //用户签名
    private TextView tv_homepage_user_sign;

    //用户动态数量
    private TextView tv_homepage_user_moment_number;

    //用户关注数量
    private TextView tv_homepage_user_following_number;

    //用户粉丝数量
    private TextView tv_homepage_user_follower_number;

    //用户动态点击区域
    private LinearLayout ll_homepage_user_moment;

    //用户关注点击区域
    private LinearLayout ll_homepage_user_following;

    //用户粉丝点击区域
    private LinearLayout ll_homepage_user_follower;

    //用户项目展示列表
    private ExpandableListView elv_homepage_project;

    //列表头部
    private RelativeLayout homepage_header;

    //适配器
    private UserProjectListAdapter adapter;

    private ErrorHandler handler=new ErrorHandler(this){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case FANRequestCode.GET_PERSONAL_INFO_SUCCESS:
                    setUserInfo();
                    break;
                case FANRequestCode.GET_PERSONAL_INFO_FAILURE:
                    if(HomepageActivity.this.isFinishing()==false){
                        Toast.makeText(HomepageActivity.this,"请求用户信息失败",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case FANRequestCode.GER_USER_SPONSOR_PROJECT_SUCCESS:
                    //完成请求
                    isFinishRequestSponsorProject=true;
                    //如果返回的结果为空
                    if(requestUserSponsorProject.getUserPublishProject()==null){
                        break;
                    }
                    //如果获取到的项目为空
                    if(requestUserSponsorProject.getUserPublishProject().getData().getList().size()==0&&requestUserSponsorProject.getUserPublishProject().getData().getTotal()!=0&&adapter.getChildrenCount(UserProjectListAdapter.sponsorPosition)<=1){
                        if(HomepageActivity.this.isFinishing()==false){
                            Toast.makeText(HomepageActivity.this,"已无更多项目",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        requestUserSponsorProject.setPage(requestUserSponsorProject.getPage()+1);
                    }
                    for(int i=0;i<requestUserSponsorProject.getUserPublishProject().getData().getList().size();i++){
                        if(requestUserSponsorProject.getUserPublishProject().getData().getList().get(i)!=null){
                            adapter.addSponsorItem(requestUserSponsorProject.getUserPublishProject().getData().getList().get(i));
                        }
                    }
                    adapter.setSponsorProjectNum(requestUserSponsorProject.getUserPublishProject().getData().getTotal());
                    adapter.notifyDataSetChanged();


                    break;
                case FANRequestCode.GET_USER_SPONSOR_PROJECT_FAILURE:
                    //完成请求
                    isFinishRequestSponsorProject=true;
                    if(HomepageActivity.this.isFinishing()==false){
                        Toast.makeText(HomepageActivity.this,"请求用户发起项目失败",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case FANRequestCode.GET_USER_FOLLOW_PROJECT_SUCCESS:
                    //完成请求
                    isFinishRequestFollowProject=true;
                    //如果返回结果为空
                    if(requestUserFollowProject.getUserFollowProject()==null){
                        break;
                    }
                    //如果返回的项目为空
                    if(requestUserFollowProject.getUserFollowProject().getData().getList().size()==0&&requestUserFollowProject.getUserFollowProject().getData().getTotal()!=0&&adapter.getChildrenCount(UserProjectListAdapter.followPosition)<=1){
                        if(HomepageActivity.this.isFinishing()==false){
                            Toast.makeText(HomepageActivity.this,"已无更多项目",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        requestUserFollowProject.setPage(requestUserFollowProject.getPage()+1);
                    }
                    //添加项目
                    for(int i=0;i<requestUserFollowProject.getUserFollowProject().getData().getList().size();i++){
                        if(requestUserFollowProject.getUserFollowProject().getData().getList().get(i)!=null){
                            adapter.addFollowItem(requestUserFollowProject.getUserFollowProject().getData().getList().get(i));
                        }
                    }
                    adapter.setFollowProjectNum(requestUserFollowProject.getUserFollowProject().getData().getTotal());
                    adapter.notifyDataSetChanged();


                    break;
                case FANRequestCode.GET_USER_FOLLOW_PROJECT_FAILURE:
                    //完成请求
                    isFinishRequestFollowProject=true;
                    if(HomepageActivity.this.isFinishing()==false){
                        Toast.makeText(HomepageActivity.this,"请求用户关注项目失败",Toast.LENGTH_SHORT).show();
                    }

                    break;
                case FANRequestCode.GET_USER_SUPPORT_PROJECT_SUCCESS:
                    //完成请求
                    isFinishRequestSupportProject=true;
                    //如果返回结果为空
                    if(requestUserSupportProject.getUserSupportProject()==null){
                        break;
                    }
                    //如果返回的项目为空
                    if(requestUserSupportProject.getUserSupportProject().getData().getList().size()==0&&requestUserSupportProject.getUserSupportProject().getData().getTotal()!=0&&adapter.getChildrenCount(UserProjectListAdapter.supportPosition)<=1){
                        if(HomepageActivity.this.isFinishing()==false){
                            Toast.makeText(HomepageActivity.this,"已无更多项目",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        requestUserSupportProject.setPage(requestUserSupportProject.getPage()+1);
                    }
                    //添加项目
                    for(int i=0;i<requestUserSupportProject.getUserSupportProject().getData().getList().size();i++){
                        if(requestUserSupportProject.getUserSupportProject().getData().getList().get(i)!=null){
                            adapter.addSupportItem(requestUserSupportProject.getUserSupportProject().getData().getList().get(i));
                        }
                    }
                    adapter.setSupportProjectNum(requestUserSupportProject.getUserSupportProject().getData().getTotal());
                    adapter.notifyDataSetChanged();

                    break;
                case FANRequestCode.GET_USER_SUPPORT_PROJECT_FAILURE:
                    //完成请求
                    isFinishRequestSupportProject=true;
                    if(HomepageActivity.this.isFinishing()==false){
                        Toast.makeText(HomepageActivity.this,"请求用户支持项目失败",Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_homepage);
        initVariables();
        initViews();
        loadData();
    }


    private void initVariables(){
        target_userId=getIntent().getIntExtra(TARGET_USERID,-1);
        userId=getSharedPreferences(getString(R.string.sharepreference_login_by_phone),MODE_PRIVATE).getInt("id",-2);
        httpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
        requestUserInfo=new RequestUserInfo();
        adapter=new UserProjectListAdapter(this);

        requestUserSponsorProject=new RequestUserSponsorProject();
        requestUserFollowProject=new RequestUserFollowProject();
        requestUserSupportProject=new RequestUserSupportProject();
    }

    private void initViews(){
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_homepage);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        //设置返回键
        ActionBar actionBar=this.getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);

        homepage_header= (RelativeLayout)LayoutInflater.from(this).inflate(R.layout.activity_homepage_header,null);

        iv_homepage_user_head=(CircleImageView)homepage_header.findViewById(R.id.iv_homepage_user_head);
        tv_homepage_user_sign=(TextView)homepage_header.findViewById(R.id.tv_homepage_user_sign);
        tv_homepage_user_moment_number=(TextView)homepage_header.findViewById(R.id.tv_homepage_user_moment_number);
        tv_homepage_user_following_number=(TextView)homepage_header.findViewById(R.id.tv_homepage_user_following_number);
        tv_homepage_user_follower_number=(TextView)homepage_header.findViewById(R.id.tv_homepage_user_follower_number);
        ll_homepage_user_moment=(LinearLayout)homepage_header.findViewById(R.id.ll_homepage_user_moment);
        ll_homepage_user_following=(LinearLayout)homepage_header.findViewById(R.id.ll_homepage_user_following);
        ll_homepage_user_follower=(LinearLayout)homepage_header.findViewById(R.id.ll_homepage_user_follower);
        elv_homepage_project=(ExpandableListView)findViewById(R.id.elv_homepage_project);
        elv_homepage_project.addHeaderView(homepage_header);
    }

    private void loadData(){
        //请求用户个人信息
        requestUserInfo.requestPersonalInfo(this,handler,httpClient,target_userId);
        getUserSponsorProject();
        getUserFollowProject();
        getUserSupportProject();
    }

    private void setUserInfo(){
        if(requestUserInfo.getPersonalInfo()==null){
            return;
        }
        PersonalInfo personalInfo=requestUserInfo.getPersonalInfo();
        if(personalInfo.getData().getHead()!=null&&personalInfo.getData().getHead().equals("")==false){
            Picasso.with(this).load(getString(R.string.url_resources)+personalInfo.getData().getHead()).memoryPolicy(MemoryPolicy.NO_CACHE).into(iv_homepage_user_head);
        }
        getSupportActionBar().setTitle(""+personalInfo.getData().getNickname());
        tv_homepage_user_sign.setText(personalInfo.getData().getIntro());
        tv_homepage_user_moment_number.setText(""+personalInfo.getData().getMomentNum());
        tv_homepage_user_following_number.setText(""+personalInfo.getData().getFollowingNum());
        tv_homepage_user_follower_number.setText(""+personalInfo.getData().getFollowerNum());
        ll_homepage_user_moment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra(UserMomentListActivity.VIEWID,target_userId);
                intent.setAction(getString(R.string.activity_homepage_user_moment_list));
                startActivity(intent);
            }
        });

        ll_homepage_user_following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(getString(R.string.activity_homepage_user_following));
                intent.putExtra(UserFollowingActivity.TARGET_USERID,target_userId);
                startActivity(intent);
            }
        });

        ll_homepage_user_follower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(getString(R.string.activity_homepage_user_follower));
                intent.putExtra(UserFollowingActivity.TARGET_USERID,target_userId);
                startActivity(intent);
            }
        });

        elv_homepage_project.setAdapter(adapter);
        elv_homepage_project.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                ProjectInfo projectInfo=adapter.getChild(groupPosition,childPosition);
                if(projectInfo==null){
                    switch (groupPosition){
                        case UserProjectListAdapter.sponsorPosition:
                            getUserSponsorProject();
                            return true;
                        case UserProjectListAdapter.followPosition:
                            getUserFollowProject();
                            return true;
                        case UserProjectListAdapter.supportPosition:
                            getUserSupportProject();
                            return true;
                    }
                    return false;
                }
                int projectId=projectInfo.getId();
                int categoryId=projectInfo.getCategoryId();
                Intent intent=new Intent();
                intent.putExtra("projectId",projectId);
                intent.putExtra("categoryId",categoryId);
                intent.setAction(getString(R.string.activity_project_detail));
                startActivity(intent);
                return false;
            }
        });

        //默认展开所有项目
        for(int i=0;i<elv_homepage_project.getCount();i++){
            elv_homepage_project.expandGroup(i);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case UserInfoActivity.REQUEST_CODE_START_USER_INFO_ACTIVITY:
                if(resultCode==RESULT_OK){
                    requestUserInfo.requestPersonalInfo(this,handler,httpClient,target_userId);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            //修改账户信息按钮
            case R.id.menu_user_homepage_editor:
                if(userId!=target_userId){
                    break;
                }
                Intent intent=new Intent();
                intent.setAction(getString(R.string.activity_user_info));
                startActivityForResult(intent, UserInfoActivity.REQUEST_CODE_START_USER_INFO_ACTIVITY);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(userId==target_userId){
            getMenuInflater().inflate(R.menu.menu_user_homepage, menu);
        }else {
            return super.onCreateOptionsMenu(menu);
        }

        return true;
    }


    //请求获取个人发起项目
    private void getUserSponsorProject(){
        if(isFinishRequestSponsorProject==false){
            return;
        }
        isFinishRequestSponsorProject=false;
        requestUserSponsorProject.requestUserSponsorProject(this,handler,httpClient,target_userId);


    }

    //请求获取个人关注项目
    private void getUserFollowProject(){
        if(isFinishRequestFollowProject==false){
            return;
        }
        isFinishRequestFollowProject=false;
        requestUserFollowProject.requestUserFollowProject(this,handler,httpClient,target_userId);
    }

    //请求获取个人支持项目
    private void getUserSupportProject(){
        if(isFinishRequestSupportProject==false){
            return;
        }
        isFinishRequestSupportProject=false;
        requestUserSupportProject.requestUserSupportProject(this,handler,httpClient,target_userId);
    }



    //启动个人主页
    public static void startHomepageActivity(Activity activity,int target_userId){
        Intent intent=new Intent();
        intent.setAction(activity.getString(R.string.activity_homepage));
        intent.putExtra(TARGET_USERID,target_userId);
        activity.startActivity(intent);
    }
    //启动个人主页
}
