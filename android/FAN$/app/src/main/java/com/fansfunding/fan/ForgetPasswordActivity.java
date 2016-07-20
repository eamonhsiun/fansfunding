package com.fansfunding.fan;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.fansfunding.internal.*;
import com.fansfunding.internal.MD5Util;
import com.fansfunding.internal.VerificationListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ForgetPasswordActivity extends AppCompatActivity {

    //短信验证码按钮的间隔时间
    private final static String REMAINTIME="remainTime";

    //用来表示验证码请求失败
    private final static int VERIFICATION_ERROR=VerificationListener.VERIFICATION_ERROR;            //100

    //用来表示允许再次按验证码按钮
    private final static int VERIFICATION_ENABLE=VerificationListener.VERIFICATION_ENABLE;          //101

    //用来表示验证码按钮的时间又经过一秒，从而需要更改按钮显示的时间
    private final static int VERIFICATION_TIMECHANGE=VerificationListener.VERIFICATION_TIMECHANGE;  //102

    //修改密码成功
    private final static int CHANGE_PASSWORD_SUCCESS=103;

    //修改密码失败
    private final static int CHANGE_PASSWORD_FAILURE=104
            ;

    //httpclient
    private OkHttpClient httpClient;

    //验证码按钮
    private Button btn_forget_password_verification_code;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case CHANGE_PASSWORD_SUCCESS:
                    new AlertDialog.Builder(ForgetPasswordActivity.this).setTitle("更换密码成功").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ForgetPasswordActivity.this.finish();
                        }
                    }).show();
                    break;
                case CHANGE_PASSWORD_FAILURE:
                    Toast.makeText(ForgetPasswordActivity.this,"修改密码失败",Toast.LENGTH_LONG).show();
                    break;
                //验证码请求失败
                case VERIFICATION_ERROR:
                    Toast.makeText(ForgetPasswordActivity.this,"请求失败，请重新获取验证码",Toast.LENGTH_LONG).show();
                    break;
                //允许再次按下验证按
                case VERIFICATION_ENABLE:
                    if(btn_forget_password_verification_code!=null) {
                        btn_forget_password_verification_code.setEnabled(true);
                        btn_forget_password_verification_code.setText("获取验证码");
                    }
                    break;
                //更改验证码按钮里的倒计时
                case VERIFICATION_TIMECHANGE:
                    Bundle bundle=msg.getData();
                    if(btn_forget_password_verification_code!=null&&btn_forget_password_verification_code.isEnabled()==false) {
                        btn_forget_password_verification_code.setText("获取验证码(" + bundle.getInt(REMAINTIME, 60) + ")");
                    }
                    break;
                //请求过于频繁
                case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                    Toast.makeText(ForgetPasswordActivity.this,"请求过于频繁",Toast.LENGTH_LONG).show();
                    break;
                //验证码错误
                case ErrorCode.VERIFICATION_CODE_ERROR:
                    Toast.makeText(ForgetPasswordActivity.this,"验证码错误",Toast.LENGTH_LONG).show();
                    break;
                //验证码过期
                case ErrorCode.VERIFICATION_CODE_OVERDUE:
                    Toast.makeText(ForgetPasswordActivity.this,"验证码过期",Toast.LENGTH_LONG).show();
                    break;
                case ErrorCode.USER_NOT_EXIST:
                    Toast.makeText(ForgetPasswordActivity.this,"用户名不存在",Toast.LENGTH_LONG).show();
                    break;
                //参数错误
                case ErrorCode.PARAMETER_ERROR:
                    Toast.makeText(ForgetPasswordActivity.this,"参数错误",Toast.LENGTH_LONG).show();
                    break;
                //权限不足
                case ErrorCode.AUTHORITY_NOT_ENOUGH:
                    Toast.makeText(ForgetPasswordActivity.this,"权限不足",Toast.LENGTH_LONG).show();
                    break;

            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_forget_password);
        setSupportActionBar(toolbar);

        //设置返回键和标题
        ActionBar actionBar=this.getSupportActionBar();
        actionBar.setTitle("找回密码");
        actionBar.setDisplayHomeAsUpEnabled(true);



        //获取新密码输入框


        //获取httpclient
        httpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();

        //获取验证码按钮
        btn_forget_password_verification_code =(Button)findViewById(R.id.btn_forget_password_verification_code);
        btn_forget_password_verification_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputEditText tiet_forget_password_account=(TextInputEditText)findViewById(R.id.tiet_forget_password_account);
                VerificationListener verificationListener=new VerificationListener(tiet_forget_password_account.getText().toString(),ForgetPasswordActivity.this,handler,httpClient);
                verificationListener.onClick(v);
            }
        });

        //找回密码按钮
        Button btn_forget_password_ensure=(Button)findViewById(R.id.btn_forget_password_ensure);
        btn_forget_password_ensure.setOnClickListener(new ForgetPasswordListener());
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

    public class ForgetPasswordListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            //新密码输入框
            TextInputEditText tiet_forget_password_new_password=(TextInputEditText)findViewById(R.id.tiet_forget_password_new_password);
            String password=tiet_forget_password_new_password.getText().toString();

            //验证码输入框
            TextInputEditText tiet_forget_password_verification_code=(TextInputEditText)findViewById(R.id.tiet_forget_password_verification_code);
            String verificationCode=tiet_forget_password_verification_code.getText().toString();

            //获取验证码token
            SharedPreferences share=getSharedPreferences(getString(R.string.sharepreference_checker),MODE_PRIVATE);
            String token=share.getString("token","");

            //处理密码长度
            if(password.length()<6||password.length()>16){
                Toast.makeText(ForgetPasswordActivity.this,"密码应处于6-16位",Toast.LENGTH_LONG).show();
                return;
            }
            FormBody formBody=new FormBody
                    .Builder()
                    .add("password", MD5Util.getMD5String(password))
                    .add("checker",verificationCode)
                    .add("token",token)
                    .build();

            Request request=new Request.Builder()
                    .url(getString(R.string.url_forget_Pwd))
                    .post(formBody)
                    .build();

            Call call=httpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Message msg=new Message();
                    msg.what=CHANGE_PASSWORD_FAILURE;
                    handler.sendMessage(msg);
                    System.out.println("error1");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    //服务器响应失败
                    if(response==null||response.isSuccessful()==false){
                        Message msg=new Message();
                        msg.what=CHANGE_PASSWORD_FAILURE;
                        handler.sendMessage(msg);
                        System.out.println("error2");
                        return;
                    }
                    Gson gson=new GsonBuilder().create();
                    ForgetPwd forgetPwd=new ForgetPwd();
                    String str_response=response.body().string();
                    try {

                        //用Gson进行解析，并判断结果是否为空
                        if((forgetPwd = gson.fromJson(str_response, forgetPwd.getClass()))==null){
                            Message msg=new Message();
                            msg.what=CHANGE_PASSWORD_FAILURE;
                            handler.sendMessage(msg);
                            System.out.println("error3");
                        }
                        //重置密码失败
                        if(forgetPwd.isResult()==false){
                            Message msg=new Message();
                            switch (forgetPwd.getErrCode()){
                                case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                                    msg.what=ErrorCode.REQUEST_TOO_FRENQUENTLY;
                                    break;
                                case ErrorCode.PARAMETER_ERROR:
                                    msg.what=ErrorCode.PARAMETER_ERROR;
                                    break;
                                case ErrorCode.USER_NOT_EXIST:
                                    msg.what=ErrorCode.USER_NOT_EXIST;
                                    break;
                                case ErrorCode.AUTHORITY_NOT_ENOUGH:
                                    msg.what=ErrorCode.AUTHORITY_NOT_ENOUGH;
                                    break;
                                case ErrorCode.VERIFICATION_CODE_OVERDUE:
                                    msg.what=ErrorCode.VERIFICATION_CODE_OVERDUE;
                                    break;
                                case ErrorCode.VERIFICATION_CODE_ERROR:
                                    msg.what=ErrorCode.VERIFICATION_CODE_ERROR;
                                    break;
                                default:
                                    msg.what=CHANGE_PASSWORD_FAILURE;
                                    System.out.println("error4");
                                    break;
                            }
                            handler.sendMessage(msg);
                            return;
                        }

                        //将验证码信息保存到SharePreference里
                        SharedPreferences share=ForgetPasswordActivity.this.getSharedPreferences(getString(R.string.sharepreference_login_by_phone),MODE_PRIVATE);
                        SharedPreferences.Editor editor=share.edit();
                        editor.putBoolean("isLogin",true);
                        editor.putBoolean("result",forgetPwd.isResult());
                        editor.putInt("errCode",forgetPwd.getErrCode());
                        editor.putInt("id",forgetPwd.getData().getId());
                        editor.putString("token",forgetPwd.getToken());
                        editor.putString("head",forgetPwd.getData().getHead());
                        editor.putString("nickname",forgetPwd.getData().getNickname());
                        editor.commit();

                        //注册成功处理
                        Message msg=new Message();
                        msg.what=CHANGE_PASSWORD_SUCCESS;
                        handler.sendMessage(msg);
                    }catch (IllegalStateException e){
                        Message msg=new Message();
                        msg.what=CHANGE_PASSWORD_FAILURE;
                        handler.sendMessage(msg);
                        e.printStackTrace();
                    }catch (JsonSyntaxException e){
                        Message msg=new Message();
                        msg.what=CHANGE_PASSWORD_FAILURE;
                        handler.sendMessage(msg);
                        e.printStackTrace();
                    }
                }
            });

        }
    }
}
