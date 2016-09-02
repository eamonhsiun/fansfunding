package com.fansfunding.fan.utils;

import android.app.Activity;
import android.view.View;

import com.fansfunding.fan.user.info.activity.HomepageActivity;

/**
 * Created by 13616 on 2016/8/30.
 */
public class StartHomepage implements View.OnClickListener {

    private Activity activity;

    private int target_userId;

    public StartHomepage(Activity activity,int target_userId){
        this.activity=activity;
        this.target_userId=target_userId;
    }

    public static void startHomePage(Activity activity,int target_userId){
        HomepageActivity.startHomepageActivity(activity,target_userId);
    }
    @Override
    public void onClick(View v) {
        startHomePage(activity,target_userId);
    }


}
