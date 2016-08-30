package com.fansfunding.fan.request;

import android.app.Activity;
import android.util.Log;

import com.fansfunding.fan.R;
import com.fansfunding.fan.utils.ErrorHandler;
import com.fansfunding.fan.utils.FANRequestCode;
import com.fansfunding.internal.social.UserMoment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 13616 on 2016/8/24.
 */
public class RequestUserFollowUserMoment {


    private UserMoment userMoment;

    //一次获取动态的数量
    private int rows=10;
    //获取动态的页数
    private int page=1;

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if (rows>=0)
            this.rows = rows;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        if(page>0)
            this.page = page;
    }

    public UserMoment getUserMoment(){
        return userMoment;
    }

    public void requestUserMoment(Activity activity, final ErrorHandler handler, OkHttpClient httpClient, final int userId, final String token,final int viewId){
        Request request=new Request.Builder()
                .get()
                .url(activity.getString(R.string.url_user)+userId+"/moment/follow?token="+token+"&rows="+rows+"&page="+page+"&viewId="+viewId)
                .build();
        Call call=httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(FANRequestCode.GET_USER_FOLLOW_USER_MOMENT_FAILURE);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response==null||response.isSuccessful()==false){
                    handler.sendEmptyMessage(FANRequestCode.GET_USER_FOLLOW_USER_MOMENT_FAILURE);
                    return;
                }
                Gson gson=new GsonBuilder().create();
                String str_response=response.body().string();
                Log.i("TAG","动态："+str_response);
                userMoment=new UserMoment();
                try {

                    //用Gson进行解析，并判断结果是否为空
                    if((userMoment = gson.fromJson(str_response, userMoment.getClass()))==null){
                        handler.sendEmptyMessage(FANRequestCode.GET_USER_FOLLOW_USER_MOMENT_FAILURE);
                        return;
                    }
                    //获取用户动态失败
                    if(userMoment.isResult()==false){
                        handler.handlerFanErrorMessage(userMoment.getErrCode());
                        handler.sendEmptyMessage(FANRequestCode.GET_USER_FOLLOW_USER_MOMENT_FAILURE);
                        return;
                    }
                    //获取用户动态成功
                    handler.sendEmptyMessage(FANRequestCode.GET_USER_FOLLOW_USER_MOMENT_SUCCESS);
                }catch (IllegalStateException e){
                    handler.sendEmptyMessage(FANRequestCode.GET_USER_FOLLOW_USER_MOMENT_FAILURE);
                    e.printStackTrace();
                }catch (JsonSyntaxException e){
                    handler.sendEmptyMessage(FANRequestCode.GET_USER_FOLLOW_USER_MOMENT_FAILURE);
                    e.printStackTrace();
                }
            }
        });
    }
}
