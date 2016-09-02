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

import com.activeandroid.query.Select;
import com.fansfunding.app.App;
import com.fansfunding.fan.MainActivity;
import com.fansfunding.fan.R;
import com.fansfunding.fan.message.entity.CommentDynamic;
import com.fansfunding.fan.message.entity.CommentsProject;
import com.fansfunding.fan.message.entity.NotificationDynamic;
import com.fansfunding.fan.message.entity.NotificationProject;
import com.fansfunding.fan.message.entity.PrivateLetter;
import com.fansfunding.fan.message.model.Comments;
import com.fansfunding.fan.message.model.Content;
import com.fansfunding.fan.message.model.Notifications;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static com.fansfunding.fan.message.activity.ChatActivity.chatReceiverId;
import static com.fansfunding.fan.message.activity.ChatActivity.contentList;
import static com.fansfunding.fan.message.activity.ChatActivity.listView;
import static com.fansfunding.fan.message.activity.ChatActivity.msgAdapter;
import static com.fansfunding.fan.message.fragment.CommentFragment.commentsAdapter;
import static com.fansfunding.fan.message.fragment.CommentFragment.commentses;
import static com.fansfunding.fan.message.fragment.CommentFragment.notRead;
import static com.fansfunding.fan.message.fragment.NotifacationFragment.notificationAdapter;
import static com.fansfunding.fan.message.fragment.NotifacationFragment.notificationses;
import static com.fansfunding.fan.message.fragment.NotifacationFragment.unreadNt;
import static com.fansfunding.fan.message.fragment.PrivateLetterFragment.letterAdapter;
import static com.fansfunding.fan.message.fragment.PrivateLetterFragment.messages;
import static com.fansfunding.fan.message.fragment.PrivateLetterFragment.unreadMsg;

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
    public static WebSocketClient client;
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
                    String dataNotificaition = (String) msg.obj;
                    notificationHandling(dataNotificaition);

                    break;
                case TYPE_TWO:
                    Log.d(TAG, "开始处理评论消息");
                    String dataComment = (String) msg.obj;
                    commentHandling(dataComment);
                    break;
                case TYPE_ONE:
                    Log.d(TAG, "开始处理私聊消息");
                    String dataPrivateLetter = (String) msg.obj;
                    privateLetterHanding(dataPrivateLetter);

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
                            //重新获取id
                            SharedPreferences share = getSharedPreferences(getString(R.string.sharepreference_login_by_phone), MODE_PRIVATE);
                            id = share.getInt("id", 0);
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
                    client  = new WebSocketClient(new URI("ws://api.immortalfans.com:8080/websocket?userId=" + id  + "&token=" + token), new Draft_17()) {
                    @Override
                    public void onOpen(ServerHandshake serverHandshake) {
                        Log.d(TAG, "重新连接成功");
                    }

                    @Override
                    public void onMessage(String s) {
                        Log.d(TAG, "接收消息" + s);
                        //重新获取id
                        SharedPreferences share = getSharedPreferences(getString(R.string.sharepreference_login_by_phone), MODE_PRIVATE);
                        id = share.getInt("id", 0);
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
                client.connect();
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
                        m.what = TYPE_ONE;
                        handler.sendMessage(m);
                        break;
                    //评论消息
                    case 2:
                        m.what = TYPE_TWO;
                        handler.sendMessage(m);
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
      *处理私聊消息
      *@author RJzz
      *create at 2016/9/1 10:02
      */
    public void privateLetterHanding(String s) {
        Gson gson = new GsonBuilder().create();
        PrivateLetter pl = new PrivateLetter();
        pl = gson.fromJson(s, pl.getClass());
        String title = "FAN$";
        String text = "您有新的私信消息";
        insertToMessages(s);
        push(title, text, 1, 1);
    }

    /**
      *处理评论消息
     * type 1 项目评论
     * type 2 动态评论
      *@author RJzz
      *create at 2016/8/30 1:14
      */
    public void commentHandling(String s) {
        try {
            JSONObject json = new JSONObject(s);
            int type = json.getInt("type");
            Gson gson = new GsonBuilder().create();
            CommentsProject c = new CommentsProject();
            CommentDynamic commentDynamic = new CommentDynamic();
            String title = "";
            String text = "";
            if(type == 1) {
                c = gson.fromJson(s, c.getClass());
            }else {
                commentDynamic = gson.fromJson(s, commentDynamic.getClass());
            }
            switch (type) {
                //项目评论
                case 1:
                    title = c.getCommenter().getNickname();
                    text = "评论了你的项目:" + c.getPointTo().getName();
                    break;
                //动态评论
                case 2:
                    title = commentDynamic.getCommenter().getNickname();
                    text = "评论了你的动态" + commentDynamic.getPointTo().getContent();
                    break;
                default:
                    break;
            }
            insertToComment(s);
            push(title, text, 2, type);
        } catch (JSONException e) {
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
            Gson gson = new GsonBuilder().create();
            NotificationProject n = new NotificationProject();
            NotificationDynamic notificationDynamic = new NotificationDynamic();
            if(type == 1 || type == 3) {
                notificationDynamic = gson.fromJson(s, notificationDynamic.getClass());
            } else {
                n = gson.fromJson(s, NotificationProject.class);
            }
            //通知的标题
            String title = "";
            //通知的内容
            String text = "";
            switch (type) {
                case 1:
                    title = notificationDynamic.getCauser().getNickname();
                    text = "赞了你的动态";
                    break;
                case 3:
                    break;
                case 2:
                    break;
                case 4:
                    title = n.getCauser().getNickname();
                    text = "关注了你的项目" + n.getReference().getName();
                    break;
                case 5:
                    title = n.getCauser().getNickname();
                    text = "关注了你";
                    break;
                case 6:
                    title = n.getCauser().getNickname();
                    text = "更新了项目" + n.getReference().getName();
                    break;
                default:
                    break;
            }
            insertToNotification(s);
            push(title, text, 3, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
      *插入通知评论表中
      *@author RJzz
      *create at 2016/9/1 10:13
      */
    public void insertToComment(String s) {
        JSONObject json = null;
        try {
            json = new JSONObject(s);
            //评论类型的消息
            int type = json.getInt("type");
            long time = json.getLong("time");
            String comment = json.getString("comment");
            String commenter = json.getString("commenter");
            String pointTo = json.getString("pointTo");

            Comments n = new Comments();
            n.setTime(time);
            n.setComment(comment);
            n.setCommenter(commenter);
            n.setPointTo(pointTo);
            n.setType(type);
            n.setRead(false);
            n.setJson(s);
            n.setWillDelete(false);
            n.setUserId(id);
            n.save();
            Log.d(TAG, "插入评论表成功");
            //更新ui
            commentses.add(0, n);
            if(commentsAdapter != null) {
                int i  = new Select().from(Comments.class).where("isRead = ? and userId  = ?", 0, id).count();
                notRead.setText(i + "");
                commentsAdapter.notifyDataSetChanged();
            }
            //小红点嘿嘿嘿+1
            App app = (App)getApplication();
            app.getBadgeView().incrementBadgeCount(1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    
    /**
      *插入私聊消息表中
      *@author RJzz
      *create at 2016/9/1 10:19
      */
    public void insertToMessages(String s) {
        Gson gson = new GsonBuilder().create();
        PrivateLetter p = new PrivateLetter();
        p = gson.fromJson(s, p.getClass());
       List<com.fansfunding.fan.message.model.Message> messageList =  new Select().from(com.fansfunding.fan.message.model.Message.class).where("userId = ? and senderId = ?", id, p.getSender().getId()).execute();

        //还没有接受过这个人的消息
        if(messageList.size() == 0) {
            com.fansfunding.fan.message.model.Message message = new com.fansfunding.fan.message.model.Message();
            message.setSenderId(p.getSender().getId());
            message.setJson(s);
            message.setUserId(id);
            message.setTime(p.getSendTime());
            message.setRead(false);
            message.setWillDelete(false);
            Content content = new Content();
            content.setContent(p.getContent());
            //1为我的消息，2为对方的消息
            content.setType(2);
            content.setTime(p.getSendTime());
            message.save();
            content.setMessage(message);
            content.save();
            //小红点嘿嘿嘿+1
            App app = (App)getApplication();
            //私信条目加一
            messages.add(message);
            app.getBadgeView().incrementBadgeCount(1);
            if(letterAdapter != null) {
                int i  = new Select().from(com.fansfunding.fan.message.model.Message.class).where("isRead = ? and userId  = ?", 0, id).count();
                unreadMsg.setText(i + "");
                letterAdapter.notifyDataSetChanged();
            }
            //聊天的界面,需要判断是不是当前对话的user
            if(chatReceiverId == p.getSender().getId()) {
                if(msgAdapter != null) {
                    contentList.add(content);
                    msgAdapter.notifyDataSetChanged();
                    listView.setSelection(contentList.size());
                }
            }
        }else {
            //已经插入了一条数据
            //小红点嘿嘿嘿+1
            App app = (App)getApplication();
            //被设置为将要被删除
            if(messageList.get(0).getWillDelete() && messageList.get(0).getRead()) {
                messageList.get(0).setWillDelete(false);
                messages.add(0,  messageList.get(0));
                app.getBadgeView().incrementBadgeCount(1);
            } else if(messageList.get(0).getRead()){
                //如果已经读过了但是没有被设置为删除小红点加一
                app.getBadgeView().incrementBadgeCount(1);
            }
            Content content = new Content();
            content.setMessage(messageList.get(0));
            content.setContent(p.getContent());
            //1为我的消息，2为对方的消息
            content.setType(2);
            content.setTime(p.getSendTime());
            messageList.get(0).setTime(p.getSendTime());
            messageList.get(0).setRead(false);
            messageList.get(0).setJson(s);
            messageList.get(0).save();
            content.save();

            //通知的界面
            if(letterAdapter != null) {
                int i  = new Select().from(com.fansfunding.fan.message.model.Message.class).where("isRead = ? and userId  = ?", 0, id).count();
                unreadMsg.setText(i + "");
                letterAdapter.notifyDataSetChanged();
            }
            //聊天的界面,需要判断是不是当前对话的user
            if(chatReceiverId == p.getSender().getId()) {
                if(msgAdapter != null) {
                    contentList.add(content);
                    msgAdapter.notifyDataSetChanged();
                    listView.setSelection(contentList.size());
                }
            }

        }


    }
 

    /**
      *将接收到的未读通知插入表中
      *@author RJzz
      *create at 2016/8/30 1:31
      */
    public void insertToNotification(String s) {
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
            n.setWillDelete(false);
            n.setUserId(id);
            n.save();
            Log.d(TAG, "插入通知表成功");
            //更新ui
            notificationses.add(0, n);
            if(notificationAdapter != null) {
                notificationAdapter.notifyDataSetChanged();
                int i  = new Select().from(Notifications.class).where("isRead = ? and userId  = ?", 0, id).count();
                unreadNt.setText(i + "");
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
    public void push(String title, String text, int typeFather, int typeSon) {
        App app = (App)getApplication();
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(App.getContext());
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("push", 2);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        Notification notification = builder
                .setContentTitle(title)
                .setContentText(text)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.icon_small)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon))
                .setContentIntent(pi)
                .setDefaults(Notification.DEFAULT_ALL)
                .build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        
        if(typeFather == 1) {
            manager.notify(id, notification);
        }else {
            manager.notify((int) System.currentTimeMillis(), notification);
        }

    }
 }
