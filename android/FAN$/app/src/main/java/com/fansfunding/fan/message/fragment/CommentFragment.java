package com.fansfunding.fan.message.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.fansfunding.fan.message.adapter.CommentsAdapter;
import com.fansfunding.fan.message.entity.CommentDynamic;
import com.fansfunding.fan.message.entity.CommentsProject;
import com.fansfunding.fan.message.model.Comments;
import com.fansfunding.fan.project.activity.ProjectCommentActivity;
import com.fansfunding.fan.social.activity.MomentActivity;
import com.fansfunding.internal.ProjectInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RJzz on 2016/8/26.
 */

public class CommentFragment extends Fragment {
    //userId
    private int userId;

    private App app;

    private ListView listView;

    private TextView notRead;

    public static List<Comments> commentses = new ArrayList<>();

    public static CommentsAdapter commentsAdapter;

    private ImageButton imageButton;

    private static final int UPDATE_UI = 100;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_UI:
                    int i  = new Select().from(Comments.class).where("isRead = ? and userId  = ?", 0, userId).count();
                    notRead.setText(i + "");
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
        int i = new Select().from(Comments.class).where("isRead = 0 and userId = ?", userId).count();
        notRead.setText(i + "");
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
                        int d = 0;
                        ActiveAndroid.beginTransaction();
                        try {
                            d = new Select().from(Comments.class).where("isRead = 0").count();
                            for(int i = 0; i < commentses.size(); ++i) {
                                //更新数据库
                                Comments c = Comments.load(Comments.class, commentses.get(i).getId());
                                c.setRead(true);
                                c.setWillDelete(true);
                                c.save();
                            }
                            ActiveAndroid.setTransactionSuccessful();
                        } finally {
                            app.getBadgeView().decrementBadgeCount(d);
                            commentses.clear();
                            handler.sendEmptyMessage(UPDATE_UI);
                            ActiveAndroid.endTransaction();
                        }
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                final Comments comments = commentses.get(position);
                CommentsProject commentsProject = new CommentsProject();
                CommentDynamic commentDynamic = new CommentDynamic();
                Gson gson = new GsonBuilder().create();
                switch (comments.getType()) {
                    case 1:
                        commentsProject = gson.fromJson(comments.getJson(), commentsProject.getClass());
                        break;
                    case 2:
                        commentDynamic = gson.fromJson(comments.getJson(), commentDynamic.getClass());
                        break;
                    default:
                        break;
                }
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                final CommentsProject finalCommentsProject = commentsProject;
                final CommentDynamic finalCommentDynamic = commentDynamic;
                dialog.setItems(new String[]{"       回复评论", "       查看动态"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                switch (comments.getType()) {
                                    //项目评论
                                    case 1:
                                        Intent intent=new Intent();
                                        //打开评论页
                                        intent.setAction(getString(R.string.activity_project_comment));
                                        intent.putExtra("categoryId", finalCommentsProject.getPointTo().getCategoryId());
                                        intent.putExtra("projectId",finalCommentsProject.getPointTo().getId());
                                        intent.putExtra("pointTo",finalCommentsProject.getCommenter().getId());
                                        intent.putExtra("pointToNickname",finalCommentsProject.getCommenter().getNickname());
                                        intent.putExtra("mode", ProjectCommentActivity.SEND_PROJECT_COMMENT);
                                        startActivityForResult(intent, 300);
                                        //没有读过,小红点数量减一
                                        if(!comments.isRead()) {
                                            comments.setRead(true);
                                            app.getBadgeView().decrementBadgeCount(1);
                                            //将此通知标记为已读
                                            //更新数据库
                                            Comments c = Comments.load(Comments.class, comments.getId());
                                            c.setRead(true);
                                            c.save();
                                            view.setBackgroundResource(R.color.colorDividerGrey);
                                            handler.sendEmptyMessage(UPDATE_UI);
                                        }
                                        break;
                                    //动态评论
                                    case 2:
                                        Intent intent1=new Intent();
                                        intent1.setAction(getString(R.string.activity_project_comment));
                                        intent1.putExtra("momentId",finalCommentDynamic.getPointTo().getMomentId());
                                        intent1.putExtra("pointTo",finalCommentDynamic.getCommenter().getId());
                                        intent1.putExtra("pointToNickname",finalCommentDynamic.getCommenter().getNickname());
                                        intent1.putExtra("mode", ProjectCommentActivity.SEND_MOMENT_COMMENT);
                                        startActivityForResult(intent1 ,ProjectCommentActivity.REQUESR_CODE_SEND_COMMENT_ACTIVITY);
                                        //没有读过,小红点数量减一
                                        if(!comments.isRead()) {
                                            comments.setRead(true);
                                            app.getBadgeView().decrementBadgeCount(1);
                                            //将此通知标记为已读
                                            //更新数据库
                                            Comments c = Comments.load(Comments.class, comments.getId());
                                            c.setRead(true);
                                            c.save();
                                            view.setBackgroundResource(R.color.colorDividerGrey);
                                            handler.sendEmptyMessage(UPDATE_UI);
                                        }
                                        break;
                                     default:
                                         break;
                                }


                                break;
                            case 1:

                                switch (comments.getType()) {
                                    //跳转到相关的项目
                                    case 1:
                                        //跳转到相关的项目详情页
                                        Intent intent = new Intent();
                                        ProjectInfo detail = finalCommentsProject.getPointTo();
                                        intent.setAction(getString(R.string.activity_project_detail));
                                        intent.putExtra("detail",detail);
                                        intent.putExtra("page", 1);
                                        startActivityForResult(intent, 1002);
//                                        //删除当前这条数据
//                                        commentses.remove(position);
                                        //没有读过,小红点数量减一
                                        if(!comments.isRead()) {
                                            comments.setRead(true);
                                            app.getBadgeView().decrementBadgeCount(1);
                                            //将此通知标记为已读
                                            //更新数据库
                                            Comments c = Comments.load(Comments.class, comments.getId());
                                            c.setRead(true);
                                            c.save();
                                            view.setBackgroundResource(R.color.colorDividerGrey);
                                            handler.sendEmptyMessage(UPDATE_UI);
                                        }
                                        break;
                                    case 2:
                                        Intent intentD = new Intent();
                                        intentD.putExtra(MomentActivity.MOMENTID, finalCommentDynamic.getPointTo().getMomentId());
                                        intentD.setAction(app.getApplicationContext().getString(R.string.activity_moment));
                                        startActivityForResult(intentD, 1002);
//                                        //删除当前这条数据
//                                        commentses.remove(position);

                                        //没有读过,小红点数量减一
                                        if(!comments.isRead()) {
                                            comments.setRead(true);
                                            app.getBadgeView().decrementBadgeCount(1);
                                            //将此通知标记为已读
                                            //更新数据库
                                            Comments c = Comments.load(Comments.class, comments.getId());
                                            c.setRead(true);
                                            c.save();
                                            view.setBackgroundResource(R.color.colorDividerGrey);
                                            handler.sendEmptyMessage(UPDATE_UI);
                                        }
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            default:
                                break;
                        }
                    }
                });
                dialog.show();

            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.sharepreference_login_by_phone), Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("id", 0);
        handler.sendEmptyMessage(UPDATE_UI);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1002) {
            handler.sendEmptyMessage(UPDATE_UI);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.sharepreference_login_by_phone), Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("id", 0);
    }
}
