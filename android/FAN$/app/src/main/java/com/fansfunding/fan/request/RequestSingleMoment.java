package com.fansfunding.fan.request;

import android.app.Activity;
import android.util.Log;

import com.fansfunding.fan.R;
import com.fansfunding.fan.utils.ErrorHandler;
import com.fansfunding.fan.utils.FANRequestCode;
import com.fansfunding.internal.social.SingleUserMoment;
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
 * Created by 13616 on 2016/8/27.
 */
public class RequestSingleMoment {

    private SingleUserMoment singleUserMoment;

    public SingleUserMoment getSingleUserMoment(){
        return singleUserMoment;
    }

    public void requestSingleMoment(Activity activity, final ErrorHandler handler, OkHttpClient httpClient, final int momentId, final int userId,final String token){
        Request request=new Request.Builder()
                .get()
                .url(activity.getString(R.string.url_user)+userId+"/moment/"+momentId+"?token="+token)
                .build();
        Call call=httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(FANRequestCode.GET_SINGLE_USER_MOMENT_FAILURE);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response==null||response.isSuccessful()==false){
                    handler.sendEmptyMessage(FANRequestCode.GET_SINGLE_USER_MOMENT_FAILURE);
                    return;
                }
                Gson gson=new GsonBuilder().create();
                String str_response=response.body().string();
                Log.i("TAG","SINGLE"+str_response);
                singleUserMoment=new SingleUserMoment();
                try {

                    //用Gson进行解析，并判断结果是否为空
                    if((singleUserMoment = gson.fromJson(str_response, singleUserMoment.getClass()))==null){
                        handler.sendEmptyMessage(FANRequestCode.GET_SINGLE_USER_MOMENT_FAILURE);

                        return;
                    }
                    //获取用户动态失败
                    if(singleUserMoment.isResult()==false){
                        if(handler.handlerFanErrorMessage(singleUserMoment.getErrCode())==false){
                            handler.sendEmptyMessage(FANRequestCode.GET_SINGLE_USER_MOMENT_FAILURE);
                        }

                        return;
                    }
                    //获取用户动态成功
                    handler.sendEmptyMessage(FANRequestCode.GET_SINGLE_USER_MOMENT_SUCCESS);
                }catch (IllegalStateException e){
                    handler.sendEmptyMessage(FANRequestCode.GET_SINGLE_USER_MOMENT_FAILURE);
                    e.printStackTrace();
                }catch (JsonSyntaxException e){
                    handler.sendEmptyMessage(FANRequestCode.GET_SINGLE_USER_MOMENT_FAILURE);
                    e.printStackTrace();
                }
            }
        });
    }
}
