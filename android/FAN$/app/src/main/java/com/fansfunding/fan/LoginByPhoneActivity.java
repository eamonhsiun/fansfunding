package com.fansfunding.fan;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fansfunding.internal.ErrorCode;
import com.fansfunding.internal.Login;
import com.fansfunding.internal.MD5Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginByPhoneActivity extends AppCompatActivity {


    //登陆成功
    private final static int LOGIN_BY_PHONE_SUCCESS=100;

    //登陆失败
    private final static int LOGIN_BY_PHONE_FAILURE=101;


    //手机号码输入栏
    private TextInputEditText tiet_account;

    //密码输入栏
    private TextInputEditText tiet_password;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                //登录成功
                case LOGIN_BY_PHONE_SUCCESS:
                    AlertDialog dialog=new AlertDialog.Builder(LoginByPhoneActivity.this).setTitle("登陆成功").setPositiveButton("登陆", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(LoginByPhoneActivity.this.isFinishing()==false) {
                                setResult(1);
                                LoginByPhoneActivity.this.finish();
                            }
                        }
                    }).setNegativeButton("社交账户登陆",null).setNeutralButton("忘记密码",null).create();
                    dialog.show();
                    Button btn=dialog.getButton(AlertDialog.BUTTON_NEUTRAL);
                    btn.setTextColor(Color.BLUE);
                    break;
                //登录失败
                case LOGIN_BY_PHONE_FAILURE:
                    Toast.makeText(LoginByPhoneActivity.this,"登录失败",Toast.LENGTH_LONG).show();
                    break;
                //请求过于频繁
                case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                    Toast.makeText(LoginByPhoneActivity.this,"请求过于频繁",Toast.LENGTH_LONG).show();
                    break;
                //参数错误
                case ErrorCode.PARAMETER_ERROR:
                    Toast.makeText(LoginByPhoneActivity.this,"参数错误",Toast.LENGTH_LONG).show();
                    break;
                //权限不足
                case ErrorCode.AUTHORITY_NOT_ENOUGH:
                    Toast.makeText(LoginByPhoneActivity.this,"权限不足",Toast.LENGTH_LONG).show();
                    break;
                //密码错误
                case ErrorCode.PASSWORD_ERROR:
                    Toast.makeText(LoginByPhoneActivity.this,"密码错误",Toast.LENGTH_LONG).show();
                    break;
                //用户名不存在
                case ErrorCode.USER_NOT_EXIST:
                    Toast.makeText(LoginByPhoneActivity.this,"用户名不存在",Toast.LENGTH_LONG).show();
                    break;

            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_by_phone);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_loginByPhone);
        setSupportActionBar(toolbar);
        Intent intent=getIntent();

        //设置返回键
        ActionBar actionBar=this.getSupportActionBar();
        actionBar.setTitle("登陆");
        actionBar.setDisplayHomeAsUpEnabled(true);

        tiet_account=(TextInputEditText) findViewById(R.id.tiet_account);
        tiet_password=(TextInputEditText) findViewById(R.id.tiet_password);

        Button btn_loginByPhone=(Button)findViewById(R.id.btn_loginByPhone);

        //登陆按钮
        btn_loginByPhone.setOnClickListener(new LoginByPhoneListener());

        //注册按钮
        TextView tv_register_phone=(TextView)findViewById(R.id.tv_register_phone);
        tv_register_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(getString(R.string.activity_registerbyphone));
                startActivity(intent);
            }
        });

        //找回密码按钮
        TextView tv_forget_password=(TextView)findViewById(R.id.tv_forget_password);
        tv_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(getString(R.string.activity_forget_password));
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case android.R.id.home:
                if(LoginByPhoneActivity.this.isFinishing()==false) {
                    setResult(0);
                    finish();
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;

    }

    public class LoginByPhoneListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            //手机号
            String phone=tiet_account.getText().toString();
            //密码
            String password=tiet_password.getText().toString();

            //手机号未输入的情况
            if(phone==null||phone.equals("")){
                Toast.makeText(LoginByPhoneActivity.this,"请输入手机号",Toast.LENGTH_LONG).show();
                return;
            }
            //密码未输入的情况
            if(password==null||password.equals("")){
                Toast.makeText(LoginByPhoneActivity.this,"请输入密码",Toast.LENGTH_LONG).show();
                return;
            }

            FormBody formBody=new FormBody.Builder()
                    .add("name",phone)
                    .add("password", MD5Util.getMD5String(password))
                    .build();
            OkHttpClient httpClient=new OkHttpClient();
            Request request=new Request.Builder()
                    .url(getString(R.string.url_login_by_phone))
                    .post(formBody)
                    .build();

            Call call=httpClient.newCall(request);
            call.enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    Message msg=new Message();
                    msg.what=LOGIN_BY_PHONE_FAILURE;
                    handler.sendMessage(msg);

                    //可能是网络原因导致的问题
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    //服务器响应失败
                    if(response==null||response.isSuccessful()==false){
                        Message msg=new Message();
                        msg.what=LOGIN_BY_PHONE_FAILURE;
                        handler.sendMessage(msg);
                        return;
                    }

                    Gson gson=new GsonBuilder().create();
                    Login login=new Login();
                    String str_response=response.body().string();
                    try {

                        //用Gson进行解析，并判断结果是否为空
                        if((login = gson.fromJson(str_response, login.getClass()))==null){
                            Message msg=new Message();
                            msg.what=LOGIN_BY_PHONE_FAILURE;
                            handler.sendMessage(msg);
                            return;
                        }
                        //处理登陆失败
                        if(login.isResult()==false){
                            Message msg=new Message();
                            switch (login.getErrCode()){
                                case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                                    msg.what=ErrorCode.REQUEST_TOO_FRENQUENTLY;
                                    break;
                                case ErrorCode.PARAMETER_ERROR:
                                    msg.what=ErrorCode.PARAMETER_ERROR;
                                    break;
                                case ErrorCode.AUTHORITY_NOT_ENOUGH:
                                    msg.what=ErrorCode.AUTHORITY_NOT_ENOUGH;
                                    break;
                                case ErrorCode.PASSWORD_ERROR:
                                    msg.what=ErrorCode.PASSWORD_ERROR;
                                    break;
                                case ErrorCode.USER_NOT_EXIST:
                                    msg.what=ErrorCode.USER_NOT_EXIST;
                                    break;
                                default:
                                    msg.what=LOGIN_BY_PHONE_FAILURE;
                                    break;
                            }
                            handler.sendMessage(msg);
                            return;
                        }
                        //将验证码信息保存到SharePreference里
                        SharedPreferences share=LoginByPhoneActivity.this.getSharedPreferences(getString(R.string.sharepreference_login_by_phone),MODE_PRIVATE);
                        SharedPreferences.Editor editor=share.edit();
                        editor.putBoolean("isLogin",true);
                        editor.putBoolean("result",login.isResult());
                        editor.putInt("errCode",login.getErrCode());
                        editor.putInt("id",login.getData().getId());
                        editor.putString("token",login.getToken());
                        editor.putString("head",login.getData().getHead());
                        editor.putString("nickname",login.getData().getNickname());
                        editor.commit();

                      /*  //将是否登陆写入sharePreference中
                        SharedPreferences share_is_login=getSharedPreferences(getString(R.string.sharepreference_login_message),MODE_PRIVATE);
                        SharedPreferences.Editor editor_is_login=share_is_login.edit();
                        editor_is_login.putBoolean("isLogin",true);
                        editor_is_login.commit();*/

                        //登陆成功
                        Message msg=new Message();
                        msg.what=LOGIN_BY_PHONE_SUCCESS;
                        handler.sendMessage(msg);

                    }catch (IllegalStateException e){
                        Message msg=new Message();
                        msg.what=LOGIN_BY_PHONE_FAILURE;
                        handler.sendMessage(msg);
                        e.printStackTrace();
                    }catch (JsonSyntaxException e){
                        Message msg=new Message();
                        msg.what=LOGIN_BY_PHONE_FAILURE;
                        handler.sendMessage(msg);
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
