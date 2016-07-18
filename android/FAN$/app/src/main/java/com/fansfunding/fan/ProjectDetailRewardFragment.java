package com.fansfunding.fan;

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
import android.widget.SimpleAdapter;

import com.fansfunding.PullListView.XListView;
import com.fansfunding.internal.AllProjectInCategory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    //项目描述信息(比如目标金额之类的)
    private AllProjectInCategory.ProjectDetail projectDetail;

    //展示回报列表
    private XListView lv_project_detail_reward;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GET_PROJECT_DETAIL_REWARD_SUCCESS:
                    break;
                case GET_PROJECT_DETAIL_REWARD_FAILURE:
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
     * @return A new instance of fragment ProjectDetailRewardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProjectDetailRewardFragment newInstance(AllProjectInCategory.ProjectDetail projectDetail) {
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
            this.projectDetail= (AllProjectInCategory.ProjectDetail) getArguments().getSerializable("projectDetail");
            categoryId=projectDetail.getCategoryId();
            projectId=projectDetail.getId();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_project_detail_reward, container, false);
        XListView lv_project_detail_reward=(XListView)rootView.findViewById(R.id.lv_project_detail_reward);
        lv_project_detail_reward.setAutoLoadEnable(false);
        lv_project_detail_reward.setPullLoadEnable(false);
        lv_project_detail_reward.setPullRefreshEnable(true);
        lv_project_detail_reward.setRefreshTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        lv_project_detail_reward.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {

            }
        });
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
        OkHttpClient httpClient=new OkHttpClient();
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
                Log.i("TAG",str_response);
            }
        });
    }
}
