package com.fansfunding.fan.request;

import android.app.Activity;
import android.util.Log;

import com.fansfunding.fan.R;
import com.fansfunding.fan.utils.ErrorHandler;
import com.fansfunding.fan.utils.FANRequestCode;
import com.fansfunding.internal.ErrorCode;
import com.fansfunding.internal.project.ProjectSupportsInfo;
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
 * Created by 13616 on 2016/8/9.
 */
public class RequestProjectSupportInfo {

    //获取到的支持者信息
    private ProjectSupportsInfo supportsInfo=null;


    //一次获取回报的数量
    private int rows=10;
    //获取回报的页数
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


    public ProjectSupportsInfo getSupportsInfo() {
        return supportsInfo;
    }

    public void getProjectSupportsInfo(Activity activity, final ErrorHandler handler, OkHttpClient httpClient, final int categoryId, final int projectId){

        Request request=new Request.Builder()
                .get()
                .url(activity.getString(R.string.url_project)+categoryId+"/"+projectId+"/supporters?rows="+rows+"&page="+page)
                .build();
        Call call=httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(FANRequestCode.GET_PROJECT_DETAIL_SUPPORTER_FAILURE);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //服务器响应失败
                if(response==null||response.isSuccessful()==false){
                    handler.sendEmptyMessage(FANRequestCode.GET_PROJECT_DETAIL_SUPPORTER_FAILURE);
                    return;
                }
                Gson gson=new GsonBuilder().create();
                String str_response=response.body().string();
                Log.i("TAG","support:"+str_response);

                supportsInfo=new ProjectSupportsInfo();
                try {
                    //用Gson进行解析，并判断结果是否为空
                    if((supportsInfo = gson.fromJson(str_response, supportsInfo.getClass()))==null){
                        handler.sendEmptyMessage(FANRequestCode.GET_PROJECT_DETAIL_SUPPORTER_FAILURE);
                        return;
                    }
                    //获取项目详情失败
                    if(supportsInfo.isResult()==false){
                        switch (supportsInfo.getErrCode()){
                            case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                                handler.sendEmptyMessage(ErrorCode.REQUEST_TOO_FRENQUENTLY);
                                break;
                            case ErrorCode.PARAMETER_ERROR:
                                handler.sendEmptyMessage(ErrorCode.PARAMETER_ERROR);
                                break;
                            default:
                                handler.sendEmptyMessage(FANRequestCode.GET_PROJECT_DETAIL_SUPPORTER_FAILURE);
                        }
                        return;
                    }

                    //获取项目信息成功
                    handler.sendEmptyMessage(FANRequestCode.GET_PROJECT_DETAIL_SUPPORTER_SUCCESS);
                }catch (IllegalStateException e){
                    handler.sendEmptyMessage(FANRequestCode.GET_PROJECT_DETAIL_SUPPORTER_FAILURE);
                    e.printStackTrace();
                }catch (JsonSyntaxException e){
                    handler.sendEmptyMessage(FANRequestCode.GET_PROJECT_DETAIL_SUPPORTER_FAILURE);
                    e.printStackTrace();
                }

            }
        });


    }
}
