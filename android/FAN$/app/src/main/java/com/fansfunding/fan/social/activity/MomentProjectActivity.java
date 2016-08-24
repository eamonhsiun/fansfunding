package com.fansfunding.fan.social.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fansfunding.fan.R;

public class MomentProjectActivity extends MomentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moment_project);

        initVariables();
        initViews();
        loadData();
    }


    @Override
    protected void initVariables(){
        super.initVariables();
    }

    @Override
    protected void initViews(){}

    @Override
    protected void loadData(){}
}
