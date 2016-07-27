package com.fansfunding.fan.request;

import android.app.Activity;
import android.os.Message;
import android.util.Log;

import com.fansfunding.fan.R;
import com.fansfunding.fan.utils.ErrorHandler;
import com.fansfunding.fan.utils.FANRequestCode;
import com.fansfunding.internal.ErrorCode;
import com.fansfunding.internal.ProjectDetailReward;
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
 * Created by 13616 on 2016/7/24.
 */
public class RequestProjectDetailReward {

    //获取到的回报信息
    private ProjectDetailReward reward=null;

    public ProjectDetailReward getReward(){
        return reward;
    }

    public void getProjectDetailReward(Activity activity,final ErrorHandler handler,OkHttpClient httpClient,final int categoryId,final int projectId){
        Request request=new Request.Builder()
                .get()
                .url(activity.getString(R.string.url_project)+categoryId+"/"+projectId+"/feedbacks")
                .build();
        Call call=httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(FANRequestCode.GET_PROJECT_DETAIL_REWARD_FAILURE);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //服务器响应失败
                if(response==null||response.isSuccessful()==false){
                    handler.sendEmptyMessage(FANRequestCode.GET_PROJECT_DETAIL_REWARD_FAILURE);
                    return;
                }
                Gson gson=new GsonBuilder().create();
                String str_response=response.body().string();
                Log.i("TAG","REWARD:"+str_response) ;
                reward=new ProjectDetailReward();
                try {
                    //用Gson进行解析，并判断结果是否为空
                    if((reward = gson.fromJson(str_response, reward.getClass()))==null){
                        handler.sendEmptyMessage(FANRequestCode.GET_PROJECT_DETAIL_REWARD_FAILURE);
                        return;
                    }
                    //获取项目详情失败
                    if(reward.isResult()==false){
                        switch (reward.getErrCode()){
                            case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                                handler.sendEmptyMessage(ErrorCode.REQUEST_TOO_FRENQUENTLY);
                                break;
                            case ErrorCode.PARAMETER_ERROR:
                                handler.sendEmptyMessage(ErrorCode.PARAMETER_ERROR);
                                break;
                            default:
                                handler.sendEmptyMessage(FANRequestCode.GET_PROJECT_DETAIL_REWARD_FAILURE);
                        }
                        return;
                    }

                    //获取项目信息成功
                    handler.sendEmptyMessage(FANRequestCode.GET_PROJECT_DETAIL_REWARD_SUCCESS);
                }catch (IllegalStateException e){
                    handler.sendEmptyMessage(FANRequestCode.GET_PROJECT_DETAIL_REWARD_FAILURE);
                    e.printStackTrace();
                }catch (JsonSyntaxException e){
                    handler.sendEmptyMessage(FANRequestCode.GET_PROJECT_DETAIL_REWARD_FAILURE);
                    e.printStackTrace();
                }
            }
        });
    }
}
