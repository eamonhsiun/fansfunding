package com.fansfunding.fan.request;

import android.app.Activity;
import android.util.Log;

import com.fansfunding.fan.R;
import com.fansfunding.fan.utils.ErrorHandler;
import com.fansfunding.fan.utils.FANRequestCode;
import com.fansfunding.internal.ErrorCode;
import com.fansfunding.internal.SearchProject;
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
 * Created by 13616 on 2016/8/26.
 */
public class RequestSearchProject {

    //获取到的项目信息
    private SearchProject searchProject;

    //一次获取项目的数量
    private int rows=10;
    //获取评论的页数
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
    public SearchProject getSearchProject(){
        return searchProject;
    }

    public void requestSearchProject(Activity activity, final ErrorHandler handler, OkHttpClient httpClient,final String keyword){

        Request request=new Request.Builder()
                .get()
                .url(activity.getString(R.string.url_search_project)+"?keyword="+keyword+"&page="+page+"&rows="+rows)
                .build();
        Call call=httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(FANRequestCode.SEARCH_PROJECT_FAILURE);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //服务器响应失败
                if(response==null||response.isSuccessful()==false){
                    handler.sendEmptyMessage(FANRequestCode.SEARCH_PROJECT_FAILURE);
                    return;
                }
                Gson gson=new GsonBuilder().create();
                String str_response=response.body().string();
                searchProject=new SearchProject();
                try {
                    //用Gson进行解析，并判断结果是否为空
                    if((searchProject = gson.fromJson(str_response, searchProject.getClass()))==null){
                        handler.sendEmptyMessage(FANRequestCode.SEARCH_PROJECT_FAILURE);
                        return;
                    }
                    //搜索项目失败
                    if(searchProject.isResult()==false){
                        if(handler.handlerFanErrorMessage(searchProject.getErrCode())==false){
                            handler.sendEmptyMessage(FANRequestCode.SEARCH_PROJECT_FAILURE);
                        }
                        return;
                    }

                    //搜索项目信息成功
                    handler.sendEmptyMessage(FANRequestCode.SEARCH_PROJECT_SUCCESS);
                }catch (IllegalStateException e){
                    handler.sendEmptyMessage(FANRequestCode.SEARCH_PROJECT_FAILURE);
                    e.printStackTrace();
                }catch (JsonSyntaxException e){
                    handler.sendEmptyMessage(FANRequestCode.SEARCH_PROJECT_FAILURE);
                    e.printStackTrace();
                }

            }
        });

    }
}
