package com.fansfunding.fan;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fansfunding.internal.AllProjectInCategory;
import com.fansfunding.internal.ErrorCode;
import com.fansfunding.internal.ProjectDetail;
import com.fansfunding.internal.ProjectDetailComment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.squareup.picasso.Picasso;

import java.io.IOException;

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


    //httpclient
    private OkHttpClient httpClient;

    //项目分类
    private int categoryId;

    //项目Id
    private int projectId;

    //项目附件描述信息(比如图片呀，音频呀)
    private ProjectDetail detail;

    //项目描述信息(比如目标金额之类的)
    private AllProjectInCategory.ProjectDetail projectDetail;

    //项目评论
    private  ProjectDetailComment projectDetailComment;

    //项目详情图片
    private ImageView iv_project_detail_main_image;

    //项目评论内容
    private TextView tv_news_detail_comment;
    //项目评论人头像
    private ImageView iv_project_detail_dynamic_head;
    //项目评论人头像昵称
    private TextView tv_news_detail_commenter_name;



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
     * @return A new instance of fragment ProjectDetailMainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProjectDetailMainFragment newInstance(AllProjectInCategory.ProjectDetail projectDetail) {
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
            this.projectDetail= (AllProjectInCategory.ProjectDetail) getArguments().getSerializable("projectDetail");
            categoryId=projectDetail.getCategoryId();
            projectId=projectDetail.getId();
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        httpClient=new OkHttpClient();

        View rootView=inflater.inflate(R.layout.fragment_project_detail_main, container, false);
        //项目图片
        iv_project_detail_main_image=(ImageView)rootView.findViewById(R.id.iv_project_detail_main_image);
        //项目评论内容
        tv_news_detail_comment=(TextView)rootView.findViewById(R.id.tv_news_detail_comment);
        //项目评论人头像
        iv_project_detail_dynamic_head=(ImageView)rootView.findViewById(R.id.iv_project_detail_dynamic_head);
        //项目评论人昵称
        tv_news_detail_commenter_name=(TextView)rootView.findViewById(R.id.tv_news_detail_commenter_name);


        //筹集金额
        TextView tv_project_detail_main_target_money=(TextView)rootView.findViewById(R.id.tv_project_detail_main_target_money);
        tv_project_detail_main_target_money.setText(projectDetail.getTargetMoney().toString()+"元");

        //项目名称
        TextView tv_project_detail_name=(TextView)rootView.findViewById(R.id.tv_project_detail_name);
        tv_project_detail_name.setText(projectDetail.getName());

        //项目发起人昵称
        TextView tv_project_detail_main_publisher_name=(TextView)rootView.findViewById(R.id.tv_project_detail_main_publisher_name);
        tv_project_detail_main_publisher_name.setText(projectDetail.getSponsorNickname());



        //获取全部评论按钮
        Button btn_project_detail_main_all_comment=(Button) rootView.findViewById(R.id.btn_project_detail_main_all_comment);
        btn_project_detail_main_all_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(getString(R.string.activity_project_detail_comment));
                intent.putExtra("detail",projectDetail);
                startActivity(intent);
            }
        });
        getProjectDetailMain();
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
        if(detail.getData().getIamges()!=null&&detail.getData().getIamges().equals("")==false){
            Picasso.with(this.getActivity()).load(getString(R.string.url_resources)+detail.getData().getIamges()).into(iv_project_detail_main_image);
        }


    }

    //初始化评论
    private void initComment(){
        if(projectDetailComment.getData().getList()!=null&&projectDetailComment.getData().getList().size()>0){
            //项目评论人内容
            tv_news_detail_comment.setText(projectDetailComment.getData().getList().get(0).getContent());
            //项目评论人头像
            Picasso.with(this.getActivity()).load(getString(R.string.url_resources)+projectDetailComment.getData().getList().get(0).getCommenterHead()).into(iv_project_detail_dynamic_head);

            //项目评论人昵称
            tv_news_detail_commenter_name.setText(projectDetailComment.getData().getList().get(0).getCommenterNickname());

        }

    }

    private void getProjectDetailMain(){


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


    private void getProjectDetailComment(){
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
}


