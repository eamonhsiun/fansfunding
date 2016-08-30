package com.fansfunding.fan.message.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Update;
import com.fansfunding.app.App;
import com.fansfunding.fan.R;
import com.fansfunding.fan.message.adapter.CommentsAdapter;
import com.fansfunding.fan.message.model.Comments;

import java.util.List;

/**
 * Created by RJzz on 2016/8/26.
 */

public class CommentFragment extends Fragment {
    private App app;

    private ListView listView;

    private TextView notRead;

    public static List<Comments> commentses;

    public static CommentsAdapter commentsAdapter;

    private ImageButton imageButton;

    private static final int UPDATE_UI = 100;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_UI:
                    notRead.setText(commentses.size() + "");
                    commentsAdapter.notifyDataSetChanged();
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
        View rootView = inflater.inflate(R.layout.fragment_comment, container, false);
        notRead = (TextView) rootView.findViewById(R.id.tv_message_comment_not_read);
        imageButton = (ImageButton) rootView.findViewById(R.id.ib_message_comment);
        listView = (ListView) rootView.findViewById(R.id.lv_message_comment);
        commentsAdapter = new CommentsAdapter(getContext(), R.layout.item_comment_push, commentses);
        notRead.setText(commentses.size() + "");
        listView.setAdapter(commentsAdapter);
        app = (App)getActivity().getApplication();
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder  alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("是否清空评论消息");
                alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                ActiveAndroid.beginTransaction();
                                try {
                                    for(Comments comments : commentses) {
                                        new Update(Comments.class).set("isRead = ?", 1).where("id = ?", comments.getId()).execute();
                                        app.getBadgeView().decrementBadgeCount(commentses.size());
                                    }
                                }finally {
                                    ActiveAndroid.endTransaction();
                                }
                            }
                        }).start();
                        commentses.clear();
                        handler.sendEmptyMessage(UPDATE_UI);
                    }
                });
                alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
            }
        });
        return rootView;
    }
}
