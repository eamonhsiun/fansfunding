package com.fansfunding.fan.project.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.fansfunding.PullListView.XListView;
import com.fansfunding.fan.project.adapter.ProjectDetailRewardAdapter;
import com.fansfunding.fan.R;
import com.fansfunding.fan.request.RequestProjectDetailReward;
import com.fansfunding.fan.utils.ErrorHandler;
import com.fansfunding.fan.utils.FANRequestCode;
import com.fansfunding.fan.utils.ProjectDetailListView;
import com.fansfunding.internal.ErrorCode;
import com.fansfunding.internal.ProjectDetailReward;
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
 * Use the {@link ProjectDetailRewardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectDetailRewardFragment extends Fragment {

    //获取项目回报成功
    private static final int GET_PROJECT_DETAIL_REWARD_SUCCESS=100;

    //获取项目回报失败
    private static final int GET_PROJECT_DETAIL_REWARD_FAILURE=101;
    //项目分类
    private int categoryId;

    //项目Id
    private int projectId;

    //是否已经完成了项目数据获取的请求
    private boolean isFinishRequest=true;

    //请求回报信息
    private RequestProjectDetailReward requestProjectDetailReward;

    //回报图片

    //项目描述信息(比如目标金额之类的)
    private ProjectInfo projectDetail;

    //获取的回报信息
    //private ProjectDetailReward reward;
    //展示回报列表
    private ProjectDetailListView lv_project_detail_reward;

    //列表适配器
    private ProjectDetailRewardAdapter adapter;

    //httpclient
    private OkHttpClient httpClient;

    private ErrorHandler handler=new ErrorHandler(this.getActivity()){
        @Override
        public void handleMessage(Message msg) {
            //结束刷新动画
            endRefresh();
            switch (msg.what){
                case FANRequestCode.GET_PROJECT_DETAIL_REWARD_SUCCESS:
                    if(requestProjectDetailReward.getReward().getData().getList().size()< requestProjectDetailReward.getRows()){
                        requestProjectDetailReward.setPage(1);
                        lv_project_detail_reward.setPullLoadEnable(false);
                        lv_project_detail_reward.setAutoLoadEnable(false);
                    }else{
                        requestProjectDetailReward.setPage(requestProjectDetailReward.getPage()+1);
                        lv_project_detail_reward.setPullLoadEnable(true);
                        lv_project_detail_reward.setAutoLoadEnable(true);
                    }
                    for(int i=0;i<requestProjectDetailReward.getReward().getData().getList().size();i++){
                        if(requestProjectDetailReward.getReward().getData().getList().get(i)!=null)
                            adapter.addItem(requestProjectDetailReward.getReward().getData().getList().get(i));
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case FANRequestCode.GET_PROJECT_DETAIL_REWARD_FAILURE:
                    if(ProjectDetailRewardFragment.this.getActivity()==null){
                        break;
                    }
                    Toast.makeText(ProjectDetailRewardFragment.this.getActivity(),"获取回报信息失败",Toast.LENGTH_LONG).show();
                    break;

                default:
                    super.handleMessage(msg);
            }

        }
    };

    public ProjectDetailRewardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment P0rojectDetailRewardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProjectDetailRewardFragment newInstance(ProjectInfo projectDetail) {
        ProjectDetailRewardFragment fragment = new ProjectDetailRewardFragment();
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
        View rootView= inflater.inflate(R.layout.fragment_project_detail_reward, container, false);
        handler.setContext(this.getActivity());
        httpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
        requestProjectDetailReward=new RequestProjectDetailReward();
        adapter=new ProjectDetailRewardAdapter(getActivity());

        lv_project_detail_reward=(ProjectDetailListView)rootView.findViewById(R.id.lv_project_detail_reward);
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
                    requestProjectDetailReward.getProjectDetailReward(ProjectDetailRewardFragment.this.getActivity(),handler,httpClient,categoryId,projectId);}
            }
        });

        lv_project_detail_reward.setAdapter(adapter);
        //getProjectDetailReward();
        requestProjectDetailReward.getProjectDetailReward(this.getActivity(),handler,httpClient,categoryId,projectId);
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
    //停止更新的动画
    private void endRefresh(){
        isFinishRequest=true;
        lv_project_detail_reward.stopRefresh();
        lv_project_detail_reward.stopLoadMore();
        lv_project_detail_reward.setRefreshTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
    }

}
