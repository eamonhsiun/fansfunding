package com.fansfunding.fan;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;

import com.fansfunding.internal.AllProjectInCategory;
import com.fansfunding.internal.ErrorCode;
import com.fansfunding.internal.ProjectDetail;
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
 * Use the {@link ProjectDetailWebFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectDetailWebFragment extends Fragment {


    //项目附件描述信息(比如图片呀，音频呀)
    private ProjectDetail detail;

    //获取项目详情成功
    private static final int GET_PROJECT_DETAIL_SUCCESS=100;

    //获取项目详情失败
    private static final int GET_PROJECT_DETAIL_FAILURE=101;

    //项目分类
    private int categoryId;

    //项目Id
    private int projectId;

    //项目描述信息(比如目标金额之类的)
    private AllProjectInCategory.ProjectDetail projectDetail;

    private com.fansfunding.verticalslide.CustWebView webView;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                //获取项目详情成功
                case GET_PROJECT_DETAIL_SUCCESS:
                    InitWeb();
                    Log.i("TAG","webSuccess");
                    break;
                //获取项目详情失败
                case GET_PROJECT_DETAIL_FAILURE:
                    if(ProjectDetailWebFragment.this.getActivity()==null){
                        break;
                    }
                    Toast.makeText(ProjectDetailWebFragment.this.getActivity(),"获取项目详情失败",Toast.LENGTH_LONG).show();
                    Log.i("TAG","webfailure");
                    break;

                case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                    if(ProjectDetailWebFragment.this.getActivity()==null){
                        break;
                    }
                    Toast.makeText(ProjectDetailWebFragment.this.getActivity(),"请求过于频繁",Toast.LENGTH_LONG).show();
                    Log.i("TAG","webfailure");
                    break;
                case ErrorCode.PARAMETER_ERROR:
                    if(ProjectDetailWebFragment.this.getActivity()==null){
                        break;
                    }
                    Toast.makeText(ProjectDetailWebFragment.this.getActivity(),"参数错误",Toast.LENGTH_LONG).show();
                    Log.i("TAG","webfailure");
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public ProjectDetailWebFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ProjectDetailWebFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProjectDetailWebFragment newInstance(AllProjectInCategory.ProjectDetail projectDetail) {
        ProjectDetailWebFragment fragment = new ProjectDetailWebFragment();
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
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_project_detail_web, container, false);
        webView=(com.fansfunding.verticalslide.CustWebView)rootView.findViewById(R.id.web_project_detail);
        webView.getSettings().setJavaScriptEnabled(false);

        //获取项目详情信息
        getProjectDetailMain();
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


    private void getProjectDetailMain(){

        OkHttpClient httpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
        Request request=new Request.Builder()
                .get()
                .url(getString(R.string.url_project)+categoryId+"/"+projectId+"/detail")
                .build();
        Call call=httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg=new Message();
                msg.what=GET_PROJECT_DETAIL_FAILURE;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //服务器响应失败
                if(response==null||response.isSuccessful()==false){
                    Message msg=new Message();
                    msg.what=GET_PROJECT_DETAIL_FAILURE;
                    handler.sendMessage(msg);
                    return;
                }
                Gson gson=new GsonBuilder().create();
                String str_response=response.body().string();
                detail =new ProjectDetail();
                try {

                    //用Gson进行解析，并判断结果是否为空
                    if((detail = gson.fromJson(str_response, detail.getClass()))==null){
                        Message msg=new Message();
                        msg.what=GET_PROJECT_DETAIL_FAILURE;
                        handler.sendMessage(msg);
                    }
                    //获取项目详情失败
                    if(detail.isResult()==false){
                        Message msg=new Message();
                        switch (detail.getErrCode()){
                            case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                                msg.what=ErrorCode.REQUEST_TOO_FRENQUENTLY;
                                break;
                            case ErrorCode.PARAMETER_ERROR:
                                msg.what=ErrorCode.PARAMETER_ERROR;
                                break;
                            default:
                                msg.what=GET_PROJECT_DETAIL_FAILURE;
                                break;
                        }
                        handler.sendMessage(msg);
                        return;
                    }


                    //获取项目信息成功
                    Message msg=new Message();
                    msg.what=GET_PROJECT_DETAIL_SUCCESS;
                    handler.sendMessage(msg);
                }catch (IllegalStateException e){
                    Message msg=new Message();
                    msg.what=GET_PROJECT_DETAIL_FAILURE;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }catch (JsonSyntaxException e){
                    Message msg=new Message();
                    msg.what=GET_PROJECT_DETAIL_FAILURE;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            }
        });
    }

    private void InitWeb(){
        if(detail.getData().getContent()!=null&&detail.getData().getContent().equals("")){
            webView.loadData(detail.getData().getContent(),"text/html","utf-8");
        }else{
            webView.loadData("<html><head><meta charset=\"utf-8\"></head><body><p>找不到项目详情资源</p></body></html>","text/html","utf-8");
        }
    }
}
