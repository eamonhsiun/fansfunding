package com.fansfunding.fan.social.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.cpoopc.scrollablelayoutlib.ScrollableHelper;
import com.fansfunding.PullListView.LoadListView;
import com.fansfunding.PullListView.XListView;
import com.fansfunding.fan.R;
import com.fansfunding.fan.project.activity.ProjectCommentActivity;
import com.fansfunding.fan.request.RequestMomentComment;
import com.fansfunding.fan.social.activity.MomentActivity;
import com.fansfunding.fan.social.adapter.MomentCommentAdapter;
import com.fansfunding.fan.social.interfacetest.IInitNum;
import com.fansfunding.fan.utils.ErrorHandler;
import com.fansfunding.fan.utils.FANRequestCode;
import com.fansfunding.internal.social.MomentComment;

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
 * Use the {@link MomentCommentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MomentCommentFragment extends Fragment implements ScrollableHelper.ScrollableContainer,MomentActivity.OnLoadListViewReset{

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

    private LoadListView lv_moment_comment;

    //adapter
    private MomentCommentAdapter adapter;

    //httpclient
    private OkHttpClient httpClient;

    //request
    private RequestMomentComment requestMomentComment;

    private ErrorHandler handler=new ErrorHandler(this.getActivity()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case FANRequestCode.GET_MOMENT_COMMENT_DETAIL_SUCCESS:
                    isFinishRequest=true;
                    endRefresh();
                    if(requestMomentComment.getMomentComment().getData().getList().size()<requestMomentComment.getRows()){
                        requestMomentComment.setPage(1);
                        lv_moment_comment.setPullLoadEnable(false);
                        lv_moment_comment.setAutoLoadEnable(false);
                    }else {
                        requestMomentComment.setPage(requestMomentComment.getPage()+1);
                        lv_moment_comment.setPullLoadEnable(true);
                        lv_moment_comment.setAutoLoadEnable(true);
                    }
                    if(requestMomentComment.getMomentComment()!=null){
                        for(int i=0;i<requestMomentComment.getMomentComment().getData().getList().size();i++){
                            adapter.addItem(requestMomentComment.getMomentComment().getData().getList().get(i));
                        }
                    }
                    adapter.notifyDataSetChanged();
                    mListener.initNum(requestMomentComment.getMomentComment().getData().getTotal(),MomentActivity.RESET_COLUMN_NUM);
                    break;
                case FANRequestCode.GET_MOMENT_COMMENT_DETAIL_FAILURE:
                    if(MomentCommentFragment.this.getActivity().isFinishing()==false){
                        Toast.makeText(MomentCommentFragment.this.getActivity(),"获取评论失败",Toast.LENGTH_SHORT).show();
                    }
                    isFinishRequest=true;
                    endRefresh();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    public MomentCommentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MomentCommentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MomentCommentFragment newInstance(int momentId) {
        MomentCommentFragment fragment = new MomentCommentFragment();
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
        View rootView=inflater.inflate(R.layout.fragment_moment_comment, container, false);
        initvariables();
        initViews(rootView);
        loadData();
        return rootView;
    }


    private void initvariables(){
        SharedPreferences share=getActivity().getSharedPreferences(getString(R.string.sharepreference_login_by_phone),Context.MODE_PRIVATE);
        userId=share.getInt("id",-1);
        token=share.getString("token"," ");


        adapter=new MomentCommentAdapter(this.getActivity());
        handler.setContext(this.getActivity());
        httpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
        requestMomentComment=new RequestMomentComment();
    }

    private void initViews(View rootView){
        lv_moment_comment=(LoadListView) rootView.findViewById(R.id.lv_moment_comment);
        lv_moment_comment.setPullLoadEnable(false);
        lv_moment_comment.setAutoLoadEnable(false);
        lv_moment_comment.setRefreshTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        lv_moment_comment.setXListViewListener(new LoadListView.IXListViewListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                if(isFinishRequest==false){
                    return;
                }
                isFinishRequest=false;
                requestMomentComment.requestMomentComment(MomentCommentFragment.this.getActivity(),handler,httpClient,momentId);
            }
        });


        lv_moment_comment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MomentComment.DataBean.ListBean comment=(MomentComment.DataBean.ListBean)lv_moment_comment.getAdapter().getItem(position);
                if(comment==null){
                    return;
                }
                Intent intent=new Intent();
                intent.setAction(getString(R.string.activity_project_comment));
                intent.putExtra("momentId",momentId);
                intent.putExtra("pointTo",comment.getUser().getId());
                intent.putExtra("pointToNickname",comment.getUser().getNickname());
                intent.putExtra("mode", ProjectCommentActivity.SEND_MOMENT_COMMENT);
                startActivityForResult(intent,ProjectCommentActivity.REQUESR_CODE_SEND_COMMENT_ACTIVITY);
            }
        });
        lv_moment_comment.setAdapter(adapter);
    }

    private void loadData(){
        requestMomentComment.requestMomentComment(MomentCommentFragment.this.getActivity(),handler,httpClient,momentId);
        isFinishRequest=false;
    }


    public void resetCommentList(){
        if(isFinishRequest==false){
            return;
        }
        isFinishRequest=false;
        adapter.clear();
        requestMomentComment.setPage(1);
        requestMomentComment.requestMomentComment(MomentCommentFragment.this.getActivity(),handler,httpClient,momentId);

    }



    @Override
    public View getScrollableView() {
        return lv_moment_comment;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==ProjectCommentActivity.REQUESR_CODE_SEND_COMMENT_ACTIVITY){
            if(resultCode== AppCompatActivity.RESULT_OK){
                adapter.clear();
                requestMomentComment.setPage(1);
                isFinishRequest=false;
                requestMomentComment.requestMomentComment(MomentCommentFragment.this.getActivity(),handler,httpClient,momentId);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //将listview置为顶部
    @Override
    public void resetLoadListView() {
        if(lv_moment_comment!=null&&lv_moment_comment.isAtTop()==false){
            lv_moment_comment.setSelection(0);
        }
    }

    //停止更新的动画
    private void endRefresh(){
        isFinishRequest=true;
        lv_moment_comment.stopRefresh();
        lv_moment_comment.stopLoadMore();
        lv_moment_comment.setRefreshTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
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
