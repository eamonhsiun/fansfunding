package com.fansfunding.fan;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.fansfunding.PullListView.XListView;
import com.fansfunding.internal.AllProjectInCategory;
import com.fansfunding.internal.ErrorCode;
import com.fansfunding.internal.ProjectInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CrowdFundingFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * 用来创建众筹界面
 */
public class CrowdFundingFragment extends Fragment {

    //获取项目成功
    private static final int GET_HOT_PROJECT_SUCCESS=100;

    //获取项目失败
    private static final int GET_HOT_PROJECT_FAILURE=101;

    //是否已经完成了项目数据获取的请求
    private boolean isFinishRequest=true;

    //listview适配器
    ListProjectAdapter adapter;

    //获取到的项目列表
    private List<ProjectInfo> projectDetailList=null;

    //httpclient
    private OkHttpClient httpClient;
    //热门项目列表
    private XListView lv_PR;

    //每次获取的数量
    private  final int rows=10;

    //获取的页数
    private int page=1;

    //是否加在头部
    private boolean isAddAtHead=false;

    //消息处理
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GET_HOT_PROJECT_FAILURE:
                    endRefresh();
                    if(CrowdFundingFragment.this.getActivity()==null){
                        break;
                    }
                    Toast.makeText(CrowdFundingFragment.this.getActivity(),"获取项目失败",Toast.LENGTH_LONG).show();
                    break;
                case GET_HOT_PROJECT_SUCCESS:
                    if(projectDetailList.size()<rows){
                        //已结获取到最后一页，再从第一页开始获取
                        page=1;
                        lv_PR.setPullLoadEnable(false);
                    }else {
                        //获取完本页后，获取下一页的内容
                        lv_PR.setPullLoadEnable(true);
                        page++;
                    }
                    for(int i=0;i<projectDetailList.size();i++){
                        //加载头部
                        adapter.addItemAtHead(projectDetailList.get(i));
                        /*if(isAddAtHead==true){
                            adapter.addItemAtHead(projectDetailList.get(i));
                        }
                        //加载在尾部
                        else{
                            adapter.addItemAtFoot(projectDetailList.get(i));
                        }*/

                    }
                    adapter.notifyDataSetChanged();
                    endRefresh();
                    break;
                case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                    endRefresh();
                    if(CrowdFundingFragment.this.getActivity()==null){
                        break;
                    }
                    Toast.makeText(CrowdFundingFragment.this.getActivity(),"请求过于频繁",Toast.LENGTH_LONG).show();

