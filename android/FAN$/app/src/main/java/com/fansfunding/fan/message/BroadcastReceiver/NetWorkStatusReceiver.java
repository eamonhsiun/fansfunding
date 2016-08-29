package com.fansfunding.fan.message.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.fansfunding.fan.R;

/**
 * Created by RJzz on 2016/8/26.
 */

public class NetWorkStatusReceiver extends BroadcastReceiver {
    private static final String TAG = NetWorkStatusReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "网络状态改变");
        boolean success = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //获取wifi网络连接状态
        NetworkInfo.State wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        //获取GPRS网络连接状态
        NetworkInfo.State mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();

        //判断是否正在使用网络
        if(NetworkInfo.State.CONNECTED == wifi || NetworkInfo.State.CONNECTED == mobile) {
            success = true;
        }

        if(!success) {
            Toast.makeText(context, context.getString(R.string.network_state), Toast.LENGTH_LONG).show();
        }

    }
}
