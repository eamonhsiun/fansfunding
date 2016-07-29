package com.fansfunding.fan.request;

import android.app.Activity;

import com.fansfunding.fan.R;
import com.fansfunding.fan.utils.ErrorHandler;
import com.fansfunding.fan.utils.FANRequestCode;
import com.fansfunding.internal.ErrorCode;
import com.fansfunding.internal.user.OrderDetail;
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
 * Created by 13616 on 2016/7/28.
 */
public class RequestOrderDetail {

    private OrderDetail orderDetail=null;

    public OrderDetail getOrderDetail(){
        return orderDetail;
    }


    public void getOrderDetail(Activity activity, final ErrorHandler handler, OkHttpClient httpClient,final String orderNo,final int userId,final String token){

        Request request=new Request.Builder()
                .get()
                .url(activity.getString(R.string.url_user)+userId+"/orders/"+orderNo+"?token="+token)
                .build();

        Call call=httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(FANRequestCode.GET_ORDER_DETAIL_FAILURE);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //服务器响应失败
                if(response==null||response.isSuccessful()==false){
                    handler.sendEmptyMessage(FANRequestCode.GET_ORDER_DETAIL_FAILURE);
                    return;
                }
                Gson gson=new GsonBuilder().create();
                String str_response=response.body().string();

                orderDetail=new OrderDetail();
                try {
                    //用Gson进行解析，并判断结果是否为空
                    if((orderDetail = gson.fromJson(str_response, orderDetail.getClass()))==null){
                        handler.sendEmptyMessage(FANRequestCode.GET_ORDER_DETAIL_FAILURE);
                        return;
                    }
                    //获取项目详情失败
                    if(orderDetail.isResult()==false){
                        switch (orderDetail.getErrCode()){
                            case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                                handler.sendEmptyMessage(ErrorCode.REQUEST_TOO_FRENQUENTLY);
                                break;
                            case ErrorCode.PARAMETER_ERROR:
                                handler.sendEmptyMessage(ErrorCode.PARAMETER_ERROR);
                                break;
                            default:
                                handler.sendEmptyMessage(FANRequestCode.GET_ORDER_DETAIL_FAILURE);
                        }
                        return;
                    }
                    //获取项目信息成功
                    handler.sendEmptyMessage(FANRequestCode.GET_ORDER_DETAIL_SUCCESS);
                }catch (IllegalStateException e){
                    handler.sendEmptyMessage(FANRequestCode.GET_ORDER_DETAIL_FAILURE);
                    e.printStackTrace();
                }catch (JsonSyntaxException e){
                    handler.sendEmptyMessage(FANRequestCode.GET_ORDER_DETAIL_FAILURE);
                    e.printStackTrace();
                }
            }
        });
    }
}
