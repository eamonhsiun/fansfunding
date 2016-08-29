package com.fansfunding.fan.social.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fansfunding.fan.R;
import com.fansfunding.fan.project.activity.ProjectCommentActivity;
import com.fansfunding.fan.request.RequestPraiseMoment;
import com.fansfunding.fan.utils.ErrorHandler;
import com.fansfunding.fan.utils.FANRequestCode;
import com.fansfunding.fan.utils.MyGridView;
import com.fansfunding.internal.social.UserMoment;
import com.rockerhieu.emojicon.EmojiconTextView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;

/**
 * Created by 13616 on 2016/8/19.
 */
public class SocialListAdapter extends BaseAdapter {

    private boolean isFinishRequest=true;

    private ArrayList<UserMoment.DataBean.ListBean> momentList;

    private OkHttpClient httpClient;

    private Activity context;

    private Fragment fragment;

    private ErrorHandler handler=new ErrorHandler(context){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case FANRequestCode.PRAISE_MOMENT_SUCCESS:
                    for(int i=0;i<momentList.size();i++){
                        if(momentList.get(i).getMomentId()==msg.arg1){
                            momentList.get(i).setLikeNum(momentList.get(i).getLikeNum()+1);
                            momentList.get(i).setIsLike(true);
                            notifyDataSetChanged();
                            break;
                        }
                    }
                    break;
                case FANRequestCode.PRAISE_MOMENT_FAILURE:
                    if(context.isFinishing()==false){
                        Toast.makeText(context,"点赞失败,请稍后重试",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case FANRequestCode.CANCEL_PRAISE_MOMENT_SUCCESS:
                    for(int i=0;i<momentList.size();i++){
                        if(momentList.get(i).getMomentId()==msg.arg1){
                            momentList.get(i).setLikeNum(momentList.get(i).getLikeNum()-1);
                            momentList.get(i).setIsLike(false);
                            notifyDataSetChanged();
                            break;
                        }
                    }
                    break;
                case FANRequestCode.CANCEL_PRAISE_MOMENT_FAILURE:
                    if(context.isFinishing()==false){
                        Toast.makeText(context,"取消赞失败,请稍后重试",Toast.LENGTH_SHORT).show();
                    }
                default:
                super.handleMessage(msg);
            }
            isFinishRequest=true;
        }
    };

    public SocialListAdapter(Activity context){
        this.context=context;
        momentList=new ArrayList<>();
        handler.setContext(context);
        httpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();

    }


    public SocialListAdapter(Activity context,Fragment fragment){
        this.context=context;
        momentList=new ArrayList<>();
        handler.setContext(context);
        httpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();

        this.fragment=fragment;

    }


    public void addItem(UserMoment.DataBean.ListBean moment){
        if(moment==null){
            return;
        }
        momentList.add(moment);

    }
    public void clear(){
        momentList=new ArrayList<>();
    }

    //再用户评论后增加评论数量
    public void resetCommentNumber(int momentId){
        for(int i=0;i<getCount();i++){
            if(momentList.get(i).getMomentId()==momentId){
                momentList.get(i).setCommentNum(momentList.get(i).getCommentNum()+1);
                notifyDataSetChanged();
            }
        }
    }
    @Override
    public int getCount() {
        return momentList.size();
    }

