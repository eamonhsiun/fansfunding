package com.fansfunding.fan.project.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.fansfunding.fan.R;
import com.fansfunding.fan.project.entity.ResponseItem;
import com.fansfunding.fan.project.entity.Reward;
import com.fansfunding.fan.project.fragment.CreateProjectAddRewordFragment;
import com.fansfunding.fan.project.fragment.CreateProjectFragment;
import com.fansfunding.fan.project.fragment.CreateProjectRewordFragment;
import com.fansfunding.fan.project.utils.BitmapUtils;
import com.fansfunding.fan.project.utils.MyDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.finalteam.galleryfinal.model.PhotoInfo;
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
 *
 */

public class CreateProjectActivity extends CreateProjectActivityBase {
    private boolean isPublishProject =false;
    private boolean isPublishReward=false;

    private final int SERVER_ERROR=1000;
    private final int PUBLISH_PROJECT=1010;
    private final int PUBLISH_PROJECT_FINISHED=1011;
    private final int PUBLISH_REWARD=1012;
    private final int PUBLISH_REWARD_FINISHED=1013;
    private final int PUBLISH_REWARD_IMAGE=1014;
    private final int PUBLISH_PROJECT_IMAGES_FINISH =1015;

    private  List<Map<String,Object>> rewardList=new ArrayList<>();
    private ArrayList<String> projectFileList = new ArrayList<>();
    private ArrayList<String> rewardFileList = new ArrayList<>();
    private int rewardPublishState=-1;
    private String projectDesc;
    private String projectName;
    private int targetMoney;
    private String targetTime;
    private OkHttpClient httpClient;
    private MyDialog dialog;

    private static CreateProjectActivity createProjectActivity;
    private int projectId;

