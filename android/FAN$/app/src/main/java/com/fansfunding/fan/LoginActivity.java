package com.fansfunding.fan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.provider.SyncStateContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fansfunding.weibo.*;
import com.sina.weibo.sdk.auth.*;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

import java.text.SimpleDateFormat;
import android.view.ViewGroup.LayoutParams;
/**
 * 用来创建登陆界面
 * */

public class LoginActivity extends AppCompatActivity {

    //启动使用手机登陆的请求码
    private final static int LOGIN_BY_PHONE=100;

    //判断是否登录成功
    private boolean isloginSuccess;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                //登陆成功
                case LoginDialog.LOGIN_BY_PHONE_SUCCESS:
                    if(LoginActivity.this.isFinishing()==false){
                        LoginActivity.this.finish();
                    }
                    break;
                case LoginDialog.LOGIN_BY_PHONE_FAILURE:
                    if(LoginActivity.this.isFinishing()==false){
                        LoginActivity.this.finish();
                    }
                    break;

            }
            super.handleMessage(msg);
        }
    };
    private AuthInfo auth_weibo;
    private SsoHandler ssoHandler_weibo;
    LoginDialog loginDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_dialog);

        setFinishOnTouchOutside(false);

        //登录框处理对象
        loginDialog=new LoginDialog(this,handler);


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
}


/*
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("登陆");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //actionBar.setLogo(R.drawable.cancellogintest);

        //使用手机号码登陆
        TextView tv_login_phone=(TextView)findViewById(R.id.tv_login_phone);
        tv_login_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(getString(R.string.activity_loginbyphone));
                startActivityForResult(intent,LOGIN_BY_PHONE);
                LoginDialog loginDialog=new LoginDialog(LoginActivity.this);
            }
        });
        //使用微博登陆
        auth_weibo=new AuthInfo(this,Constants.APP_KEY,Constants.REDIRECT_URL,Constants.SCOPE);
        Button btn_login_weibo=(Button)findViewById(R.id.btn_login_weibo);
        btn_login_weibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ssoHandler_weibo=new SsoHandler(LoginActivity.this, auth_weibo);
                ssoHandler_weibo.authorize(new AuthListener());
            }
        });
        */


/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ssoHandler_weibo != null) {
            ssoHandler_weibo.authorizeCallBack(requestCode, resultCode, data);
        }
        switch(requestCode){
            //如果登陆成功，则关闭此页面
            case LOGIN_BY_PHONE:
                if(resultCode==1)
                    this.finish();
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }



    }

    private class AuthListener implements WeiboAuthListener {
        @Override
        public void onComplete(Bundle values) {
            Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(values);
            if (accessToken != null && accessToken.isSessionValid()) {
                String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
                        new java.util.Date(accessToken.getExpiresTime()));
                String format = getString(R.string.weibosdk_demo_token_to_string_format_1);
                //result.setText(String.format(format, accessToken.getToken(), date));

                AccessTokenKeeper.writeAccessToken(getApplicationContext(), accessToken);
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(LoginActivity.this,
                    R.string.weibosdk_demo_toast_auth_canceled, Toast.LENGTH_SHORT).show();
        }
    }

*/