    @Override
    public UserMoment.DataBean.ListBean getItem(int position) {
        if(position> momentList.size()||position<0){
            return null;
        }
        return momentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        if(position<0||position>= momentList.size()){
            return null;
        }
        final UserMoment.DataBean.ListBean moment=momentList.get(position);
        final View rootView;
        final ViewHolder viewHolder;
        if(convertView==null){
            rootView  = LayoutInflater.from(context).inflate(R.layout.item_social_moment, null);
            viewHolder = new ViewHolder();

            viewHolder.iv_social_publisher_head=(CircleImageView)rootView.findViewById(R.id.iv_social_publisher_head);

            viewHolder.tv_social_publisher_nickname=(TextView)rootView.findViewById(R.id.tv_social_publisher_nickname);
            viewHolder.tv_social_publish_time=(TextView)rootView.findViewById(R.id.tv_social_publish_time) ;
            viewHolder.tv_social_publisher_content=(EmojiconTextView)rootView.findViewById(R.id.tv_social_publisher_content);

            viewHolder.gv_social_photos=(MyGridView)rootView.findViewById(R.id.gv_social_photos);

            viewHolder.ll_social_project=(LinearLayout)rootView.findViewById(R.id.ll_social_project);
            viewHolder.iv_social_project_cover=(ImageView)rootView.findViewById(R.id.iv_social_project_cover);
            viewHolder.tv_social_project_name=(TextView)rootView.findViewById(R.id.tv_social_project_name);

            viewHolder.ll_social_moment_comment =(LinearLayout) rootView.findViewById(R.id.ll_social_moment_comment);
            viewHolder.ll_social_moment_praise =(LinearLayout) rootView.findViewById(R.id.ll_social_moment_praise);
            viewHolder.tv_social_comment_number =(TextView) rootView.findViewById(R.id.tv_social_comment_number);
            viewHolder.tv_social_praise_number =(TextView) rootView.findViewById(R.id.tv_social_praise_number);
            viewHolder.iv_social_praise_picture=(ImageView) rootView.findViewById(R.id.iv_social_praise_picture);
            rootView.setTag(viewHolder);

        }else{
            rootView = convertView;
            viewHolder = (ViewHolder) rootView.getTag();
        }

        if(moment.getUser().getHead()!=null&&moment.getUser().getHead().equals("")==false){
            Picasso.with(context).load(context.getString(R.string.url_resources)+moment.getUser().getHead()).memoryPolicy(MemoryPolicy.NO_CACHE).into(viewHolder.iv_social_publisher_head);
        }

        //设置动态发起人昵称
        viewHolder.tv_social_publisher_nickname.setText(moment.getUser().getNickname());
        //设置动态发起时间
        viewHolder.tv_social_publish_time.setText(new SimpleDateFormat("MM-dd HH:mm").format(new Date(moment.getPostTime())));
        //设置动态内容
        viewHolder.tv_social_publisher_content.setText(moment.getContent());

        //设置动态图片
        if(moment.getImages()!=null){
            SocialMomentPhotoAdapter adapter=new SocialMomentPhotoAdapter(context);
            for(int i=0;i<moment.getImages().size();i++){
                if(moment.getImages().get(i)!=null&&moment.getImages().get(i).equals("")==false){
                    adapter.addItem(moment.getImages().get(i));
                }
            }
            viewHolder.gv_social_photos.setAdapter(adapter);
            if(adapter.getCount()>1){
                viewHolder.gv_social_photos.setNumColumns(3);
            }else {
                viewHolder.gv_social_photos.setNumColumns(1);
            }
            adapter.notifyDataSetChanged();
        }

        //设置动态附带项目的信息
        if(moment.getProject()==null){
            viewHolder.ll_social_project.setVisibility(View.GONE);
        }else {
            viewHolder.ll_social_project.setVisibility(View.VISIBLE);
            Picasso.with(context).load(context.getString(R.string.url_resources)+moment.getProject().getCover()).resize(70,70).centerCrop().memoryPolicy(MemoryPolicy.NO_CACHE).into(viewHolder.iv_social_project_cover);
            viewHolder.tv_social_project_name.setText(moment.getProject().getName());
            viewHolder.ll_social_project.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent();
                    intent.setAction(context.getString(R.string.activity_project_detail));
                    intent.putExtra("projectId",moment.getProject().getId());
                    intent.putExtra("categoryId",moment.getProject().getCategoryId());
                    context.startActivity(intent);
                }
            });
        }

        //设置动态评论数量
        if(moment.getCommentNum()>0){
            viewHolder.tv_social_comment_number.setText(""+moment.getCommentNum());
        }else {
            viewHolder.tv_social_comment_number.setText("评论");
        }
        //设置动态赞数量
        if(moment.getLikeNum()>0){
            viewHolder.tv_social_praise_number.setText(""+moment.getLikeNum());
            //设置用户关于该动态是否赞的情况
            if(moment.isIsLike()==false){
                viewHolder.tv_social_praise_number.setTextColor(context.getResources().getColor(R.color.colorLabelGrey));
            }else {
                viewHolder.tv_social_praise_number.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            }
        }else {
            viewHolder.tv_social_praise_number.setText("赞");
            viewHolder.tv_social_praise_number.setTextColor(context.getResources().getColor(R.color.colorLabelGrey));
        }

        //发起评论
        viewHolder.ll_social_moment_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent();
                intent.setAction(context.getString(R.string.activity_project_comment));
                intent.putExtra("momentId",moment.getMomentId());
                intent.putExtra("mode", ProjectCommentActivity.SEND_MOMENT_COMMENT);
                if(fragment!=null){
                    fragment.startActivityForResult(intent,ProjectCommentActivity.REQUESR_CODE_SEND_COMMENT_ACTIVITY);
                }else {
                    context.startActivityForResult(intent,ProjectCommentActivity.REQUESR_CODE_SEND_COMMENT_ACTIVITY);
                }

            }
        });

        //发起赞
        viewHolder.ll_social_moment_praise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFinishRequest==false){
                    Toast.makeText(context,"正在处理，请稍后再试",Toast.LENGTH_SHORT).show();
                    return;
                }
                isFinishRequest=false;
                SharedPreferences share=context.getSharedPreferences(context.getString(R.string.sharepreference_login_by_phone), Context.MODE_PRIVATE);
                int userId=share.getInt("id",-1);
                String token=share.getString("token"," ");
                if(moment.isIsLike()==false){
                    RequestPraiseMoment.requestPraiseMoment(context,handler,httpClient,moment.getMomentId(),userId,token);
                }else {
                    RequestPraiseMoment.requestCancelPraiseMoment(context,handler,httpClient,moment.getMomentId(),userId,token);
                }

            }
        });

        return rootView;
    }

    class ViewHolder{
        //动态发起人头像
        CircleImageView iv_social_publisher_head;

        //动态发起人昵称
        TextView tv_social_publisher_nickname;

        //动态发起时间
        TextView tv_social_publish_time;

        //动态内容
        EmojiconTextView tv_social_publisher_content;

        //动态图片展示区
        MyGridView gv_social_photos;

        //动态所指项目所在的布局
        LinearLayout ll_social_project;

        //动态展示的项目的封面
        ImageView iv_social_project_cover;

        //动态展示的项目的名称
        TextView tv_social_project_name;

        //动态评论按钮
        LinearLayout ll_social_moment_comment;

        //动态的赞按钮
        LinearLayout ll_social_moment_praise;

        //评论数量
        TextView tv_social_comment_number;

        //赞数量
        TextView tv_social_praise_number;

        //赞的图片
        ImageView iv_social_praise_picture;

    }
}
