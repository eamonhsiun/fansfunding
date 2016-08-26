package com.fansfunding.fan.message.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;

/**
 * Created by RJzz on 2016/8/25.
 */
public class PushService extends Service {
    //连接客户端
    private WebSocketClient client;
    //连接协议
    private Draft_17 draft_17;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("PushService", "PushService created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("PushService", "PushService onStartCommand");


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("PushService", "PushService onDestory");
    }
}
