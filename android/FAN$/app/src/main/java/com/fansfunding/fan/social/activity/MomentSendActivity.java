package com.fansfunding.fan.social.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.fansfunding.emoji.SimpleCommonUtils;
import com.fansfunding.fan.R;
import com.fansfunding.fan.project.activity.CreateProjectActivity;
import com.fansfunding.fan.project.adapter.ChoosePhotoListAdapter;
import com.fansfunding.fan.project.listener.ChooseImageListener;
import com.fansfunding.fan.request.RequestSendComment;
import com.fansfunding.fan.request.RequestSendMoment;
import com.fansfunding.fan.social.fragment.MomentProjectNotShowFragment;
import com.fansfunding.fan.social.fragment.MomentProjectShowFragment;
import com.fansfunding.fan.utils.ErrorHandler;
import com.fansfunding.fan.utils.FANRequestCode;
import com.fansfunding.fan.utils.MyGridView;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import okhttp3.OkHttpClient;
import sj.keyboard.EmoticonsKeyBoardPopWindow;
import sj.keyboard.adpater.PageSetAdapter;
import sj.keyboard.interfaces.EmoticonClickListener;
import sj.keyboard.widget.EmoticonsEditText;

public class MomentSendActivity extends AppCompatActivity {

    //发送动态界面启动码
    public final static int REQUEST_MOMENT_SEND_CODE=1000;

    //参数名称，如要添加项目信息必须
    //项目id
    public static final String PROJECT_ID = "PROJECT_ID";
    //项目分类id
    public static final String PROJECT_CATEGORYID = "PROJECT_CATEGORYID";
    //项目封面
    public static final String PROJECT_COVER = "PROJECT_COVER";
    //项目名称
    public static final String PROJECT_NAME = "PROJECT_NAME";

    //获取到的照片信息
    private List<PhotoInfo> mPhotoList;

    private ChoosePhotoListAdapter mChoosePhotoListAdapter;

    private boolean isFinishSendMoment=true;

    //用户动态内容
    private String content;

    //用户id
    private int userId;

    //用户token
    private String token;

    //项目分类id
    private int categoryId;

    //项目id
    private int projectId;

    //项目名称
    private String projectName;

    //项目封面
    private String projectCover;

    //发送项目
    private RequestSendMoment requestSendMoment;

    //httpclient
    private OkHttpClient httpClient;

    //输入栏
    private EmoticonsEditText et_moment_send_content;

    //emoji选择框
    private EmoticonsKeyBoardPopWindow mKeyBoardPopWindow;

    //用来选择emoji的按钮
    private ImageView iv_moment_send_add_emoji;

    //用来选择图片的按钮
    private ImageView iv_moment_send_add_photo;

    //图片展示区
    private MyGridView gv_moment_send_photos;

