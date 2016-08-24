package com.fansfunding.fan.project.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.fansfunding.PullListView.LoadListView;
import com.fansfunding.PullListView.XListView;
import com.fansfunding.fan.R;
import com.fansfunding.fan.project.adapter.ProjectDetailCommentAdapter;
import com.fansfunding.fan.request.RequestProjectDetailComment;
import com.fansfunding.fan.utils.ErrorHandler;
import com.fansfunding.fan.utils.FANRequestCode;
import com.fansfunding.fan.utils.ProjectDetailListView;
import com.fansfunding.internal.ProjectDetailComment;
import com.fansfunding.internal.ProjectInfo;
import com.github.clans.fab.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProjectDetailCommentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectDetailCommentFragment extends Fragment {
    //发表评论请求码
    private static final int REQUEST_CODE_SEND_COMMENT=300;

    //是否请求过项目评论
    private boolean hasbeenRequest=false;

    //是否已经完成了项目评论获取的请求
    private boolean isFinishRequest=true;

    //xlistview适配器
    private ProjectDetailCommentAdapter adapter;

    //评论展示列表
    private ProjectDetailListView lv_PJ_detail_comment;

    //用来请求评论
    private RequestProjectDetailComment requestProjectDetailComment;

    //项目描述信息(比如目标金额之类的)
    private ProjectInfo projectDetail;

    //项目分类id
    private int categoryId;

    //项目id
    private int projectId;

    //httpclient
    private OkHttpClient httpClient;

    private ErrorHandler handler=new ErrorHandler(this.getActivity()){
        @Override
        public void handleMessage(Message msg) {
            endRefresh();
            switch (msg.what){
                case FANRequestCode.GET_PROJECT_COMMENT_SUCCESS:
                    if(requestProjectDetailComment.getProjectDetailComment().getData().getList().size()< requestProjectDetailComment.getRows()){
                        requestProjectDetailComment.setPage(1);
                        lv_PJ_detail_comment.setPullLoadEnable(false);
                        lv_PJ_detail_comment.setAutoLoadEnable(false);
                    }else{
                        requestProjectDetailComment.setPage(requestProjectDetailComment.getPage()+1);
                        lv_PJ_detail_comment.setPullLoadEnable(true);
                        lv_PJ_detail_comment.setAutoLoadEnable(true);
                    }
                    for(int i = 0; i< requestProjectDetailComment.getProjectDetailComment().getData().getList().size(); i++){
                        adapter.addItem(requestProjectDetailComment.getProjectDetailComment().getData().getList().get(i));
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case FANRequestCode.GET_PROJECT_COMMENT_FAILURE:
                    if(ProjectDetailCommentFragment.this.getActivity()!=null){
                        Toast.makeText(ProjectDetailCommentFragment.this.getActivity(),"获取评论失败",Toast.LENGTH_LONG).show();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }

        }
    };

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProjectDetailCommentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProjectDetailCommentFragment newInstance(ProjectInfo projectDetail) {
        ProjectDetailCommentFragment fragment = new ProjectDetailCommentFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView=inflater.inflate(R.layout.fragment_project_detail_comment, container, false);

        handler.setContext(this.getActivity());
        adapter=new ProjectDetailCommentAdapter(this.getActivity());
        requestProjectDetailComment =new RequestProjectDetailComment();
        httpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();

        lv_PJ_detail_comment=(ProjectDetailListView)rootView.findViewById(R.id.lv_PJ_detail_comment);
        lv_PJ_detail_comment.setAutoLoadEnable(false);
        lv_PJ_detail_comment.setPullLoadEnable(false);
        lv_PJ_detail_comment.setPullRefreshEnable(false);
        lv_PJ_detail_comment.setRefreshTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        lv_PJ_detail_comment.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadMore() {
                if(isFinishRequest==true){
                    isFinishRequest=false;
                    requestProjectDetailComment.getProjectDetailComment(ProjectDetailCommentFragment.this.getActivity(),handler,httpClient,categoryId,projectId);
                }
            }
        });
        lv_PJ_detail_comment.setAdapter(adapter);
        lv_PJ_detail_comment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
                ProjectDetailComment.ProjectComment comment=(ProjectDetailComment.ProjectComment)lv_PJ_detail_comment.getAdapter().getItem(position);
                if(comment==null){
                    return;
                }
                intent.setAction(getString(R.string.activity_project_comment));
                intent.putExtra("categoryId",categoryId);
                intent.putExtra("projectId",projectId);
                intent.putExtra("pointTo",comment.getCommenterId());
                intent.putExtra("pointToNickname",comment.getCommenterNickname());
                startActivityForResult(intent,REQUEST_CODE_SEND_COMMENT);
            }
        });

        final FloatingActionButton fab_PJ_detail_comment=(FloatingActionButton)rootView.findViewById(R.id.fab_PJ_detail_comment);
        fab_PJ_detail_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
        requestProjectDetailComment.getProjectDetailComment(this.getActivity(),handler,httpClient,categoryId,projectId);
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

    private void endRefresh(){
        isFinishRequest=true;
        lv_PJ_detail_comment.stopRefresh();
        lv_PJ_detail_comment.stopLoadMore();
        lv_PJ_detail_comment.setRefreshTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CODE_SEND_COMMENT:
                if(resultCode==getActivity().RESULT_OK){
                    isFinishRequest=false;
                    requestProjectDetailComment.getProjectDetailComment(this.getActivity(),handler,httpClient,categoryId,projectId);
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
