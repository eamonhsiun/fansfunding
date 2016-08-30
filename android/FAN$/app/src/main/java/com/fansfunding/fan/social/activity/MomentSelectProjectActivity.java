package com.fansfunding.fan.social.activity;

import android.content.Intent;
import android.os.Message;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.fansfunding.PullListView.XListView;
import com.fansfunding.fan.AddressActivity;
import com.fansfunding.fan.R;
import com.fansfunding.fan.request.RequestSearchProject;
import com.fansfunding.fan.search.adapter.SearchProjectAdapter;
import com.fansfunding.fan.utils.ErrorHandler;
import com.fansfunding.fan.utils.FANRequestCode;
import com.fansfunding.internal.ProjectInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class MomentSelectProjectActivity extends AppCompatActivity {

    //启动该activity的请求码
    public static final int REQUEST_MOMENT_SELECT_PROJECT=300;

    //参数名称
    public static final String PROJECT_ID = "PROJECT_ID";
    public static final String PROJECT_CATEGORYID = "PROJECT_CATEGORYID";
    public static final String PROJECT_COVER = "PROJECT_COVER";
    public static final String PROJECT_NAME = "PROJECT_NAME";

    //搜索关键字
    private String keyword;

    //是否结束搜索
    private boolean isFinishRequest=true;

    //搜索输入栏
    private TextInputEditText tiet_search_input;

    //项目展示栏
    private XListView lv_moment_select_project;

    //展示栏适配器
    private SearchProjectAdapter adapter;
    //httpclient
    private OkHttpClient httpClient;

    //用来请求搜索项目
    private RequestSearchProject requestSearchProject;


    //handler
    private ErrorHandler handler=new ErrorHandler(this){
        @Override
        public void handleMessage(Message msg) {
            isFinishRequest=true;
            switch (msg.what){
                case FANRequestCode.SEARCH_PROJECT_SUCCESS:
                    endRefresh();
                    if(requestSearchProject.getSearchProject()==null||requestSearchProject.getSearchProject().getData().getList().size()==0){
                        if(MomentSelectProjectActivity.this.isFinishing()==false){
                            Toast.makeText(MomentSelectProjectActivity.this, "无此项目", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                    if(requestSearchProject.getSearchProject().getData().getList().size()<requestSearchProject.getRows()){
                        lv_moment_select_project.setPullLoadEnable(false);
                        lv_moment_select_project.setAutoLoadEnable(false);
                    }else {
                        requestSearchProject.setPage(requestSearchProject.getPage()+1);
                        lv_moment_select_project.setPullLoadEnable(true);
                        lv_moment_select_project.setAutoLoadEnable(true);
                    }

                    for(int i=0;i<requestSearchProject.getSearchProject().getData().getList().size();i++){
                        adapter.addItemAtHead(requestSearchProject.getSearchProject().getData().getList().get(i));
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case FANRequestCode.SEARCH_PROJECT_FAILURE:
                    endRefresh();
                    if(MomentSelectProjectActivity.this.isFinishing()==false){
                        Toast.makeText(MomentSelectProjectActivity.this, "搜索失败", Toast.LENGTH_SHORT).show();
                        break;
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
        setContentView(R.layout.activity_moment_select_project);

        initVariables();
        initViews();
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

    private void initVariables(){
        requestSearchProject=new RequestSearchProject();
        httpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
        adapter=new SearchProjectAdapter(this);
    }

    private void initViews(){
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_moment_select_project);
        setSupportActionBar(toolbar);
        //设置返回键
        ActionBar actionBar=this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(null);


        tiet_search_input=(TextInputEditText)findViewById(R.id.tiet_search_input);
        tiet_search_input.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode== KeyEvent.KEYCODE_ENTER&&event.getAction()==KeyEvent.ACTION_DOWN){
                    keyword=tiet_search_input.getText().toString();
                    if(keyword==null||keyword.equals("")){
                        Toast.makeText(MomentSelectProjectActivity.this,"请输入要搜索的内容",Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    if(isFinishRequest==true){
                        requestSearchProject.requestSearchProject(MomentSelectProjectActivity.this,handler,httpClient,keyword);
                    }

                    return true;
                }
                return false;
            }
        });


        lv_moment_select_project=(XListView)findViewById(R.id.lv_moment_select_project);
        lv_moment_select_project.setAdapter(adapter);
        lv_moment_select_project.setPullLoadEnable(false);
        lv_moment_select_project.setPullRefreshEnable(false);
        lv_moment_select_project.setAutoLoadEnable(false);
        lv_moment_select_project.setRefreshTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        lv_moment_select_project.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadMore() {
                if(isFinishRequest==true){
                    adapter.cleanItem();
                    requestSearchProject.requestSearchProject(MomentSelectProjectActivity.this,handler,httpClient,keyword);
                }
            }
        });

        lv_moment_select_project.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProjectInfo detail=(ProjectInfo)lv_moment_select_project.getAdapter().getItem(position);
                Intent intent=new Intent();
                intent.putExtra(PROJECT_COVER,detail.getCover());
                intent.putExtra(PROJECT_NAME,detail.getName());
                intent.putExtra(PROJECT_ID,detail.getId());
                intent.putExtra(PROJECT_CATEGORYID,detail.getCategoryId());
                setResult(RESULT_OK,intent);
                if(MomentSelectProjectActivity.this.isFinishing()==false){
                    MomentSelectProjectActivity.this.finish();
                }
            }
        });
    }

    //停止更新的动画
    private void endRefresh(){
        isFinishRequest=true;
        lv_moment_select_project.stopRefresh();
        lv_moment_select_project.stopLoadMore();
        lv_moment_select_project.setRefreshTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
    }
}
