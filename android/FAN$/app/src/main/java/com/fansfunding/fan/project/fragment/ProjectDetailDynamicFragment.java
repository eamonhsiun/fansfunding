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

import com.fansfunding.fan.project.adapter.ProjectDetailDynamicAdapter;
import com.fansfunding.fan.R;
import com.fansfunding.internal.ErrorCode;
import com.fansfunding.internal.ProjectDetailDynamic;
import com.fansfunding.internal.ProjectInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
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

    //获取动态成功
    private final static int GET_PROJECT_DETAIL_MOMENT_SUCCESS=100;

    //获取动态失败
    private final static int GET_PROJECT_DETAIL_MOMENT_FAILURE=101;

    //动态内容
    private ProjectDetailDynamic dynamic;

    //list适配器
    private ProjectDetailDynamicAdapter adapter;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_PROJECT_DETAIL_MOMENT_SUCCESS:
                    for(int i=0;i<dynamic.getData().getList().size();i++){
                        if(dynamic.getData().getList().get(i)!=null)
                            adapter.addItem(dynamic.getData().getList().get(i));
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case GET_PROJECT_DETAIL_MOMENT_FAILURE:
                    if (ProjectDetailDynamicFragment.this.getActivity() == null) {
                        break;
                    }
                    Toast.makeText(ProjectDetailDynamicFragment.this.getActivity(), "获取动态信息失败", Toast.LENGTH_LONG).show();
                    break;
                case ErrorCode.PARAMETER_ERROR:
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
                    break;
            }
            super.handleMessage(msg);
        }

    };

    //项目分类
    private int categoryId;

    //项目Id
    private int projectId;
    //项目描述信息(比如目标金额之类的)
    private ProjectInfo projectDetail;

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
        ListView lv_project_detail_reward=(ListView)rootView.findViewById(R.id.lv_project_detail_dynamic);



        adapter=new ProjectDetailDynamicAdapter(this.getActivity());
        lv_project_detail_reward.setAdapter(adapter);
        //获取动态信息
        getProjectDetailDynamic();
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


    //获取动态信息
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
    }

}
