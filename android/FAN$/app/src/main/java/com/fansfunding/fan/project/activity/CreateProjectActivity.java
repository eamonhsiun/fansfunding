package com.fansfunding.fan.project.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.fansfunding.fan.project.fragment.CreateProjectAddRewordFragment;
import com.fansfunding.fan.project.fragment.CreateProjectFragment;
import com.fansfunding.fan.project.fragment.CreateProjectRewordFragment;
import com.fansfunding.fan.project.utils.MotionLessViewPager;
import com.fansfunding.fan.project.adapter.ProjectCreateAdapter;
import com.fansfunding.fan.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 *用来显示个人项目的界面，包括发起的项目，关注的项目，支持的项目
 *
 */

public class CreateProjectActivity extends AppCompatActivity {
    private static final int CREATE_SUCCESS = 1001;
    private static final int SERVER_ERROR = 1002;
    private static final int SEND_SUCCEED = 1003;
    private static final int CREATE_FINISH = 1004;
    private static final int ADD_FEEDBACK_FINISHED = 1005;
    private static final int SEND_RESOURCES = 1006;
    private static final int SEND_PROJECT_COVER_FINISH = 1007;
    private static final int SEND_FEEDBACK_IMAGES_FINISHED= 1008;
    private static String FILE_URI;
    private static int reward_list_state=0;
    private static List<Map<String,Object>> reward_list=new ArrayList<>();
    public static String target_time;
    public static int target_money;
    public static String project_name;
    public static String project_desc;
    public static boolean address_is_need;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){

                case SERVER_ERROR:
                    if(CreateProjectActivity.this.isFinishing()==true){
                        break;
                    }
                    Toast.makeText(CreateProjectActivity.this,"服务器响应失败，请重试",Toast.LENGTH_LONG).show();
                    break;
                case SEND_SUCCEED:
                    if(CreateProjectActivity.this.isFinishing()==true){
                        break;
                    }
                    Toast.makeText(CreateProjectActivity.this,"发起成功",Toast.LENGTH_LONG).show();
                    CreateProjectActivity.this.finish();
                    break;
                case CREATE_FINISH:
                    if(CreateProjectActivity.this.isFinishing()==false){
                        Toast.makeText(CreateProjectActivity.this,"项目上传成功...",Toast.LENGTH_LONG).show();
                        Toast.makeText(CreateProjectActivity.this,"回馈图片上传中...",Toast.LENGTH_LONG).show();
                    }

                    sendFeedbackImages();
                    break;
                case ADD_FEEDBACK_FINISHED:
                    Toast.makeText(CreateProjectActivity.this,"回馈"+reward_list_state+"上传成功",Toast.LENGTH_LONG).show();
                    reward_list_state++;
                    if(reward_list_state<reward_list.size()) {
                        sendFeedBack(reward_list_state);
                    }else{
                        handler.sendEmptyMessage(SEND_SUCCEED);
                    }
                    break;
                case SEND_RESOURCES:
                    if(CreateProjectActivity.this.isFinishing()==false) {
                        Toast.makeText(CreateProjectActivity.this, "封面上传中...", Toast.LENGTH_LONG).show();
                    }
                    sendImages();
                    break;
                case SEND_PROJECT_COVER_FINISH:
                    if(CreateProjectActivity.this.isFinishing()==false) {
                        Toast.makeText(CreateProjectActivity.this, "封面上传成功", Toast.LENGTH_LONG).show();
                    }
                    sendProject();
                    break;
                case SEND_FEEDBACK_IMAGES_FINISHED:
                    if(reward_list_state<reward_list.size()){
                        sendFeedBack(reward_list_state);
                    }
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }

        }
    };

    private void sendFeedbackImages() {
        RequestBody requestBodyTest = FormBody.create(MediaType.parse("image/jpeg"), tempFile);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("files", tempFile.getName(), requestBodyTest)
                .build();
        Request request = new Request.Builder()
                .post(requestBody)
                .url(getString(R.string.url_project_image) + "/images")
                .build();

        Call call = httpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("TEST", "onResponse " + response.toString());
                Looper.prepare();
                if (response == null || response.isSuccessful() == false) {
                    handler.sendEmptyMessage(SERVER_ERROR);
                } else {
                    String str_response = response.body().string();
                    Gson gson = new GsonBuilder().create();
                    Item item = new Item();
                    item = gson.fromJson(str_response, item.getClass());
                    ArrayList<String> list = (ArrayList<String>) item.data;
                    FILE_URI = list.get(0);
                    handler.sendEmptyMessage(SEND_FEEDBACK_IMAGES_FINISHED);
                }
                Looper.loop();
                return;
            }
        });
    }

    private void sendProject() {
        SharedPreferences share=getSharedPreferences(getString(R.string.sharepreference_login_by_phone),MODE_PRIVATE);

                    FormBody formBody=new FormBody.Builder()
                            .add("name", project_name)
                            .add("targetDeadline",target_time)
                            .add("targetMoney",target_money+"")
                            .add("description",project_desc)
                            .add("sponsor", share.getInt("id",0)+"")
                            .add("cover",FILE_URI)
                            .build();

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
                            Log.e("TEST","onResponse "+response.toString());
                            Looper.prepare();
                            if(response==null||response.isSuccessful()==false){
                                handler.sendEmptyMessage(SERVER_ERROR);

                            }else{
                                String str_response=response.body().string();
                                Gson gson=new GsonBuilder().create();
                                Item item = new Item();
                                item=gson.fromJson(str_response,item.getClass());

                                Log.e("TEST",str_response);
                                Log.e("TEST",item.data.toString());
                                double d=Double.valueOf(item.data.toString());
                                project_id=(int)d;
                                Log.e("TEST",project_id+"");


                                handler.sendEmptyMessage(CREATE_FINISH);
                            }
                            Looper.loop();
                            return;
                        }
                    });
    }


    private void sendImages() {
        RequestBody requestBodyTest = FormBody.create(MediaType.parse("image/jpeg"), tempFile);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("files", tempFile.getName(), requestBodyTest)
                .build();
        Request request = new Request.Builder()
                .post(requestBody)
                .url(getString(R.string.url_project_image) + "/images")
                .build();

        Call call = httpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("TEST", "onResponse " + response.toString());
                Looper.prepare();
                if (response == null || response.isSuccessful() == false) {
                    handler.sendEmptyMessage(SERVER_ERROR);
                } else {
                    String str_response = response.body().string();
                    Gson gson = new GsonBuilder().create();
                    Item item = new Item();
                    item = gson.fromJson(str_response, item.getClass());
                    ArrayList<String> list = (ArrayList<String>) item.data;
                    FILE_URI = list.get(0);
                    handler.sendEmptyMessage(SEND_PROJECT_COVER_FINISH);
                }
                Looper.loop();
                return;
            }
        });
    }




    private void sendFeedBack(int i) {
        String title = (String) (reward_list.get(i).get("msg_name"));
        String description =(String) (reward_list.get(i).get("msg_content"));
        String limitation=((String) (reward_list.get(i).get("msg_name"))).replace("支持金额： ","");

        FormBody formBody=new FormBody.Builder()
                .add("title", title)
                .add("description",description)
                .add("limitation",limitation)
                .add("images",FILE_URI)
                .build();

        Log.e("TEST",title + " "+description+" "+limitation );

        Request request = new Request.Builder()
                .post(formBody)
                .url(CreateProjectActivity.this.getString(R.string.url_add_feedback)+project_id+"/feedbacks/")
                .build();

        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("TEST","onResponse "+response.toString());
                Looper.prepare();
                if(response==null||response.isSuccessful()==false){
                    handler.sendEmptyMessage(SERVER_ERROR);
                }else{
                    handler.sendEmptyMessage(ADD_FEEDBACK_FINISHED);
                }
                Looper.loop();
                return;
            }
        });
    }

    //头像的bitmap
    private static Bitmap bitmap;
    private int project_id;

    public void addItem(Map<String,Object> mapItem){
        reward_list.add(mapItem);
    }

    public void removeItem(int i){
        reward_list.remove(i);
        paperAdapter.notifyDataSetChanged();
    }

    public List<Map<String,Object>> getList(){
        return this.reward_list;
    }



    private static final int PHOTO_REQUEST_CAMERA = 1001;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 1002;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 1003;// 结果
    //设置相机所获取的照片的名字
    private static final String PHOTO_FILE_NAME = "temp_head_photo.jpg";
    //用来存储选择的头像的文件
    private File tempFile;
    //设置存储的头像的逃跑名字
    private static final String PHTOT_FILE_NAME_IN_APP="com.fansfunding.fan/photo_file_name_in_app.jpeg";

    //用来保存修改后的头像的文件
    private File photoFile;


    private MotionLessViewPager viewPager;
    private ProjectCreateAdapter paperAdapter;
    private int pageState=0;
    private ActionBar actionBar;
    private MenuItem item;
    private AlertDialog dialog_user_head_source;
    //httpclient
    private OkHttpClient httpClient;

    private static CreateProjectActivity createProjectActivity;

    public static CreateProjectActivity getInstance(){
        return createProjectActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        httpClient=new OkHttpClient();
        createProjectActivity=this;
        setContentView(R.layout.activity_create_project);


        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_createProject);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        actionBar=getSupportActionBar();
        actionBar.setTitle("发起项目");
        actionBar.setDisplayHomeAsUpEnabled(true);

        paperAdapter = new ProjectCreateAdapter(getSupportFragmentManager());

        viewPager=(MotionLessViewPager)findViewById(R.id.vp_project_create);
        viewPager.setAdapter(paperAdapter);
        viewPager.setCurrentItem(pageState);

        String local_file =Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+PHTOT_FILE_NAME_IN_APP;
        photoFile=new File(local_file);
    }

    public void setPageState(int state){
        pageState=state;
        changeFrag();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case android.R.id.home:
                pageState--;
                changeFrag();
                break;
            case R.id.menu_create_next:
                pageState++;
                if(pageState==1){
                    project_desc=CreateProjectFragment.getInstance().et_project_desc.getText().toString();
                    project_name=CreateProjectFragment.getInstance().et_project_title.getText().toString();
                    if(!CreateProjectFragment.getInstance().et_target_money.getText().toString().equals(""))target_money=Integer.parseInt(CreateProjectFragment.getInstance().et_target_money.getText().toString());
                    target_time=CreateProjectFragment.getInstance().tv_project_create_time.getText().toString();
                    if((project_desc==null)||project_name.equals("")||(target_money==1)||target_time.equals("")){
                        Toast.makeText(CreateProjectActivity.this,"(*^__^*) ……数据不能为空嘛",Toast.LENGTH_LONG).show();
                        pageState=0;
                        break;
                    }else {
                        changeFrag();
                        break;
                    }

                }
                if(pageState==3){
                    Map<String,Object> mapItem = new HashMap<>();
                    mapItem.put("msg_name","支持金额： "+CreateProjectAddRewordFragment.getInstance().getSupportMoney());
                    mapItem.put("msg_content",CreateProjectAddRewordFragment.getInstance().getRewardContent());
                    addItem(mapItem);
                }
                if(pageState==2){
                    handler.sendEmptyMessage(SEND_RESOURCES);
                }
                pageState=1;
                changeFrag();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    class Item {
        public boolean result;
        public int errCode;
        public Object data;
        public String token;
    }

    private void changeFrag(){
        switch (pageState){
            case -1:
                finish();
                break;
            case 0:
                item.setTitle("下一步");
                item.setIcon(null);
                break;
            case 1:
                item.setTitle("");
                item.setIcon(getResources().getDrawable(R.drawable.send));
                break;
            case 2:
                item.setTitle("");
                item.setIcon(getResources().getDrawable(R.drawable.correct));
                break;
        }
        actionBar.setTitle(paperAdapter.getPageTitle(pageState));
        viewPager.setCurrentItem(pageState);

        View view = getWindow().peekDecorView();
        //关闭软键盘
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        CreateProjectRewordFragment.getInstance().refreshAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_project_create, menu);
        item = menu.findItem(R.id.menu_create_next);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case PHOTO_REQUEST_GALLERY:
                if (data != null) {
                    tempFile = new File(Environment.getExternalStorageDirectory(),
                            PHOTO_FILE_NAME);
                    // 得到图片的全路径
                    Uri uri = data.getData();
                    crop(uri, Uri.fromFile(photoFile));
                }
                break;

            case PHOTO_REQUEST_CAMERA:
                tempFile = new File(Environment.getExternalStorageDirectory(),
                        PHOTO_FILE_NAME);
                crop(Uri.fromFile(tempFile), Uri.fromFile(photoFile));
                Log.e("TEST","SomeThingWrong106");
                break;

            case PHOTO_REQUEST_CUT:
                Log.e("TEST","SomeThingWrong111");
                if (data == null) {
                    break;
                }
                if (data.getData() == null) {
                    break;
                }

                bitmap = data.getParcelableExtra("data");
               /* Uri uri = data.getData();
                bitmap = decodeUriAsBitmap(uri);*/

                if (photoFile.exists()) {
                    photoFile.delete();
                }
                try {
                    FileOutputStream out = new FileOutputStream(photoFile);

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
                    out.flush();
                    out.close();
                    tempFile = photoFile;

                    //获取压缩过后的bitmap
                    bitmap = decodeUriAsBitmap(Uri.fromFile(photoFile));


                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
        }
    }

    //将uri转化为bitmap
    private Bitmap decodeUriAsBitmap(Uri uri){
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    private void crop(Uri uri,Uri newUri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 800);
        intent.putExtra("outputY", 600);
        // 图片格式
        intent.putExtra(MediaStore.EXTRA_OUTPUT, newUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }



}
