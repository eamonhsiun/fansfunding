package com.fansfunding.fan.project.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.fansfunding.fan.R;
import com.fansfunding.internal.ErrorCode;
import com.fansfunding.internal.ProjectDetail;
import com.fansfunding.internal.ProjectDetailComment;
import com.fansfunding.internal.ProjectInfo;
import com.fansfunding.internal.UserFollowProject;
import com.fansfunding.internal.project.ProjectFollower;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProjectDetailMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectDetailMainFragment extends Fragment {

    //获取项目详情成功
    private static final int GET_PROJECT_DETAIL_SUCCESS=100;

    //获取项目详情失败
    private static final int GET_PROJECT_DETAIL_FAILURE=101;

    //获取第一条评论成功
    private static final int GET_PROJECT_COMMENT_SUCCESS=102;
    //获取第一条评论失败
    private static final int GET_PROJECT_COMMENT_FAILURE=103;

    //获取关注者信息成功
    private static final int GET_PROJECT_FOLLOWER_SUCCESS=104;

    //获取关注者信息失败
    private static final int GET_PROJECT_FOLLOWER_FAILURE=105;



    //发表评论请求码
    private static final int REQUEST_CODE_SEND_COMMENT=300;

    //httpclient
    //private OkHttpClient httpClient;


    //项目分类
    private int categoryId;

    //项目Id
    private int projectId;

    //每次获取关注者的数量
    private final int followerRows=10;

    //关注者信息
    private ProjectFollower follower=null;

    //项目附件描述信息(比如图片呀，音频呀)
    private ProjectDetail detail;

    //项目描述信息(比如目标金额之类的)
    private ProjectInfo projectDetail;

    //项目评论
    private  ProjectDetailComment projectDetailComment;

    //项目详情图片
    private ConvenientBanner iv_project_detail_main_image;

    //项目评论内容
    private TextView tv_news_detail_comment;
    //项目评论人头像
    private ImageView iv_project_detail_dynamic_head;
    //项目评论人头像昵称
    private TextView tv_news_detail_commenter_name;
    //评论时间
    private TextView tv_news_detail_comment_tine;

    //关注人数
    private TextView tv_project_detail_main_follower_number;


    //获取或发表评论函数
    private Button btn_project_detail_main_all_comment;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                //获取项目详情成功
                case GET_PROJECT_DETAIL_SUCCESS:
                    initProject();
                    break;
                //获取项目详情失败
                case GET_PROJECT_DETAIL_FAILURE:
                    if(ProjectDetailMainFragment.this.getActivity()==null){
                        break;
                    }
                    Toast.makeText(ProjectDetailMainFragment.this.getActivity(),"获取项目详情失败",Toast.LENGTH_LONG).show();
                    break;

                //获取第一条评论成功
                case GET_PROJECT_COMMENT_SUCCESS:
                    initComment();
                    break;
                //获取第一条评论失败
                case GET_PROJECT_COMMENT_FAILURE:
                    if(ProjectDetailMainFragment.this.getActivity()==null){
                        break;
                    }
                    Toast.makeText(ProjectDetailMainFragment.this.getActivity(),"获取评论失败",Toast.LENGTH_LONG).show();
                    break;

                //获取关注者信息成功
                case GET_PROJECT_FOLLOWER_SUCCESS:

                    //设置关注者信息
                    if(follower!=null&&follower.getData()!=null){
                        tv_project_detail_main_follower_number.setText(String.valueOf(follower.getData().getTotal()));
                    }
                    break;
                //获取关注者信息失败
                case GET_PROJECT_FOLLOWER_FAILURE:
                    if(ProjectDetailMainFragment.this.getActivity()==null){
                        break;
                    }
                    Toast.makeText(ProjectDetailMainFragment.this.getActivity(),"获取关注者信息失败",Toast.LENGTH_LONG).show();
                    break;
                case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                    if(ProjectDetailMainFragment.this.getActivity()==null){
                        break;
                    }
                    Toast.makeText(ProjectDetailMainFragment.this.getActivity(),"请求过于频繁",Toast.LENGTH_LONG).show();

                    break;
                case ErrorCode.PARAMETER_ERROR:
                    if(ProjectDetailMainFragment.this.getActivity()==null){
                        break;
                    }
                    Toast.makeText(ProjectDetailMainFragment.this.getActivity(),"参数错误",Toast.LENGTH_LONG).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };
    public ProjectDetailMainFragment(){

    }

    /**
     * Use this factory method to create a new instance of
     * @return A new instance of fragment P0rojectDetailMainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProjectDetailMainFragment newInstance(ProjectInfo projectDetail) {
        ProjectDetailMainFragment fragment = new ProjectDetailMainFragment();
        Bundle args = new Bundle();
        args.putSerializable("projectDetail",projectDetail);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.projectDetail= (ProjectInfo) getArguments().getSerializable("projectDetail");
            categoryId=projectDetail.getCategoryId();
            projectId=projectDetail.getId();
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //httpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();

        View rootView=inflater.inflate(R.layout.fragment_project_detail_main, container, false);
        //项目图片
        iv_project_detail_main_image=(ConvenientBanner)rootView.findViewById(R.id.iv_project_detail_main_image);
        /*if(projectDetail.getCover()!=null&&projectDetail.getCover().equals("")==false){
            Picasso.with(this.getActivity()).load(getString(R.string.url_resources)+projectDetail.getCover()).into(iv_project_detail_main_image);
            //点击查看大图
            iv_project_detail_main_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent();
                    intent.setAction(getString(R.string.activity_big_picture));
                    intent.putExtra("url",getString(R.string.url_resources)+projectDetail.getCover());
                    startActivity(intent);
                }
            });
        }*/
        ArrayList<String> networkImages=new ArrayList<String>();
        if(projectDetail.getCover()!=null&&projectDetail.getCover().equals("")==false){
            networkImages.add(getString(R.string.url_resources)+projectDetail.getCover());
        }
        if(projectDetail.getImages()!=null&&projectDetail.getImages().size()>0){
            for(int i=0;i<projectDetail.getImages().size();i++) {
                if(projectDetail.getImages().get(i)!=null&&projectDetail.getImages().get(i).equals("")==false){
                    if(networkImages.contains(getString(R.string.url_resources)+projectDetail.getImages().get(i))==false){
                        networkImages.add(getString(R.string.url_resources)+projectDetail.getImages().get(i));
                    }

                }
            }
        }

        iv_project_detail_main_image.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        },networkImages)
                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused}) ;

        //项目评论内容
        tv_news_detail_comment=(TextView)rootView.findViewById(R.id.tv_news_detail_comment);
        //项目评论人头像
        iv_project_detail_dynamic_head=(ImageView)rootView.findViewById(R.id.iv_project_detail_dynamic_head);
        //项目评论人昵称
        tv_news_detail_commenter_name=(TextView)rootView.findViewById(R.id.tv_news_detail_commenter_name);
        //评论时间
        tv_news_detail_comment_tine=(TextView) rootView.findViewById(R.id.tv_news_detail_comment_tine);


        //筹集金额
        TextView tv_project_detail_main_target_money=(TextView)rootView.findViewById(R.id.tv_project_detail_main_target_money);
        tv_project_detail_main_target_money.setText(projectDetail.getTargetMoney().toString());

        //已筹金额
        TextView tv_project_detail_main_get_money=(TextView)rootView.findViewById(R.id.tv_project_detail_main_get_money);
        tv_project_detail_main_get_money.setText(new java.text.DecimalFormat("0.00").format(projectDetail.getSum()));

        //关注人数
        tv_project_detail_main_follower_number=(TextView)rootView.findViewById(R.id.tv_project_detail_main_support_times);

        //项目进度
        ProgressBar progressBar=(ProgressBar)rootView.findViewById(R.id.progressBar_project_detail_main);
        progressBar.setProgress((int)(100*(projectDetail.getSum().doubleValue()/projectDetail.getTargetMoney().doubleValue())));

        //项目名称
        TextView tv_project_detail_name=(TextView)rootView.findViewById(R.id.tv_project_detail_name);
        tv_project_detail_name.setText(projectDetail.getName());


        //项目发起人昵称
        TextView tv_project_detail_main_publisher_name=(TextView)rootView.findViewById(R.id.tv_project_detail_main_publisher_name);
        tv_project_detail_main_publisher_name.setText(projectDetail.getSponsorNickname());

        //项目发起人头像
        CircleImageView iv_project_detail_main_publisher_head=(CircleImageView)rootView.findViewById(R.id.iv_project_detail_main_publisher_head);
        if(this.getActivity()!=null&&projectDetail.getSponsorHead()!=null&&projectDetail.getSponsorHead().equals("")==false){
            Picasso.with(this.getActivity()).load(getString(R.string.url_resources)+projectDetail.getSponsorHead()).into(iv_project_detail_main_publisher_head);
        }


        //开始时间
        TextView tv_project_detail_main_time_start=(TextView)rootView.findViewById(R.id.tv_project_detail_main_time_start);
        tv_project_detail_main_time_start.setText(getStartTime(projectDetail.getCreateTime()));


        //截止日期
        TextView tv_project_detail_main_time_end=(TextView)rootView.findViewById(R.id.tv_project_detail_main_time_end);
        tv_project_detail_main_time_end.setText(getEndTime(projectDetail.getTargetDeadline()));

        //获取全部评论按钮
        btn_project_detail_main_all_comment=(Button) rootView.findViewById(R.id.btn_project_detail_main_all_comment);
        btn_project_detail_main_all_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getComment();

            }
        });

        //获取关注信息
        getProjectFollower();

        //获取项目详情
        getProjectDetailMain();

        //获取项目第一条评论
        getProjectDetailComment();


        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    //通过获取的数据初始化各个控件的值
    private void initProject(){
        //初始化项目详情图片

        //初始化剩余时间



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CODE_SEND_COMMENT:
                if(resultCode==getActivity().RESULT_OK)
                getProjectDetailComment();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //初始化评论
    private void initComment(){
        if(projectDetailComment.getData().getList()!=null&&projectDetailComment.getData().getList().size()>0){

            //更改按钮
            btn_project_detail_main_all_comment.setText("查看全部评论");
            btn_project_detail_main_all_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getComment();
                }
            });


            //项目评论人内容
            if(projectDetailComment.getData().getList().get(0).getPointTo()==0){
                tv_news_detail_comment.setText(projectDetailComment.getData().getList().get(0).getContent());
            }else{
                String comment="回复 "
                        +projectDetailComment.getData().getList().get(0).getPointToNickname()
                        + ": "
                        +projectDetailComment.getData().getList().get(0).getContent();
                tv_news_detail_comment.setText(comment);

            }

            //项目评论人头像
            if(this.getActivity()!=null)
                Picasso.with(this.getActivity()).load(getString(R.string.url_resources)+projectDetailComment.getData().getList().get(0).getCommenterHead()).into(iv_project_detail_dynamic_head);

            //项目评论人昵称
            tv_news_detail_commenter_name.setText(projectDetailComment.getData().getList().get(0).getCommenterNickname());

            //评论时间
            tv_news_detail_comment_tine.setText(new SimpleDateFormat("MM-dd HH:mm").format(new Date(projectDetailComment.getData().getList().get(0).getCommentTime())));
        }else{
            btn_project_detail_main_all_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn_project_detail_main_all_comment.setText("评论");
                    sendComment();
                }
            });
        }

    }

    //获取项目详情
    private void getProjectDetailMain(){

        OkHttpClient httpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
        Request request=new Request.Builder()
                .get()
                .url(getString(R.string.url_project)+categoryId+"/"+projectId+"/detail")
                .build();
        Call call=httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg=new Message();
                msg.what=GET_PROJECT_DETAIL_FAILURE;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //服务器响应失败
                if(response==null||response.isSuccessful()==false){
                    Message msg=new Message();
                    msg.what=GET_PROJECT_DETAIL_FAILURE;
                    handler.sendMessage(msg);
                    return;
                }
                Gson gson=new GsonBuilder().create();
                String str_response=response.body().string();
                detail =new ProjectDetail();
                try {

                    //用Gson进行解析，并判断结果是否为空
                    if((detail = gson.fromJson(str_response, detail.getClass()))==null){
                        Message msg=new Message();
                        msg.what=GET_PROJECT_DETAIL_FAILURE;
                        handler.sendMessage(msg);
                    }
                    //获取项目详情失败
                    if(detail.isResult()==false){
                        Message msg=new Message();
                        switch (detail.getErrCode()){
                            case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                                msg.what=ErrorCode.REQUEST_TOO_FRENQUENTLY;
                                break;
                            case ErrorCode.PARAMETER_ERROR:
                                msg.what=ErrorCode.PARAMETER_ERROR;
                                break;
                            default:
                                msg.what=GET_PROJECT_DETAIL_FAILURE;
                                break;
                        }
                        handler.sendMessage(msg);
                        return;
                    }


                    //获取项目信息成功
                    Message msg=new Message();
                    msg.what=GET_PROJECT_DETAIL_SUCCESS;
                    handler.sendMessage(msg);
                }catch (IllegalStateException e){
                    Message msg=new Message();
                    msg.what=GET_PROJECT_DETAIL_FAILURE;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }catch (JsonSyntaxException e){
                    Message msg=new Message();
                    msg.what=GET_PROJECT_DETAIL_FAILURE;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            }
        });
    }

    //转化时间,获取开始的时间格式
    private String getStartTime(long milliscond){
        String time="";
        Date startDate=new Date(milliscond);
        Date now=new Date();

        int differ=(int)(now.getTime()/(1000*3600*24))-(int)(startDate.getTime()/(1000*3600*24));
        if(differ<0){
            time="已完成";
        }
        else if(differ==0){
            time="今天";
        }else if(differ==1){
            time="昨天";
        }else if(differ==2){
            time="前天";
        }else if(differ>2&&differ<7){
            time= new SimpleDateFormat("E").format(startDate);
        }else if(differ>=7){
            time=new SimpleDateFormat("MM-dd").format(startDate);
        }

        return time;
    }

    //获取截止时间格式
    private String getEndTime(long milliscond){
        String time="";
        Date endtDate=new Date(milliscond);
        Date now=new Date();
        int differ=(int)(endtDate.getTime()/(1000*3600*24))-(int)(now.getTime()/(1000*3600*24));
        if(differ<0){
            time="已完成";
        }else {
            time="还剩"+(differ+1)+"天";
        }
        return time;
    }


    //直接发表评论函数
    private void sendComment(){
        SharedPreferences share=getActivity().getSharedPreferences(getString(R.string.sharepreference_login_by_phone),getActivity().MODE_PRIVATE);
        boolean isLogin=share.getBoolean("isLogin",false);

        Intent intent=new Intent();
        //如果没有登陆，则先登陆
        if(isLogin==false){
            intent.setAction(getString(R.string.activity_login));
            startActivity(intent);
            return;
        }
        //打开评论页
        intent.setAction(getString(R.string.activity_project_comment));
        intent.putExtra("categoryId",categoryId);
        intent.putExtra("projectId",projectId);
        intent.putExtra("pointTo",0);
        startActivityForResult(intent,REQUEST_CODE_SEND_COMMENT);
    }

    //获取全部评论函数
    private void getComment(){
        Intent intent=new Intent();
        intent.setAction(getString(R.string.activity_project_detail_comment));
        intent.putExtra("detail",projectDetail);
        startActivity(intent);
    }
    //获取评论
    private void getProjectDetailComment(){
        OkHttpClient httpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
        Request request=new Request.Builder()
                .get()
                .url(getString(R.string.url_project)+categoryId+"/"+projectId+"/comments?rows=1")
                .build();
        Call call=httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg=new Message();
                msg.what=GET_PROJECT_COMMENT_FAILURE;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //服务器响应失败
                if(response==null||response.isSuccessful()==false){
                    Message msg=new Message();
                    msg.what=GET_PROJECT_COMMENT_FAILURE;
                    handler.sendMessage(msg);
                    return;
                }
                Gson gson=new GsonBuilder().create();
                String str_response=response.body().string();

                Log.i("TAG",str_response);

                projectDetailComment=new ProjectDetailComment();
                try {

                    //用Gson进行解析，并判断结果是否为空
                    if((projectDetailComment = gson.fromJson(str_response, projectDetailComment.getClass()))==null){
                        Message msg=new Message();
                        msg.what=GET_PROJECT_COMMENT_FAILURE;
                        handler.sendMessage(msg);
                    }
                    //获取项目详情失败
                    if(projectDetailComment.isResult()==false){
                        Message msg=new Message();
                        switch (projectDetailComment.getErrCode()){
                            case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                                msg.what=ErrorCode.REQUEST_TOO_FRENQUENTLY;
                                break;
                            case ErrorCode.PARAMETER_ERROR:
                                msg.what=ErrorCode.PARAMETER_ERROR;
                                break;
                            default:
                                msg.what=GET_PROJECT_COMMENT_FAILURE;
                                break;
                        }
                        handler.sendMessage(msg);
                        return;
                    }


                    //获取项目信息成功
                    Message msg=new Message();
                    msg.what=GET_PROJECT_COMMENT_SUCCESS;
                    handler.sendMessage(msg);
                }catch (IllegalStateException e){
                    Message msg=new Message();
                    msg.what=GET_PROJECT_COMMENT_FAILURE;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }catch (JsonSyntaxException e){
                    Message msg=new Message();
                    msg.what=GET_PROJECT_COMMENT_FAILURE;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            }
        });
    }

    //获取关注者信息
    private void getProjectFollower(){

        OkHttpClient httpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();

        Request request=new Request.Builder()
                .get()
                .url(getString(R.string.url_project)+categoryId+"/"+projectId+"/followers")
                .build();

        Call call=httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(GET_PROJECT_FOLLOWER_FAILURE);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //服务器响应失败
                if(response==null||response.isSuccessful()==false){
                    handler.sendEmptyMessage(GET_PROJECT_FOLLOWER_FAILURE);
                    return;
                }
                Gson gson=new GsonBuilder().create();
                String str_response=response.body().string();
                follower=new ProjectFollower();
                try {
                    //用Gson进行解析，并判断结果是否为空
                    if((follower = gson.fromJson(str_response, follower.getClass()))==null){
                        handler.sendEmptyMessage(GET_PROJECT_FOLLOWER_FAILURE);
                        return;
                    }
                    //搜索项目关注者信息失败
                    if(follower.isResult()==false){
                        switch (follower.getErrCode()){
                            case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                                handler.sendEmptyMessage(ErrorCode.REQUEST_TOO_FRENQUENTLY);
                                break;
                            case ErrorCode.PARAMETER_ERROR:
                                handler.sendEmptyMessage(ErrorCode.PARAMETER_ERROR);
                                break;
                            default:
                                handler.sendEmptyMessage(GET_PROJECT_FOLLOWER_FAILURE);
                                break;
                        }
                        return;
                    }

                    //搜索项目关注者信息成功
                    handler.sendEmptyMessage(GET_PROJECT_FOLLOWER_SUCCESS);
                }catch (IllegalStateException e){
                    handler.sendEmptyMessage(GET_PROJECT_FOLLOWER_FAILURE);
                    e.printStackTrace();
                }catch (JsonSyntaxException e){
                    handler.sendEmptyMessage(GET_PROJECT_FOLLOWER_FAILURE);
                    e.printStackTrace();
                }
            }
        });
    }


    //通过用户是否按下关注按钮来增加或减少关注人数
    public void refreshFollowNumber(int number){
        if(number!=1&&number!=-1){
            return;
        }
        else {
            if(follower!=null&&follower.getData()!=null){
                if(follower.getData().getTotal()+number>=0){
                    follower.getData().setTotal(follower.getData().getTotal()+number);
                    handler.sendEmptyMessage(GET_PROJECT_FOLLOWER_SUCCESS);
                }
            }
        }
    }


    public class NetworkImageHolderView implements Holder<String> {
        private ImageView imageView;
        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context,int position, final String data) {
            if(data!=null&&data.equals("")==false){
                Picasso.with(ProjectDetailMainFragment.this.getActivity()).load(data).memoryPolicy(MemoryPolicy.NO_CACHE).resize(720,400).centerCrop().into(imageView);
                //点击查看大图
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent();
                        intent.setAction(getString(R.string.activity_big_picture));
                        intent.putExtra("url",data);
                        startActivity(intent);
                    }
                });
            }
        }
    }
}


