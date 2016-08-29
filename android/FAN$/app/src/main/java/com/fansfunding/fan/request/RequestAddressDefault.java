package com.fansfunding.fan.request;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.fansfunding.fan.R;
import com.fansfunding.fan.utils.ErrorHandler;
import com.fansfunding.fan.utils.FANRequestCode;
import com.fansfunding.internal.ErrorCode;
import com.fansfunding.internal.user.AddressDefault;
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
 * Created by 13616 on 2016/7/24.
 */
public class RequestAddressDefault {

    private AddressDefault address=null;

    public AddressDefault getAddress(){
        return address;
    }

    public void getDefaultAddress(Activity activity, final ErrorHandler handler, OkHttpClient httpClient){
        SharedPreferences share=activity.getSharedPreferences(activity.getString(R.string.sharepreference_login_by_phone), Context.MODE_PRIVATE);
        int userId=share.getInt("id",0);
        String token=share.getString("token"," ");

        Request request=new Request.Builder()
                .get()
                .url(activity.getString(R.string.url_user)+userId+"/shopping_address/default?token="+token)
                .build();
        Call call=httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(FANRequestCode.GET_USER_DEFAULT_ADDRESS_FAILURE);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //服务器响应失败
                if(response==null||response.isSuccessful()==false){
                    handler.sendEmptyMessage(FANRequestCode.GET_USER_DEFAULT_ADDRESS_FAILURE);
                    return;
                }
                Gson gson=new GsonBuilder().create();
                String str_response=response.body().string();
                Log.i("TAG","DEFAULTAddress:"+str_response);
                address=new AddressDefault();
                try {
                    //用Gson进行解析，并判断结果是否为空
                    if((address = gson.fromJson(str_response, address.getClass()))==null){
                        handler.sendEmptyMessage(FANRequestCode.GET_USER_DEFAULT_ADDRESS_FAILURE);
                        return;
                    }
                    //获取项目详情失败
                    if(address.isResult()==false){
                        handler.handlerFanErrorMessage(address.getErrCode());
                        handler.sendEmptyMessage(FANRequestCode.GET_USER_DEFAULT_ADDRESS_FAILURE);
                        return;
                    }

                    //获取项目信息成功
                    handler.sendEmptyMessage(FANRequestCode.GET_USER_DEFAULT_ADDRESS_SUCCESS);
                }catch (IllegalStateException e){
                    handler.sendEmptyMessage(FANRequestCode.GET_USER_DEFAULT_ADDRESS_FAILURE);
                    e.printStackTrace();
                }catch (JsonSyntaxException e){
                    handler.sendEmptyMessage(FANRequestCode.GET_USER_DEFAULT_ADDRESS_FAILURE);
                    e.printStackTrace();
                }
            }
        });


    }
}
