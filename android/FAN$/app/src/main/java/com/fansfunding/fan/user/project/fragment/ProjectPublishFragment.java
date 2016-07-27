package com.fansfunding.fan.user.project.fragment;

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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.fansfunding.PullListView.XListView;
import com.fansfunding.fan.R;
import com.fansfunding.fan.user.project.adapter.UserProjectAdapter;
import com.fansfunding.internal.ErrorCode;
import com.fansfunding.internal.ProjectInfo;
import com.fansfunding.internal.UserPublishProject;
import com.github.clans.fab.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
  * Use the {@link ProjectPublishFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * 用来显示我发起的项目的的界面
 */
public class ProjectPublishFragment extends Fragment {

    //获取用户发起项目信息成功
    private final static int GET_USER_PUBLISH_PROJECT_SUCCESS=100;

    //获取用户发起项目信息失败
    private final static int GET_USER_PUBLISH_PROJECT_FAILURE=101;
    //用户的项目类型
    private final String type="sponsor";

    //httpclient
    //private OkHttpClient httpClient;

    //是否已经完成搜索了
    private boolean isFinishRequest=true;

    //一次获取的数量
    private final int rows=10;

    //获取第几页
    private int page=1;

    //用户id
    private int userId;

    //用户token
    private String token;

    //获取的信息
    private UserPublishProject project;

    //展示的项目列表
    private XListView lv_PJ_list;

    //适配器
    private UserProjectAdapter adapter;

    //handler
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GET_USER_PUBLISH_PROJECT_SUCCESS:
                    if(project.getData().getList().size()<rows){
                        lv_PJ_list.setPullLoadEnable(false);
                        lv_PJ_list.setAutoLoadEnable(false);
                    }else {
                        page++;
                        lv_PJ_list.setPullLoadEnable(true);
                        lv_PJ_list.setAutoLoadEnable(true);
                    }

                    for(int i=0;i<project.getData().getList().size();i++){
                        adapter.addItemAtHead(project.getData().getList().get(i));
                    }
                    adapter.notifyDataSetChanged();
                    endRefresh();
                    break;
                case GET_USER_PUBLISH_PROJECT_FAILURE:
                    if(ProjectPublishFragment.this.getActivity()==null){
                        break;
                    }
                    Toast.makeText(ProjectPublishFragment.this.getActivity(), "搜索失败", Toast.LENGTH_LONG).show();
                    endRefresh();
                    break;
                case ErrorCode.PARAMETER_ERROR:
                    if(ProjectPublishFragment.this.getActivity()==null){
                        break;
                    }
                    Toast.makeText(ProjectPublishFragment.this.getActivity(), "参数错误", Toast.LENGTH_LONG).show();
                    endRefresh();
                    break;
                case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                    if(ProjectPublishFragment.this.getActivity()==null){
                        break;
                    }
                    Toast.makeText(ProjectPublishFragment.this.getActivity(), "请求过于频繁", Toast.LENGTH_LONG).show();
                    endRefresh();
                    break;

            }
            super.handleMessage(msg);
        }
    };
    public ProjectPublishFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment P0rojectPublishFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProjectPublishFragment newInstance() {
        ProjectPublishFragment fragment = new ProjectPublishFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        //httpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
        adapter=new UserProjectAdapter(this.getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView=inflater.inflate(R.layout.fragment_user_project_publish, container, false);

        lv_PJ_list=(XListView)rootView.findViewById(R.id.lv_PJ_list);
        lv_PJ_list.setPullLoadEnable(false);
        lv_PJ_list.setPullRefreshEnable(false);
        lv_PJ_list.setAutoLoadEnable(false);
        lv_PJ_list.setRefreshTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        lv_PJ_list.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                if(isFinishRequest==true){
                    getUserPublishProject(userId,token,type,page,rows);
                }
            }

            @Override
            public void onLoadMore() {
                if(isFinishRequest==true){
                    getUserPublishProject(userId,token,type,page,rows);
                }
            }
        });
        //获取用户id
        SharedPreferences share=getActivity().getSharedPreferences(getString(R.string.sharepreference_login_by_phone),Context.MODE_PRIVATE);
        userId=share.getInt("id",0);
        token=share.getString("token"," ");

        //获取用户发起项目信息
        getUserPublishProject(userId,token,type,page,rows);

        lv_PJ_list.setAdapter(adapter);
        lv_PJ_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                ProjectInfo detail=(ProjectInfo)lv_PJ_list.getAdapter().getItem(position);
                int categoryId=detail.getCategoryId();
                int projectId=detail.getId();
                intent.setAction(getString(R.string.activity_project_detail));
                intent.putExtra("detail",detail);
                startActivity(intent);

            }
        });

        FloatingActionButton fab=(FloatingActionButton)rootView.findViewById(R.id.fab_PJ_pubish);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(getResources().getString(R.string.activity_project_create));
                startActivity(intent);
            }
        });
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

    private void getUserPublishProject(final int userId,final String token,final String type,int page,final int rows){
        OkHttpClient httpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();

        Request request=new Request.Builder()
                .get()
                .url(getString(R.string.url_user)+userId+"/projects"+"?type="+type+"&token="+token+"&page="+page+"&rows="+rows)
                .build();

        Call call=httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(GET_USER_PUBLISH_PROJECT_FAILURE);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //服务器响应失败
                if(response==null||response.isSuccessful()==false){
                    handler.sendEmptyMessage(GET_USER_PUBLISH_PROJECT_FAILURE);
                    return;
                }
                Gson gson=new GsonBuilder().create();
                String str_response=response.body().string();
                project=new UserPublishProject();
                try {

                    //用Gson进行解析，并判断结果是否为空
                    if((project = gson.fromJson(str_response, project.getClass()))==null){
                        handler.sendEmptyMessage(GET_USER_PUBLISH_PROJECT_FAILURE);
                        return;
                    }
                    //搜索用户发起项目失败
                    if(project.isResult()==false){
                        switch (project.getErrCode()){
                            case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                                handler.sendEmptyMessage(ErrorCode.REQUEST_TOO_FRENQUENTLY);
                                break;
                            case ErrorCode.PARAMETER_ERROR:
                                handler.sendEmptyMessage(ErrorCode.PARAMETER_ERROR);
                                break;
                            default:
                                handler.sendEmptyMessage(GET_USER_PUBLISH_PROJECT_FAILURE);
                                break;
                        }
                        return;
                    }

                    //搜索用户发起项目成功
                    handler.sendEmptyMessage(GET_USER_PUBLISH_PROJECT_SUCCESS);
                }catch (IllegalStateException e){
                    handler.sendEmptyMessage(GET_USER_PUBLISH_PROJECT_FAILURE);
                    e.printStackTrace();
                }catch (JsonSyntaxException e){
                    handler.sendEmptyMessage(GET_USER_PUBLISH_PROJECT_FAILURE);
                    e.printStackTrace();
                }
            }
        });

    }

    //停止更新的动画
    private void endRefresh(){
        isFinishRequest=true;
        lv_PJ_list.stopRefresh();
        lv_PJ_list.stopLoadMore();
        lv_PJ_list.setRefreshTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
    }
}
