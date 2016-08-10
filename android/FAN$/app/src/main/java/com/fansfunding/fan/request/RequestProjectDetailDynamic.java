package com.fansfunding.fan.request;

import android.app.Activity;
import android.os.Message;
import android.util.Log;

import com.fansfunding.fan.R;
import com.fansfunding.fan.utils.ErrorHandler;
import com.fansfunding.fan.utils.FANRequestCode;
import com.fansfunding.internal.ErrorCode;
import com.fansfunding.internal.ProjectDetailDynamic;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 13616 on 2016/8/9.
 * 获取项目动态
 */
public class RequestProjectDetailDynamic {

    //项目动态
    private ProjectDetailDynamic dynamic;

    public ProjectDetailDynamic getDynamic(){
        return dynamic;
    }

    //一次获取动态的数量
    private int rows=10;
    //获取动态的页数
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
    //获取动态信息
    public void getProjectDetailDynamic(Activity activity, final ErrorHandler handler, OkHttpClient httpClient, final int categoryId, final int projectId){
        //OkHttpClient okHttpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();


        Request request=new Request.Builder()
                .get()
                .url(activity.getString(R.string.url_project)+categoryId+"/"+projectId+"/moment?rows="+rows+"&page="+page)
                .build();

        Call call=httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(FANRequestCode.GET_PROJECT_DETAIL_MOMENT_FAILURE);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //服务器响应失败
                if(response==null||response.isSuccessful()==false){
                    handler.sendEmptyMessage(FANRequestCode.GET_PROJECT_DETAIL_MOMENT_FAILURE);
                    return;
                }
                Gson gson=new GsonBuilder().create();
                String str_response=response.body().string();
                Log.i("TAG","Dynamic:"+str_response) ;
                dynamic=new ProjectDetailDynamic();
                try {

                    //用Gson进行解析，并判断结果是否为空
                    if((dynamic = gson.fromJson(str_response, dynamic.getClass()))==null){
                        handler.sendEmptyMessage(FANRequestCode.GET_PROJECT_DETAIL_MOMENT_FAILURE);
                        return;
                    }
                    //获取项目详情失败
                    if(dynamic.isResult()==false){
                        switch (dynamic.getErrCode()){
                            case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                                handler.sendEmptyMessage(ErrorCode.REQUEST_TOO_FRENQUENTLY);
                                break;
                            case ErrorCode.PARAMETER_ERROR:
                                handler.sendEmptyMessage(ErrorCode.PARAMETER_ERROR);
                                break;
                            default:
                                handler.sendEmptyMessage(FANRequestCode.GET_PROJECT_DETAIL_MOMENT_FAILURE);
                                break;
                        }

                        return;
                    }
                    //获取项目信息成功
                    handler.sendEmptyMessage(FANRequestCode.GET_PROJECT_DETAIL_MOMENT_SUCCESS);
                }catch (IllegalStateException e){
                    handler.sendEmptyMessage(FANRequestCode.GET_PROJECT_DETAIL_MOMENT_FAILURE);
                    e.printStackTrace();
                }catch (JsonSyntaxException e){
                    handler.sendEmptyMessage(FANRequestCode.GET_PROJECT_DETAIL_MOMENT_FAILURE);
                    e.printStackTrace();
                }
            }

        });
    }

}
