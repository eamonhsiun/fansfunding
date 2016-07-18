package com.fansfunding.internal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fansfunding.fan.R;
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

import com.fansfunding.internal.ErrorCode;

/**
 * Created by 13616 on 2016/7/9.
 */
public class VerificationListener implements View.OnClickListener {

    //短信验证码按钮的间隔时间
    public final static String REMAINTIME="remainTime";

    //用来表示验证码请求失败
    public final static int VERIFICATION_ERROR=100;

    //用来表示允许再次按验证码按钮
    public final static int VERIFICATION_ENABLE=101;

    //用来表示验证码按钮的时间又经过一秒，从而需要更改按钮显示的时间
    public final static int VERIFICATION_TIMECHANGE=102;



    //用来表示验证码过期

    //用来获取验证码的手机号码
    private String phone;

    //用来获取SharePreference
    private Context context;

    //用来处理发送错误信息的handler
    private Handler handler;

    //httpclient
    private OkHttpClient httpClient;


    public VerificationListener(String phone,Context context,Handler handler, OkHttpClient httpClient){
        this.phone=phone;
        this.context=context;
        this.handler=handler;
        this.httpClient=httpClient;
    }


    public void setPhone(String phone){
        this.phone=phone;
    }
    @Override
    public void onClick(View v) {
        Button button=(Button)v;

        //手机号码为空的情况
        if(phone==null||phone.equals("")){
            Toast.makeText(context,"请输入手机号",Toast.LENGTH_LONG).show();
            return;
        }

        //发送验证码请求
        FormBody formBody=new FormBody.Builder().add("phone",phone).build();
        Request request = new Request.Builder()
                .post(formBody)
                .url(context.getString(R.string.url_checker))
                .build();
        Call call = httpClient.newCall(request);

        System.out.println(request.toString());
        //反馈响应函数
        call.enqueue(new Callback() {

            //验证码获取失败，服务器未响应
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg=new Message();
                msg.what=VERIFICATION_ERROR;
                handler.sendMessage(msg);

            }

            //服务器响应
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response==null||response.isSuccessful()==false){
                    Message msg=new Message();
                    msg.what=VERIFICATION_ERROR;
                    handler.sendMessage(msg);
                    return;
                }

                Gson gson=new GsonBuilder().create();
                NewChecker newChecker=new NewChecker();
                String str_response=response.body().string();
                try {

                    //用Gson进行解析，并判断结果是否为空
                    if((newChecker = gson.fromJson(str_response, newChecker.getClass()))==null){
                        Message msg=new Message();
                        msg.what=VERIFICATION_ERROR;
                        handler.sendMessage(msg);
                    }

                    //验证码请求失败
                    if(newChecker.isResult()==false){
                        Message msg=new Message();
                        switch (newChecker.getErrCode()){
                            case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                                msg.what=ErrorCode.REQUEST_TOO_FRENQUENTLY;
                                break;
                            case ErrorCode.VERIFICATION_CODE_OVERDUE:
                                msg.what=ErrorCode.VERIFICATION_CODE_OVERDUE;
                                break;
                            case ErrorCode.VERIFICATION_CODE_ERROR:
                                msg.what=ErrorCode.VERIFICATION_CODE_ERROR;
                                break;
                            default:
                                msg.what=VERIFICATION_ERROR;
                                break;
                        }
                        handler.sendMessage(msg);
                        return;
                    }

                    //将验证码信息保存到SharePreference里
                    SharedPreferences share=context.getSharedPreferences(context.getString(R.string.sharepreference_checker),context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=share.edit();
                    editor.putBoolean("result",newChecker.isResult());
                    editor.putInt("errCode",newChecker.getErrCode());
                    editor.putString("data",newChecker.getData());
                    editor.putString("token",newChecker.getToken());
                    editor.commit();
                    //打印出验证码的值
                    System.out.println("验证码:"+newChecker.getToken());
                    System.out.println("验证码:"+newChecker.getData());

                    //提交失败进行提醒
                    if(!editor.commit()){
                        Message msg=new Message();
                        msg.what=VERIFICATION_ERROR;
                        handler.sendMessage(msg);
                    }



                }catch (IllegalStateException e){
                    Message msg=new Message();
                    msg.what=VERIFICATION_ERROR;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }catch (JsonSyntaxException e){
                    Message msg=new Message();
                    msg.what=VERIFICATION_ERROR;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }

            }
        });

        button.setEnabled(false);
        //在60秒后允许再次按验证码按钮

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                int second=60;
                while(second>0){
                    second--;
                    Message msg=new Message();
                    msg.what=VERIFICATION_TIMECHANGE;
                    Bundle bundle=new Bundle();
                    bundle.putInt(REMAINTIME,second);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                //允许再次按下验证码按钮
                Message msg=new Message();
                msg.what=VERIFICATION_ENABLE;
                handler.sendMessage(msg);
            }

        });
        thread.start();
    }

}