    //显示项目的区域
    private FrameLayout frame_moment_send_project_info;
    //handler
    private ErrorHandler handler=new ErrorHandler(this){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case FANRequestCode.SEND_USER_MOMENT_PICTURE_SUCCESS:
                    if(requestSendMoment.getSendMomentPicture()==null){
                        break;
                    }
                    String images="";
                    for(String image:requestSendMoment.getSendMomentPicture().getData()){
                        images+=image+",";
                    }
                    requestSendMoment.requestSendMoment(MomentSendActivity.this,handler,httpClient,userId,token,content,-1,categoryId,projectId,images);

                    break;
                case FANRequestCode.SEND_USER_MOMENT_PICTURE_FAILURE:
                    isFinishSendMoment=true;
                    if(MomentSendActivity.this.isFinishing()==false){
                        Toast.makeText(MomentSendActivity.this,"发送动态失败",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case FANRequestCode.SEND_USER_MOMENT_SUCCESS:
                    isFinishSendMoment=true;
                    if(MomentSendActivity.this.isFinishing()==false){
                        setResult(RESULT_OK);
                        MomentSendActivity.this.finish();
                    }
                    break;
                case FANRequestCode.SEND_USER_MOMENT_FAILURE:
                    isFinishSendMoment=true;
                    if(MomentSendActivity.this.isFinishing()==false){
                        Toast.makeText(MomentSendActivity.this,"发送动态失败",Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_moment_send);

        initVariables();
        initViews();
        loadData();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            //发送动态按钮
            case R.id.menu_send_comment:
                sendUserMoment();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_project_comment, menu);
        return true;
    }


    private void initVariables(){
        Intent intent=getIntent();
        categoryId=intent.getIntExtra(PROJECT_ID,-1);
        projectId=intent.getIntExtra(PROJECT_CATEGORYID,-1);
        projectName=intent.getStringExtra(PROJECT_NAME);
        projectCover=intent.getStringExtra(PROJECT_COVER);

        SharedPreferences share=getSharedPreferences(getString(R.string.sharepreference_login_by_phone),MODE_PRIVATE);
        userId=share.getInt("id",0);
        token=share.getString("token"," ");

        initImageLoader(this);

        requestSendMoment=new RequestSendMoment();
        httpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
        mPhotoList = new ArrayList<>();
        mChoosePhotoListAdapter=new ChoosePhotoListAdapter(this,mPhotoList);
    }

    private void initViews(){
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_moment_send);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        //设置返回键
        ActionBar actionBar=this.getSupportActionBar();
        actionBar.setTitle("发动态");
        actionBar.setDisplayHomeAsUpEnabled(true);

        gv_moment_send_photos=(MyGridView)findViewById(R.id.gv_moment_send_photos);
        gv_moment_send_photos.setAdapter(mChoosePhotoListAdapter);

        frame_moment_send_project_info=(FrameLayout)findViewById(R.id.frame_moment_send_project_info);
        frame_moment_send_project_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(getString(R.string.activity_moment_select_project));
                startActivityForResult(intent,MomentSelectProjectActivity.REQUEST_MOMENT_SELECT_PROJECT);
            }
        });

        et_moment_send_content=(EmoticonsEditText)findViewById(R.id.et_moment_send_content);
        initEmoticonsEditText();

        //添加emoji
        iv_moment_send_add_emoji=(ImageView)findViewById(R.id.iv_moment_send_add_emoji);
        iv_moment_send_add_emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mKeyBoardPopWindow != null && mKeyBoardPopWindow.isShowing()) {
                    mKeyBoardPopWindow.dismiss();
                } else {
                    if (mKeyBoardPopWindow == null) {
                        initKeyBoardPopWindow();
                    }
                    mKeyBoardPopWindow.showPopupWindow();
                }
            }
        });

        //添加图片
        iv_moment_send_add_photo=(ImageView)findViewById(R.id.iv_moment_send_add_photo);
        iv_moment_send_add_photo.setOnClickListener(new ChooseImageListener(MomentSendActivity.this,mOnHanlderResultCallback,mPhotoList));

        //设置项目信息
        //如果已经传入了项目信息，则直接展示
        if(projectCover!=null&&projectName!=null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_moment_send_project_info,
                            MomentProjectShowFragment.newInstance(
                                    projectCover,
                                    projectName))
                    .commit();
        }
        //如果没有传入项目信息，则展示梦想
        else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_moment_send_project_info, MomentProjectNotShowFragment.newInstance())
                    .commit();
        }

    }

    private void loadData(){

    }

    //发送用户动态
    private void sendUserMoment(){
        content=et_moment_send_content.getText().toString();
        if(content==null||content.equals("")){
            Toast.makeText(this,"动态内容不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if(content.length()>140){
            Toast.makeText(this,"动态不能超过140字",Toast.LENGTH_SHORT).show();
            return;
        }
        if(isFinishSendMoment==false){
            Toast.makeText(this,"正在发送动态，请稍等",Toast.LENGTH_SHORT).show();
            return;
        }
        isFinishSendMoment=false;
        if(mPhotoList.size()>0){
            requestSendMoment.requestSendMomentPicture(this,handler,httpClient,userId,token,mPhotoList);
        }else{
            requestSendMoment.requestSendMoment(MomentSendActivity.this,handler,httpClient,userId,token,content,-1,categoryId,projectId,null);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case MomentSelectProjectActivity.REQUEST_MOMENT_SELECT_PROJECT:
                //如果选择了项目，则展示项目
                if(resultCode==RESULT_OK&&data!=null){
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_moment_send_project_info,
                                    MomentProjectShowFragment.newInstance(
                                            data.getStringExtra(MomentSelectProjectActivity.PROJECT_COVER),
                                            data.getStringExtra(MomentSelectProjectActivity.PROJECT_NAME)))
                            .commitAllowingStateLoss();

                    projectId=data.getIntExtra(MomentSelectProjectActivity.PROJECT_ID,-1);
                    categoryId=data.getIntExtra(MomentSelectProjectActivity.PROJECT_CATEGORYID,-1);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //初始化emoji的编辑输入框
    private void initEmoticonsEditText() {
        SimpleCommonUtils.initEmoticonsEditText(et_moment_send_content);
        et_moment_send_content.setFocusable(true);
        et_moment_send_content.setFocusableInTouchMode(true);
        et_moment_send_content.requestFocus();
    }

    //初始化emoji的选择弹出框
    private void initKeyBoardPopWindow() {
        mKeyBoardPopWindow = new EmoticonsKeyBoardPopWindow(this);

        EmoticonClickListener emoticonClickListener = SimpleCommonUtils.getCommonEmoticonClickListener(et_moment_send_content);
        PageSetAdapter pageSetAdapter = new PageSetAdapter();
        SimpleCommonUtils.addEmojiPageSetEntity(pageSetAdapter, this, emoticonClickListener);
        mKeyBoardPopWindow.setAdapter(pageSetAdapter);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && mKeyBoardPopWindow != null && mKeyBoardPopWindow.isShowing()) {
            mKeyBoardPopWindow.dismiss();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mKeyBoardPopWindow != null && mKeyBoardPopWindow.isShowing()) {
            mKeyBoardPopWindow.dismiss();
        }
    }

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
                if(mPhotoList.size()==0){
                    mPhotoList.addAll(resultList);
                }else{
                    for(PhotoInfo result:resultList){
                        for(int i=0;i<mPhotoList.size();i++){
                            if(result.getPhotoPath().equals(mPhotoList.get(i).getPhotoPath())){
                                break;
                            }
                            if(i==mPhotoList.size()-1){
                                mPhotoList.add(result);
                            }
                        }
                    }
                }
                if(resultList.size()==0){
                    mPhotoList.clear();
                }

                if(mPhotoList.size()>1){
                    gv_moment_send_photos.setNumColumns(3);
                }else {
                    gv_moment_send_photos.setNumColumns(1);
                }
                mChoosePhotoListAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(MomentSendActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };

    private void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }


}
