package com.fansfunding.fan.message.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.fansfunding.app.App;
import com.fansfunding.fan.MainActivity;
import com.fansfunding.fan.R;
import com.fansfunding.fan.message.entity.NotificationProject;
import com.fansfunding.fan.message.model.Notifications;
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

import static com.fansfunding.fan.message.fragment.NotifacationFragment.notificationAdapter;
import static com.fansfunding.fan.message.fragment.NotifacationFragment.notificationses;

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

    //判断用户是否登陆
    private boolean isLogin;

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
                    break;
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
                            Log.d(TAG, "连接失败");
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
     * type 1 3 动态
     * type 2 4 5 6 项目
      *@author RJzz
      *create at 2016/8/28 10:42
      */
    public void notificationHandling(String s) {
        try {
            JSONObject json = new JSONObject(s);
            //通知消息的类型
            int type = json.getInt("type");
            long time = json.getLong("time");
            String causer = json.getString("causer");
            String reference = json.getString("reference");


            switch (type) {
                case 1:
                case 3:
                    break;
                case 2:
                case 4:
                    push(s, "关注了你的项目");
                    break;
                case 5:
                    push(s, "关注了你");
                    break;
                case 6:
                    push(s, "更新了项目动态");
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void insertToTable(String s) {
        JSONObject json = null;
        try {
            json = new JSONObject(s);
            //通知消息的类型
            int type = json.getInt("type");
            long time = json.getLong("time");
            String causer = json.getString("causer");
            String reference = json.getString("reference");
            Notifications n = new Notifications();
            n.setTime(time);
            n.setCauser(causer);
            n.setReference(reference);
            n.setType(type);
            n.setRead(false);
            n.setJson(s);
            n.save();
            //更新ui
            notificationses.add(0, n);
            if(notificationAdapter != null) {
                notificationAdapter.notifyDataSetChanged();
            }
            //小红点嘿嘿嘿+1
            App app = (App)getApplication();
            app.getBadgeView().incrementBadgeCount(1);
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
        isLogin = share.getBoolean("isLogin",false);
        Log.d("PushService", "PushService created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("PushService", "PushService onStartCommand");
        return  Service.START_STICKY;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("PushService", "PushService onDestory");
    }

    //通知栏提醒
    public void push(String s, String info) {
        Gson gson = new GsonBuilder().create();
        NotificationProject nP = new NotificationProject();
        try {
            if((nP = gson.fromJson(s, nP.getClass())) == null) {
                handler.sendEmptyMessage(RESPONSE_ERROR);
                return;
            }
            insertToTable(s);

            Log.d(TAG, "项目相关通知插入通知表成功");

        }catch (IllegalStateException e){
            handler.sendEmptyMessage(RESPONSE_ERROR);
            e.printStackTrace();
        }catch (JsonSyntaxException e){
            handler.sendEmptyMessage(RESPONSE_ERROR);
            e.printStackTrace();
        }
        App app = (App)getApplication();
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(App.getContext());
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("push", 2);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        Notification notification = builder
                .setContentTitle(nP.getCauser().getNickname())
                .setContentText(info)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.icon)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon))
                .setContentIntent(pi)
                .setDefaults(Notification.DEFAULT_ALL)
                .build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        manager.notify((int) System.currentTimeMillis(), notification);
    }
 }
