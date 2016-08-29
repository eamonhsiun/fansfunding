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
import com.fansfunding.fan.message.entity.NotificationProject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by RJzz on 2016/8/25.
 */
public class PushService extends Service {
    //连接关闭或失败
    private final int CONNECTION_CLOSE = 100;

    //返回Json数据解析出错
    private final int RESPONSE_ERROR = 101;

    //返回Json数据解析成功
    private final int RESPONSE_SUCCESS = 102;

    //消息类型0:请求反馈
    private final int TYPE_ZERO = 103;

    //消息类型1：私聊消息
    private final int TYPE_ONE = 104;

    //消息类型2：评论消息
    private final int TYPE_TWO = 105;

    //消息类型3：评论消息
    private final int TYPE_THREE = 106;

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
                case RESPONSE_ERROR:
                    Log.e(TAG, "解析Json失败");
                    break;
                case RESPONSE_SUCCESS:
                    Log.d(TAG, "解析Json成功");
                    break;
                case TYPE_THREE:
                    Log.d(TAG, "开始处理通知消息");
                    String data = (String) msg.obj;
                    notificationHandling(data);
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };



    public class WebSocketBinder extends Binder {

      /**
        *开始第一次连接
        *@author RJzz
        *create at 2016/8/27 11:04
        */
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
                            analysisJson(s);
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



        /**
          *断线
          *@author RJzz
          *create at 2016/8/27 10:59
         * @param
          */


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
                        analysisJson(s);
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


    /**
      *解析Json
      *@author RJzz
      *create at 2016/8/28 10:40
      */
    public void analysisJson(String s) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(s);
            boolean result = jsonObject.getBoolean("result");
            int statusCode = jsonObject.getInt("statusCode");
            int type = jsonObject.getInt("type");
            String data = jsonObject.getString("data");
            Log.d(TAG, data);
            if(result && statusCode == 200) {
                handler.sendEmptyMessage(RESPONSE_SUCCESS);
                Message m = new Message();
                m.obj = data;
                switch (type) {
                    //请求反馈
                    case 0:
                        break;
                    //私聊消息
                    case 1:
                        break;
                    //评论消息
                    case 2:
                        break;
                    //通知消息
                    case 3:
                        m.what = TYPE_THREE;
                        handler.sendMessage(m);
                        break;
                    default:
                        break;
                }
            }

            handler.sendEmptyMessage(RESPONSE_SUCCESS);
        } catch (JSONException e) {
            handler.sendEmptyMessage(RESPONSE_ERROR);
            e.printStackTrace();
        }
    }
    
    /**
      *处理通知消息
      *@author RJzz
      *create at 2016/8/28 10:42
      */
    public void notificationHandling(String s) {
        try {
            JSONObject j = new JSONObject(s);
            //通知消息的类型
            int type = j.getInt("type");

            Gson gson = new GsonBuilder().create();
            switch (type) {
                case 1:
                case 3:
                    break;
                case 2:
                case 4:
                case 5:
                case 6:
                    NotificationProject nP = new NotificationProject();
                    try {
                        if((nP = gson.fromJson(s, nP.getClass())) == null) {
                            handler.sendEmptyMessage(RESPONSE_ERROR);
                            return;
                        }
                        Log.d(TAG, nP.getCauser().getHead());
                    }catch (IllegalStateException e){
                        handler.sendEmptyMessage(RESPONSE_ERROR);
                        e.printStackTrace();
                    }catch (JsonSyntaxException e){
                        handler.sendEmptyMessage(RESPONSE_ERROR);
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
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
