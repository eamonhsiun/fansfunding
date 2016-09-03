package com.fansfunding.fan.message.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.fansfunding.fan.R;
import com.fansfunding.fan.message.adapter.MsgAdapter;
import com.fansfunding.fan.message.entity.PrivateLetter;
import com.fansfunding.fan.message.model.Content;
import com.fansfunding.fan.message.model.Message;
import com.fansfunding.internal.PersonalInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import static com.fansfunding.fan.message.service.PushService.client;

/**
 * Created by RJzz on 2016/9/1.
 */

public class ChatActivity extends AppCompatActivity {



    //我的id
    public static  int chatUserId;

    //接收消息者的id
    public static int chatReceiverId = 0;

    private Toolbar toolbar;

    //聊天的人
    private TextView name;

    //发送消息
    private ImageButton send;

    //聊天昵称
    private PersonalInfo personalInfo;
//
//    //为了键盘弹出的时候将所有的全部顶上去
//    private ScrollView scroller;


    public static ListView listView;

    PrivateLetter privateLetter = new PrivateLetter();

    public static List<Message> message1;

    private EditText input;

    public static MsgAdapter msgAdapter;

    public static List<Content> contentList = new ArrayList<>();

    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        analysisJson();
        initData();
        Log.d("半夜4点", chatReceiverId + "");
    }


    public String send(int receiverId) {
        message1 = new Select().from(Message.class).where("userId = ? and senderId = ?", chatUserId, chatReceiverId).execute();
        //如果是第一次发送本地不存在数据的话
        if(message1.size() == 0) {
            com.fansfunding.fan.message.model.Message message = new com.fansfunding.fan.message.model.Message();
            message.setSenderId(chatReceiverId);
            message.setUserId(chatUserId);
            message.setTime(System.currentTimeMillis());
            message.setRead(false);
            message.setJson(getPersonJson(input.getText() + ""));
            message.setWillDelete(false);
            Content content = new Content();
            content.setContent(input.getText() + "");
            //1为我的消息，2为对方的消息
            content.setType(1);
            content.setTime(System.currentTimeMillis());
            message.save();
            content.setMessage(message);
            content.save();
            contentList.add(content);
        }else {
            //现在数据库里面已经有数据了

            Content content = new Content();
            if(message1.size() != 0) {
                content.setMessage(message1.get(0));
            }
            content.setType(1);
            content.setTime(System.currentTimeMillis());
            content.setContent(input.getText() + "");
            content.save();
            contentList.add(content);
        }
        msgAdapter.notifyDataSetChanged();
        return "{\"receiver\":" + receiverId  + "," + "\"content\":" +  "\"" + input.getText() + "" + "\"}";
    }


    public void initData() {

        toolbar = (Toolbar) findViewById(R.id.toolbar_chat);
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    default:
                        break;
                }
                return false;
            }
        });
        toolbar.inflateMenu(R.menu.menu_chat);
        name = (TextView) toolbar.findViewById(R.id.tv_chat_name);
        listView = (ListView) findViewById(R.id.lv_chat);
        input = (EditText) findViewById(R.id.et_chat);
        send = (ImageButton) findViewById(R.id.btn_chat_send);
//        scroller = (ScrollView) findViewById(R.id.sv_chat);
        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listView.setSelection(contentList.size());
                    }
                }, 50);

            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(input.length() == 0) {
                    Toast.makeText(ChatActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
                }else {
                    if(client != null && client.getConnection() != null) {
                        client.send(send(chatReceiverId));
                        input.setText("");
                        listView.setSelection(contentList.size());
                    }
                        else {
                            Toast.makeText(ChatActivity.this, "网络好像不稳定呢", Toast.LENGTH_SHORT).show();
                        }

                }
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        scroller.fullScroll(View.FOCUS_DOWN);
//                    }
//                }, 100);

            }
        });
        if(privateLetter.getContent() == null) {
            name.setText(personalInfo.getData().getNickname());
        }else {
            name.setText(privateLetter.getSender().getNickname());
        }

        msgAdapter = new MsgAdapter(ChatActivity.this, R.layout.item_msg, contentList);
        listView.setAdapter(msgAdapter);
        listView.setSelection(contentList.size());
        msgAdapter.notifyDataSetChanged();

    }

    public void analysisJson() {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.sharepreference_login_by_phone), MODE_PRIVATE);
        chatUserId = sharedPreferences.getInt("id", 0);
        Intent intent = getIntent();
        chatReceiverId = intent.getIntExtra("senderId", 0);
        personalInfo = (PersonalInfo) intent.getSerializableExtra("personalInfo");
        message1 = new Select().from(Message.class).where("userId = ? and senderId = ?", chatUserId, chatReceiverId).execute();
        if(message1.size() != 0) {
            if(message1.get(0).contents() != null) {
                contentList = message1.get(0).contents();
                Gson gson = new GsonBuilder().create();
                privateLetter = gson.fromJson(message1.get(0).getJson(), privateLetter.getClass());
            }
        }else{
            if(msgAdapter != null) {
                contentList.clear();
                msgAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

    public String getPersonJson(String content) {
        return
             "{" + "\"sender\":{" +
                "\"head\":"  + "\"" + personalInfo.getData().getHead() +  "\"" + "," +
                "\"intro\":" +  "\""  + personalInfo.getData().getIntro() +  "\""  + "," +
                "\"nickname\":"  +  "\""  + personalInfo.getData().getNickname() + "\""  + "," +
                        "\"id\":" + chatUserId +
            "}," +
            "\"content\":" +  "\"content\"" +  "," +
                    "\"sendTime\":" + System.currentTimeMillis() + "}";


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //干掉当前的线程，销毁静态变量
//        android.os.Process.killProcess(android.os.Process.myPid());
        Log.d("半夜4点","我死了");
    }
}
