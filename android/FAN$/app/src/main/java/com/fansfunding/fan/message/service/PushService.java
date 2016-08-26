package com.fansfunding.fan.message.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.fansfunding.fan.R;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by RJzz on 2016/8/25.
 */
public class PushService extends Service {
    private final int CONNECTION_CLOSE = 100;

    private final String TAG = "WebSocket";
    //连接客户端
    private WebSocketClient client;
    //连接协议
    private Draft_17 draft_17 = new Draft_17();

    //用户id
    private int id;

    //token
    private String token;

    private WebSocketBinder webSocketBinder = new WebSocketBinder();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CONNECTION_CLOSE:
                    if(webSocketBinder != null){
                        webSocketBinder.reConnection();
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };



    public class WebSocketBinder extends Binder {
        //开始第一次连接
        public void startConnection() {
            Log.d("PushService", "PushService onStartCommand");
            if(client == null) {
                try {
                    client = new WebSocketClient(new URI("ws://api.immortalfans.com:8080/websocket?userId=" + id  + "&token=" + token), new Draft_17()) {
                        @Override
                        public void onOpen(ServerHandshake serverHandshake) {
                            Log.d(TAG, "连接成功");
                        }

                        @Override
                        public void onMessage(String s) {
                            Log.d(TAG, "接收消息" + s);
                        }

                        @Override
                        public void onClose(int i, String s, boolean b) {
                            Log.d(TAG, "连接关闭");
                            //如果连接关闭则进行重连
                            reConnection();
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.d(TAG, "连接失败");
                            reConnection();
                        }
                    };
                    client.connect();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

            }
        }


        //断线时候进行重连
        public void reConnection() {
            try {
                WebSocketClient clientReconnection = new WebSocketClient(new URI("ws://api.immortalfans.com:8080/websocket?userId=" + id  + "&token=" + token), new Draft_17()) {
                    @Override
                    public void onOpen(ServerHandshake serverHandshake) {
                        Log.d(TAG, "重新连接成功");
                    }

                    @Override
                    public void onMessage(String s) {
                        Log.d(TAG, "接收消息" + s);
                    }

                    @Override
                    public void onClose(int i, String s, boolean b) {
                        Log.d(TAG, "重新连接关闭");
                        //如果重新连接也失败了，那么就等待5秒时候继续连接，直到连接成功。
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(5000);
                                    handler.sendEmptyMessage(CONNECTION_CLOSE);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.d(TAG, "重新连接失败");
                        //如果重新连接也失败了，那么就等待5秒时候继续连接，直到连接成功。
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(5000);
                                    handler.sendEmptyMessage(CONNECTION_CLOSE);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                };
                clientReconnection.connect();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return webSocketBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //请求需要的id和token
        SharedPreferences share = getSharedPreferences(getString(R.string.sharepreference_login_by_phone), MODE_PRIVATE);
        id = share.getInt("id", 0);
        token = share.getString("token","");
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
