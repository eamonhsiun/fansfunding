package com.fansfunding.fan.user.info.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.fansfunding.PullListView.XListView;
import com.fansfunding.fan.R;
import com.fansfunding.fan.login.LoginActivity;
import com.fansfunding.fan.project.activity.ProjectCommentActivity;
import com.fansfunding.fan.request.RequestUserMoment;
import com.fansfunding.fan.social.activity.MomentActivity;
import com.fansfunding.fan.social.activity.MomentSendActivity;
import com.fansfunding.fan.social.adapter.SocialListAdapter;
import com.fansfunding.fan.social.fragment.SocialFragment;
import com.fansfunding.fan.utils.ErrorHandler;
import com.fansfunding.fan.utils.FANRequestCode;
import com.fansfunding.fan.utils.LoginSituation;
import com.fansfunding.internal.social.UserMoment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class UserMomentListActivity extends AppCompatActivity {

    //
    public final static String VIEWID="VIEWID";



    //要查看的用户id
    private int viewId;

    //用户id
    private int userId;

    private boolean isFinishRequest=true;

    private XListView lv_user_moment_list;

    private SocialListAdapter adapter;

    private OkHttpClient httpClient;

    private RequestUserMoment requestUserMoment;

    private ErrorHandler handler=new ErrorHandler(this){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case FANRequestCode.GET_USER_MOMENT_SUCCESS:
                    endRefresh();
                    if(requestUserMoment.getUserMoment()==null){
                        if(UserMomentListActivity.this.isFinishing()==false){
                            Toast.makeText(UserMomentListActivity.this,"请求动态失败",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                    if(requestUserMoment.getUserMoment().getData().getList().size()<requestUserMoment.getRows()){
                        requestUserMoment.setPage(1);
                        lv_user_moment_list.setPullLoadEnable(false);
                        lv_user_moment_list.setAutoLoadEnable(false);
                    }else {
                        requestUserMoment.setPage(requestUserMoment.getPage()+1);
                        lv_user_moment_list.setPullLoadEnable(true);
                        lv_user_moment_list.setAutoLoadEnable(true);
                    }
                    for(int i=0;i<requestUserMoment.getUserMoment().getData().getList().size();i++){
                        adapter.addItem(requestUserMoment.getUserMoment().getData().getList().get(i));
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case FANRequestCode.GET_USER_MOMENT_FAILURE:
                    endRefresh();
                    if(UserMomentListActivity.this.isFinishing()==false){
                        Toast.makeText(UserMomentListActivity.this,"请求动态失败",Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_moment_list);
        initVariables();
        initViews();
        loadData();

    }

    private void initVariables(){
        Intent intent=getIntent();
        viewId=intent.getIntExtra(VIEWID,-1);

        SharedPreferences share=getSharedPreferences(getString(R.string.sharepreference_login_by_phone),MODE_PRIVATE);
        userId=share.getInt("id",-1);

        adapter=new SocialListAdapter(this);
        httpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
        requestUserMoment=new RequestUserMoment();

    }

    private void initViews(){
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_user_moment_list);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("动态");
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(Color.WHITE);

        lv_user_moment_list=(XListView)findViewById(R.id.lv_user_moment_list);
        lv_user_moment_list.setAutoLoadEnable(false);
        lv_user_moment_list.setPullLoadEnable(false);
        lv_user_moment_list.setPullRefreshEnable(true);
        lv_user_moment_list.setRefreshTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        lv_user_moment_list.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                if(isFinishRequest==false){
                    return;
                }
                isFinishRequest=false;
                adapter.clear();
                requestUserMoment.setPage(1);
                requestUserMoment.requestUserMoment(UserMomentListActivity.this,handler,httpClient,userId,viewId);
            }

            @Override
            public void onLoadMore() {
                if(isFinishRequest==false){
                    return;
                }
                isFinishRequest=false;
                adapter.clear();
                requestUserMoment.setPage(1);
                requestUserMoment.requestUserMoment(UserMomentListActivity.this,handler,httpClient,userId,viewId);
            }
        });

        lv_user_moment_list.setAdapter(adapter);
        lv_user_moment_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserMoment.DataBean.ListBean data=(UserMoment.DataBean.ListBean)lv_user_moment_list.getAdapter().getItem(position);
                if(data==null){
                    Toast.makeText(UserMomentListActivity.this,"发生异常，请尝试刷新动态",Toast.LENGTH_SHORT).show();
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
        isFinishRequest=false;
        requestUserMoment.setPage(1);
        requestUserMoment.requestUserMoment(UserMomentListActivity.this,handler,httpClient,userId,viewId);
    }

    private void endRefresh(){
        isFinishRequest=true;
        lv_user_moment_list.stopRefresh();
        lv_user_moment_list.stopLoadMore();
        lv_user_moment_list.setRefreshTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case ProjectCommentActivity.REQUESR_CODE_SEND_COMMENT_ACTIVITY:
                if(resultCode== AppCompatActivity.RESULT_OK&&data!=null){
                    adapter.resetCommentNumber(data.getIntExtra("momentId",-1));
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;

    }
}
