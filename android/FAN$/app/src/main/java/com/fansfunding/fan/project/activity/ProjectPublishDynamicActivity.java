package com.fansfunding.fan.project.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.fansfunding.fan.R;
import com.fansfunding.fan.project.adapter.ChoosePhotoListAdapter;
import com.fansfunding.fan.project.entity.ResponseItem;
import com.fansfunding.fan.project.listener.ChooseImageListener;
import com.fansfunding.fan.project.utils.BitmapUtils;
import com.fansfunding.fan.project.utils.MyDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.finalteam.galleryfinal.widget.HorizontalListView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Eamon on 2016/7/25.
 */
public class ProjectPublishDynamicActivity extends AppCompatActivity{
    private boolean isPublishDynamic=false;
    private static final int SERVER_ERROR = 1010;
    private static final int PUBLISH_DYNAMIC_IMAGES = 1011;
    private static final int PUBLISH_DYNAMIC = 1012;
    private static final int PUBLISH_FINISHED = 1013;
    public ActionBar actionBar;
    private FloatingActionButton mOpenGallery;
    private List<PhotoInfo> mPhotoList;
    private ChoosePhotoListAdapter mChoosePhotoListAdapter;
    //Constans
    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private HorizontalListView mLvPhoto;
    private OkHttpClient httpClient;
    private ArrayList<String> dynamicFileList=new ArrayList<>();
    private int projectId;
    private AppCompatEditText etDynamicContent;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_publish);
        Intent intent= getIntent();
        projectId = intent.getIntExtra("projectId",0);
        Log.e("TEST",projectId+"");
        etDynamicContent= (AppCompatEditText) findViewById(R.id.et_dynamic_content);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_dynamic);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        actionBar=getSupportActionBar();
        actionBar.setTitle("项目更新");
        actionBar.setDisplayHomeAsUpEnabled(true);

        mLvPhoto=(HorizontalListView)findViewById(R.id.lv_imageView);
        mPhotoList = new ArrayList<>();
        mChoosePhotoListAdapter = new ChoosePhotoListAdapter(this, mPhotoList);
        mLvPhoto.setAdapter(mChoosePhotoListAdapter);
        mOpenGallery = (FloatingActionButton) findViewById(R.id.btn_open_gallery);
        mOpenGallery.setOnClickListener(new ChooseImageListener(this,mOnHanlderResultCallback,mPhotoList));
        initImageLoader(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dynamic_create, menu);
        return true;
    }


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

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            switch (reqeustCode) {
                case REQUEST_CODE_CAMERA:
                    Log.e("TEST", "REQUEST_CODE_CAMERA " + resultList.size());

                    break;
                case REQUEST_CODE_GALLERY:
                    Log.e("TEST", "REQUEST_CODE_GALLERY " + resultList.size());
                    break;
            }
            for (PhotoInfo p : resultList) {
                Log.e("TEST", p.getPhotoPath());
            }
            if (resultList != null) {
                mPhotoList.addAll(resultList);
                mChoosePhotoListAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(ProjectPublishDynamicActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case  R.id.menu_create_next:
                if(etDynamicContent.getText().toString().equals("")){
                    Toast.makeText(ProjectPublishDynamicActivity.this,"数据不能为空哟",Toast.LENGTH_LONG).show();
                }else {
                    if(!isPublishDynamic){
                        isPublishDynamic=true;
                        handler.sendEmptyMessage(PUBLISH_DYNAMIC_IMAGES);
                    }

                }

                break;
            default:
                super.onOptionsItemSelected(item);
                break;
        }
        return true;
    }

    private void publicDynamicImage() {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for(PhotoInfo p:mPhotoList){
            File file = new File(p.getPhotoPath());
            Bitmap bm = BitmapUtils.getimage(p.getPhotoPath());
            file=BitmapUtils.saveMyBitmap(bm,file.getName());

            RequestBody requestBody = FormBody.create(MediaType.parse("image/jpeg"), file);
            builder.addFormDataPart("files",file.getName(),requestBody);
        }

        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .post(requestBody)
                .url(getString(R.string.url_dynamic_image)+projectId + "/moment/images")
                .build();

        httpClient=new OkHttpClient();
        Call call = httpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {}
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Looper.prepare();
                Log.e("TEST2",response.toString());
                if (response == null || response.isSuccessful() == false) {
                    handler.sendEmptyMessage(SERVER_ERROR);
                } else {
                    String str_response = response.body().string();
                    Log.e("TEST2",str_response);
                    Gson gson = new GsonBuilder().create();
                    ResponseItem item = new ResponseItem();
                    item = gson.fromJson(str_response, item.getClass());
                    if(item.getErrCode()!=200){
                        handler.sendEmptyMessage(SERVER_ERROR);
                    }else{
                        dynamicFileList = (ArrayList<String>) item.getData();
                        //完成时调用
                        handler.sendEmptyMessage(PUBLISH_DYNAMIC);
                    }
                }
                Looper.loop();
                return;
            }
        });


    }

    private void publicDynamic() {
        SharedPreferences share=getSharedPreferences(getString(R.string.sharepreference_login_by_phone),MODE_PRIVATE);

        String dynamicPhotos="";
        for(String s:dynamicFileList){
            dynamicPhotos+=s;
            dynamicPhotos+=",";
        }
        Log.e("TEST",etDynamicContent.getText().toString());
        Log.e("TEST",dynamicPhotos);
        Log.e("TEST",share.getInt("id",0)+"");
        FormBody formBody=new FormBody.Builder()
                .add("content",etDynamicContent.getText().toString())
                .add("images",dynamicPhotos)
                .add("sponsorId",share.getInt("id",0)+"")
                .build();

        Request request = new Request.Builder()
                .post(formBody)
                .url(ProjectPublishDynamicActivity.this.getString(R.string.url_dynamic_image)+projectId+"/moment")
                .build();

        httpClient=new OkHttpClient();
        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("TEST2","onResponse "+response.toString());
                Looper.prepare();
                if(response==null||response.isSuccessful()==false){
                    handler.sendEmptyMessage(SERVER_ERROR);
                }else{
                    String str_response=response.body().string();
                    Gson gson=new GsonBuilder().create();
                    ResponseItem item = new ResponseItem();
                    item=gson.fromJson(str_response,item.getClass());
                    Log.e("TEST2","onResponse "+str_response);
                    if(item.getErrCode()!=200){
                        handler.sendEmptyMessage(SERVER_ERROR);
                    }else{
                        //完成时调用
                        handler.sendEmptyMessage(PUBLISH_FINISHED);
                    }
                }
                Looper.loop();
                return;
            }
        });




    }

    private MyDialog dialog;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SERVER_ERROR:
                    if(ProjectPublishDynamicActivity.this.isFinishing()!=true) {
                        Toast.makeText(ProjectPublishDynamicActivity.this, "服务器错误", Toast.LENGTH_SHORT).show();
                    }
                    ProjectPublishDynamicActivity.this.finish();
                    break;
                case PUBLISH_FINISHED:
                    isPublishDynamic=false;
                    isPublishDynamic=false;
                    dialog.dismiss();
                    ProjectPublishDynamicActivity.this.finish();
                    break;
                case PUBLISH_DYNAMIC_IMAGES:
                    if(ProjectPublishDynamicActivity.this.isFinishing()!=true) {
                        dialog=new MyDialog(ProjectPublishDynamicActivity.this,R.style.Custom);
                        dialog.show();
                        if(mPhotoList.size()==0){
                            publicDynamic();
                        }else{
                            publicDynamicImage();
                        }
                    }
                    break;

                case PUBLISH_DYNAMIC:
                    publicDynamic();
                    break;
                default:
                    break;
            }

        }
    };
}
