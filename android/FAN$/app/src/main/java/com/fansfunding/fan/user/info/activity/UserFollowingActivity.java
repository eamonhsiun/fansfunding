package com.fansfunding.fan.user.info.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fansfunding.fan.R;

public class UserFollowingActivity extends AppCompatActivity {

    public static final String TARGET_USERID="TARGET_USERID";

    //目标id
    private int target_userId;

    //用户id
    private int userId;

    //用户token
    private String token;



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

        SharedPreferences share=getSharedPreferences(getString(R.string.sharepreference_login_by_phone),MODE_PRIVATE);
        userId=share.getInt("id",userId);
        token=share.getString("token"," ");
    }

    private void initViews(){

    }

    private void loadData(){

    }

}
