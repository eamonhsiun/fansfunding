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
import android.widget.Toast;

import com.fansfunding.PullListView.XListView;
import com.fansfunding.fan.project.adapter.ProjectDetailRewardAdapter;
import com.fansfunding.fan.R;
import com.fansfunding.internal.ErrorCode;
import com.fansfunding.internal.ProjectDetailReward;
import com.fansfunding.internal.ProjectInfo;
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


    //回报图片


    //项目描述信息(比如目标金额之类的)
    private ProjectInfo projectDetail;

    //获取的回报信息
    private ProjectDetailReward reward;
    //展示回报列表
    private XListView lv_project_detail_reward;

    //列表适配器
    private ProjectDetailRewardAdapter adapter;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GET_PROJECT_DETAIL_REWARD_SUCCESS:
                    endRefresh();
                    for(int i=0;i<reward.getData().getList().size();i++){
                        if(reward.getData().getList().get(i)!=null)
                            adapter.addItem(reward.getData().getList().get(i));
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case GET_PROJECT_DETAIL_REWARD_FAILURE:
                    if(ProjectDetailRewardFragment.this.getActivity()==null){
                        break;
                    }
                    endRefresh();
                    Toast.makeText(ProjectDetailRewardFragment.this.getActivity(),"获取回报信息失败",Toast.LENGTH_LONG).show();
                    break;
                case ErrorCode.PARAMETER_ERROR:
                    if(ProjectDetailRewardFragment.this.getActivity()==null){
                        break;
                    }
                    endRefresh();
                    Toast.makeText(ProjectDetailRewardFragment.this.getActivity(),"参数错误",Toast.LENGTH_LONG).show();
                    break;
                case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                    if(ProjectDetailRewardFragment.this.getActivity()==null){
                        break;
                    }
                    endRefresh();
                    Toast.makeText(ProjectDetailRewardFragment.this.getActivity(),"请求过于频繁",Toast.LENGTH_LONG).show();
                    break;
            }
            super.handleMessage(msg);
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

        adapter=new ProjectDetailRewardAdapter(getActivity());

        lv_project_detail_reward=(XListView)rootView.findViewById(R.id.lv_project_detail_reward);
        lv_project_detail_reward.setAutoLoadEnable(false);
        lv_project_detail_reward.setPullLoadEnable(false);
        lv_project_detail_reward.setPullRefreshEnable(false);
        lv_project_detail_reward.setRefreshTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        lv_project_detail_reward.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                if(isFinishRequest==true){
                    getProjectDetailReward();
                }
            }

            @Override
            public void onLoadMore() {

            }
        });

        lv_project_detail_reward.setAdapter(adapter);
        getProjectDetailReward();
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

    private void getProjectDetailReward(){
        OkHttpClient httpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
        Request request=new Request.Builder()
                .get()
                .url(getString(R.string.url_project)+categoryId+"/"+projectId+"/feedbacks")
                .build();
        Call call=httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg=new Message();
                msg.what=GET_PROJECT_DETAIL_REWARD_FAILURE;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //服务器响应失败
                if(response==null||response.isSuccessful()==false){
                    Message msg=new Message();
                    msg.what=GET_PROJECT_DETAIL_REWARD_FAILURE;
                    handler.sendMessage(msg);
                    return;
                }
                Gson gson=new GsonBuilder().create();
                String str_response=response.body().string();
                Log.i("TAG","REWARD:"+str_response) ;
                reward=new ProjectDetailReward();
                try {

                    //用Gson进行解析，并判断结果是否为空
                    if((reward = gson.fromJson(str_response, reward.getClass()))==null){
                        Message msg=new Message();
                        msg.what=GET_PROJECT_DETAIL_REWARD_FAILURE;
                        handler.sendMessage(msg);
                        return;
                    }
                    //获取项目详情失败
                    if(reward.isResult()==false){
                        Message msg=new Message();
                        switch (reward.getErrCode()){
                            case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                                msg.what=ErrorCode.REQUEST_TOO_FRENQUENTLY;
                                break;
                            case ErrorCode.PARAMETER_ERROR:
                                msg.what=ErrorCode.PARAMETER_ERROR;
                                break;
                            default:
                                msg.what=GET_PROJECT_DETAIL_REWARD_FAILURE;
                                break;
                        }
                        handler.sendMessage(msg);
                        return;
                    }

                    //获取项目信息成功
                    Message msg=new Message();
                    msg.what=GET_PROJECT_DETAIL_REWARD_SUCCESS;
                    handler.sendMessage(msg);
                }catch (IllegalStateException e){
                    Message msg=new Message();
                    msg.what=GET_PROJECT_DETAIL_REWARD_FAILURE;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }catch (JsonSyntaxException e){
                    Message msg=new Message();
                    msg.what=GET_PROJECT_DETAIL_REWARD_FAILURE;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            }
        });
    }

}
