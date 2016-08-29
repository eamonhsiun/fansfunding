package com.fansfunding.fan.user.info.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.fansfunding.fan.R;

public class HomepageActivity extends AppCompatActivity {

    //请求该activity所需要的参数

    public final static String TARGET_USERID="TARGET_USERID";

    //所要展示的用户id
    private int target_userId;

    //app用户的id
    private int userId;

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
    }

    private void initViews(){
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_homepage);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        //设置返回键
        ActionBar actionBar=this.getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);
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
            //修改账户信息按钮
            case R.id.menu_user_homepage_editor:
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


    //启动个人主页
    public static void startHomepageActivity(Activity activity,int target_userId){
        Intent intent=new Intent();
        intent.setAction(activity.getString(R.string.activity_homepage));
        intent.putExtra(TARGET_USERID,target_userId);
        activity.startActivity(intent);

    }
}
