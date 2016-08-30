package com.fansfunding.fan.social.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.fansfunding.PullListView.XListView;
import com.fansfunding.fan.MainActivity;
import com.fansfunding.fan.R;
import com.fansfunding.fan.login.LoginActivity;
import com.fansfunding.fan.project.activity.ProjectCommentActivity;
import com.fansfunding.fan.request.RequestUserFollowUserMoment;
import com.fansfunding.fan.social.activity.MomentActivity;
import com.fansfunding.fan.social.activity.MomentSendActivity;
import com.fansfunding.fan.social.adapter.SocialListAdapter;
import com.fansfunding.fan.utils.ErrorHandler;
import com.fansfunding.fan.utils.FANRequestCode;
import com.fansfunding.fan.utils.LoginSituation;
import com.fansfunding.internal.social.UserMoment;
import com.github.clans.fab.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SocialFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SocialFragment extends Fragment {

    //动态列表
    private XListView lv_social;

    //添加动态按钮
    private FloatingActionButton fab_social_add_moment;

    //用户是否登陆
    private boolean isLogin;

    //用户id
    private int userId;

    //用户token
    private String token;

    //是否完成动态的请求
    private boolean isFinishRequest=true;

    //adapter
    private SocialListAdapter adapter;

    //请求动态信息
    private RequestUserFollowUserMoment requestUserFollowUserMoment;

    //httpclient
    private OkHttpClient httpClient;

    //handler
    private ErrorHandler handler=new ErrorHandler(this.getActivity()){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case FANRequestCode.GET_USER_FOLLOW_USER_MOMENT_SUCCESS:
                    isFinishRequest=true;
                    endRefresh();
                    if(requestUserFollowUserMoment.getUserMoment().getData().getList().size()< requestUserFollowUserMoment.getRows()){
                        requestUserFollowUserMoment.setPage(1);
                        lv_social.setPullLoadEnable(false);
                        lv_social.setAutoLoadEnable(false);
                    }else{
                        requestUserFollowUserMoment.setPage(requestUserFollowUserMoment.getPage()+1);
                        lv_social.setPullLoadEnable(true);
                        lv_social.setAutoLoadEnable(true);
                    }
                    for(int i = 0; i< requestUserFollowUserMoment.getUserMoment().getData().getList().size(); i++){
                        adapter.addItem(requestUserFollowUserMoment.getUserMoment().getData().getList().get(i));
                    }
                    break;
                case FANRequestCode.GET_USER_FOLLOW_USER_MOMENT_FAILURE:
                    isFinishRequest=true;
                    endRefresh();
                    if(SocialFragment.this.getActivity()!=null){
                        Toast.makeText(SocialFragment.this.getActivity(),"获取动态失败",Toast.LENGTH_LONG).show();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }

        }
    };

    public SocialFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment SocialFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SocialFragment newInstance() {
        SocialFragment fragment = new SocialFragment();
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
        View rootView=inflater.inflate(R.layout.fragment_social, container, false);
        Toolbar toolbar=(Toolbar) rootView.findViewById(R.id.toolbar_social);
        toolbar.setTitle("圈子");

        initVariables();
        initViews(rootView);
        loadData();
        return rootView;
    }

    private void initVariables(){
        SharedPreferences share=getActivity().getSharedPreferences(getString(R.string.sharepreference_login_by_phone),Context.MODE_PRIVATE);
        isLogin=share.getBoolean("isLogin",false);
        userId=share.getInt("id",-1);
        token=share.getString("token"," ");

        adapter=new SocialListAdapter(getActivity(),this);
        handler.setContext(this.getActivity());
        httpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
        requestUserFollowUserMoment=new RequestUserFollowUserMoment();

    }

    private void initViews(View rootView){
        fab_social_add_moment=(FloatingActionButton)rootView.findViewById(R.id.fab_social_add_moment);
        fab_social_add_moment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(LoginSituation.isLogin(SocialFragment.this.getActivity())==false){
                    LoginActivity.login(getActivity());
                    return;
                }
                Intent intent=new Intent();
                intent.setAction(getString(R.string.activity_send_moment));
                startActivityForResult(intent, MomentSendActivity.REQUEST_MOMENT_SEND_CODE);
            }
        });


        lv_social=(XListView)rootView.findViewById(R.id.lv_social);
        lv_social.setAutoLoadEnable(false);
        lv_social.setPullLoadEnable(false);
        lv_social.setPullRefreshEnable(true);
        lv_social.setRefreshTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        lv_social.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                if(isFinishRequest==false){
                    return;
                }
                adapter.clear();
                requestUserFollowUserMoment.setPage(1);
                isFinishRequest=false;
                requestUserFollowUserMoment.requestUserMoment(getActivity(),handler,httpClient,userId,token,userId);
            }

            @Override
            public void onLoadMore() {
                if(isFinishRequest==false){
                    return;
                }
                isFinishRequest=false;
                requestUserFollowUserMoment.requestUserMoment(getActivity(),handler,httpClient,userId,token,userId);
            }
        });


        lv_social.setAdapter(adapter);
        //lv_social.setAdapter();
        lv_social.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("TAG","inItemclick");
                UserMoment.DataBean.ListBean data=(UserMoment.DataBean.ListBean)lv_social.getAdapter().getItem(position);
                if(data==null){
                    Toast.makeText(SocialFragment.this.getActivity(),"发生异常，请尝试刷新动态",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent=new Intent();
                intent.putExtra(MomentActivity.MOMENTID,data.getMomentId());
                intent.setAction(getString(R.string.activity_moment));
                startActivity(intent);
            }
        });
    }

    private void loadData(){
        //如果登陆，则请求动态信息
        if(isLogin==true){
            requestUserFollowUserMoment.requestUserMoment(getActivity(),handler,httpClient,userId,token,userId);
            isFinishRequest=false;
        }

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
        lv_social.stopRefresh();
        lv_social.stopLoadMore();
        lv_social.setRefreshTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case MomentSendActivity.REQUEST_MOMENT_SEND_CODE:
                if(resultCode== AppCompatActivity.RESULT_OK){
                    isFinishRequest=false;
                    adapter.clear();
                    requestUserFollowUserMoment.setPage(1);
                    requestUserFollowUserMoment.requestUserMoment(getActivity(),handler,httpClient,userId,token,userId);
                }
                break;
            case ProjectCommentActivity.REQUESR_CODE_SEND_COMMENT_ACTIVITY:
                if(resultCode== AppCompatActivity.RESULT_OK&&data!=null){
                    adapter.resetCommentNumber(data.getIntExtra("momentId",-1));
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
