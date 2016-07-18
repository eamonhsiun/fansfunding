package com.fansfunding.fan;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
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

import java.io.IOException;
import java.io.StringReader;
import java.net.Proxy;
import java.text.Normalizer;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.fansfunding.internal.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

public class RegisterByPhoneActivity extends AppCompatActivity {

    private OkHttpClient httpClient;
    //短信验证码按钮的间隔时间
    private final static String REMAINTIME="remainTime";

    //用来表示验证码请求失败
    private final static int VERIFICATION_ERROR=VerificationListener.VERIFICATION_ERROR;            //100

    //用来表示允许再次按验证码按钮
    private final static int VERIFICATION_ENABLE=VerificationListener.VERIFICATION_ENABLE;          //101

    //用来表示验证码按钮的时间又经过一秒，从而需要更改按钮显示的时间
    private final static int VERIFICATION_TIMECHANGE=VerificationListener.VERIFICATION_TIMECHANGE;  //102

    //用来表示手机号为空
    private final static int PHONE_NUMBER_ISNULL=103;


    //用来表示注册失败
    private final static int REGISTER_ERROR=104;

    //用来表示注册成功
    private final static int REGISTER_SUCCESS=105;

    //验证码按钮
    private Button btn_register_verification_code;

    //Handler
    final Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg){

            switch(msg.what){
                //验证码请求失败
                case VERIFICATION_ERROR:
                    Toast.makeText(RegisterByPhoneActivity.this,"请求失败，请重新获取验证码",Toast.LENGTH_LONG).show();
                    break;
                //允许再次按下验证按
                case VERIFICATION_ENABLE:
                    if(btn_register_verification_code!=null) {
                        btn_register_verification_code.setEnabled(true);
                        btn_register_verification_code.setText("获取验证码");
                    }
                    break;
                //更改验证码按钮里的倒计时
                case VERIFICATION_TIMECHANGE:
                    Bundle bundle=msg.getData();
                    if(btn_register_verification_code!=null&&btn_register_verification_code.isEnabled()==false) {
                        btn_register_verification_code.setText("获取验证码(" + bundle.getInt(REMAINTIME, 60) + ")");
                    }
                    break;
                //提醒未输入手机号
                case PHONE_NUMBER_ISNULL:
                    Toast.makeText(RegisterByPhoneActivity.this,"请输入手机号",Toast.LENGTH_LONG).show();
                    break;
                //注册失败
                case REGISTER_ERROR:
                    Toast.makeText(RegisterByPhoneActivity.this,"注册失败，请重新注册",Toast.LENGTH_LONG).show();
                    break;

                //注册成功
                case REGISTER_SUCCESS:
                    new AlertDialog.Builder(RegisterByPhoneActivity.this).setTitle("注册成功").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if(RegisterByPhoneActivity.this.isFinishing()==false)
                                RegisterByPhoneActivity.this.finish();
                        }
                    }).show();
                    break;
                //请求过于频繁
                case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                    Toast.makeText(RegisterByPhoneActivity.this,"请求过于频繁",Toast.LENGTH_LONG).show();
                    break;
                //验证码错误
                case ErrorCode.VERIFICATION_CODE_ERROR:
                    Toast.makeText(RegisterByPhoneActivity.this,"验证码错误",Toast.LENGTH_LONG).show();
                    break;
                //验证码过期
                case ErrorCode.VERIFICATION_CODE_OVERDUE:
                    Toast.makeText(RegisterByPhoneActivity.this,"验证码过期",Toast.LENGTH_LONG).show();
                    break;

                //用户名已注册
                case ErrorCode.USERNAME_IS_EXIST:
                    Toast.makeText(RegisterByPhoneActivity.this,"用户名已注册",Toast.LENGTH_LONG).show();
                    break;
                //参数错误
                case ErrorCode.PARAMETER_ERROR:
                    Toast.makeText(RegisterByPhoneActivity.this,"参数错误",Toast.LENGTH_LONG).show();
                    break;
                //权限不足
                case ErrorCode.AUTHORITY_NOT_ENOUGH:
                    Toast.makeText(RegisterByPhoneActivity.this,"权限不足",Toast.LENGTH_LONG).show();
                    break;

            }
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_by_phone);


        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_registerByPhone);
        setSupportActionBar(toolbar);
        Intent intent=getIntent();

        //设置返回键
        ActionBar actionBar=this.getSupportActionBar();
        actionBar.setTitle("注册");
        actionBar.setDisplayHomeAsUpEnabled(true);



        httpClient=new OkHttpClient();

        //获取验证码响应函数
        btn_register_verification_code=(Button)findViewById(R.id.btn_register_verification_code);
        btn_register_verification_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextInputEditText tiet_register_account=(TextInputEditText)RegisterByPhoneActivity.this.findViewById(R.id.tiet_register_account);
                String phone=tiet_register_account.getText().toString();
                com.fansfunding.internal.VerificationListener verificationListener=new com.fansfunding.internal.VerificationListener(phone,RegisterByPhoneActivity.this,mHandler,httpClient);
                verificationListener.onClick(v);
            }
        });

        //注册按钮
        Button btn_register_ensure=(Button)findViewById(R.id.btn_register_ensure);
        btn_register_ensure.setOnClickListener(new RegisterEusureListener());



    }




    //确认注册按钮的响应函数
    public class RegisterEusureListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            //获取密码的MD5码
            TextInputEditText tiet_register_password=(TextInputEditText)RegisterByPhoneActivity.this.findViewById(R.id.tiet_register_password);
            String password=MD5Util.getMD5String(tiet_register_password.getText().toString());

            SharedPreferences share=RegisterByPhoneActivity.this.getSharedPreferences(getString(R.string.sharepreference_checker),MODE_PRIVATE);
            String token=share.getString("token","");

            //验证码
            TextInputEditText tiet_register_verification_code=(TextInputEditText)RegisterByPhoneActivity.this.findViewById(R.id.tiet_register_verification_code);
            String verificationCode=tiet_register_verification_code.getText().toString();

            //处理密码长度
            if(tiet_register_password.getText().toString().length()<6||tiet_register_password.getText().toString().length()>16){
                Toast.makeText(RegisterByPhoneActivity.this,"密码应处于6-16位",Toast.LENGTH_LONG).show();
                return;
            }

            FormBody formBody=new FormBody
                    .Builder()
                    .add("password",password)
                    .add("checker",verificationCode)
                    .add("token",token)
                    .build();

            Request request = new Request.Builder()
                    .post(formBody)
                    .url(getString(R.string.url_new_user))
                    .build();
            Call call = httpClient.newCall(request);

            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Message msg=new Message();
                    msg.what=REGISTER_ERROR;
                    mHandler.sendMessage(msg);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    //服务器响应失败
                    if(response==null||response.isSuccessful()==false){
                        Message msg=new Message();
                        msg.what=REGISTER_ERROR;
                        mHandler.sendMessage(msg);
                        return;
                    }

                    Gson gson=new GsonBuilder().create();
                    NewUser newUser=new NewUser();
                    String str_response=response.body().string();
                    try {

                        //用Gson进行解析，并判断结果是否为空
                        if((newUser = gson.fromJson(str_response, newUser.getClass()))==null){
                            Message msg=new Message();
                            msg.what=REGISTER_ERROR;
                            mHandler.sendMessage(msg);
                            return;
                        }
                        //处理注册失败
                        if(newUser.isResult()==false){
                            Message msg=new Message();
                            switch (newUser.getErrCode()){
                                case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                                    msg.what=ErrorCode.REQUEST_TOO_FRENQUENTLY;
                                    break;
                                case ErrorCode.PARAMETER_ERROR:
                                    msg.what=ErrorCode.PARAMETER_ERROR;
                                    break;
                                case ErrorCode.USERNAME_IS_EXIST:
                                    msg.what=ErrorCode.USERNAME_IS_EXIST;
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
                                    msg.what=REGISTER_ERROR;
                                    break;
                            }
                            mHandler.sendMessage(msg);
                            return;
                        }

                        //将验证码信息保存到SharePreference里
                        SharedPreferences share=RegisterByPhoneActivity.this.getSharedPreferences(getString(R.string.sharepreference_new_user),MODE_PRIVATE);
                        SharedPreferences.Editor editor=share.edit();
                        editor.putBoolean("isLogin",true);
                        editor.putBoolean("result",newUser.isResult());
                        editor.putInt("errCode",newUser.getErrCode());
                        editor.putInt("id",newUser.getData().getId());
                        editor.putString("token",newUser.getToken());
                        editor.putString("head",newUser.getData().getHead());
                        editor.putString("nickname",newUser.getData().getNickname());
                        editor.commit();

                        //写入到手机失败
                        if(!editor.commit()){
                            Message msg=new Message();
                            msg.what=REGISTER_ERROR;
                            mHandler.sendMessage(msg);
                            return;
                        }
                        //注册成功处理
                        Message msg=new Message();
                        msg.what=REGISTER_SUCCESS;
                        mHandler.sendMessage(msg);
                    }catch (IllegalStateException e){
                        Message msg=new Message();
                        msg.what=REGISTER_ERROR;
                        mHandler.sendMessage(msg);
                        e.printStackTrace();
                    }catch (JsonSyntaxException e){
                        Message msg=new Message();
                        msg.what=REGISTER_ERROR;
                        mHandler.sendMessage(msg);
                        e.printStackTrace();
                    }
                }
            });

        }
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

}

