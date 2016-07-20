package com.fansfunding.fan;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fansfunding.internal.ErrorCode;
import com.fansfunding.internal.ForgetPwd;
import com.fansfunding.internal.Login;
import com.fansfunding.internal.MD5Util;
import com.fansfunding.internal.NewChecker;
import com.fansfunding.internal.NewUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.w3c.dom.Text;
import org.xml.sax.helpers.LocatorImpl;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 13616 on 2016/7/12.
 */

//为每个dialog添加cancel响应函数

public class LoginDialog {

    //登陆成功
    public final static int LOGIN_BY_PHONE_SUCCESS=400;

    //登陆失败
    public final static int LOGIN_BY_PHONE_FAILURE=401;


    //忘记密码的请求验证码编号(此时只有一个手机号码输入框)
    private final static int VERIFICATION_CODE_BY_FORGET_PASSWORD=300;

    //忘记密码的请求验证码编号(此时既有手机号码输入框又有验证码输入框)
    private final static int VERIFICATION_CODE_BY_FORGET_PASSWORD_VERIFICATION_PHONE=301;

    //注册的请求验证码编号
    private final static int VERIFICATION_CODE_BY_REGISTER_VERIFICATION=302;

    //所要显示的容器
    private static AppCompatActivity context;

    //所在Activity的Handler
    private Handler handler;

    //httpclient
    private OkHttpClient httpClient;


    /***
     *弹出框
     */

    //手机登陆弹出框
    private AlertDialog dialog_login_by_phone;

    //找回密码弹出框(只有手机号)
    private AlertDialog dialog_forget_password;

    //重置密码弹出框(两个密码输入框)
    private AlertDialog dialog_reset_password;

    //验证手机(手机号和验证码输入框)
    private AlertDialog dialog_verification_phone;

    //注册界面弹出框(手机号和密码框)
    private AlertDialog dialog_register;

    //注册界面弹出框(手机号和验证码)
    private AlertDialog dialog_register_verification;




    //手机登陆弹出框的View
    private View view_login_by_phone;

    //登陆弹出框的手机号输入框
    private TextInputEditText tiet_login_by_phone_account;

    //登陆弹出框的密码输入框
    private TextInputEditText tiet_login_by_phone_password;

    //找回密码弹出框的view
    private View view_forget_password;

    //找回密码弹出框的手机号输入框
    private TextInputEditText tiet_forget_password_account;



    //重置密码弹出框的view
    private View view_reset_password;


    //重置密码输入框
    private TextInputEditText tiet_reset_password_new_password;

    //再次输入重置密码的输入框
    private TextInputEditText tiet_reset_password_new_password_again;

    //验证手机弹出框的view
    private View view_verification_phone;

    //(找回密码)验证手机号码输入框
    private TextInputEditText tiet_forget_password_verification_account;

    //(找回密码)验证手机的验证码输入框
    private TextInputEditText tiet_forget_password_verification_verification_code;


    //注册的弹出框(只有手机号输入框和密码框)
    private View view_register;

    //注册手机号码输入框
    private TextInputEditText  tiet_register_account;

    //注册密码输入框
    private TextInputEditText  tiet_register_password;


    //注册的弹出框(手机号码和验证码)
    private View view_register_verification;

    //注册弹出框的手机号输入框
    private TextInputEditText tiet_register_verification_account;

    //注册弹出框的验证码输入框
    private TextInputEditText tiet_register_verification_verification_code;


    //构造函数
    public LoginDialog(AppCompatActivity context,Handler handler){
        this.context=context;
        this.handler=handler;
        httpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
        InitLoginByPhone();

    }


