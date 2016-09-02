package com.fansfunding.fan.user.info.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.fansfunding.PullListView.LoadListView;
import com.fansfunding.fan.R;
import com.fansfunding.fan.request.user.RequestUserFollowing;
import com.fansfunding.fan.user.info.adapter.UserFollowingAdapter;
import com.fansfunding.fan.utils.ErrorHandler;
import com.fansfunding.fan.utils.FANRequestCode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class UserFollowingActivity extends AppCompatActivity {

    public static final String TARGET_USERID="TARGET_USERID";

    //目标id
    private int target_userId;

    private boolean isFinishRequest=true;

    private RequestUserFollowing requestUserFollowing;

    private UserFollowingAdapter adapter;

  /*  //用户id
    private int userId;

    //用户token
    private String token;*/

    //httpclient
    private OkHttpClient httpClient;

    private LoadListView lv_homepage_user_following;

    private ErrorHandler handler=new ErrorHandler(this){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case FANRequestCode.GET_USER_FOLLOWING_SUCCESS:
                    endRefresh();
                    if(requestUserFollowing.getUserList()==null){
                        break;
                    }
                    if(requestUserFollowing.getUserList().getData().getList().size()<requestUserFollowing.getRows()){
                        requestUserFollowing.setPage(1);
                        lv_homepage_user_following.setPullLoadEnable(false);
                        lv_homepage_user_following.setAutoLoadEnable(false);
                    }else {
                        requestUserFollowing.setPage(requestUserFollowing.getPage()+1);
                        lv_homepage_user_following.setPullLoadEnable(true);
                        lv_homepage_user_following.setAutoLoadEnable(true);
                    }
                    for(int i = 0; i<requestUserFollowing.getUserList().getData().getList().size(); i++){
                        adapter.addItem(requestUserFollowing.getUserList().getData().getList().get(i));
                    }
                    adapter.notifyDataSetChanged();

                    break;
                case FANRequestCode.GET_USER_FOLLOWING_FAILURE:
                    endRefresh();
                    if(UserFollowingActivity.this.isFinishing()==false){
                        Toast.makeText(UserFollowingActivity.this,"请求关注人失败",Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_user_following);

        initVariables();
        initViews();
        loadData();

    }
    private void initVariables(){
        Intent intent=getIntent();
        target_userId=intent.getIntExtra(TARGET_USERID,-1);

        requestUserFollowing=new RequestUserFollowing();
        httpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
        adapter=new UserFollowingAdapter(this);
        /*SharedPreferences share=getSharedPreferences(getString(R.string.sharepreference_login_by_phone),MODE_PRIVATE);
        userId=share.getInt("id",userId);
        token=share.getString("token"," ");*/
    }

    private void initViews(){

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_homepage_user_following);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        //设置返回键
        ActionBar actionBar=this.getSupportActionBar();
        actionBar.setTitle("关注");
        actionBar.setDisplayHomeAsUpEnabled(true);

        lv_homepage_user_following=(LoadListView)findViewById(R.id.lv_homepage_user_following);
        lv_homepage_user_following.setAutoLoadEnable(false);
        lv_homepage_user_following.setPullLoadEnable(false);
        lv_homepage_user_following.setPullRefreshEnable(false);
        lv_homepage_user_following.setRefreshTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        lv_homepage_user_following.setXListViewListener(new LoadListView.IXListViewListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                if(isFinishRequest==false){
                    return;
                }
                isFinishRequest=false;
                requestUserFollowing.requestUserFollowing(UserFollowingActivity.this,handler,httpClient,target_userId);

            }
        });

        lv_homepage_user_following.setAdapter(adapter);
    }

    private void loadData(){
        isFinishRequest=false;
        requestUserFollowing.requestUserFollowing(UserFollowingActivity.this,handler,httpClient,target_userId);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;

    }

    private void endRefresh(){
        isFinishRequest=true;
        lv_homepage_user_following.stopRefresh();
        lv_homepage_user_following.stopLoadMore();
        lv_homepage_user_following.setRefreshTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
    }

}
