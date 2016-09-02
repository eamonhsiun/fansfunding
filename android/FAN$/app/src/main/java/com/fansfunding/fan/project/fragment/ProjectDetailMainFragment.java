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
import com.fansfunding.fan.project.adapter.ProjectSupportAdapter;
import com.fansfunding.fan.project.adapter.ProjectSupportHeadAdapter;
import com.fansfunding.fan.request.RequestCancelFollowUser;
import com.fansfunding.fan.request.RequestEnsureFollowUser;
import com.fansfunding.fan.request.RequestFollowUser;
import com.fansfunding.fan.request.RequestProjectSupportInfo;
import com.fansfunding.fan.utils.ErrorHandler;
import com.fansfunding.fan.utils.FANRequestCode;
import com.fansfunding.fan.utils.MyGridView;
import com.fansfunding.fan.utils.StartHomepage;
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

    //登录按钮启动码
    private static final int REQUEST_CODE_LOGIN=300;
    //httpclient
    private OkHttpClient httpClient;

    //项目分类
    private int categoryId;

    //项目Id
    private int projectId;


    //项目描述信息(比如目标金额之类的)
    private ProjectInfo projectDetail;

    //项目详情图片
    private ConvenientBanner iv_project_detail_main_image;


    //剩余时间
    private TextView tv_project_detail_main_remain_time;

    //支持者头像适配器
    private ProjectSupportHeadAdapter adapter;

    //获取支持者信息
    private RequestProjectSupportInfo requestProjectSupportInfo;

    //支持者人数
    private TextView tv_project_detail_main_support_number;

    //关注按钮
    private Button btn_project_detail_main_follow_user;

    //关注用户
    private RequestFollowUser requestFollowUser;

    //取消关注用户
    private RequestCancelFollowUser requestCancelFollowUser;

    //是否已经关注了发起人
    private RequestEnsureFollowUser requestEnsureFollowUser;

    //是否正在请求 用户是否已经关注了该发起人
    private boolean isGetUserFollowMessage=false;

    //是否正在请求发起人
    private boolean isFinishFollow=true;

    //是否已经关注了发起人
    private boolean isFollow=false;

    //用户是否登录
    private boolean isLogin;
    //用户id
    private int userId;
    //用户token
    private String token;

    //要显示的支持者的头像的数量
    private final int supportHeadNumber=5;

    private ErrorHandler handler=new ErrorHandler(this.getActivity()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                //获取支持者人数成功
                case FANRequestCode.GET_PROJECT_DETAIL_SUPPORTER_SUCCESS:
                    if(ProjectDetailMainFragment.this.getActivity()==null){
                        break;
                    }
                    //设置支持人数
                    tv_project_detail_main_support_number.setText(requestProjectSupportInfo.getSupportsInfo().getData().getTotal()+"人支持 >");
                    //设置支持者头像
                    if(requestProjectSupportInfo.getSupportsInfo().getData().getList()!=null&&requestProjectSupportInfo.getSupportsInfo().getData().getList().size()>0){
                        for(int i=0;i<requestProjectSupportInfo.getSupportsInfo().getData().getList().size();i++){
                            if(i>=supportHeadNumber){
                                break;
                            }
                            if(requestProjectSupportInfo.getSupportsInfo().getData().getList().get(i)!=null
                                    &&requestProjectSupportInfo.getSupportsInfo().getData().getList().get(i).getHead()!=null
                                    &&requestProjectSupportInfo.getSupportsInfo().getData().getList().get(i).getHead().equals("")==false){
                                adapter.addItem(requestProjectSupportInfo.getSupportsInfo().getData().getList().get(i).getHead());
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case FANRequestCode.GET_PROJECT_DETAIL_SUPPORTER_FAILURE:
                    if(ProjectDetailMainFragment.this.getActivity()==null){
                        break;
                    }
                    Toast.makeText(ProjectDetailMainFragment.this.getActivity(),"获取项目支持信息失败",Toast.LENGTH_SHORT).show();
                    break;
                case FANRequestCode.GET_IS_FOLLOW_PUBLISH_SUCCESS:
                    isGetUserFollowMessage=true;
                    if(requestEnsureFollowUser.getEnsureFollowUser()!=null){
                        if(requestEnsureFollowUser.getEnsureFollowUser().isData()==true){
                            btn_project_detail_main_follow_user.setText("已关注");
                            isFollow=true;
                        }else {
                            btn_project_detail_main_follow_user.setText("关注");
                            isFollow=false;
                        }
                    }
                    break;
                case FANRequestCode.GET_IS_FOLLOW_PUBLISH_FAILURE:
                    isGetUserFollowMessage=true;
                    if(ProjectDetailMainFragment.this.getActivity().isFinishing()==true){
                        break;
                    }
                    Toast.makeText(ProjectDetailMainFragment.this.getActivity(), "请求关注信息失败", Toast.LENGTH_SHORT).show();
                    break;
                case FANRequestCode.FOLLOW_USER_SUCCESS:
                    if(ProjectDetailMainFragment.this.getActivity().isFinishing()==false){
                        Toast.makeText(ProjectDetailMainFragment.this.getActivity(), "关注成功", Toast.LENGTH_SHORT).show();
                    }else {
                        break;
                    }
                    btn_project_detail_main_follow_user.setText("已关注");
                    isFollow=true;
                    isFinishFollow=true;
                    break;
                case FANRequestCode.FOLLOW_USER_FAILURE:
                    if(ProjectDetailMainFragment.this.getActivity().isFinishing()==false){
                        Toast.makeText(ProjectDetailMainFragment.this.getActivity(), "关注失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case FANRequestCode.CANCEL_FOLLOW_USER_SUCCESS:

                    if(ProjectDetailMainFragment.this.getActivity().isFinishing()==false){
                        Toast.makeText(ProjectDetailMainFragment.this.getActivity(), "取消关注成功", Toast.LENGTH_SHORT).show();
                    }else {
                        break;
                    }
                    btn_project_detail_main_follow_user.setText("关注");
                    isFollow=false;
                    isFinishFollow=true;
                    break;
                case FANRequestCode.CANCEL_FOLLOW_USER_FAILURE:
                    if(ProjectDetailMainFragment.this.getActivity().isFinishing()==false){
                        Toast.makeText(ProjectDetailMainFragment.this.getActivity(), "取消关注失败", Toast.LENGTH_SHORT).show();
                    }else {
                        break;
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }

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
        handler.setContext(this.getActivity());
        adapter=new ProjectSupportHeadAdapter(this.getActivity());
        requestProjectSupportInfo=new RequestProjectSupportInfo();
        requestFollowUser=new RequestFollowUser();
        requestCancelFollowUser=new RequestCancelFollowUser();
        requestEnsureFollowUser=new RequestEnsureFollowUser();
        httpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();

        //项目图片
        iv_project_detail_main_image=(ConvenientBanner)rootView.findViewById(R.id.iv_project_detail_main_image);
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


        //筹集金额
        TextView tv_project_detail_main_target_money=(TextView)rootView.findViewById(R.id.tv_project_detail_main_target_money);
        tv_project_detail_main_target_money.setText(projectDetail.getTargetMoney().toString());

        //已筹金额
        TextView tv_project_detail_main_get_money=(TextView)rootView.findViewById(R.id.tv_project_detail_main_get_money);
        tv_project_detail_main_get_money.setText(new java.text.DecimalFormat("0.00").format(projectDetail.getSum()));

        //剩余时间
        tv_project_detail_main_remain_time=(TextView)rootView.findViewById(R.id.tv_project_detail_main_remain_time);
        tv_project_detail_main_remain_time.setText(getEndTime(projectDetail.getTargetDeadline()));

        //项目进度
        final ProgressBar progressBar=(ProgressBar)rootView.findViewById(R.id.progressBar_project_detail_main);
        progressBar.setProgress((int)(100*(projectDetail.getSum().doubleValue()/projectDetail.getTargetMoney().doubleValue())));

        //项目名称
        TextView tv_project_detail_name=(TextView)rootView.findViewById(R.id.tv_project_detail_name);
        tv_project_detail_name.setText(projectDetail.getName());

        //项目简介
        TextView tv_project_detail_intro=(TextView)rootView.findViewById(R.id.tv_project_detail_intro);
        tv_project_detail_intro.setText(projectDetail.getDescription());


        //项目发起人昵称
        TextView tv_project_detail_main_publisher_name=(TextView)rootView.findViewById(R.id.tv_project_detail_main_publisher_name);
        tv_project_detail_main_publisher_name.setText(projectDetail.getSponsorNickname());
        tv_project_detail_main_publisher_name.setOnClickListener(new StartHomepage(getActivity(),projectDetail.getSponsor()));

        //项目发起人头像
        CircleImageView iv_project_detail_main_publisher_head=(CircleImageView)rootView.findViewById(R.id.iv_project_detail_main_publisher_head);
        if(this.getActivity()!=null&&projectDetail.getSponsorHead()!=null&&projectDetail.getSponsorHead().equals("")==false){
            Picasso.with(this.getActivity()).load(getString(R.string.url_resources)+projectDetail.getSponsorHead()).into(iv_project_detail_main_publisher_head);
        }
        iv_project_detail_main_publisher_head.setOnClickListener(new StartHomepage(getActivity(),projectDetail.getSponsor()));

        //支持者人数
        tv_project_detail_main_support_number=(TextView)rootView.findViewById(R.id.tv_project_detail_main_support_number);
        tv_project_detail_main_support_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(getString(R.string.activity_project_supporter_list));
                intent.putExtra("categoryId",categoryId);
                intent.putExtra("projectId",projectId);
                startActivity(intent);
            }
        });

        //项目支持者列表
        MyGridView gv_project_detail_main_support_head=(MyGridView)rootView.findViewById(R.id.gv_project_detail_main_support_head);
        gv_project_detail_main_support_head.setAdapter(adapter);

        //富文本信息按钮
        TextView tv_project_detail_main_richtext=(TextView)rootView.findViewById(R.id.tv_project_detail_main_richtext);
        tv_project_detail_main_richtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(getString(R.string.activity_project_web));
                intent.putExtra("categoryId",categoryId);
                intent.putExtra("projectId",projectId);
                startActivity(intent);
            }
        });

        //关注按钮
        btn_project_detail_main_follow_user=(Button)rootView.findViewById(R.id.btn_project_detail_main_follow_user);
        btn_project_detail_main_follow_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isFinishFollow==false){
                    return;
                }
                if(isLogin==true){
                    if(isGetUserFollowMessage==false){
                        Toast.makeText(ProjectDetailMainFragment.this.getActivity(),"正在获取关注信息，请稍等",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    isFinishFollow=false;
                    //如果没有关注
                    if(isFollow==false){
                        requestFollowUser.followUser(ProjectDetailMainFragment.this.getActivity(),handler,httpClient,userId,projectDetail.getSponsor(),token);
                    }
                    //如果已经关注
                    else {
                        requestCancelFollowUser.cancelFollowUser(ProjectDetailMainFragment.this.getActivity(),handler,httpClient,userId,projectDetail.getSponsor(),token);
                    }

                }else{
                    Intent intent=new Intent();
                    intent.setAction(getString(R.string.activity_login));
                    startActivityForResult(intent,REQUEST_CODE_LOGIN);
                }

            }
        });

        SharedPreferences share=getActivity().getSharedPreferences(getString(R.string.sharepreference_login_by_phone),Context.MODE_PRIVATE);
        isLogin=share.getBoolean("isLogin",false);
        userId=share.getInt("id",0);
        token=share.getString("token"," ");

        requestProjectSupportInfo.setRows(supportHeadNumber);
        requestProjectSupportInfo.getProjectSupportsInfo(this.getActivity(),handler,httpClient,categoryId,projectId);

        //获取是否关注信息
        if(isLogin==true){
            isGetUserFollowMessage=false;
            requestEnsureFollowUser.ensureFollowUser(this.getActivity(),handler,httpClient,userId,projectDetail.getSponsor(),token);
        }else {
            //isGetUserFollowMessage=true;
        }




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
            case REQUEST_CODE_LOGIN:
                if(resultCode==getActivity().RESULT_OK){
                    SharedPreferences share=getActivity().getSharedPreferences(getString(R.string.sharepreference_login_by_phone),getActivity().MODE_PRIVATE);
                    isLogin=share.getBoolean("isLogin",false);
                    userId=share.getInt("id",0);
                    token=share.getString("token"," ");
                    requestEnsureFollowUser.ensureFollowUser(this.getActivity(),handler,httpClient,userId,projectDetail.getSponsor(),token);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }




    //获取项目详情
    /*private void getProjectDetailMain(){

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
*/
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
            time=(differ+1)+"天";
        }
        return time;
    }



    //获取评论
    /*private void getProjectDetailComment(){
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
*/
    /*//获取关注者信息
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
*/


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


