package com.fansfunding.fan.message.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.fansfunding.app.App;
import com.fansfunding.fan.R;
import com.fansfunding.fan.message.adapter.LetterAdapter;
import com.fansfunding.fan.message.entity.PrivateLetter;
import com.fansfunding.fan.message.model.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by RJzz on 2016/8/26.
 */

public class PrivateLetterFragment extends Fragment {
    private App app;

    private int userId;

    private ListView listView;

    public static LetterAdapter letterAdapter;

    public static List<Message> messages = new ArrayList<>();

    //未读条数
    public static TextView unreadMsg;

    //清空私聊
    private ImageButton imageButton;


    private static final int UPDATE_UI = 100;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case UPDATE_UI:
                    int i  = new Select().from(Message.class).where("isRead = ? and userId  = ?", 0, userId).count();
                    unreadMsg.setText(i + "");
                    letterAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_private_letter, container, false);
        unreadMsg = (TextView) rootView.findViewById(R.id.tv_message_letter_not_read);
        final int i  = new Select().from(Message.class).where("isRead = ? and userId = ?", 0, userId).count();
        unreadMsg.setText(i + "");
        listView = (ListView) rootView.findViewById(R.id.lv_messag_letter);
        imageButton = (ImageButton) rootView.findViewById(R.id.ib_message_letter);
        letterAdapter = new LetterAdapter(getContext(), R.layout.item_letter, messages);
        listView.setAdapter(letterAdapter);
        letterAdapter.notifyDataSetChanged();


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int size = messages.size();
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("是否清空私聊通知");
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActiveAndroid.beginTransaction();
                        try{
                            for(int i = 0; i < size; ++i) {
                                Message message = Message.load(Message.class, messages.get(i).getId());
                                message.setWillDelete(true);
                                message.setRead(true);
                                message.save();
                            }
                            ActiveAndroid.setTransactionSuccessful();
                        }finally {
                            int d = 0;
                            for(int i = 0; i < messages.size(); ++i) {
                               if(!messages.get(i).getRead()) {
                                   ++d;
                               }
                            }
                            app.getBadgeView().decrementBadgeCount(d);
                            messages.clear();
                            handler.sendEmptyMessage(UPDATE_UI);
                            ActiveAndroid.endTransaction();
                        }
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Message message = messages.get(position);
                Gson gson = new GsonBuilder().create();
                PrivateLetter privateLetter = new PrivateLetter();
                privateLetter = gson.fromJson(message.getJson(), privateLetter.getClass());
                if(!message.getRead()) {
                    app.getBadgeView().decrementBadgeCount(1);
                    message.setRead(true);
                    view.setBackgroundResource(R.color.colorDividerGrey);
                    //更新数据库
                    Message nb = Message.load(Message.class, message.getId());
                    nb.setRead(true);
                    nb.save();
                    handler.sendEmptyMessage(UPDATE_UI);
                }

                Intent intent = new Intent();
                intent.setAction(getString(R.string.activity_chat));
                intent.putExtra("senderId",  message.getSenderId());
                startActivity(intent);
            }
        });



        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.sharepreference_login_by_phone), Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("id", 0);
        app = (App)getActivity().getApplication();
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.sharepreference_login_by_phone), Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("id", 0);
    }
}