    public static CreateProjectActivity getInstance(){
        return createProjectActivity;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createProjectActivity=this;
        httpClient = new OkHttpClient();
        View view = getWindow().peekDecorView();
        //关闭软键盘
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        File destDir = new File("/sdcard/fans");
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case android.R.id.home:
                if(this.getPageState()==0){
                    finish();
                }else if(this.getPageState()==1){
                    finish();
                }else if(this.getPageState()==2){
                    //添加回馈
                    CreateProjectActivity.this.setPageState(1);
                }
                break;

            case R.id.menu_create_next:
                if(this.getPageState()==0){
                    projectDesc= CreateProjectFragment.getInstance().getEtProjectDesc();
                    projectName=CreateProjectFragment.getInstance().getEtProjectTitle();
                    targetMoney=CreateProjectFragment.getInstance().getEtTargetMoney();
                    targetTime= CreateProjectFragment.getInstance().getTvTargetTime();
                    if(projectDesc.equals("")||projectName.equals("")||(targetMoney==0)||targetTime.equals("")){
                        Toast.makeText(CreateProjectActivity.this,"(*^__^*) ……数据不能为空嘛",Toast.LENGTH_LONG).show();
                    }else if(CreateProjectFragment.getInstance().getPhotoList().size()==0){
                        Toast.makeText(CreateProjectActivity.this,"请添加几张封面图片",Toast.LENGTH_LONG).show();
                    }
                    else {
                        if(!isPublishProject){
                            isPublishProject=true;
                            handler.sendEmptyMessage(PUBLISH_PROJECT);
                        }

                    }
                }else if(this.getPageState()==1){
                    if(rewardList.size()==0){
                        Toast.makeText(CreateProjectActivity.this,"(*^__^*) …很抱歉数据不能为空，请重新设置",Toast.LENGTH_LONG).show();
                    }else{
                        if(!isPublishReward){
                            isPublishReward=true;
                            handler.sendEmptyMessage(PUBLISH_REWARD_IMAGE);
                        }

                    }
                }else if(this.getPageState()==2){
                    //添加一个回馈
                    Map<String,Object> mapItem = new HashMap<>();
                    String supportMoney=CreateProjectAddRewordFragment.getInstance().getSupportMoney();
                    String content=CreateProjectAddRewordFragment.getInstance().getRewardContent();
                    Reward reward = new Reward();
                    reward.setPhotoList(CreateProjectAddRewordFragment.getInstance().getPhotoList());
                    if(supportMoney.equals("")||content.equals("")||(reward.getPhotoList().size()==0)){
                        Toast.makeText(CreateProjectActivity.this,"(*^__^*) …很抱歉数据不能为空，请重新设置",Toast.LENGTH_LONG).show();
                    }else{
                        CreateProjectAddRewordFragment.getInstance().ClearSupportMoney();
                        CreateProjectAddRewordFragment.getInstance().ClearRewardContent();
                        CreateProjectAddRewordFragment.getInstance().getAndClearPhotoList();
                        reward.setSupportMoney(Double.parseDouble(supportMoney));
                        reward.setContent(content);
                        mapItem.put("msg_name","支持金额： "+ supportMoney);
                        mapItem.put("msg_content", content);
                        mapItem.put("msg_extra",reward);
                        addItem(mapItem);
                        CreateProjectActivity.this.setPageState(1);
                    }

                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


    public List<Map<String,Object>> getRewardList(){
        return rewardList;
    }

    public void addItem(Map<String, Object> mapItem) {
        rewardList.add(mapItem);
        CreateProjectRewordFragment.getInstance().refreshAdapter();
    }
    public void removeItem(int index) {
        rewardList.remove(index);
        CreateProjectRewordFragment.getInstance().refreshAdapter();
    }


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case SERVER_ERROR:
                    if(CreateProjectActivity.this.isFinishing()!=true) {
                        Toast.makeText(CreateProjectActivity.this, "服务器错误", Toast.LENGTH_SHORT).show();
                    }
                    CreateProjectActivity.this.finish();
                    break;
                case PUBLISH_PROJECT:
                    if(CreateProjectActivity.this.isFinishing()!=true) {
                        dialog=new MyDialog(CreateProjectActivity.this,R.style.Custom);
                        dialog.show();
                        //Toast.makeText(CreateProjectActivity.this, "宝贝儿，正在上传请稍等...", Toast.LENGTH_SHORT).show();
                    }
                    if(CreateProjectFragment.getInstance().getPhotoList().size()!=0){
                        sendImages();
                    }else{
                        sendProject();
                    }
                    break;
                case PUBLISH_PROJECT_IMAGES_FINISH:
                    sendProject();
                    break;
                case PUBLISH_PROJECT_FINISHED:
                    if(CreateProjectActivity.this.isFinishing()!=true) {
                        dialog.dismiss();
                        isPublishProject=false;
                        Toast.makeText(CreateProjectActivity.this, "发起项目成功，请添加您的回馈方式", Toast.LENGTH_LONG).show();
                    }
                    CreateProjectActivity.this.setPageState(1);
                    break;

                case PUBLISH_REWARD:
                    if(CreateProjectActivity.this.isFinishing()!=true) {
                    }
                    sendReward((Reward) rewardList.get(rewardPublishState).get("msg_extra"));
                    break;
                case PUBLISH_REWARD_IMAGE:
                    dialog=new MyDialog(CreateProjectActivity.this,R.style.Custom);
                    dialog.show();
                    rewardPublishState++;
                    if(rewardPublishState<rewardList.size()){
                        sendRewardImages((Reward) rewardList.get(rewardPublishState).get("msg_extra"));
                    }else{
                        sendEmptyMessage(PUBLISH_REWARD_FINISHED);
                    }
                    break;
                case PUBLISH_REWARD_FINISHED:
                    if(CreateProjectActivity.this.isFinishing()!=true) {
                        dialog.dismiss();
                        isPublishReward=false;
                        Toast.makeText(CreateProjectActivity.this, "项目发布成功", Toast.LENGTH_SHORT).show();
                    }
                    CreateProjectActivity.this.finish();
                    break;
                default:
                    break;
            }
        }
    };

    private void sendReward(Reward reword) {
        double targetMoney=reword.getSupportMoney();
        String content = reword.getContent();
        String rewardPhoto="";
        for(String s:rewardFileList){
            rewardPhoto+=s;
            rewardPhoto+=",";
        }
        Log.e("TEST2","rewardPhoto "+rewardPhoto);
        FormBody formBody=new FormBody.Builder()
                .add("title", "支持金额： "+targetMoney)
                .add("description",content)
                .add("limitation",targetMoney+"")
                .add("images",rewardPhoto)
                .build();
        Request request = new Request.Builder()
                .post(formBody)
                .url(CreateProjectActivity.this.getString(R.string.url_add_feedback)+projectId+"/feedbacks/")
                .build();

        httpClient = new OkHttpClient();
        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {}

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("TEST","onResponse "+response.toString());
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
                        handler.sendEmptyMessage(PUBLISH_REWARD_IMAGE);
                    }
                }

                Looper.loop();
                return;
            }
        });

    }

     private void sendRewardImages(Reward reword) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for(PhotoInfo p:reword.getPhotoList()){
            File file = new File(p.getPhotoPath());
            Bitmap bm = BitmapUtils.getimage(p.getPhotoPath());
            file=BitmapUtils.saveMyBitmap(bm,file.getName());

            RequestBody requestBody = FormBody.create(MediaType.parse("image/jpeg"), file);
            builder.addFormDataPart("files",file.getName(),requestBody);
            Log.e("TEST2","ADD "+p.getPhotoPath());
        }
        Log.e("TEST2",getString(R.string.url_project_image)+projectId + "/feedback/images");
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .post(requestBody)
                .url(getString(R.string.url_project_image)+projectId + "/feedback/images")
                .build();

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
                        rewardFileList = (ArrayList<String>) item.getData();
                        //完成时调用
                        handler.sendEmptyMessage(PUBLISH_REWARD);
                    }
                }
                Looper.loop();
                return;
            }
        });
    }

    private void sendProject() {
        SharedPreferences share=getSharedPreferences(getString(R.string.sharepreference_login_by_phone),MODE_PRIVATE);
        String images="";
        for(String s:projectFileList){
            images+=s;
            images+=",";
        }

        FormBody.Builder formBuilder=new FormBody.Builder()
                .add("name", projectName)
                .add("targetDeadline",targetTime)
                .add("targetMoney",targetMoney+"")
                .add("description",projectDesc)
                .add("sponsor", share.getInt("id",0)+"")
                .add("cover",projectFileList.get(0))
                .add("images",images);


        FormBody formBody = formBuilder.build();
        Request request = new Request.Builder()
                .post(formBody)
                .url(CreateProjectActivity.this.getString(R.string.url_add_project))
                .build();

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
                        double d=Double.valueOf(item.getData().toString());
                        projectId=(int)d;
                        Log.e("TEST2",projectId+"");
                        //完成时调用
                        handler.sendEmptyMessage(PUBLISH_PROJECT_FINISHED);
                    }
                }
                Looper.loop();
                return;
            }
        });

    }

    private void sendImages() {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for(PhotoInfo p:CreateProjectFragment.getInstance().getAndClearPhotoList()){
            File file = new File(p.getPhotoPath());
            Bitmap bm = BitmapUtils.getimage(p.getPhotoPath());
            file=BitmapUtils.saveMyBitmap(bm,file.getName());
            RequestBody requestBody = FormBody.create(MediaType.parse("image/jpeg"), file);
            builder.addFormDataPart("files",file.getName(),requestBody);
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .post(requestBody)
                .url(getString(R.string.url_project_image) + "/images")
                .build();

        Call call = httpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {}
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Looper.prepare();
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
                        projectFileList = (ArrayList<String>) item.getData();
                        //完成时调用
                        handler.sendEmptyMessage(PUBLISH_PROJECT_IMAGES_FINISH);
                    }
                }
                Looper.loop();
                return;
            }
        });
    }
}
