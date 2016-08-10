package com.fansfunding.fan.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.fansfunding.fan.R;
import com.fansfunding.internal.ErrorCode;

/**
 * Created by 13616 on 2016/7/24.
 */
public class ErrorHandler extends Handler {

    private Activity context;
    public ErrorHandler(Activity context){
        this.context=context;
    }

    public void setContext(Activity context){
        this.context=context;
    }

    @Override
    public void handleMessage(Message msg){
        switch(msg.what){
            case ErrorCode.PARAMETER_ERROR:
                if(context.isFinishing()==true){
                    break;
                }
                Toast.makeText(context,"参数错误",Toast.LENGTH_LONG).show();
                break;
            case ErrorCode.AUTHORITY_NOT_ENOUGH:
                //取消登陆状态
                SharedPreferences share=context.getSharedPreferences(context.getString(R.string.sharepreference_login_by_phone), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=share.edit();
                editor.putBoolean("isLogin",false);
                editor.commit();
                if(context.isFinishing()==true){
                    break;
                }
                Toast.makeText(context,"权限不足,请重新登陆",Toast.LENGTH_LONG).show();
                break;
            case ErrorCode.PASSWORD_ERROR:
                if(context.isFinishing()==true){
                    break;
                }
                Toast.makeText(context,"账户或密码错误",Toast.LENGTH_LONG).show();
                break;
            case ErrorCode.REQUEST_FAILURE:
                break;
            case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                if(context.isFinishing()==true){
                    break;
                }
                Toast.makeText(context,"请求过于频繁",Toast.LENGTH_LONG).show();
                break;
            case ErrorCode.USER_NOT_EXIST:
                if(context.isFinishing()==true){
                    break;
                }
                Toast.makeText(context,"用户不存在",Toast.LENGTH_LONG).show();
                break;
            case ErrorCode.USERNAME_IS_EXIST:
                if(context.isFinishing()==true){
                    break;
                }
                Toast.makeText(context,"用户名已存在",Toast.LENGTH_LONG).show();
                break;
            case ErrorCode.REQUEST_SUCCESS:
                break;
            case ErrorCode.VERIFICATION_CODE_OVERDUE:
                if(context.isFinishing()==true){
                    break;
                }
                Toast.makeText(context,"验证码已过期",Toast.LENGTH_LONG).show();
                break;
            case ErrorCode.VERIFICATION_CODE_ERROR:
                if(context.isFinishing()==true){
                    break;
                }
                Toast.makeText(context,"验证码错误",Toast.LENGTH_LONG).show();
                break;
            default:
                super.handleMessage(msg);
        }
    }
}
