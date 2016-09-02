package com.fansfunding.fan.request.user;

import android.app.Activity;

import com.fansfunding.fan.R;
import com.fansfunding.fan.utils.ErrorHandler;
import com.fansfunding.fan.utils.FANRequestCode;
import com.fansfunding.internal.PersonalInfo;
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
public class RequestUserInfo {
    private PersonalInfo personalInfo;

    public PersonalInfo getPersonalInfo(){
        return personalInfo;
    }

    public void requestPersonalInfo(Activity activity, final ErrorHandler handler, OkHttpClient httpClient,final int viewId){
        Request request=new Request.Builder()
                .get()
                .url(activity.getString(R.string.url_userbasic)+"info?viewId="+viewId)
                .build();

        Call call= httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(FANRequestCode.GET_PERSONAL_INFO_FAILURE);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response==null||response.isSuccessful()==false){
                    handler.sendEmptyMessage(FANRequestCode.GET_PERSONAL_INFO_FAILURE);
                    return;
                }
                Gson gson=new GsonBuilder().create();
                String str_response=response.body().string();
                personalInfo=new PersonalInfo();
                try {
                    //用Gson进行解析，并判断结果是否为空
                    if((personalInfo = gson.fromJson(str_response, personalInfo.getClass()))==null){
                        handler.sendEmptyMessage(FANRequestCode.GET_PERSONAL_INFO_FAILURE);
                        return;
                    }
                    //获取项目详情失败
                    if(personalInfo.isResult()==false){
                        handler.handlerFanErrorMessage(personalInfo.getErrCode());
                        handler.sendEmptyMessage(FANRequestCode.GET_PERSONAL_INFO_FAILURE);
                        return;
                    }

                    //获取项目信息成功
                    handler.sendEmptyMessage(FANRequestCode.GET_PERSONAL_INFO_SUCCESS);
                }catch (IllegalStateException e){
                    handler.sendEmptyMessage(FANRequestCode.GET_PERSONAL_INFO_FAILURE);
                    e.printStackTrace();
                }catch (JsonSyntaxException e){
                    handler.sendEmptyMessage(FANRequestCode.GET_PERSONAL_INFO_FAILURE);
                    e.printStackTrace();
                }

            }
        });
    }
}
