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
 * Created by 13616 on 2016/8/26.
 */
public class RequestSendMomentComment {

    public static void requestSendMomentComment(Activity context, final ErrorHandler handler, OkHttpClient httpClient, final int userId, final String token, final String content, final int momentId,final int replyTo){
        FormBody formBody=new FormBody.Builder()
                .add("token",token)
                .add("content",content)
                .add("replyTo",String.valueOf(replyTo))
                .build();

        Request request=new Request.Builder()
                .post(formBody)
                .url(context.getString(R.string.url_user)+userId+"/moment/"+momentId+"/comment")
                .build();

        Call call=httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg=new Message();
                msg.what=FANRequestCode.SEND_MOMENT_COMMENT_FAILURE;
                msg.arg1=momentId;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //服务器响应失败
                if(response==null||response.isSuccessful()==false){
                    Message msg=new Message();
                    msg.what=FANRequestCode.SEND_MOMENT_COMMENT_FAILURE;
                    msg.arg1=momentId;
                    handler.sendMessage(msg);
                    return;
                }
                Gson gson=new GsonBuilder().create();
                String str_response=response.body().string();
                Log.i("TAG","ERRPR"+str_response);
                FeedbackCode sendComment=new FeedbackCode();
                try {

                    //用Gson进行解析，并判断结果是否为空
                    if((sendComment = gson.fromJson(str_response, sendComment.getClass()))==null){
                        Message msg=new Message();
                        msg.what=FANRequestCode.SEND_MOMENT_COMMENT_FAILURE;
                        msg.arg1=momentId;
                        handler.sendMessage(msg);
                        return;
                    }

                    //发送评论失败
                    if(sendComment.isResult()==false){
                        if(handler.handlerFanErrorMessage(sendComment.getErrCode())==false){
                            Message msg=new Message();
                            msg.what=FANRequestCode.SEND_MOMENT_COMMENT_FAILURE;
                            msg.arg1=momentId;
                            handler.sendMessage(msg);
                        }
                        return;
                    }
                    //发送评论成功
                    Message msg=new Message();
                    msg.what=FANRequestCode.SEND_MOMENT_COMMENT_SUCCESS;
                    msg.arg1=momentId;
                    handler.sendMessage(msg);
                }catch (IllegalStateException e){
                    Message msg=new Message();
                    msg.what=FANRequestCode.SEND_MOMENT_COMMENT_FAILURE;
                    msg.arg1=momentId;
                    handler.sendMessage(msg);
                }catch (JsonSyntaxException e){
                    Message msg=new Message();
                    msg.what=FANRequestCode.SEND_MOMENT_COMMENT_FAILURE;
                    msg.arg1=momentId;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            }
        });

    }
}
