package com.fansfunding.fan;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fansfunding.internal.ErrorCode;
import com.fansfunding.internal.Logout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SettingActivity extends AppCompatActivity {

    //登出失败
    private final static int LOGOUT_FAILURE=100;

    //登出成功
    private final static int LOGOUT_SUCCESS=101;

    //登出成功的返回码
    public static final int REQUEST_CODE_LOGOUT_SUCCESS =400;

    private AlertDialog dialog_waitting;


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){

                case LOGOUT_FAILURE:
                case ErrorCode.AUTHORITY_NOT_ENOUGH:
                case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                    if(SettingActivity.this.isFinishing()==false){
                        //将是否登陆写入sharePreference中,即逝登出错误也同样当做登出
                        SharedPreferences share_is_login=getSharedPreferences(getString(R.string.sharepreference_login_by_phone),MODE_PRIVATE);
                        SharedPreferences.Editor editor_is_login=share_is_login.edit();
                        editor_is_login.putBoolean("isLogin",false);
                        editor_is_login.commit();
                        new AlertDialog.Builder(SettingActivity.this).setTitle("登出出错").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (SettingActivity.this.isFinishing() == false){
                                    if(dialog_waitting.isShowing()==true){
                                        dialog_waitting.cancel();
                                    }

                                    setResult(REQUEST_CODE_LOGOUT_SUCCESS);
                                    SettingActivity.this.finish();
                                }

                            }
                        }).setCancelable(false)
                                .show();

                    }
                    break;
                case LOGOUT_SUCCESS:
                    if(SettingActivity.this.isFinishing()==false) {
                        new AlertDialog.Builder(SettingActivity.this).setTitle("登出成功").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (SettingActivity.this.isFinishing() == false){
                                    if(dialog_waitting.isShowing()==true){
                                        dialog_waitting.cancel();
                                    }
                                    setResult(REQUEST_CODE_LOGOUT_SUCCESS);
                                    SettingActivity.this.finish();
                                }

                            }
                        }).setCancelable(false)
                                .show();
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_setting);
        setSupportActionBar(toolbar);
        Intent intent=getIntent();

        toolbar.setTitleTextColor(Color.WHITE);
        //设置返回键
        ActionBar actionBar=this.getSupportActionBar();
        actionBar.setTitle("设置");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.arrow);

        //登出按钮
        Button btn_setting_logout=(Button)findViewById(R.id.btn_setting_logout);
        btn_setting_logout.setOnClickListener(new LogoutListener());

        //管理地址按钮
        TextView tv_setting_address=(TextView)findViewById(R.id.tv_setting_address);
        tv_setting_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(getString(R.string.activity_address));
                startActivity(intent);
            }
        });

        //账号和密码设置按钮
        LinearLayout ll_setting_account=(LinearLayout)findViewById(R.id.ll_setting_account);
        ll_setting_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(getString(R.string.activity_setting_account));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;

    }

    public class LogoutListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            //循环等待框
            dialog_waitting=new AlertDialog.Builder(SettingActivity.this)
                    .setTitle("数据传输")
                    .setView(R.layout.activity_internal_waiting)
                    .create();
            dialog_waitting.setCancelable(false);
            //dialog_waitting.show();


            SharedPreferences share=getSharedPreferences(getString(R.string.sharepreference_login_by_phone),MODE_PRIVATE);
            int userId=share.getInt("id",0);
            String token=share.getString("token","false");
            if(userId==0||token.equals("false")){
                SharedPreferences.Editor editor=share.edit();
                editor.putBoolean("isLogin",false);
                editor.commit();
                SettingActivity.this.finish();
                return;
            }

            System.out.println("token="+token);
            OkHttpClient httpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
            FormBody formBody=new FormBody.Builder()
                    .add("token",token)
                    .build();
            Request request=new Request.Builder()
                    .url(getString(R.string.url_user)+userId+"/logout")
                    .post(formBody)
                    .build();

            Call call=httpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Message msg=new Message();
                    msg.what=LOGOUT_FAILURE;
                    handler.sendMessage(msg);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    //服务器响应失败
                    if(response==null||response.isSuccessful()==false){
                        Message msg=new Message();
                        msg.what=LOGOUT_FAILURE;
                        handler.sendMessage(msg);
                        return;
                    }

                    Gson gson=new GsonBuilder().create();
                    Logout logout=new Logout();
                    String str_response=response.body().string();
                    try {
                        //用Gson进行解析，并判断结果是否为空
                        if((logout = gson.fromJson(str_response, logout.getClass()))==null){
                            Message msg=new Message();
                            msg.what=LOGOUT_FAILURE;
                            handler.sendMessage(msg);
                            return;
                        }

                        System.out.println("2:"+str_response);
                        //处理登陆失败
                        if(logout.isResult()==false){
                            Message msg=new Message();
                            switch (logout.getErrCode()){
                                case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                                    msg.what=ErrorCode.REQUEST_TOO_FRENQUENTLY;
                                    break;
                                case ErrorCode.AUTHORITY_NOT_ENOUGH:
                                    msg.what=ErrorCode.AUTHORITY_NOT_ENOUGH;
                                    break;
                                default:
                                    msg.what=LOGOUT_FAILURE;
                                    break;
                            }
                            handler.sendMessage(msg);
                            return;
                        }
                        //将验证码信息保存到SharePreference里
                        SharedPreferences share=SettingActivity.this.getSharedPreferences(getString(R.string.sharepreference_logout),MODE_PRIVATE);
                        SharedPreferences.Editor editor=share.edit();
                        editor.putBoolean("result",logout.isResult());
                        editor.putInt("errCode",logout.getErrCode());
                        editor.commit();



                        //将是否登陆写入sharePreference中
                        SharedPreferences share_is_login=getSharedPreferences(getString(R.string.sharepreference_login_by_phone),MODE_PRIVATE);
                        SharedPreferences.Editor editor_is_login=share_is_login.edit();
                        editor_is_login.putBoolean("isLogin",false);
                        editor_is_login.commit();


                        //登出成功
                        Message msg=new Message();
                        msg.what=LOGOUT_SUCCESS;
                        handler.sendMessage(msg);

                    }catch (IllegalStateException e){
                        Message msg=new Message();
                        msg.what=LOGOUT_FAILURE;
                        handler.sendMessage(msg);
                        e.printStackTrace();
                    }catch (JsonSyntaxException e){
                        Message msg=new Message();
                        msg.what=LOGOUT_FAILURE;
                        handler.sendMessage(msg);
                        e.printStackTrace();
                    }
                }
            });


        }
    }


}
