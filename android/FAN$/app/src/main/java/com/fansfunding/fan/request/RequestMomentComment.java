package com.fansfunding.fan.request;

import android.app.Activity;
import android.util.Log;

import com.fansfunding.fan.R;
import com.fansfunding.fan.utils.ErrorHandler;
import com.fansfunding.fan.utils.FANRequestCode;
import com.fansfunding.internal.ErrorCode;
import com.fansfunding.internal.social.MomentComment;
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
 * Created by 13616 on 2016/8/29.
 */
public class RequestMomentComment {

    private MomentComment momentComment;

    public MomentComment getMomentComment(){
        return momentComment;
    }

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

    public void requestMomentComment(Activity activity, final ErrorHandler handler, OkHttpClient httpClient,final int momentId){
        Request request=new Request.Builder()
                .url(activity.getString(R.string.url_userbasic)+"moment/"+momentId+"/comment?"+"rows="+rows+"&page="+page)
                .get()
                .build();

        Call call=httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(FANRequestCode.GET_MOMENT_COMMENT_DETAIL_FAILURE);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //服务器响应失败
                if(response==null||response.isSuccessful()==false){
                    handler.sendEmptyMessage(FANRequestCode.GET_MOMENT_COMMENT_DETAIL_FAILURE);
                    return;
                }
                Gson gson=new GsonBuilder().create();
                String str_response=response.body().string();
                momentComment=new MomentComment();
                try {

                    //用Gson进行解析，并判断结果是否为空
                    if((momentComment = gson.fromJson(str_response, momentComment.getClass()))==null){
                        handler.sendEmptyMessage(FANRequestCode.GET_MOMENT_COMMENT_DETAIL_FAILURE);
                        return;
                    }

                    //获取动态评论失败
                    if(momentComment.isResult()==false){
                        handler.handlerFanErrorMessage(momentComment.getErrCode());
                        handler.sendEmptyMessage(FANRequestCode.GET_MOMENT_COMMENT_DETAIL_FAILURE);
                        return;
                    }

                    //获取动态评论成功
                    handler.sendEmptyMessage(FANRequestCode.GET_MOMENT_COMMENT_DETAIL_SUCCESS);
                }catch (IllegalStateException e){
                    handler.sendEmptyMessage(FANRequestCode.GET_MOMENT_COMMENT_DETAIL_FAILURE);
                    e.printStackTrace();
                }catch (JsonSyntaxException e){
                    handler.sendEmptyMessage(FANRequestCode.GET_MOMENT_COMMENT_DETAIL_FAILURE);
                    e.printStackTrace();
                }
            }
        });
    }
}