    //判断所有的对话框是否全部消失
    public boolean isFinish(){
        if((dialog_login_by_phone==null||dialog_login_by_phone.isShowing()==false)
                &&(dialog_forget_password==null||dialog_forget_password.isShowing()==false)
                &&(dialog_reset_password==null||dialog_reset_password.isShowing()==false)
                &&(dialog_verification_phone==null||dialog_verification_phone.isShowing()==false)
                &&(dialog_register==null||dialog_register.isShowing()==false)
                &&(dialog_register_verification==null||dialog_register_verification.isShowing()==false))
            return true;
        else
            return false;

    }
    private TextInputEditText getPhoneInput(int mode){
        switch (mode){
            //忘记密码的请求验证码编号(此时只有手机号码输入框)
            case VERIFICATION_CODE_BY_FORGET_PASSWORD:
                return tiet_forget_password_account;

            //忘记密码的请求验证码编号(此时既有手机号码输入框又有验证码输入框)
            case VERIFICATION_CODE_BY_FORGET_PASSWORD_VERIFICATION_PHONE:
                return tiet_forget_password_verification_account;
            //注册的请求验证码编号
            case VERIFICATION_CODE_BY_REGISTER_VERIFICATION:
                return tiet_register_verification_account;
            default:
                return null;

        }
    }

