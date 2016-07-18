package com.fansfunding.fan;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.fansfunding.internal.ErrorCode;
import com.fansfunding.internal.MD5Util;
import com.fansfunding.internal.NewUser;
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

public class ResetPasswordActivity extends AppCompatActivity {

    //表示重置密码框消失时的事件
    private final static int DIALOG_IS_CANELED=100;

    //表示修改密码失败
    private final static int RESET_PASSWORD_FAILURE=101;
    //表示修改密码成功
    private final static int RESET_PASSWORD_SUCCESS=102;

    //用来判断是否点击过"确认修改"按钮
    private boolean isClicker=false;

    //用来判断是否已经响应完成了
    private boolean isFinish=false;

    //重置密码输入框
    private TextInputEditText tiet_reset_password_new_password;

    //再次输入重置密码的输入框
    private TextInputEditText tiet_reset_password_new_password_again;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case DIALOG_IS_CANELED:
                    if(isClicker==false&&ResetPasswordActivity.this.isFinishing()==false){
                        ResetPasswordActivity.this.finish();
                    }
                    break;
                case RESET_PASSWORD_FAILURE:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private AlertDialog dialog_reset_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internal_waiting);

        setFinishOnTouchOutside(false);

        //打开重置密码框
        InitResetPassword();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

    private void InitResetPassword(){
        isClicker=false;
        View view_reset_password=View.inflate(this,R.layout.dialog_reset_password,null);

        //两个密码输入框
        tiet_reset_password_new_password=(TextInputEditText) view_reset_password.findViewById(R.id.tiet_reset_password_new_password);
        tiet_reset_password_new_password_again=(TextInputEditText) view_reset_password.findViewById(R.id.tiet_reset_password_new_password_again);

        dialog_reset_password=new AlertDialog.Builder(this)
                .setTitle("重置密码")
                .setView(view_reset_password)
                .setPositiveButton("确认修改",new ResetPasswordListener())
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        Message msg=new Message();
                        msg.what=DIALOG_IS_CANELED;
                        handler.sendMessage(msg);
                    }
                })
                .create();
        dialog_reset_password.setCanceledOnTouchOutside(false);
        dialog_reset_password.show();
    }

    private class ResetPasswordListener implements DialogInterface.OnClickListener{

        @Override
        public void onClick(DialogInterface dialog, int which) {
            isClicker=true;
            //密码长度不符合
            if(tiet_reset_password_new_password.getText().toString().length()<6
                    ||tiet_reset_password_new_password.getText().toString().length()>16){
                InitResetPassword();
                tiet_reset_password_new_password.setError("密码长度应为于6-16位");
                return;
            }
            //两次密码输入不相等
            if(tiet_reset_password_new_password.getText().toString()
                    .equals(tiet_reset_password_new_password_again.getText().toString())==false){
                InitResetPassword();
                tiet_reset_password_new_password.setError("两次密码输入不一致，请重新输入");
                return;
            }
            String password=tiet_reset_password_new_password.getText().toString();

            //获得userId
            SharedPreferences share=getSharedPreferences(getString(R.string.sharepreference_login_by_phone),MODE_PRIVATE);
            int userId=share.getInt("id",0);
            //获得登陆token
            String token=share.getString("token","false");
            if(userId==0||token.equals("false")){
                SharedPreferences.Editor editor=share.edit();
                editor.putBoolean("isLogin",false);
                editor.commit();
                ResetPasswordActivity.this.finish();
                return;
            }

            OkHttpClient httpClient=new OkHttpClient();
            FormBody formBody=new FormBody.Builder()
                    .add("token",token)
                    .add("password", MD5Util.getMD5String(password))
                    .build();
            Request request=new Request.Builder()
                    .url(getString(R.string.url_user)+userId+"/newPwd")
                    .post(formBody)
                    .build();

            Call call=httpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Looper.prepare();
                    InitResetPassword();
                    Toast.makeText(ResetPasswordActivity.this,"网络异常，请检查网络",Toast.LENGTH_LONG).show();
                    Looper.loop();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    //服务器响应失败
                    if(response==null||response.isSuccessful()==false){
                        Looper.prepare();
                        InitResetPassword();
                        Toast.makeText(ResetPasswordActivity.this,"服务器响应失败，请重试1",Toast.LENGTH_LONG).show();
                        Looper.loop();
                        return;
                    }
                    Gson gson=new GsonBuilder().create();
                    NewUser newUser=new NewUser();
                    String str_response=response.body().string();
                    try {

                        //用Gson进行解析，并判断结果是否为空
                        if((newUser = gson.fromJson(str_response, newUser.getClass()))==null){
                            Looper.prepare();

                            InitResetPassword();
                            Toast.makeText(ResetPasswordActivity.this,"服务器响应失败，请重试",Toast.LENGTH_LONG).show();
                            Looper.loop();
                            return;
                        }
                        //重置密码失败
                        if(newUser.isResult()==false){
                            Looper.prepare();
                            InitResetPassword();
                            switch (newUser.getErrCode()){
                                case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                                    Toast.makeText(ResetPasswordActivity.this,"请求太频繁",Toast.LENGTH_LONG).show();
                                    break;
                                case ErrorCode.PARAMETER_ERROR:
                                    Toast.makeText(ResetPasswordActivity.this,"参数错误",Toast.LENGTH_LONG).show();
                                    break;

                                case ErrorCode.AUTHORITY_NOT_ENOUGH:
                                    Toast.makeText(ResetPasswordActivity.this,"权限不足",Toast.LENGTH_LONG).show();
                                    break;
                                default:
                                    Toast.makeText(ResetPasswordActivity.this,"重置密码失败",Toast.LENGTH_LONG).show();
                                    break;
                            }
                            Looper.loop();
                            return;
                        }

                        //将验证码信息保存到SharePreference里
                        SharedPreferences share=ResetPasswordActivity.this.getSharedPreferences(getString(R.string.sharepreference_new_user),MODE_PRIVATE);
                        SharedPreferences.Editor editor=share.edit();
                        editor.putBoolean("isLogin",true);
                        editor.putBoolean("result",newUser.isResult());
                        editor.putInt("errCode",newUser.getErrCode());
                        editor.putInt("id",newUser.getData().getId());
                        editor.putString("token",newUser.getToken());
                        editor.putString("head",newUser.getData().getHead());
                        editor.putString("nickname",newUser.getData().getNickname());
                        editor.commit();
                        Looper.prepare();
                        AlertDialog dialog_success=new AlertDialog.Builder(ResetPasswordActivity.this)
                                .setTitle("重置密码成功")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(ResetPasswordActivity.this.isFinishing()==false){
                                            ResetPasswordActivity.this.finish();
                                        }
                                    }
                                })
                                .create();
                        dialog_success.setCanceledOnTouchOutside(false);
                        dialog_success.show();
                        Looper.loop();
                    }catch (IllegalStateException e){
                        Looper.prepare();
                        InitResetPassword();
                        Toast.makeText(ResetPasswordActivity.this,"服务器响应失败，请重试",Toast.LENGTH_LONG).show();
                        Looper.loop();
                        e.printStackTrace();
                    }catch (JsonSyntaxException e){
                        Looper.prepare();
                        InitResetPassword();
                        Toast.makeText(ResetPasswordActivity.this,"服务器响应失败，请重试",Toast.LENGTH_LONG).show();
                        Looper.loop();
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
