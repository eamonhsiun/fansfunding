package com.fansfunding.fan.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.fansfunding.app.App;
import com.fansfunding.fan.R;

/**
 * 用来创建登陆界面
 * */

public class LoginActivity extends AppCompatActivity {
    private App app;

    //启动使用手机登陆的请求码
    public final static int REQUEST_LOGIN_BY_PHONE=1000;

    //判断是否登录成功
    private boolean isloginSuccess;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                //登陆成功
                case LoginDialog.LOGIN_BY_PHONE_SUCCESS:
                    setResult(RESULT_OK);
                    if(LoginActivity.this.isFinishing()==false){
                        LoginActivity.this.finish();
                    }
                    break;
                case LoginDialog.LOGIN_BY_PHONE_FAILURE:
                    setResult(RESULT_CANCELED);
                    if(LoginActivity.this.isFinishing()==false){
                        LoginActivity.this.finish();
                    }
                    break;

            }
            super.handleMessage(msg);
        }
    };
    LoginDialog loginDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_dialog);

        setFinishOnTouchOutside(false);

        //登录框处理对象
        loginDialog=new LoginDialog(this,handler);

        app = (App)getApplication();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

    public static void login(Activity activity){
        Intent intent=new Intent();
        intent.setAction(activity.getString(R.string.activity_login));
        activity.startActivityForResult(intent,LoginActivity.REQUEST_LOGIN_BY_PHONE);
    }

    public static void loginForResult(Activity activity,int requestCode){
        Intent intent=new Intent();
        intent.setAction(activity.getString(R.string.activity_login));
        activity.startActivityForResult(intent,requestCode);
    }
}

