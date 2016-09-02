package com.fansfunding.fan.request;

import android.app.Activity;
import android.os.Message;
import android.util.Log;

import com.fansfunding.fan.R;
import com.fansfunding.fan.utils.ErrorHandler;
import com.fansfunding.fan.utils.FANRequestCode;
import com.fansfunding.internal.ErrorCode;
import com.fansfunding.internal.FeedbackCode;
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
 * Created by 13616 on 2016/8/28.
 */
public class RequestPraiseMoment {

    public static void requestPraiseMoment(Activity activity, final ErrorHandler handler, OkHttpClient httpClient, final int momentId, final int userId, final String token){
        FormBody formBody=new FormBody.Builder()
                .add("token",token)
                .add("userId",String.valueOf(userId))
                .add("momentId",String.valueOf(momentId))
                .build();

        Request request=new Request.Builder()
                .post(formBody)
                .url(activity.getString(R.string.url_user)+userId+"/moment/"+momentId+"/like")
                .build();

        Call call=httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg=new Message();
                msg.what=FANRequestCode.PRAISE_MOMENT_FAILURE;
                msg.arg1=momentId;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //服务器响应失败
                if(response==null||response.isSuccessful()==false){
                    Message msg=new Message();
                    msg.what=FANRequestCode.PRAISE_MOMENT_FAILURE;
                    msg.arg1=momentId;
                    handler.sendMessage(msg);
                    return;
                }
                Gson gson=new GsonBuilder().create();
                String str_response=response.body().string();
                Log.i("TAG","点赞："+str_response);
                FeedbackCode feedbackCode=new FeedbackCode();
                try {
                    //用Gson进行解析，并判断结果是否为空
                    if((feedbackCode = gson.fromJson(str_response, feedbackCode.getClass()))==null){
                        Message msg=new Message();
                        msg.what=FANRequestCode.PRAISE_MOMENT_FAILURE;
                        msg.arg1=momentId;
                        handler.sendMessage(msg);
                        return;
                    }
                    //获取项目详情失败
                    if(feedbackCode.isResult()==false){
                        handler.handlerFanErrorMessage(feedbackCode.getErrCode());
                        Message msg=new Message();
                        msg.what=FANRequestCode.PRAISE_MOMENT_FAILURE;
                        msg.arg1=momentId;
                        handler.sendMessage(msg);
                        return;
                    }
                    //获取项目信息成功
                    Message msg=new Message();
                    msg.what=FANRequestCode.PRAISE_MOMENT_SUCCESS;
                    msg.arg1=momentId;
                    handler.sendMessage(msg);
                }catch (IllegalStateException e){
                    Message msg=new Message();
                    msg.what=FANRequestCode.PRAISE_MOMENT_FAILURE;
                    msg.arg1=momentId;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }catch (JsonSyntaxException e){
                    Message msg=new Message();
                    msg.what=FANRequestCode.PRAISE_MOMENT_FAILURE;
                    msg.arg1=momentId;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            }
        });
    }

    public static void requestCancelPraiseMoment(Activity activity, final ErrorHandler handler, OkHttpClient httpClient, final int momentId, final int userId, final String token){
        FormBody formBody=new FormBody.Builder()
                .add("token",token)
                .add("userId",String.valueOf(userId))
                .add("momentId",String.valueOf(momentId))
                .build();

        Request request=new Request.Builder()
                .post(formBody)
                .url(activity.getString(R.string.url_user)+userId+"/moment/"+momentId+"/unlike")
                .build();

        Call call=httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg=new Message();
                msg.what=FANRequestCode.CANCEL_PRAISE_MOMENT_FAILURE;
                msg.arg1=momentId;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //服务器响应失败
                if(response==null||response.isSuccessful()==false){
                    Message msg=new Message();
                    msg.what=FANRequestCode.CANCEL_PRAISE_MOMENT_FAILURE;
                    msg.arg1=momentId;
                    handler.sendMessage(msg);
                    return;
                }
                Gson gson=new GsonBuilder().create();
                String str_response=response.body().string();
                FeedbackCode feedbackCode=new FeedbackCode();
                try {
                    //用Gson进行解析，并判断结果是否为空
                    if((feedbackCode = gson.fromJson(str_response, feedbackCode.getClass()))==null){
                        Message msg=new Message();
                        msg.what=FANRequestCode.CANCEL_PRAISE_MOMENT_FAILURE;
                        msg.arg1=momentId;
                        handler.sendMessage(msg);
                        return;
                    }
                    //获取项目详情失败
                    if(feedbackCode.isResult()==false){
                        handler.handlerFanErrorMessage(feedbackCode.getErrCode());

                        Message msg=new Message();
                        msg.what=FANRequestCode.CANCEL_PRAISE_MOMENT_FAILURE;
                        msg.arg1=momentId;
                        handler.sendMessage(msg);
                        return;
                    }
                    //获取项目信息成功
                    Message msg=new Message();
                    msg.what=FANRequestCode.CANCEL_PRAISE_MOMENT_SUCCESS;
                    msg.arg1=momentId;
                    handler.sendMessage(msg);
                }catch (IllegalStateException e){
                    Message msg=new Message();
                    msg.what=FANRequestCode.CANCEL_PRAISE_MOMENT_FAILURE;
                    msg.arg1=momentId;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }catch (JsonSyntaxException e){
                    Message msg=new Message();
                    msg.what=FANRequestCode.CANCEL_PRAISE_MOMENT_FAILURE;
                    msg.arg1=momentId;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            }
        });

    }
}