    private void InitVerificationDialog(int mode){
        switch (mode){
            //忘记密码的请求验证码编号(此时只有手机号码输入框)
            case VERIFICATION_CODE_BY_FORGET_PASSWORD:
                InitForgetPassword();
                break;

            //忘记密码的请求验证码编号(此时既有手机号码输入框又有验证码输入框)
            case VERIFICATION_CODE_BY_FORGET_PASSWORD_VERIFICATION_PHONE:
                //InitVerificationPhone();
                break;
            //注册的请求验证码编号
            case VERIFICATION_CODE_BY_REGISTER_VERIFICATION:
                InitRegister();
                if(dialog_register_verification.isShowing()==true){
                    dialog_register_verification.cancel();
                }
            default:
                break;

        }
    }
    //登陆弹出框设置
    public void InitLoginByPhone(){
        //获取设置界面
        view_login_by_phone=View.inflate(context,R.layout.dialog_login_by_phone,null);

        //获取输入框
        tiet_login_by_phone_account=(TextInputEditText)view_login_by_phone.findViewById(R.id.tiet_account);
        tiet_login_by_phone_password=(TextInputEditText)view_login_by_phone.findViewById(R.id.tiet_password);

        //忘记密码按钮响应函数
        TextView tv_forget_password=(TextView)view_login_by_phone.findViewById(R.id.tv_forget_password) ;
        tv_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InitForgetPassword();
                dialog_login_by_phone.cancel();
            }
        });

        //注册界面按钮
        LinearLayout ll_register_phone=(LinearLayout)view_login_by_phone.findViewById(R.id.ll_register_phone);
        ll_register_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InitRegister();
                dialog_login_by_phone.cancel();
            }
        });
        //创建登录框
        dialog_login_by_phone=new AlertDialog.Builder(context).setView(view_login_by_phone).setTitle("登录")
                .setPositiveButton("登录", new LoginByPhoneListener())
                .setNegativeButton("社交账号登录", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        InitLoginByPhone();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        if(isFinish()==true){
                            Message msg=new Message();
                            msg.what=LOGIN_BY_PHONE_FAILURE;
                            handler.sendMessage(msg);
                        }
                    }
                })
                .create();
        dialog_login_by_phone.setCanceledOnTouchOutside(false);
        dialog_login_by_phone.show();

        //设置按钮颜色
        Button btn_pos=dialog_login_by_phone.getButton(AlertDialog.BUTTON_POSITIVE);
        btn_pos.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        btn_pos.setTextSize(16);
        Button btn_neg=dialog_login_by_phone.getButton(AlertDialog.BUTTON_NEGATIVE);
        btn_neg.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        btn_neg.setTextSize(16);
    }


    //找回密码(忘记密码)，即需要重置密码，这一页只需要输入手机号来获取验证码
    public void InitForgetPassword(){
        //获取设置界面
        view_forget_password=View.inflate(context,R.layout.dialog_forget_password,null);
        //忘记密码弹出框的账号输入
        tiet_forget_password_account=(TextInputEditText) view_forget_password.findViewById(R.id.tiet_forget_password_account);
        dialog_forget_password=new AlertDialog.Builder(context)
                .setTitle("重置密码")
                .setView(view_forget_password)
                .setPositiveButton("获取验证码",new VerificationListener(VERIFICATION_CODE_BY_FORGET_PASSWORD))
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        if(isFinish()==true) {
                            InitLoginByPhone();
                        }
                    }
                })
                .create();
        dialog_forget_password.setCanceledOnTouchOutside(false);
        dialog_forget_password.show();

        //设置按钮颜色
        Button btn_pos=dialog_forget_password.getButton(AlertDialog.BUTTON_POSITIVE);
        btn_pos.setTextSize(16);
        btn_pos.setTextColor(context.getResources().getColor(R.color.colorPrimary));

    }

    //重置密码
    private void InitResetPassword(){
        //获取设置界面
        view_reset_password=View.inflate(context,R.layout.dialog_reset_password,null);

        //两个密码输入框
        tiet_reset_password_new_password=(TextInputEditText) view_reset_password.findViewById(R.id.tiet_reset_password_new_password);
        tiet_reset_password_new_password_again=(TextInputEditText) view_reset_password.findViewById(R.id.tiet_reset_password_new_password_again);

        //弹出框的设置
        dialog_reset_password=new AlertDialog.Builder(context)
                .setTitle("重置密码")
                .setView(view_reset_password)
                .setPositiveButton("确认修改",new ResetPasswordListener())
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        if(isFinish()==true){
                            InitVerificationPhone();
                        }
                    }
                })
                .create();
        dialog_reset_password.setCanceledOnTouchOutside(false);
        dialog_reset_password.show();

        //设置按钮颜色
        Button btn_pos=dialog_reset_password.getButton(AlertDialog.BUTTON_POSITIVE);
        btn_pos.setTextSize(16);
        btn_pos.setTextColor(context.getResources().getColor(R.color.colorPrimary));
    }

    //找回密码
    private void InitVerificationPhone(){
        //获取设置界面
        view_verification_phone=View.inflate(context,R.layout.dialog_verification_phone,null);

        //弹出框的账号输入栏和密码输入栏
        tiet_forget_password_verification_account=(TextInputEditText)view_verification_phone.findViewById(R.id.tiet_forget_password_verification_account);
        tiet_forget_password_verification_verification_code=(TextInputEditText)view_verification_phone.findViewById(R.id.tiet_forget_password_verification_verification_code);

        //重发验证码栏
        TextView tv_forget_password_verification_send_verification_code=(TextView)view_verification_phone.findViewById(R.id.tv_forget_password_verification_send_verification_code);
        tv_forget_password_verification_send_verification_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new VerificationListener(VERIFICATION_CODE_BY_FORGET_PASSWORD_VERIFICATION_PHONE).onClick(null,-1);
            }
        });
        //弹出框的设置
        dialog_verification_phone=new AlertDialog.Builder(context)
                .setTitle("验证手机")
                .setView(view_verification_phone)
                .setPositiveButton("重置密码", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        InitResetPassword();
                        dialog.cancel();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        if(isFinish()==true){
                            InitForgetPassword();
                        }
                    }
                })
                .create();
        dialog_verification_phone.setCanceledOnTouchOutside(false);
        dialog_verification_phone.show();

        //设置按钮颜色
        Button btn_pos=dialog_verification_phone.getButton(AlertDialog.BUTTON_POSITIVE);
        btn_pos.setTextSize(16);
        btn_pos.setTextColor(context.getResources().getColor(R.color.colorPrimary));
    }


    //注册弹出框(手机号和密码)
    private void InitRegister(){
        //获取设置界面
        view_register=View.inflate(context,R.layout.dialog_register,null);
        //弹出框的账号输入栏和密码输入栏
        tiet_register_account=(TextInputEditText)view_register.findViewById(R.id.tiet_register_account);
        tiet_register_password=(TextInputEditText)view_register.findViewById(R.id.tiet_register_password);

        dialog_register=new AlertDialog.Builder(context)
                .setTitle("注册")
                .setView(view_register)
                .setNeutralButton("已有账号?登录", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        InitLoginByPhone();
                        dialog.cancel();
                    }
                })
                .setPositiveButton("下一步", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String phone=tiet_register_account.getText().toString();
                        String password=tiet_register_password.getText().toString();
                        if(phone.equals("")||(password.length()<6||password.length()>16)){
                            InitRegister();
                            if(phone.equals("")) {
                                tiet_register_account.setError("请输入手机号");
                            }
                            if(password.length()<6||password.length()>16){
                                tiet_register_password.setError("密码长度应为于6-16位");
                            }
                            return;
                        }
                        InitRegisterVerification();
                        tiet_register_verification_account.setText(tiet_register_account.getText().toString());
                        dialog.cancel();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        if(isFinish()==true){
                            InitLoginByPhone();
                        }
                    }
                })
                .create();
        dialog_register.setCanceledOnTouchOutside(false);
        dialog_register.show();

        //设置按钮颜色
        Button btn_pos=dialog_register.getButton(AlertDialog.BUTTON_POSITIVE);
        btn_pos.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        btn_pos.setTextSize(16);
        Button btn_neu=dialog_register.getButton(AlertDialog.BUTTON_NEUTRAL);
        btn_neu.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        btn_neu.setTextSize(16);

    }

    //注册弹出框(手机号和验证码)
    public void InitRegisterVerification(){
        //获取设置界面
        view_register_verification=View.inflate(context,R.layout.dialog_register_verification,null);

        //弹出框的账号输入栏和验证码输入栏
        tiet_register_verification_account=(TextInputEditText)view_register_verification.findViewById(R.id.tiet_register_verification_account);
        tiet_register_verification_verification_code=(TextInputEditText)view_register_verification.findViewById(R.id.tiet_register_verification_verification_code);

        //重发验证码响应函数
        TextView tv_register_verification_send_verification_code=(TextView) view_register_verification.findViewById(R.id.tv_register_verification_send_verification_code);
        tv_register_verification_send_verification_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView temp=(TextView)v;
                temp.setText("重发验证码");
                new VerificationListener(VERIFICATION_CODE_BY_REGISTER_VERIFICATION).onClick(null,-1);
            }
        });
        dialog_register_verification=new AlertDialog.Builder(context)
                .setTitle("注册")
                .setView(view_register_verification)
                .setPositiveButton("完成注册",new RegisterListener())
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        if(isFinish()==true){
                            InitRegister();
                        }
                    }
                })
                .create();
        dialog_register_verification.setCanceledOnTouchOutside(false);
        dialog_register_verification.show();

        //设置按钮颜色
        Button btn_pos=dialog_register_verification.getButton(AlertDialog.BUTTON_POSITIVE);
        btn_pos.setTextSize(16);
        btn_pos.setTextColor(context.getResources().getColor(R.color.colorPrimary));

    }
    public AlertDialog abc(){
        return  dialog_login_by_phone;
    }



    //确定注册按钮监听器
    public class RegisterListener implements DialogInterface.OnClickListener{

        @Override
        public void onClick(DialogInterface dialog, int which) {
            String phone=tiet_register_verification_account.getText().toString();
            if(phone.equals("")){
                InitRegister();
                tiet_register_account.setError("请输入手机号");
                return;
            }

            String password=tiet_register_password.getText().toString();
            if(password.length()<6||password.length()>16){
                InitRegister();
                tiet_register_password.setError("密码长度应为6-16位");
                return;
            }
            String verificationCode=tiet_register_verification_verification_code.getText().toString();

            //如果验证码为空，则直接返回
            if(tiet_register_verification_verification_code.equals("null")){
                InitRegisterVerification();
                tiet_register_verification_account.setText(phone);
                tiet_register_verification_verification_code.setError("请输入验证码");
                return;
            }

            SharedPreferences share=context.getSharedPreferences(context.getString(R.string.sharepreference_checker),context.MODE_PRIVATE);
            String token=share.getString("token","");
            FormBody formBody=new FormBody
                    .Builder()
                    .add("password",MD5Util.getMD5String(password))
                    .add("checker",verificationCode)
                    .add("token",token)
                    .build();

            Request request = new Request.Builder()
                    .post(formBody)
                    .url(context.getString(R.string.url_new_user))
                    .build();
            Call call = httpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Looper.prepare();
                    String phone=tiet_register_verification_account.getText().toString();
                    String verificationCode=tiet_register_verification_verification_code.getText().toString();
                    InitRegisterVerification();
                    tiet_register_verification_account.setText(phone);
                    tiet_register_verification_verification_code.setText(verificationCode);
                    Toast.makeText(context,"网络异常，请检查网络",Toast.LENGTH_LONG).show();
                    Looper.loop();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    if(response==null||response.isSuccessful()==false){
                        Looper.prepare();
                        InitRegister();
                        Toast.makeText(context,"服务器响应失败，请重试",Toast.LENGTH_LONG).show();
                        Looper.loop();
                        return;
                    }
                    Gson gson=new GsonBuilder().create();
                    NewUser newUser=new NewUser();
                    String str_response=response.body().string();
                    System.out.println(str_response);
                    try {
                        //用Gson进行解析，并判断结果是否为空
                        if((newUser = gson.fromJson(str_response, newUser.getClass()))==null){
                            Looper.prepare();
                            InitRegister();
                            Toast.makeText(context,"服务器响应失败，请重试",Toast.LENGTH_LONG).show();
                            Looper.loop();
                            return;
                        }
                        //处理注册失败
                        if(newUser.isResult()==false){
                            Looper.prepare();
                            InitRegister();
                            switch (newUser.getErrCode()){
                                case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                                    Toast.makeText(context,"请求过于频繁",Toast.LENGTH_LONG).show();
                                    break;
                                case ErrorCode.PARAMETER_ERROR:
                                    Toast.makeText(context,"参数错误",Toast.LENGTH_LONG).show();
                                    break;
                                case ErrorCode.USERNAME_IS_EXIST:
                                    Toast.makeText(context,"用户名已存在",Toast.LENGTH_LONG).show();
                                    break;
                                case ErrorCode.AUTHORITY_NOT_ENOUGH:
                                    Toast.makeText(context,"权限不足",Toast.LENGTH_LONG).show();
                                    break;
                                case ErrorCode.VERIFICATION_CODE_OVERDUE:
                                    Toast.makeText(context,"验证码过期",Toast.LENGTH_LONG).show();
                                    break;
                                case ErrorCode.VERIFICATION_CODE_ERROR:
                                    Toast.makeText(context,"验证码错误",Toast.LENGTH_LONG).show();
                                    break;
                                default:
                                    Toast.makeText(context,"注册失败，请重试",Toast.LENGTH_LONG).show();
                                    break;
                            }

                            Looper.loop();
                            return;
                        }

                        //将验证码信息保存到SharePreference里
                        SharedPreferences share=context.getSharedPreferences(context.getString(R.string.sharepreference_login_by_phone),context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=share.edit();
                        editor.putBoolean("isLogin",true);
                        editor.putBoolean("result",newUser.isResult());
                        editor.putInt("errCode",newUser.getErrCode());
                        editor.putInt("id",newUser.getData().getId());
                        editor.putString("token",newUser.getToken());
                        editor.putString("head",newUser.getData().getHead());
                        editor.putString("nickname",newUser.getData().getNickname());
                        editor.commit();
                        System.out.println(str_response);

                      /*  //将是否登陆写入sharePreference中
                        SharedPreferences share_is_login=context.getSharedPreferences(context.getString(R.string.sharepreference_login_message),context.MODE_PRIVATE);
                        SharedPreferences.Editor editor_is_login=share_is_login.edit();
                        editor_is_login.putBoolean("isLogin",true);
                        editor_is_login.commit();*/

                        //成功响应事件
                        //待写
                        Message msg=new Message();
                        msg.what=LOGIN_BY_PHONE_SUCCESS;
                        handler.sendMessage(msg);

                    }catch (IllegalStateException e){
                        Looper.prepare();
                        InitRegister();
                        Toast.makeText(context,"服务器响应失败，请重试",Toast.LENGTH_LONG).show();
                        Looper.loop();
                        e.printStackTrace();
                    }catch (JsonSyntaxException e){
                        Looper.prepare();
                        InitRegister();
                        Toast.makeText(context,"服务器响应失败，请重试",Toast.LENGTH_LONG).show();
                        Looper.loop();
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    //重置密码(确认修改)按钮监听器
    public class ResetPasswordListener implements  DialogInterface.OnClickListener{

        @Override
        public void onClick(DialogInterface dialog, int which) {

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
            //验证码输入框
            String verificationCode=tiet_forget_password_verification_verification_code.getText().toString();

            if(verificationCode.equals("")){
                InitVerificationPhone();
                tiet_forget_password_verification_verification_code.setError("请输入验证码");
                return;
            }
            //获取验证码token
            SharedPreferences share=context.getSharedPreferences(context.getString(R.string.sharepreference_checker),context.MODE_PRIVATE);
            String token=share.getString("token","");

            FormBody formBody=new FormBody
                    .Builder()
                    .add("password", MD5Util.getMD5String(password))
                    .add("checker",verificationCode)
                    .add("token",token)
                    .build();

            Request request=new Request.Builder()
                    .url(context.getString(R.string.url_forget_Pwd))
                    .post(formBody)
                    .build();

            Call call=httpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Looper.prepare();
                    Toast.makeText(context,"网络异常，请检查网络",Toast.LENGTH_LONG).show();
                    InitResetPassword();
                    Looper.loop();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    //服务器响应失败
                    if(response==null||response.isSuccessful()==false){
                        Looper.prepare();
                        Toast.makeText(context,"服务器响应失败，请重新注册或找回密码",Toast.LENGTH_LONG).show();
                        InitVerificationPhone();
                        Looper.loop();
                        return;
                    }
                    Gson gson=new GsonBuilder().create();
                    ForgetPwd forgetPwd=new ForgetPwd();
                    String str_response=response.body().string();
                    try {

                        //用Gson进行解析，并判断结果是否为空
                        if((forgetPwd = gson.fromJson(str_response, forgetPwd.getClass()))==null){
                            Looper.prepare();
                            Toast.makeText(context,"服务器响应失败，请重新注册或找回密码",Toast.LENGTH_LONG).show();
                            InitVerificationPhone();
                            Looper.loop();
                            return;
                        }
                        //重置密码失败
                        if(forgetPwd.isResult()==false){
                            Looper.prepare();
                            InitVerificationPhone();
                            switch (forgetPwd.getErrCode()){
                                case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                                    Toast.makeText(context,"请求过于频繁",Toast.LENGTH_LONG).show();
                                    break;
                                case ErrorCode.PARAMETER_ERROR:
                                    Toast.makeText(context,"参数错误",Toast.LENGTH_LONG).show();
                                    break;
                                case ErrorCode.USER_NOT_EXIST:
                                    tiet_forget_password_verification_account.setError("用户不存在");
                                    break;
                                case ErrorCode.AUTHORITY_NOT_ENOUGH:
                                    Toast.makeText(context,"权限不足",Toast.LENGTH_LONG).show();
                                    break;
                                case ErrorCode.VERIFICATION_CODE_OVERDUE:
                                    tiet_forget_password_verification_verification_code.setError("验证码失效");
                                    break;
                                case ErrorCode.VERIFICATION_CODE_ERROR:
                                    tiet_forget_password_verification_verification_code.setError("验证码错误");
                                    break;
                                default:
                                    Toast.makeText(context,"重置密码错误，请重试",Toast.LENGTH_LONG).show();
                                    break;
                            }
                            Looper.loop();
                            return;
                        }

                        //将验证码信息保存到SharePreference里
                        SharedPreferences share=context.getSharedPreferences(context.getString(R.string.sharepreference_login_by_phone),context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=share.edit();
                        editor.putBoolean("isLogin",true);
                        editor.putBoolean("result",forgetPwd.isResult());
                        editor.putInt("errCode",forgetPwd.getErrCode());
                        editor.putInt("id",forgetPwd.getData().getId());
                        editor.putString("token",forgetPwd.getToken());
                        editor.putString("head",forgetPwd.getData().getHead());
                        editor.putString("nickname",forgetPwd.getData().getNickname());
                        editor.commit();


                        //重置密码成功处理
                        Message msg=new Message();
                        msg.what=LOGIN_BY_PHONE_SUCCESS;
                        handler.sendMessage(msg);

                    }catch (IllegalStateException e){
                        Looper.prepare();
                        Toast.makeText(context,"服务器响应失败，请重新尝试",Toast.LENGTH_LONG).show();
                        InitVerificationPhone();
                        Looper.loop();
                        e.printStackTrace();
                    }catch (JsonSyntaxException e){
                        Looper.prepare();
                        Toast.makeText(context,"服务器响应失败，请重新尝试",Toast.LENGTH_LONG).show();
                        InitVerificationPhone();
                        Looper.loop();
                        e.printStackTrace();
                    }
                }
            });


        }
    }



    //请求验证码监听器
    public class VerificationListener implements DialogInterface.OnClickListener {

        public VerificationListener(int mode){
            this.mode=mode;
        }

        //确定是哪个来请求验证码
        private  int mode;
        @Override
        public void onClick(DialogInterface dialog, int which) {
            TextInputEditText phoneInput=getPhoneInput(mode);

            //手机号输入框获取失败
            if(phoneInput==null){
                Toast.makeText(context,"出错，请重新注册",Toast.LENGTH_LONG).show();
                InitLoginByPhone();
                return;
            }

            String phone=phoneInput.getText().toString();
            if(phone==null||phone.equals("")) {
                InitVerificationDialog(mode);
                phoneInput=getPhoneInput(mode);
                phoneInput.setError("请输入手机号");
                return;
            }
            //发送验证码请求
            FormBody formBody=new FormBody.Builder().add("phone",phone).build();
            Request request = new Request.Builder()
                    .post(formBody)
                    .url(context.getString(R.string.url_checker))
                    .build();
            Call call = httpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Looper.prepare();
                    Toast.makeText(context,"网络异常，请检查网络",Toast.LENGTH_LONG).show();
                    InitVerificationDialog(mode);
                    Looper.loop();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    //服务器响应失败
                    if(response==null||response.isSuccessful()==false){
                        Looper.prepare();
                        Toast.makeText(context,"服务器响应失败，请重新注册或找回密码",Toast.LENGTH_LONG).show();
                        InitVerificationDialog(mode);
                        Looper.loop();
                        return;
                    }

                    Gson gson=new GsonBuilder().create();
                    NewChecker newChecker=new NewChecker();
                    String str_response=response.body().string();
                    try {
                        //用Gson进行解析，并判断结果是否为空
                        if((newChecker = gson.fromJson(str_response, newChecker.getClass()))==null){
                            Looper.prepare();
                            Toast.makeText(context,"服务器响应失败，请重新注册或找回密码",Toast.LENGTH_LONG).show();
                            InitVerificationDialog(mode);
                            Looper.loop();
                        }

                        //验证码请求失败
                        if(newChecker.isResult()==false){
                            Looper.prepare();
                            InitVerificationDialog(mode);
                            switch (newChecker.getErrCode()){
                                case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                                    Toast.makeText(context,"请求过于频繁",Toast.LENGTH_LONG).show();
                                    break;
                                case ErrorCode.PARAMETER_ERROR:
                                    Toast.makeText(context,"参数错误",Toast.LENGTH_LONG).show();
                                    break;
                                default:
                                    Toast.makeText(context,"验证码请求失败",Toast.LENGTH_LONG).show();
                                    break;
                            }
                            Looper.loop();
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
                        Log.i("TAG","验证码token:"+newChecker.getToken());
                        Log.i("TAG","验证码:"+newChecker.getData());

                        if(mode==VERIFICATION_CODE_BY_FORGET_PASSWORD){
                            Looper.prepare();
                            InitVerificationPhone();
                            //可能会有bug
                            tiet_forget_password_verification_account.setText(tiet_forget_password_account.getText().toString());
                            Looper.loop();
                        }

                    }catch (IllegalStateException e){
                        Looper.prepare();
                        Toast.makeText(context,"服务器响应失败，请重新注册或找回密码",Toast.LENGTH_LONG).show();
                        InitVerificationDialog(mode);
                        Looper.loop();
                        e.printStackTrace();
                    }catch (JsonSyntaxException e){
                        Looper.prepare();
                        Toast.makeText(context,"服务器响应失败，请重新注册或找回密码",Toast.LENGTH_LONG).show();
                        InitVerificationDialog(mode);
                        Looper.loop();
                        e.printStackTrace();
                    }

                }
            });

        }
    }

    //登陆按钮监听器
    public class LoginByPhoneListener implements DialogInterface.OnClickListener{

        @Override
        public void onClick(DialogInterface dialog, int which) {
            //手机号
            String phone=tiet_login_by_phone_account.getText().toString();
            //密码
            String password=tiet_login_by_phone_password.getText().toString();

            //手机号未输入的情况
            if(phone==null||phone.equals("")){
                InitLoginByPhone();
                tiet_login_by_phone_account.setError("请输入手机号");
                return;
            }
            //密码未输入的情况
            if(password==null||password.equals("")){
                InitLoginByPhone();
                tiet_login_by_phone_password.setError("请输入密码");
                return;
            }
            //密码长度不符合的情况
            if(password.length()<6||password.length()>16){
                tiet_login_by_phone_account.setError("账号或密码错误");
                return;
            }
            FormBody formBody=new FormBody.Builder()
                    .add("name",phone)
                    .add("password", MD5Util.getMD5String(password))
                    .build();

            Request request=new Request.Builder()
                    .url(context.getString(R.string.url_login_by_phone))
                    .post(formBody)
                    .build();

            Call call=httpClient.newCall(request);
            call.enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    Looper.prepare();
                    Toast.makeText(context,"网络异常，请检查网络",Toast.LENGTH_LONG).show();
                    InitLoginByPhone();
                    Looper.loop();

                    //可能是网络原因导致的问题
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    //服务器响应失败
                    if(response==null||response.isSuccessful()==false){
                        Looper.prepare();
                        Toast.makeText(context,"服务器响应失败，请重新登陆",Toast.LENGTH_LONG).show();
                        InitLoginByPhone();
                        Looper.loop();
                        return;
                    }

                    Gson gson=new GsonBuilder().create();
                    Login login=new Login();
                    String str_response=response.body().string();
                    try {

                        //用Gson进行解析，并判断结果是否为空
                        if((login = gson.fromJson(str_response, login.getClass()))==null){
                            Looper.prepare();
                            System.out.println(str_response.length());
                            Toast.makeText(context,"服务器响应失败，请重新登陆",Toast.LENGTH_LONG).show();
                            InitLoginByPhone();
                            Looper.loop();

                        }

                        //处理登陆失败
                        if(login.isResult()==false){
                            Message msg=new Message();
                            Looper.prepare();
                            InitLoginByPhone();
                            switch (login.getErrCode()){
                                case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                                    Toast.makeText(context,"请求过于频繁",Toast.LENGTH_LONG).show();
                                    break;
                                case ErrorCode.PARAMETER_ERROR:
                                    Toast.makeText(context,"参数错误",Toast.LENGTH_LONG).show();
                                    break;
                                case ErrorCode.AUTHORITY_NOT_ENOUGH:
                                    Toast.makeText(context,"权限不足",Toast.LENGTH_LONG).show();
                                    break;
                                case ErrorCode.PASSWORD_ERROR:
                                    tiet_login_by_phone_account.setError("账号或密码错误");
                                    Looper.loop();
                                    break;
                                case ErrorCode.USER_NOT_EXIST:
                                    tiet_login_by_phone_account.setError("账号或密码错误");
                                    break;
                                default:
                                    Toast.makeText(context,"登录失败，请重试",Toast.LENGTH_LONG).show();
                                    break;
                            }
                            Looper.loop();
                            return;
                        }
                        //将验证码信息保存到SharePreference里
                        SharedPreferences share=context.getSharedPreferences(context.getString(R.string.sharepreference_login_by_phone),context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=share.edit();
                        editor.putBoolean("isLogin",true);
                        editor.putBoolean("result",login.isResult());
                        editor.putInt("errCode",login.getErrCode());
                        editor.putInt("id",login.getData().getId());
                        editor.putString("token",login.getToken());
                        editor.putString("head",login.getData().getHead());
                        editor.putString("nickname",login.getData().getNickname());
                        editor.commit();

                        Log.e("TAH","token"+login.getToken());
                        //成功响应事件
                        //待写
                        Message msg=new Message();
                        msg.what=LOGIN_BY_PHONE_SUCCESS;
                        handler.sendMessage(msg);

                    }catch (IllegalStateException e){
                        Looper.prepare();
                        Toast.makeText(context,"服务器响应失败，请重新登陆",Toast.LENGTH_LONG).show();
                        InitLoginByPhone();
                        Looper.loop();
                        e.printStackTrace();
                    }catch (JsonSyntaxException e){
                        Looper.prepare();
                        Toast.makeText(context,"服务器响应失败，请重新登陆",Toast.LENGTH_LONG).show();
                        InitLoginByPhone();
                        Looper.loop();
                        e.printStackTrace();
                    }
                }
            });
        }
    }

}
