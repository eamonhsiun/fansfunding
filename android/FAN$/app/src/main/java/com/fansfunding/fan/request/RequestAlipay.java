package com.fansfunding.fan.request;

import android.app.Activity;
import android.util.Log;

import com.fansfunding.fan.R;
import com.fansfunding.fan.utils.ErrorHandler;
import com.fansfunding.fan.utils.FANRequestCode;
import com.fansfunding.internal.ErrorCode;
import com.fansfunding.internal.project.FANAlipay;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 13616 on 2016/7/25.
 */
public class RequestAlipay {

    private FANAlipay alipay;

    public FANAlipay getAlipay(){
        return alipay;
    }

    public void getAlipayOrder(Activity activity, final ErrorHandler handler, OkHttpClient httpClient,final int feedbackId, final int userId,final int addressId){

        FormBody formBody=new FormBody.Builder()
                .add("feedbackId",String.valueOf(feedbackId))
                .add("userId",String.valueOf(userId))
                .add("addressId",String.valueOf(addressId))
                .build();

        Request request=new Request.Builder()
                .url(activity.getString(R.string.url_alipay_mobile))
                .post(formBody)
                .build();

        Call call=httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(FANRequestCode.GET_MOBILE_ALIPAY_FAILURE);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //服务器响应失败
                if(response==null||response.isSuccessful()==false){
                    handler.sendEmptyMessage(FANRequestCode.GET_MOBILE_ALIPAY_FAILURE);
                    return;
                }
                Gson gson=new GsonBuilder().create();
                String str_response=response.body().string();

                alipay=new FANAlipay();
                try {
                    //用Gson进行解析，并判断结果是否为空
                    if((alipay = gson.fromJson(str_response, alipay.getClass()))==null){
                        handler.sendEmptyMessage(FANRequestCode.GET_MOBILE_ALIPAY_FAILURE);
                        return;
                    }
                    //获取项目详情失败
                    if(alipay.isResult()==false){
                        switch (alipay.getErrCode()){
                            case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                                handler.sendEmptyMessage(ErrorCode.REQUEST_TOO_FRENQUENTLY);
                                break;
                            case ErrorCode.PARAMETER_ERROR:
                                handler.sendEmptyMessage(ErrorCode.PARAMETER_ERROR);
                                break;
                            default:
                                handler.sendEmptyMessage(FANRequestCode.GET_MOBILE_ALIPAY_FAILURE);
                        }
                        return;
                    }
                    //获取项目信息成功
                    handler.sendEmptyMessage(FANRequestCode.GET_MOBILE_ALIPAY_SUCCESS);
                }catch (IllegalStateException e){
                    handler.sendEmptyMessage(FANRequestCode.GET_MOBILE_ALIPAY_FAILURE);
                    e.printStackTrace();
                }catch (JsonSyntaxException e){
                    handler.sendEmptyMessage(FANRequestCode.GET_MOBILE_ALIPAY_FAILURE);
                    e.printStackTrace();
                }
            }
        });
    }


}
