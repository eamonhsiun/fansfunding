package com.fansfunding.fan.request;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

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
 * Created by 13616 on 2016/8/22.
 */
public class RequestSendComment {


    public static void sendProjectComment(Activity context, final ErrorHandler handler, OkHttpClient httpClient,final int userId,final String token,final int categoryId,final int projectId,final String comment,final int pointTo){

        FormBody formBody=new FormBody.Builder()
                .add("userId",String.valueOf(userId))
                .add("pointTo",String.valueOf(pointTo))
                .add("content",comment)
                .add("token",token)
                .build();
        Request request=new Request.Builder()
                .post(formBody)
                .url(context.getString(R.string.url_project)+categoryId+"/"+projectId+"/comments")
                .build();

        Call call=httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(FANRequestCode.SEND_PROJECT_COMMENT_FAILURE);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //服务器响应失败
                if(response==null||response.isSuccessful()==false){
                    handler.sendEmptyMessage(FANRequestCode.SEND_PROJECT_COMMENT_FAILURE);
                    return;
                }
                Gson gson=new GsonBuilder().create();
                String str_response=response.body().string();
                FeedbackCode sendComment=new FeedbackCode();
                try {

                    //用Gson进行解析，并判断结果是否为空
                    if((sendComment = gson.fromJson(str_response, sendComment.getClass()))==null){
                        handler.sendEmptyMessage(FANRequestCode.SEND_PROJECT_COMMENT_FAILURE);
                        return;
                    }

                    //发送评论失败
                    if(sendComment.isResult()==false){
                        switch (sendComment.getErrCode()){
                            case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                                handler.sendEmptyMessage(ErrorCode.REQUEST_TOO_FRENQUENTLY);
                                break;
                            case ErrorCode.PARAMETER_ERROR:
                                handler.sendEmptyMessage(ErrorCode.PARAMETER_ERROR);
                                break;
                            default:
                                handler.sendEmptyMessage(FANRequestCode.SEND_PROJECT_COMMENT_FAILURE);
                                break;
                        }
                        return;
                    }

                    //发送评论成功
                    handler.sendEmptyMessage(FANRequestCode.SEND_PROJECT_COMMENT_SUCCESS);
                }catch (IllegalStateException e){
                    handler.sendEmptyMessage(FANRequestCode.SEND_PROJECT_COMMENT_FAILURE);
                    e.printStackTrace();
                }catch (JsonSyntaxException e){
                    handler.sendEmptyMessage(FANRequestCode.SEND_PROJECT_COMMENT_FAILURE);
                    e.printStackTrace();
                }

            }
        });
    }
}
