package com.fansfunding.fan.request;

import android.app.Activity;
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
 * Created by 13616 on 2016/8/10.
 */
public class RequestCancelFollowUser {
    private FeedbackCode feedback;

    public FeedbackCode getFeedback(){
        return feedback;
    }

    public void cancelFollowUser(Activity activity, final ErrorHandler handler, OkHttpClient httpClient, final int userId, final int followId,final String token){
        FormBody formBody=new FormBody.Builder()
                .add("token",token)
                .build();
        Request request=new Request.Builder()
                .post(formBody)
                .url(activity.getString(R.string.url_user)+userId+"/unfollow/"+followId)
                .build();
        Call call=httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(FANRequestCode.CANCEL_FOLLOW_USER_FAILURE);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //服务器响应失败
                if(response==null||response.isSuccessful()==false){
                    handler.sendEmptyMessage(FANRequestCode.CANCEL_FOLLOW_USER_FAILURE);
                    return;
                }
                Gson gson=new GsonBuilder().create();
                String str_response=response.body().string();
                Log.i("TAG","ISCancelfOLLOW:"+str_response);
                feedback=new FeedbackCode();
                try {

                    //用Gson进行解析，并判断结果是否为空
                    if((feedback = gson.fromJson(str_response, feedback.getClass()))==null){
                        handler.sendEmptyMessage(FANRequestCode.CANCEL_FOLLOW_USER_FAILURE);
                        return;
                    }
                    //获取项目详情失败
                    if(feedback.isResult()==false){
                        switch (feedback.getErrCode()){
                            case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                                handler.sendEmptyMessage(ErrorCode.REQUEST_TOO_FRENQUENTLY);
                                break;
                            case ErrorCode.PARAMETER_ERROR:
                                handler.sendEmptyMessage(ErrorCode.PARAMETER_ERROR);
                                break;
                            default:
                                handler.sendEmptyMessage(FANRequestCode.CANCEL_FOLLOW_USER_FAILURE);
                                break;
                        }
                        return;
                    }


                    //获取项目信息成功
                    handler.sendEmptyMessage(FANRequestCode.CANCEL_FOLLOW_USER_SUCCESS);
                }catch (IllegalStateException e){
                    handler.sendEmptyMessage(FANRequestCode.CANCEL_FOLLOW_USER_FAILURE);
                    e.printStackTrace();
                }catch (JsonSyntaxException e){
                    handler.sendEmptyMessage(FANRequestCode.CANCEL_FOLLOW_USER_FAILURE);
                    e.printStackTrace();
                }
            }
        });
    }
}
