package com.fansfunding.fan.request;

import android.app.Activity;
import android.util.Log;

import com.fansfunding.fan.R;
import com.fansfunding.fan.utils.ErrorHandler;
import com.fansfunding.fan.utils.FANRequestCode;
import com.fansfunding.internal.AllProjectInCategory;
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
 * Created by 13616 on 2016/9/7.
 */
public class RequestSingleCategoryProject {

    private AllProjectInCategory allProjectInCategory;

    public AllProjectInCategory getAllProjectInCategory(){
        return allProjectInCategory;
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
    public void requestSingleCategoryProject(Activity activity, final ErrorHandler handler, OkHttpClient httpClient, final int categoryId){
        Request request=new Request.Builder()
                .get()
                .url(activity.getString(R.string.url_project)+categoryId+"?rows="+rows+"&page="+page)
                .build();
        Call call=httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(FANRequestCode.GET_PROJECT_IN_CATEGORY_FAILURE);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //服务器响应失败
                if(response==null||response.isSuccessful()==false){
                    handler.sendEmptyMessage(FANRequestCode.GET_PROJECT_IN_CATEGORY_FAILURE);
                    return;
                }
                Gson gson=new GsonBuilder().create();
                String str_response=response.body().string();
                allProjectInCategory=new AllProjectInCategory();
                try {

                    //用Gson进行解析，并判断结果是否为空
                    if((allProjectInCategory = gson.fromJson(str_response, allProjectInCategory.getClass()))==null){
                        handler.sendEmptyMessage(FANRequestCode.GET_PROJECT_IN_CATEGORY_FAILURE);
                        return;
                    }
                    //项目获取失败
                    if(allProjectInCategory.isResult()==false){
                        handler.handlerFanErrorMessage(allProjectInCategory.getErrCode());
                        handler.sendEmptyMessage(FANRequestCode.GET_PROJECT_IN_CATEGORY_FAILURE);
                        return;
                    }


                    //获取项目信息成功
                    handler.sendEmptyMessage(FANRequestCode.GET_PROJECT_IN_CATEGORY_SUCCESS);
                }catch (IllegalStateException e){
                    handler.sendEmptyMessage(FANRequestCode.GET_PROJECT_IN_CATEGORY_FAILURE);
                    e.printStackTrace();
                }catch (JsonSyntaxException e){
                    handler.sendEmptyMessage(FANRequestCode.GET_PROJECT_IN_CATEGORY_FAILURE);
                    e.printStackTrace();
                }
            }
        });
    }
}
