package com.fansfunding.fan.request;

import android.app.Activity;

import com.fansfunding.fan.utils.ErrorHandler;

import okhttp3.OkHttpClient;

/**
 * Created by 13616 on 2016/8/27.
 */
public class RequestUserProject {

    //请求类型--用户发起的项目
    public final static String PROJECT_TYPE_SPONSOR="sponsor";

    //请求类型--用户关注的项目
    public final static String PROJECT_TYPE_FOLLOW="follow";

    //请求类型--用户支持的项目
    public final static String PROJECT_TYPE_SUPPORT="support";

    private void requestUserProject(Activity activity, final ErrorHandler handler, OkHttpClient httpClient, final int userId, final int viewId){

    }
}
