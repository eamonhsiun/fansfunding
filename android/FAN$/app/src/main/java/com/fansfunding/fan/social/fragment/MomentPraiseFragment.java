package com.fansfunding.fan.social.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.cpoopc.scrollablelayoutlib.ScrollableHelper;
import com.fansfunding.PullListView.LoadListView;
import com.fansfunding.fan.R;
import com.fansfunding.fan.request.RequestMomentPraiseDetail;
import com.fansfunding.fan.social.activity.MomentActivity;
import com.fansfunding.fan.social.adapter.MomentPraiseAdapter;
import com.fansfunding.fan.social.interfacetest.IInitNum;
import com.fansfunding.fan.utils.ErrorHandler;
import com.fansfunding.fan.utils.FANRequestCode;
import com.fansfunding.fan.utils.StartHomepage;

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
 * Use the {@link MomentPraiseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MomentPraiseFragment extends Fragment implements ScrollableHelper.ScrollableContainer,MomentActivity.OnLoadListViewReset {

    private IInitNum mListener;

    private final static String MOMENTID="MOMENTID";

    //用户id
    private int userId;

    //用户token
    private String token;

    //动态id
    private int momentId=-1;

    //是否完成请求
    private boolean isFinishRequest=true;

    //adapter
    private MomentPraiseAdapter adapter;

    //赞展示列表
    private LoadListView lv_moment_praise;

    //请求赞的详情
    private RequestMomentPraiseDetail requestMomentPraiseDetail;

    //httpclient
    private OkHttpClient httpClient;

    private ErrorHandler handler=new ErrorHandler(this.getActivity()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case FANRequestCode.GET_MOMENT_PRAISE_DETAIL_SUCCESS:
                    isFinishRequest=true;
                    endRefresh();
                    if(requestMomentPraiseDetail.getMomentPraise().getData().getList().size()<requestMomentPraiseDetail.getRows()){
                        requestMomentPraiseDetail.setPage(1);
                        lv_moment_praise.setPullLoadEnable(false);
                        lv_moment_praise.setAutoLoadEnable(false);
                    }else {
                        requestMomentPraiseDetail.setPage(requestMomentPraiseDetail.getPage()+1);
                        lv_moment_praise.setPullLoadEnable(true);
                        lv_moment_praise.setAutoLoadEnable(true);
                    }
                    if(requestMomentPraiseDetail.getMomentPraise()!=null){
                        for(int i=0;i<requestMomentPraiseDetail.getMomentPraise().getData().getList().size();i++){
                            adapter.addItem(requestMomentPraiseDetail.getMomentPraise().getData().getList().get(i));
                        }
                    }
                    adapter.notifyDataSetChanged();
                    mListener.initNum(requestMomentPraiseDetail.getMomentPraise().getData().getTotal(),MomentActivity.RESET_PRAISE_NUM);
                    break;
                case FANRequestCode.GET_MOMENT_PRAISE_DETAIL_FAILURE:
                    isFinishRequest=true;
                    endRefresh();
                    if(MomentPraiseFragment.this.getActivity().isFinishing()==false){
                        Toast.makeText(MomentPraiseFragment.this.getActivity(),"获取点赞情况失败",Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }

        }
    };

    public MomentPraiseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MomentPraiseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MomentPraiseFragment newInstance(int momentId) {
        MomentPraiseFragment fragment = new MomentPraiseFragment();
        Bundle args = new Bundle();
        args.putInt(MOMENTID,momentId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            momentId=getArguments().getInt(MOMENTID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_moment_praise, container, false);
        initVariables();
        initViews(rootView);
        loadData();
        return rootView;
    }

    private void initVariables(){
        SharedPreferences share=getActivity().getSharedPreferences(getString(R.string.sharepreference_login_by_phone),Context.MODE_PRIVATE);
        userId=share.getInt("id",-1);
        token=share.getString("token"," ");

        adapter=new MomentPraiseAdapter(this.getActivity());
        handler.setContext(this.getActivity());
        httpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
        requestMomentPraiseDetail=new RequestMomentPraiseDetail();
    }

    private void initViews(View rootView){

        lv_moment_praise=(LoadListView) rootView.findViewById(R.id.lv_moment_praise);
        lv_moment_praise.setPullLoadEnable(false);
        lv_moment_praise.setAutoLoadEnable(false);
        lv_moment_praise.setRefreshTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        lv_moment_praise.setXListViewListener(new LoadListView.IXListViewListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                if(isFinishRequest==false){
                    return;
                }
                isFinishRequest=false;
                requestMomentPraiseDetail.requestMomentPraiseDetail(MomentPraiseFragment.this.getActivity(),handler,httpClient,momentId);
            }
        });

        lv_moment_praise.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

        lv_moment_praise.setAdapter(adapter);
    }

    private void loadData(){
        refreshPraiseList();
    }

    public void refreshPraiseList(){
        if(isFinishRequest==false){
            return;
        }
        isFinishRequest=false;
        adapter.clear();
        requestMomentPraiseDetail.setPage(1);
        requestMomentPraiseDetail.requestMomentPraiseDetail(MomentPraiseFragment.this.getActivity(),handler,httpClient,momentId);
    }


    @Override
    public void resetLoadListView() {
        if(lv_moment_praise!=null&&lv_moment_praise.isAtTop()==false){
            lv_moment_praise.setSelection(0);
        }
    }

    @Override
    public View getScrollableView() {
        return lv_moment_praise;
    }

    //停止更新的动画
    private void endRefresh(){
        isFinishRequest=true;
        lv_moment_praise.stopRefresh();
        lv_moment_praise.stopLoadMore();
        lv_moment_praise.setRefreshTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IInitNum) {
            mListener = (IInitNum) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
