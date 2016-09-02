package com.fansfunding.fan.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.fansfunding.fan.R;

/**
 * Created by 13616 on 2016/8/25.
 */
public class LoginSituation {
    public static boolean isLogin(Activity activity){
        SharedPreferences share=activity.getSharedPreferences(activity.getString(R.string.sharepreference_login_by_phone), Context.MODE_PRIVATE);
        return share.getBoolean("isLogin",false);
    }

    public static int getUserId(Activity activity){
        SharedPreferences share=activity.getSharedPreferences(activity.getString(R.string.sharepreference_login_by_phone), Context.MODE_PRIVATE);
        return share.getInt("id",-1);
    }

    public static String getUserToken(Activity activity){
        SharedPreferences share=activity.getSharedPreferences(activity.getString(R.string.sharepreference_login_by_phone), Context.MODE_PRIVATE);
        return share.getString("token","");
    }
}
