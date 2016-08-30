package com.fansfunding.fan.request.user;

import android.app.Activity;

import com.fansfunding.fan.R;
import com.fansfunding.fan.utils.ErrorHandler;
import com.fansfunding.fan.utils.FANRequestCode;
import com.fansfunding.internal.UserSupportProject;
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
 * Created by 13616 on 2016/8/30.
 */
public class RequestUserSupportProject {

    private UserSupportProject userSupportProject;

    public UserSupportProject getUserSupportProject(){
        return userSupportProject;
    }

    //请求类型--用户支持的项目
    public final static String PROJECT_TYPE_SUPPORT="support";


    //一次获取的数量
    private int rows=10;
    //获取的页数
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

    public void requestUserSupportProject(Activity activity, final ErrorHandler handler, OkHttpClient httpClient, final int userId, final int viewId){
        Request request=new Request.Builder()
                .get()
                .url(activity.getString(R.string.url_userbasic)+userId+"/projects?viewId="+viewId+"&type="+PROJECT_TYPE_SUPPORT+"&rows="+rows+"&page="+page)
                .build();

        Call call=httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(FANRequestCode.GET_USER_SUPPORT_PROJECT_FAILURE);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response==null||response.isSuccessful()==false){
                    handler.sendEmptyMessage(FANRequestCode.GET_USER_SUPPORT_PROJECT_FAILURE);
                    return;
                }
                Gson gson=new GsonBuilder().create();
                String str_response=response.body().string();
                userSupportProject=new UserSupportProject();
                try {

                    //用Gson进行解析，并判断结果是否为空
                    if((userSupportProject = gson.fromJson(str_response, userSupportProject.getClass()))==null){
                        handler.sendEmptyMessage(FANRequestCode.GET_USER_SUPPORT_PROJECT_FAILURE);
                        return;
                    }
                    //获取失败
                    if(userSupportProject.isResult()==false){
                        handler.handlerFanErrorMessage(userSupportProject.getErrCode());
                        handler.sendEmptyMessage(FANRequestCode.GET_USER_SUPPORT_PROJECT_FAILURE);
                        return;
                    }
                    //获取成功
                    handler.sendEmptyMessage(FANRequestCode.GET_USER_SUPPORT_PROJECT_SUCCESS);
                }catch (IllegalStateException e){
                    handler.sendEmptyMessage(FANRequestCode.GET_USER_SUPPORT_PROJECT_FAILURE);
                    e.printStackTrace();
                }catch (JsonSyntaxException e){
                    handler.sendEmptyMessage(FANRequestCode.GET_USER_SUPPORT_PROJECT_FAILURE);
                    e.printStackTrace();
                }
            }
        });
    }
}
