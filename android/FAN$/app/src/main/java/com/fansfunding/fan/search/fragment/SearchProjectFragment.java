package com.fansfunding.fan.search.fragment;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.fansfunding.PullListView.XListView;
import com.fansfunding.fan.R;
import com.fansfunding.fan.search.adapter.SearchProjectAdapter;
import com.fansfunding.internal.AllProjectInCategory;
import com.fansfunding.internal.ErrorCode;
import com.fansfunding.internal.ProjectInfo;
import com.fansfunding.internal.SearchProject;
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
 * Use the {@link SearchProjectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchProjectFragment extends Fragment {


    //获取项目成功
    private final static int SEARCH_PROJECT_SUCCESS=100;

    //获取项目失败
    private final static int SEARCH_PROJECT_FAILURE=101;



    //httpclient
    private OkHttpClient httpClient;

    //每次获取的数量
    private final int rows=20;

    //当前获取的页数
    private int page=1;

    //搜索的关键字
    private String keyword;

    //搜索返回的结果
    private SearchProject search;

    //是否已经完成搜索了
    private boolean isFinishRequest=true;

    //adapter
    private SearchProjectAdapter adapter;

    //list
    private XListView lv_search;

    //handler
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SEARCH_PROJECT_SUCCESS:
                    //如果搜索的结果为空
                    if(search.getData().getList().size()==0){
                        if(SearchProjectFragment.this.getActivity()!=null){
                            Toast.makeText(SearchProjectFragment.this.getActivity(), "无此项目", Toast.LENGTH_LONG).show();
                        }
                    }

                    if(search.getData().getList().size()<rows){
                        lv_search.setPullLoadEnable(false);
                        lv_search.setAutoLoadEnable(false);
                    }else {
                        page++;
                        lv_search.setPullLoadEnable(true);
                        lv_search.setAutoLoadEnable(true);
                    }

                    for(int i=0;i<search.getData().getList().size();i++){
                        adapter.addItemAtHead(search.getData().getList().get(i));
                    }
                    adapter.notifyDataSetChanged();
                    endRefresh();
                    break;
                case SEARCH_PROJECT_FAILURE:
                    if(SearchProjectFragment.this.getActivity()==null){
                        break;
                    }
                    Toast.makeText(SearchProjectFragment.this.getActivity(), "搜索失败", Toast.LENGTH_LONG).show();

                    break;
                case ErrorCode.PARAMETER_ERROR:
                    if(SearchProjectFragment.this.getActivity()==null){
                        break;
                    }
                    Toast.makeText(SearchProjectFragment.this.getActivity(), "参数错误", Toast.LENGTH_LONG).show();
                    break;
                case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                    if(SearchProjectFragment.this.getActivity()==null){
                        break;
                    }
                    Toast.makeText(SearchProjectFragment.this.getActivity(), "请求过于频繁", Toast.LENGTH_LONG).show();
                    break;

            }
            super.handleMessage(msg);
        }
    };
    public SearchProjectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SearchProjectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchProjectFragment newInstance(String keyword) {
        SearchProjectFragment fragment = new SearchProjectFragment();
        Bundle args = new Bundle();
        args.putString("keyword",keyword);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            keyword=getArguments().getString("keyword");
        }
        httpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
        adapter=new SearchProjectAdapter(this.getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView=inflater.inflate(R.layout.fragment_search, container, false);

        lv_search=(XListView)rootView.findViewById(R.id.lv_search_project);
        lv_search.setPullLoadEnable(false);
        lv_search.setPullRefreshEnable(false);
        lv_search.setAutoLoadEnable(false);
        lv_search.setRefreshTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        lv_search.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                if(isFinishRequest==true){
                    SearchProject(keyword,page,rows);
                }
            }

            @Override
            public void onLoadMore() {
                if(isFinishRequest==true){
                    SearchProject(keyword,page,rows);
                }
            }
        });

        lv_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                ProjectInfo detail=(ProjectInfo)lv_search.getAdapter().getItem(position);
                int categoryId=detail.getCategoryId();
                int projectId=detail.getId();
                intent.setAction(getString(R.string.activity_project_detail));
                intent.putExtra("detail",detail);
                startActivity(intent);


            }
        });


        if(keyword!=null&&keyword.equals("")==false){
            SearchProject(keyword,page,rows);
        }

        //设置adapter
        lv_search.setAdapter(adapter);
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


    //搜索项目
    private void SearchProject(String keyword, int page, int rows){

        Request request=new Request.Builder()
                .get()
                .url(getString(R.string.url_search_project)+"?keyword="+keyword+"&page="+page+"&rows="+rows)
                .build();

        Call call=httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(SEARCH_PROJECT_FAILURE);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //服务器响应失败
                if(response==null||response.isSuccessful()==false){
                    handler.sendEmptyMessage(SEARCH_PROJECT_FAILURE);
                    return;
                }
                Gson gson=new GsonBuilder().create();
                String str_response=response.body().string();
                search=new SearchProject();
                try {

                    //用Gson进行解析，并判断结果是否为空
                    if((search = gson.fromJson(str_response, search.getClass()))==null){
                        handler.sendEmptyMessage(SEARCH_PROJECT_FAILURE);
                        return;
                    }
                    //搜索项目失败
                    if(search.isResult()==false){
                        switch (search.getErrCode()){
                            case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                                handler.sendEmptyMessage(ErrorCode.REQUEST_TOO_FRENQUENTLY);
                                break;
                            case ErrorCode.PARAMETER_ERROR:
                                handler.sendEmptyMessage(ErrorCode.PARAMETER_ERROR);
                                break;
                            default:
                                handler.sendEmptyMessage(SEARCH_PROJECT_FAILURE);
                                break;
                        }
                        return;
                    }

                    //搜索项目信息成功
                    handler.sendEmptyMessage(SEARCH_PROJECT_SUCCESS);
                }catch (IllegalStateException e){
                    handler.sendEmptyMessage(SEARCH_PROJECT_FAILURE);
                    e.printStackTrace();
                }catch (JsonSyntaxException e){
                    handler.sendEmptyMessage(SEARCH_PROJECT_FAILURE);
                    e.printStackTrace();
                }

            }
        });

    }
    //停止更新的动画
    private void endRefresh(){
        isFinishRequest=true;
        lv_search.stopRefresh();
        lv_search.stopLoadMore();
        lv_search.setRefreshTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
    }

}