                    break;
                case ErrorCode.PARAMETER_ERROR:
                    endRefresh();
                    if(CrowdFundingFragment.this.getActivity()==null){
                        break;
                    }
                    Toast.makeText(CrowdFundingFragment.this.getActivity(),"参数错误",Toast.LENGTH_LONG).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };


    public CrowdFundingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment CrowdFundingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CrowdFundingFragment newInstance() {
        CrowdFundingFragment fragment = new CrowdFundingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //构建httpclient
        httpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();

        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_crowdfunding, container, false);
        //热门推荐的listview
        lv_PR=(XListView)rootView.findViewById(R.id.lv_popularRecommendation);
        lv_PR.setAutoLoadEnable(true);
        lv_PR.setPullLoadEnable(false);

        lv_PR.setPullRefreshEnable(true);
        lv_PR.setRefreshTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        lv_PR.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                if(isFinishRequest==false){
                    return;
                }
                isAddAtHead=true;
                getProject();

            }

            @Override
            public void onLoadMore() {
                if(isFinishRequest==false){
                    return;
                }
                isAddAtHead=false;
                getProject();
            }
        });


        adapter=new ListProjectAdapter(this.getActivity());
        lv_PR.setAdapter(adapter);
        getProject();

        //将其他view作为热门推荐listview的头部view
        /*
        * 先暂时取消banner和分类，以后再加上去
        * */
        //View listHeader=inflater.inflate(R.layout.fragment_crowdfundingheader, null, false);
        //lv_PR.addHeaderView(listHeader,null,false);




        //获取项目信息






        /*

        View view_cosmetic=listHeader.findViewById(R.id.ll_cosmetic);
        view_cosmetic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //用来设置新activity的actionbar的title部
                intent.putExtra(getResources().getString(R.string.actionbar_title),"美妆");
                intent.setAction(getResources().getString(R.string.activity_category));
                startActivity(intent);
            }
        });

        View view_food=listHeader.findViewById(R.id.ll_food);
        view_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //用来设置新activity的actionbar的title部
                intent.putExtra(getResources().getString(R.string.actionbar_title),"美食");
                intent.setAction(getResources().getString(R.string.activity_category));
                startActivity(intent);
            }
        });

        View view_game=listHeader.findViewById(R.id.ll_game);
        view_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //用来设置新activity的actionbar的title部
                intent.putExtra(getResources().getString(R.string.actionbar_title),"游戏");
                intent.setAction(getResources().getString(R.string.activity_category));
                startActivity(intent);
            }
        });

        View view_literature=listHeader.findViewById(R.id.ll_literature);
        view_literature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //用来设置新activity的actionbar的title部
                intent.putExtra(getResources().getString(R.string.actionbar_title),"文学");
                intent.setAction(getResources().getString(R.string.activity_category));
                startActivity(intent);
            }
        });*/

        lv_PR.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                ProjectInfo detail=(ProjectInfo)lv_PR.getAdapter().getItem(position);
                int categoryId=detail.getCategoryId();
                int projectId=detail.getId();
                intent.setAction(getString(R.string.activity_project_detail));
                intent.putExtra("detail",detail);
                startActivity(intent);

            }
        });

        Toolbar toolbar =(Toolbar)rootView.findViewById(R.id.toolbar_crowdfundinghead);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(getString(R.string.activity_search));
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
    //获取
    private void getProject(){
        isFinishRequest=false;
        int catagoryId=1;
        Request request=new Request.Builder()
                .get()
                .url(getString(R.string.url_project)+catagoryId+"?rows="+rows+"&page="+page)
                .build();

        Call call=httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg=new Message();
                msg.what=GET_HOT_PROJECT_FAILURE;
                handler.sendMessage(msg);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //服务器响应失败
                if(response==null||response.isSuccessful()==false){
                    Message msg=new Message();
                    msg.what=GET_HOT_PROJECT_FAILURE;
                    handler.sendMessage(msg);
                    return;
                }
                Gson gson=new GsonBuilder().create();
                AllProjectInCategory project=new AllProjectInCategory();
                String str_response=response.body().string();
                try {

                    //用Gson进行解析，并判断结果是否为空
                    if((project = gson.fromJson(str_response, project.getClass()))==null){
                        Message msg=new Message();
                        msg.what=GET_HOT_PROJECT_FAILURE;
                        handler.sendMessage(msg);
                        isFinishRequest=true;
                        return;
                    }
                    //项目获取失败
                    if(project.isResult()==false){
                        Message msg=new Message();
                        switch (project.getErrCode()){
                            case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                                msg.what=ErrorCode.REQUEST_TOO_FRENQUENTLY;
                                break;
                            case ErrorCode.PARAMETER_ERROR:
                                msg.what=ErrorCode.PARAMETER_ERROR;
                                break;
                            default:
                                msg.what=GET_HOT_PROJECT_FAILURE;
                                break;
                        }
                        handler.sendMessage(msg);
                        return;
                    }


                    //FLog.i("TAG","crowdfundingfragment:"+str_response);
                    projectDetailList=project.getData().getList();

                    //获取项目信息成功
                    Message msg=new Message();
                    msg.what=GET_HOT_PROJECT_SUCCESS;
                    handler.sendMessage(msg);
                }catch (IllegalStateException e){
                    Message msg=new Message();
                    msg.what=GET_HOT_PROJECT_FAILURE;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }catch (JsonSyntaxException e){
                    Message msg=new Message();
                    msg.what=GET_HOT_PROJECT_FAILURE;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            }
        });
    }

    //停止更新的动画
    private void endRefresh(){
        isFinishRequest=true;
        lv_PR.stopRefresh();
        lv_PR.stopLoadMore();
        lv_PR.setRefreshTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
    }

}
