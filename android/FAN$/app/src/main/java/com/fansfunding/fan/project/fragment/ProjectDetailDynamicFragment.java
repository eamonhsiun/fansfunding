package com.fansfunding.fan.project.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.fansfunding.PullListView.LoadListView;
import com.fansfunding.PullListView.XListView;
import com.fansfunding.fan.project.activity.ProjectDetailActivity;
import com.fansfunding.fan.project.adapter.ProjectDetailDynamicAdapter;
import com.fansfunding.fan.R;
import com.fansfunding.fan.project.adapter.ProjectDetailDynamicPhotoAdapter;
import com.fansfunding.fan.request.RequestProjectDetailDynamic;
import com.fansfunding.fan.utils.ErrorHandler;
import com.fansfunding.fan.utils.FANRequestCode;
import com.fansfunding.fan.utils.ProjectDetailListView;
import com.fansfunding.internal.ErrorCode;
import com.fansfunding.internal.ProjectDetailDynamic;
import com.fansfunding.internal.ProjectInfo;
import com.fansfunding.verticalslide.CustListView;
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
 * Use the {@link ProjectDetailDynamicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectDetailDynamicFragment extends Fragment {
    //是否已经完成了项目数据获取的请求
    private boolean isFinishRequest=true;

    private FloatingActionButton btnCreateDynamic;
    //动态内容
    //private ProjectDetailDynamic dynamic;

    //list适配器
    private ProjectDetailDynamicAdapter adapter;

    //动态展示适配器
    private ProjectDetailListView lv_project_detail_reward;

    //httpclient
    private OkHttpClient httpClient;
    //获取动态
    RequestProjectDetailDynamic requestProjectDetailDynamic;

    //项目分类
    private int categoryId;

    //项目Id
    private int projectId;


    //项目描述信息(比如目标金额之类的)
    private ProjectInfo projectDetail;



    private ErrorHandler handler=new ErrorHandler(this.getActivity()){
        @Override
        public void handleMessage(Message msg) {
            endRefresh();
            switch (msg.what) {
                case FANRequestCode.GET_PROJECT_DETAIL_MOMENT_SUCCESS:
                    if(requestProjectDetailDynamic.getDynamic().getData().getList().size()< requestProjectDetailDynamic.getRows()){
                        requestProjectDetailDynamic.setPage(1);
                        lv_project_detail_reward.setPullLoadEnable(false);
                        lv_project_detail_reward.setAutoLoadEnable(false);
                    }else{
                        requestProjectDetailDynamic.setPage(requestProjectDetailDynamic.getPage()+1);
                        lv_project_detail_reward.setPullLoadEnable(true);
                        lv_project_detail_reward.setAutoLoadEnable(true);
                    }
                    for(int i=0;i<requestProjectDetailDynamic.getDynamic().getData().getList().size();i++){
                        if(requestProjectDetailDynamic.getDynamic().getData().getList().get(i)!=null)
                            adapter.addItem(requestProjectDetailDynamic.getDynamic().getData().getList().get(i));
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case FANRequestCode.GET_PROJECT_DETAIL_MOMENT_FAILURE:
                    if (ProjectDetailDynamicFragment.this.getActivity() == null) {
                        break;
                    }
                    Toast.makeText(ProjectDetailDynamicFragment.this.getActivity(), "获取动态信息失败", Toast.LENGTH_LONG).show();
                    break;
                /*case ErrorCode.PARAMETER_ERROR:
                    if (ProjectDetailDynamicFragment.this.getActivity() == null) {
                        break;
                    }
                    Toast.makeText(ProjectDetailDynamicFragment.this.getActivity(), "参数错误", Toast.LENGTH_LONG).show();
                    break;
                case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                    if (ProjectDetailDynamicFragment.this.getActivity() == null) {
                        break;
                    }
                    Toast.makeText(ProjectDetailDynamicFragment.this.getActivity(), "请求过于频繁", Toast.LENGTH_LONG).show();
                    break;*/
                default:
                    super.handleMessage(msg);
            }

        }

    };


    public ProjectDetailDynamicFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment P0rojectDetailDynamicFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProjectDetailDynamicFragment newInstance (ProjectInfo projectDetail){
        ProjectDetailDynamicFragment fragment = new ProjectDetailDynamicFragment();
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
        View rootView=inflater.inflate(R.layout.fragment_project_detail_dynamic, container, false);
        requestProjectDetailDynamic=new RequestProjectDetailDynamic();
        httpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
        lv_project_detail_reward=(ProjectDetailListView)rootView.findViewById(R.id.lv_project_detail_dynamic);
        lv_project_detail_reward.setAutoLoadEnable(false);
        lv_project_detail_reward.setPullLoadEnable(false);
        lv_project_detail_reward.setPullRefreshEnable(false);
        lv_project_detail_reward.setRefreshTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        lv_project_detail_reward.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadMore() {
                if(isFinishRequest==true){
                    isFinishRequest=false;
                    requestProjectDetailDynamic.getProjectDetailDynamic(ProjectDetailDynamicFragment.this.getActivity(),handler,httpClient,categoryId,projectId);
                }
            }
        });

        btnCreateDynamic= (FloatingActionButton) rootView.findViewById(R.id.btn_create_dynamic);


        adapter=new ProjectDetailDynamicAdapter(this.getActivity());
        lv_project_detail_reward.setAdapter(adapter);

        //设置添加按钮的可见性
        SharedPreferences share=getActivity().getSharedPreferences(getString(R.string.sharepreference_login_by_phone),Context.MODE_PRIVATE);
        int userId=share.getInt("id",0);
        if(userId==projectDetail.getSponsor()){
            btnCreateDynamic.setVisibility(View.VISIBLE);
            btnCreateDynamic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent();
                    intent.setAction(getResources().getString(R.string.activity_publish_dynamic));
                    intent.putExtra("projectId", projectId);
                    startActivity(intent);
                }
            });
        }else {
            btnCreateDynamic.setVisibility(View.GONE);
        }



        //获取动态信息
        requestProjectDetailDynamic.getProjectDetailDynamic(this.getActivity(),handler,httpClient,categoryId,projectId);

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
        lv_project_detail_reward.stopRefresh();
        lv_project_detail_reward.stopLoadMore();
        lv_project_detail_reward.setRefreshTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
    }

    /*//获取动态信息
    private void getProjectDetailDynamic(){
        OkHttpClient okHttpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();


        Request request=new Request.Builder()
                .get()
                .url(getString(R.string.url_project)+categoryId+"/"+projectId+"/moment")
                .build();

        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg=new Message();
                msg.what=GET_PROJECT_DETAIL_MOMENT_FAILURE;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //服务器响应失败
                if(response==null||response.isSuccessful()==false){
                    Message msg=new Message();
                    msg.what=GET_PROJECT_DETAIL_MOMENT_FAILURE;
                    handler.sendMessage(msg);
                    return;
                }
                Gson gson=new GsonBuilder().create();
                String str_response=response.body().string();
                Log.i("TAG","Dynamic:"+str_response) ;
                dynamic=new ProjectDetailDynamic();
                try {

                    //用Gson进行解析，并判断结果是否为空
                    if((dynamic = gson.fromJson(str_response, dynamic.getClass()))==null){
                        Message msg=new Message();
                        msg.what=GET_PROJECT_DETAIL_MOMENT_FAILURE;
                        handler.sendMessage(msg);
                        return;
                    }
                    //获取项目详情失败
                    if(dynamic.isResult()==false){
                        Message msg=new Message();
                        switch (dynamic.getErrCode()){
                            case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                                msg.what=ErrorCode.REQUEST_TOO_FRENQUENTLY;
                                break;
                            case ErrorCode.PARAMETER_ERROR:
                                msg.what=ErrorCode.PARAMETER_ERROR;
                                break;
                            default:
                                msg.what=GET_PROJECT_DETAIL_MOMENT_FAILURE;
                                break;
                        }
                        handler.sendMessage(msg);
                        return;
                    }
                    //获取项目信息成功
                    Message msg=new Message();
                    msg.what=GET_PROJECT_DETAIL_MOMENT_SUCCESS;
                    handler.sendMessage(msg);
                }catch (IllegalStateException e){
                    Message msg=new Message();
                    msg.what=GET_PROJECT_DETAIL_MOMENT_FAILURE;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }catch (JsonSyntaxException e){
                    Message msg=new Message();
                    msg.what=GET_PROJECT_DETAIL_MOMENT_FAILURE;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            }

        });
    }*/

}
