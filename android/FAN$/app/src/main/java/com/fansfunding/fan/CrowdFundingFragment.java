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

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.fansfunding.PullListView.XListView;
import com.fansfunding.fan.request.RequestCategory;
import com.fansfunding.fan.utils.ErrorHandler;
import com.fansfunding.fan.utils.FANRequestCode;
import com.fansfunding.fan.utils.MyGridView;
import com.fansfunding.internal.AllProjectInCategory;
import com.fansfunding.internal.CategoryInfo;
import com.fansfunding.internal.ErrorCode;
import com.fansfunding.internal.ProjectInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import cn.finalteam.galleryfinal.widget.HorizontalListView;
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
    private ListProjectAdapter adapter;

    //获取到的项目列表
    private List<ProjectInfo> projectDetailList=null;

    //httpclient
    //private OkHttpClient httpClient;
    //热门项目列表
    private XListView lv_PR;

    //每次获取的数量
    private  final int rows=10;

    //获取的页数
    private int page=1;

    //广告
    private ConvenientBanner iv_crowdfunding_header_banner;

    //分类展示
    private MyGridView lv_crowdfunding_header_category;

    //获取分类信息
    private RequestCategory requestCategory;

    //适配器
    private CrowdFundingAdapter crowdFundingAdapter;

    //是否加在头部
    private boolean isAddAtHead=false;

    //消息处理
    private ErrorHandler handler=new ErrorHandler(getActivity()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GET_HOT_PROJECT_FAILURE:
                    endRefresh();
                    if(CrowdFundingFragment.this.getActivity().isFinishing()==false){
                        Toast.makeText(CrowdFundingFragment.this.getActivity(),"获取项目失败",Toast.LENGTH_LONG).show();
                    }
                    break;
                case GET_HOT_PROJECT_SUCCESS:
                    if(projectDetailList.size()<rows){
                        //已结获取到最后一页，再从第一页开始获取
                        page=1;
                        lv_PR.setPullLoadEnable(false);
                        lv_PR.setAutoLoadEnable(false);
                    }else {
                        //获取完本页后，获取下一页的内容
                        lv_PR.setPullLoadEnable(true);
                        lv_PR.setAutoLoadEnable(true);
                        page++;
                    }

                    for(int i=0;i<projectDetailList.size();i++){

                        adapter.addItemAtHead(projectDetailList.get(i));

                    }
                    adapter.notifyDataSetChanged();
                    endRefresh();
                    break;
                case FANRequestCode.GET_PROJECT_CATEGORY_SUCCESS:
                    for(int i=1;i<requestCategory.getCategoryInfo().getData().size();i++){
                        crowdFundingAdapter.addItem(requestCategory.getCategoryInfo().getData().get(i));
                        Log.i("TAG",requestCategory.getCategoryInfo().getData().get(i).getName());
                    }
                    crowdFundingAdapter.notifyDataSetChanged();
                    break;
                case FANRequestCode.GET_PROJECT_CATEGORY_FAILURE:
                    if(CrowdFundingFragment.this.getActivity().isFinishing()==false){
                        Toast.makeText(CrowdFundingFragment.this.getActivity(),"获取项目分类失败",Toast.LENGTH_LONG).show();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
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

        handler.setContext(getActivity());
        requestCategory=new RequestCategory();
        crowdFundingAdapter=new CrowdFundingAdapter(getActivity());
        adapter=new ListProjectAdapter(this.getActivity());


        View rootView=inflater.inflate(R.layout.fragment_crowdfunding, container, false);
        //热门推荐的listview
        lv_PR=(XListView)rootView.findViewById(R.id.lv_popularRecommendation);
        lv_PR.setAutoLoadEnable(false);
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
                adapter.Clear();
                page=1;
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




        //将其他view作为热门推荐listview的头部view
        View listHeader=inflater.inflate(R.layout.fragment_crowdfundingheader, null, false);
        lv_PR.addHeaderView(listHeader,null,false);
        iv_crowdfunding_header_banner=(ConvenientBanner)listHeader.findViewById(R.id.iv_crowdfunding_header_banner);
        lv_crowdfunding_header_category=(MyGridView)listHeader.findViewById(R.id.lv_crowdfunding_header_category);


        lv_PR.setAdapter(adapter);
        lv_crowdfunding_header_category.setAdapter(crowdFundingAdapter);


        lv_crowdfunding_header_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CategoryInfo.DataBean category=(CategoryInfo.DataBean)lv_crowdfunding_header_category.getAdapter().getItem(position);
                if(category==null){
                    return;
                }
                Intent intent=new Intent();
                intent.setAction(getString(R.string.activity_category));
                intent.putExtra(CategoryActivity.CATEGORY_NAME,category.getName());
                intent.putExtra(CategoryActivity.CATEGORY_ID,category.getId());
                startActivity(intent);
            }
        });




        //获取项目信息





        lv_PR.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                ProjectInfo detail=(ProjectInfo)lv_PR.getAdapter().getItem(position);
                intent.setAction(getString(R.string.activity_project_detail));
                intent.putExtra("detail",detail);
                startActivity(intent);

            }
        });


        //进入搜索界面
        Toolbar toolbar =(Toolbar)rootView.findViewById(R.id.toolbar_crowdfundinghead);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(getString(R.string.activity_search));
                startActivity(intent);
            }
        });


        OkHttpClient httpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
        requestCategory.requestCategory(getActivity(),handler,httpClient);
        getProject();
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

    //获取所有项目
    private void getProject(){
        OkHttpClient httpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();

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
                handler.sendEmptyMessage(GET_HOT_PROJECT_FAILURE);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //服务器响应失败
                if(response==null||response.isSuccessful()==false){
                    handler.sendEmptyMessage(GET_HOT_PROJECT_FAILURE);
                    return;
                }
                Gson gson=new GsonBuilder().create();
                AllProjectInCategory project=new AllProjectInCategory();
                String str_response=response.body().string();
                try {

                    //用Gson进行解析，并判断结果是否为空
                    if((project = gson.fromJson(str_response, project.getClass()))==null){
                        handler.sendEmptyMessage(GET_HOT_PROJECT_FAILURE);
                        return;
                    }
                    //项目获取失败
                    if(project.isResult()==false){
                        handler.handlerFanErrorMessage(project.getErrCode());
                        handler.sendEmptyMessage(GET_HOT_PROJECT_FAILURE);
                        return;
                    }


                    Log.i("TAG","crowdfundingfragment:"+str_response);
                    projectDetailList=project.getData().getList();

                    //获取项目信息成功
                    handler.sendEmptyMessage(GET_HOT_PROJECT_SUCCESS);
                }catch (IllegalStateException e){
                    handler.sendEmptyMessage(GET_HOT_PROJECT_FAILURE);
                    e.printStackTrace();
                }catch (JsonSyntaxException e){
                    handler.sendEmptyMessage(GET_HOT_PROJECT_FAILURE);
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
