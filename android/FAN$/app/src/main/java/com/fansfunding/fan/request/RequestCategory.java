package com.fansfunding.fan.request;

import android.app.Activity;
import android.util.Log;

import com.fansfunding.fan.R;
import com.fansfunding.fan.utils.ErrorHandler;
import com.fansfunding.fan.utils.FANRequestCode;
import com.fansfunding.internal.CategoryInfo;
import com.fansfunding.internal.ErrorCode;
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
 * Created by 13616 on 2016/9/6.
 */
public class RequestCategory {

    private CategoryInfo categoryInfo;

    public CategoryInfo getCategoryInfo(){
        return categoryInfo;
    }


    public void requestCategory(Activity activity, final ErrorHandler handler, OkHttpClient httpClient){
        Request request=new Request.Builder()
                .get()
                .url(activity.getString(R.string.url_project)+"categorys")
                .build();

        Call call=httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(FANRequestCode.GET_PROJECT_CATEGORY_FAILURE);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //服务器响应失败
                if(response==null||response.isSuccessful()==false){
                    handler.sendEmptyMessage(FANRequestCode.GET_PROJECT_CATEGORY_FAILURE);
                    return;
                }
                Gson gson=new GsonBuilder().create();
                String str_response=response.body().string();
                Log.i("TAG","分类信息:"+str_response);
                categoryInfo=new CategoryInfo();
                try {

                    //用Gson进行解析，并判断结果是否为空
                    if((categoryInfo = gson.fromJson(str_response, categoryInfo.getClass()))==null){
                        handler.sendEmptyMessage(FANRequestCode.GET_PROJECT_CATEGORY_FAILURE);
                        return;
                    }
                    //获取失败
                    if(categoryInfo.isResult()==false){
                        handler.handlerFanErrorMessage(categoryInfo.getErrCode());
                        handler.sendEmptyMessage(FANRequestCode.GET_PROJECT_CATEGORY_FAILURE);
                        return;
                    }


                    //获取成功
                    handler.sendEmptyMessage(FANRequestCode.GET_PROJECT_CATEGORY_SUCCESS);
                }catch (IllegalStateException e){
                    handler.sendEmptyMessage(FANRequestCode.GET_PROJECT_CATEGORY_FAILURE);
                    e.printStackTrace();
                }catch (JsonSyntaxException e){
                    handler.sendEmptyMessage(FANRequestCode.GET_PROJECT_CATEGORY_FAILURE);
                    e.printStackTrace();
                }
            }
        });
    }


}
