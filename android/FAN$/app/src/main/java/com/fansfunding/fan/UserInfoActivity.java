package com.fansfunding.fan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fansfunding.internal.ErrorCode;
import com.fansfunding.internal.PersonalInfo;
import com.fansfunding.internal.UpLoadHead;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class UserInfoActivity extends AppCompatActivity {


    private static final int PHOTO_REQUEST_CAMERA = 1001;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 1002;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 1003;// 结果

    //判断用户信息是否改变
    private boolean isChange=false;

    //获取用户信息成功
    private static final int GET_USER_INFO_SUCCESS=100;

    //获取用户信息失败
    private static final int GET_USER_INFO_FAILURE=101;

    //上传头像失败
    private static final int SEND_USER_HEAD_FAILURE=102;

    //上传头像成功
    private static final int SEND_USER_HEAD_SUCCESS=103;

    //上传用户信息失败
    private static final int SEND_USER_INFO_FAILURE=104;

    //上传用户信息成功
    private static final int SEND_USER_INFO_SUCCESS=105;


    //设置相机所获取的照片的名字
    private static final String PHOTO_FILE_NAME = "temp_head_photo.jpg";

    //设置存储的头像的逃跑名字
    private static final String PHTOT_FILE_NAME_IN_APP="com.fansfunding.fan/photo_file_name_in_app.jpeg";

    //spinner的数据源
    private static final String[] sexString={"性别","女","男"};

    //httpclient
    OkHttpClient httpClient;


    //用来存储选择的头像的文件
    private File tempFile;

    //用来保存修改后的头像的文件
    private File photoFile;
    //头像的bitmap
    private Bitmap bitmap;

    //性别
    private int sex=-1;

    //循环等待框，不能取消
    AlertDialog dialog_waitting;

    //选择头像来源的弹出框
    AlertDialog dialog_user_head_source;


    //头像控件
    private CircleImageView  iv_user_info_head;
    //昵称输入栏
    private TextInputEditText tiet_user_info_nickname;
    //性别选择框
    private Spinner spinner;
    //邮箱输入栏
    private TextInputEditText tiet_user_info_email;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                //获取用户信息失败
                case GET_USER_INFO_FAILURE:
                    if(UserInfoActivity.this.isFinishing()==true)
                        break;
                    Toast.makeText(UserInfoActivity.this,"获取信息失败",Toast.LENGTH_LONG).show();
                    if(dialog_waitting.isShowing()==true){
                        dialog_waitting.cancel();
                    }
                    break;
                //获取用户信息成功
                case GET_USER_INFO_SUCCESS:
                    if(UserInfoActivity.this.isFinishing()==true)
                        break;
                    InitUserInfo();
                    if(dialog_waitting.isShowing()==true){
                        dialog_waitting.cancel();
                    }
                    break;
                //上传用户头像失败
                case SEND_USER_HEAD_FAILURE:
                    if(UserInfoActivity.this.isFinishing()==true)
                        break;
                    Toast.makeText(UserInfoActivity.this,"上传头像失败",Toast.LENGTH_LONG).show();
                    if(dialog_waitting.isShowing()==true){
                        dialog_waitting.cancel();
                    }
                    break;
                //上传用户头像成功
                case SEND_USER_HEAD_SUCCESS:
                    if(UserInfoActivity.this.isFinishing()==true)
                        break;
                    Toast.makeText(UserInfoActivity.this,"上传头像成功",Toast.LENGTH_LONG).show();

                    //已经更改了头像
                    isChange=true;
                    UploadUserInfo();
                    break;
                //上传用户信息失败
                case SEND_USER_INFO_FAILURE:
                    if(UserInfoActivity.this.isFinishing()==true)
                        break;
                    Toast.makeText(UserInfoActivity.this,"上传用户信息失败",Toast.LENGTH_LONG).show();
                    if(dialog_waitting.isShowing()==true){
                        dialog_waitting.cancel();
                    }
                    break;
                //上传用户信息成功
                case SEND_USER_INFO_SUCCESS:
                    if(UserInfoActivity.this.isFinishing()==true)
                        break;
                    Toast.makeText(UserInfoActivity.this,"上传用户信息成功",Toast.LENGTH_LONG).show();
                    if(dialog_waitting.isShowing()==true){
                        dialog_waitting.cancel();
                    }
                    isChange=true;
                    break;
                case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                    if(UserInfoActivity.this.isFinishing()==true)
                        break;
                    Toast.makeText(UserInfoActivity.this,"请求频繁",Toast.LENGTH_LONG).show();
                    if(dialog_waitting.isShowing()==true){
                        dialog_waitting.cancel();
                    }
                    break;
                case ErrorCode.PARAMETER_ERROR:
                    if(UserInfoActivity.this.isFinishing()==true)
                        break;
                    Toast.makeText(UserInfoActivity.this,"参数错误",Toast.LENGTH_LONG).show();
                    if(dialog_waitting.isShowing()==true){
                        dialog_waitting.cancel();
                    }
                    break;
                case ErrorCode.AUTHORITY_NOT_ENOUGH:
                    if(UserInfoActivity.this.isFinishing()==true)
                        break;
                    Toast.makeText(UserInfoActivity.this,"权限不足",Toast.LENGTH_LONG).show();
                    if(dialog_waitting.isShowing()==true){
                        dialog_waitting.cancel();
                    }
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_user_info);
        setSupportActionBar(toolbar);
        Intent intent=getIntent();

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setBackgroundColor(Color.RED);
        //设置返回键
        ActionBar actionBar=this.getSupportActionBar();
        actionBar.setTitle("编辑个人信息");
        actionBar.setDisplayHomeAsUpEnabled(true);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,sexString);

        //设置性别选择框
        spinner=(Spinner)findViewById(R.id.spinner_user_info_sex);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sex=position-1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner.setVisibility(View.VISIBLE);

        httpClient=new OkHttpClient();

        //获取并设置修改头像按钮
        Button btn_user_info_head_change=(Button)findViewById(R.id.btn_user_info_head_change);
        btn_user_info_head_change.setOnClickListener(new ChangeHeadListener());

        getUserInfo();

        String local_file =Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+PHTOT_FILE_NAME_IN_APP;
        photoFile=new File(local_file);

        if(photoFile.getParentFile().exists()==false){
            photoFile.getParentFile().mkdirs();
        }

        SharedPreferences share=getSharedPreferences(getString(R.string.sharepreference_login_by_phone),MODE_PRIVATE);
        String head=share.getString("head","");

        iv_user_info_head=(CircleImageView)findViewById(R.id.iv_user_info_head);

        if(head.equals("")==false){
            Picasso.with(this).load(getString(R.string.url_resources)+head).into(iv_user_info_head);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case android.R.id.home:
                if(isChange==true){
                    setResult(RESULT_OK);
                }
                finish();
                break;
            case R.id.menu_user_info_finish:
                UploadUserInfo_All();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_info, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case PHOTO_REQUEST_GALLERY:
                if (data != null) {
                    // 得到图片的全路径
                    Uri uri = data.getData();
                    crop(uri,Uri.fromFile(photoFile));

                }
                break;
            case PHOTO_REQUEST_CAMERA:
                if (hasSdcard()) {
                    tempFile = new File(Environment.getExternalStorageDirectory(),
                            PHOTO_FILE_NAME);
                    crop(Uri.fromFile(tempFile),Uri.fromFile(photoFile));
                } else {
                    Toast.makeText(UserInfoActivity.this, "未找到存储卡，无法存储照片！", Toast.LENGTH_LONG).show();
                }
                break;

            case PHOTO_REQUEST_CUT:
                if(data==null) {
                    break;
                }
                if(data.getData()==null){
                    break;
                }
                Uri uri=data.getData();
                bitmap = decodeUriAsBitmap(uri);
                if (photoFile.exists()) {
                    photoFile.delete();
                }
                try {
                    FileOutputStream out = new FileOutputStream(photoFile);

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
                    out.flush();
                    out.close();
                    tempFile=photoFile;

                    //获取压缩过后的bitmap
                    bitmap=decodeUriAsBitmap(Uri.fromFile(photoFile));
                    iv_user_info_head.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }

    }

    //上传用户所有信息，包括头像与昵称
    private void UploadUserInfo_All(){
        //循环等待框
        dialog_waitting=new AlertDialog.Builder(this)
                .setTitle("数据传输")
                .setView(R.layout.activity_internal_waiting)
                .create();
        dialog_waitting.setCancelable(false);
        dialog_waitting.show();

        //先上传用户头像，如果没有头像修改，则直接上传其他信息
        if(tempFile!=null){
            UploadUserHead();
        }
        //上传用户其他信息
        else{
            System.out.println();
            UploadUserInfo();
        }


    }


    //上传用户除头像外的信息
    private void UploadUserInfo(){

        //获取用户id
        SharedPreferences share=getSharedPreferences(getString(R.string.sharepreference_user_info), MODE_PRIVATE);

        //获得修改后的昵称
        String new_nickname=tiet_user_info_nickname.getText().toString();
        //获得性别(为全局变量，因此不用声明)

        //获取修改后email地址
        String new_email=tiet_user_info_email.getText().toString();

        //昵称
        String old_nickname=share.getString("nickname","");
        //性别，默认为女，0为女，1为男
        int old_sex=share.getInt("sex",-1);
        //邮箱
        String old_email= share.getString("email","");


/*        System.out.println("new_nickname:"+new_nickname);
        System.out.println("old_nickname:"+old_nickname);
        System.out.println("sex:"+sex);
        System.out.println("old_sex:"+old_sex);
        System.out.println("new_email:"+new_email);
        System.out.println("old_email:"+old_email);*/
        //如果未做改动
        if(new_nickname.equals(old_nickname)
                &&sex==old_sex
                &&new_email.equals(old_email)){
            Message msg=new Message();
            msg.what=SEND_USER_INFO_SUCCESS;
            handler.sendMessage(msg);
            return;
        }

        SharedPreferences share_token=getSharedPreferences(getString(R.string.sharepreference_login_by_phone),MODE_PRIVATE);
        String token=share_token.getString("token"," ");
        int userId=share_token.getInt("id",0);
        System.out.println("userID："+userId);
        System.out.println("token："+token);
        FormBody.Builder builder=new FormBody.Builder()
                .add("token",token)
                .add("email",new_email)
                .add("nickname",new_nickname)
                ;
        if(sex!=-1){
            builder.add("sex",String.valueOf(sex));
        }
        FormBody formBody=builder.build();

        Request request=new Request.Builder()
                .url(getString(R.string.url_user)+userId+"/info")
                .post(formBody)
                .build();

        Call call=httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg=new Message();
                msg.what=SEND_USER_INFO_FAILURE;
                handler.sendMessage(msg);
                System.out.println("error0");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //服务器响应失败
                if(response==null||response.isSuccessful()==false){
                    Message msg=new Message();
                    msg.what=SEND_USER_INFO_FAILURE;
                    handler.sendMessage(msg);
                    System.out.println("error1");
                    return;
                }
                String str_response=response.body().string();
                Gson gson=new GsonBuilder().create();
                PersonalInfo personalInfo=new PersonalInfo();
                System.out.println(str_response);
                try {

                    //用Gson进行解析，并判断结果是否为空
                    if((personalInfo = gson.fromJson(str_response, personalInfo.getClass()))==null){
                        Message msg=new Message();
                        msg.what=SEND_USER_INFO_FAILURE;
                        handler.sendMessage(msg);
                        System.out.println("error2");
                        return;
                    }
                    //处理登陆失败
                    if(personalInfo.isResult()==false){
                        Message msg=new Message();
                        switch (personalInfo.getErrCode()){
                            case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                                msg.what=ErrorCode.REQUEST_TOO_FRENQUENTLY;
                                break;
                            case ErrorCode.PARAMETER_ERROR:
                                msg.what=ErrorCode.PARAMETER_ERROR;
                                break;
                            case ErrorCode.AUTHORITY_NOT_ENOUGH:
                                msg.what=ErrorCode.AUTHORITY_NOT_ENOUGH;
                                break;
                            default:
                                msg.what=SEND_USER_INFO_FAILURE;
                                break;
                        }
                        handler.sendMessage(msg);
                        return;
                    }
                    //将上传头像的返回信息保存到SharePreference里
                    SharedPreferences share=getSharedPreferences(getString(R.string.sharepreference_user_info),MODE_PRIVATE);
                    SharedPreferences.Editor editor=share.edit();
                    editor.putBoolean("result",personalInfo.isResult());
                    editor.putInt("errCode",personalInfo.getErrCode());
                    if(personalInfo.getData()!=null) {
                        editor.putString("name", personalInfo.getData().getName());
                        editor.putString("nickname", personalInfo.getData().getNickname());
                        editor.putString("phone", personalInfo.getData().getPhone());
                        editor.putString("head", personalInfo.getData().getHead());
                        editor.putString("email", personalInfo.getData().getEmail());
                        editor.putString("token", personalInfo.getToken());
                        editor.putInt("is_red", personalInfo.getData().getIs_red());

                        if (personalInfo.getData().getRealInfo() != null) {
                            editor.putInt("readInfo_sex", personalInfo.getData().getRealInfo().getSex());
                            editor.putString("readInfo_realName", personalInfo.getData().getRealInfo().getRealName());
                            editor.putString("readInfo_birthPlace", personalInfo.getData().getRealInfo().getBirthPlace());
                            editor.putLong("readInfo_birthday", personalInfo.getData().getRealInfo().getBirthday());
                        }
                    }
                    editor.commit();

                    //上传头像成功
                    Message msg=new Message();
                    msg.what=SEND_USER_INFO_SUCCESS;
                    handler.sendMessage(msg);

                }catch (IllegalStateException e){
                    Message msg=new Message();
                    msg.what=SEND_USER_INFO_FAILURE;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }catch (JsonSyntaxException e){
                    Message msg=new Message();
                    msg.what=SEND_USER_INFO_FAILURE;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            }
        });

    }

    //上传用户头像
    private void UploadUserHead(){
        //获取用户id
        SharedPreferences share=getSharedPreferences(getString(R.string.sharepreference_login_by_phone), MODE_PRIVATE);
        int userId=share.getInt("id",0);

        RequestBody requestBodyTest= FormBody.create(MediaType.parse("image/jpeg"),tempFile);
        RequestBody requestBody=new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", tempFile.getName(),requestBodyTest)
                .build();
        Request request=new Request.Builder()
                .post(requestBody)
                .url(getString(R.string.url_userbasic)+userId+"/head")
                .build();
        Call call=httpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg=new Message();
                msg.what=SEND_USER_HEAD_FAILURE;
                handler.sendMessage(msg);
                System.out.println("error1");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //服务器响应失败
                if(response==null||response.isSuccessful()==false){
                    Message msg=new Message();
                    msg.what=SEND_USER_HEAD_FAILURE;
                    handler.sendMessage(msg);
                    System.out.println("error2");
                    return;
                }
                Gson gson=new GsonBuilder().create();
                UpLoadHead uploadHead=new UpLoadHead();
                String str_response=response.body().string();
                System.out.println(str_response);
                try {

                    //用Gson进行解析，并判断结果是否为空
                    if((uploadHead = gson.fromJson(str_response, uploadHead.getClass()))==null){
                        Message msg=new Message();
                        msg.what=SEND_USER_HEAD_FAILURE;
                        handler.sendMessage(msg);
                        System.out.println("error3");
                        return;
                    }
                    //处理登陆失败
                    if(uploadHead.isResult()==false){
                        Message msg=new Message();
                        switch (uploadHead.getErrCode()){
                            case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                                msg.what=ErrorCode.REQUEST_TOO_FRENQUENTLY;
                                break;
                            case ErrorCode.PARAMETER_ERROR:
                                msg.what=ErrorCode.PARAMETER_ERROR;
                                break;
                            case ErrorCode.AUTHORITY_NOT_ENOUGH:
                                msg.what=ErrorCode.AUTHORITY_NOT_ENOUGH;
                                break;
                            default:
                                msg.what=SEND_USER_HEAD_FAILURE;
                                break;
                        }
                        handler.sendMessage(msg);
                        return;
                    }
                    //将上传头像的返回信息保存到SharePreference里
                    SharedPreferences share=getSharedPreferences(getString(R.string.sharepreference_upload_head),MODE_PRIVATE);
                    SharedPreferences.Editor editor=share.edit();
                    editor.putBoolean("result",uploadHead.isResult());
                    editor.putInt("errCode",uploadHead.getErrCode());
                    editor.putString("data",uploadHead.getData());
                    editor.commit();

                    //上传头像成功
                    Message msg=new Message();
                    msg.what=SEND_USER_HEAD_SUCCESS;
                    handler.sendMessage(msg);

                }catch (IllegalStateException e){
                    Message msg=new Message();
                    msg.what=SEND_USER_HEAD_FAILURE;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }catch (JsonSyntaxException e){
                    Message msg=new Message();
                    msg.what=SEND_USER_HEAD_FAILURE;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }

            }
        });
    }

    //在获取到用户信息后初始化
    private void InitUserInfo(){

        SharedPreferences share=getSharedPreferences(getString(R.string.sharepreference_user_info),MODE_PRIVATE);
        //昵称
        String nickname=share.getString("nickname","");
        //性别
        sex=share.getInt("sex",-1);
        //邮箱
        String email= share.getString("email","");
        //头像url
        String url_head=share.getString("head","");

        //用户昵称初始化
        tiet_user_info_nickname=(TextInputEditText)findViewById(R.id.tiet_user_info_nickname);
        tiet_user_info_nickname.setText(nickname);

        //用户性别初始化
        spinner.setSelection(0);

        //用户邮箱初始化
        tiet_user_info_email=(TextInputEditText)findViewById(R.id.tiet_user_info_email);
        tiet_user_info_email.setText(email);

        //获取头像控件并设置
        if(url_head.equals("")==false){
            //Picasso.with(this).load(url_head).into(iv_user_info_head);
        }
    }

    private void getUserInfo(){
        //循环等待框
        dialog_waitting=new AlertDialog.Builder(this)
                .setTitle("数据传输")
                .setView(R.layout.activity_internal_waiting)
                .create();
        dialog_waitting.setCancelable(false);
        dialog_waitting.show();

        httpClient=new OkHttpClient();
        SharedPreferences share=getSharedPreferences(getString(R.string.sharepreference_login_by_phone), Context.MODE_PRIVATE);
        int userId=share.getInt("id",0);
        String token=share.getString("token","token");
        Request request=new Request.Builder()
                .url(getString(R.string.url_user)+userId+"/info"+"?token="+token)
                //.addHeader("token",token)
                .get()
                .build();
        System.out.println(userId);
        System.out.println(token);
        Call call=httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg=new Message();
                msg.what=GET_USER_INFO_FAILURE;
                handler.sendMessage(msg);
                System.out.println("error1");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //服务器响应失败
                if(response==null||response.isSuccessful()==false){
                    Message msg=new Message();
                    msg.what=GET_USER_INFO_FAILURE;
                    handler.sendMessage(msg);
                    System.out.println("error2");
                    return;
                }

                Gson gson=new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                PersonalInfo personalInfo=new PersonalInfo();
                String str_response=response.body().string();
                System.out.println(str_response);
                try {
                    //用Gson进行解析，并判断结果是否为空
                    if((personalInfo = gson.fromJson(str_response, personalInfo.getClass()))==null){
                        Message msg = new Message();
                        msg.what = GET_USER_INFO_FAILURE;
                        handler.sendMessage(msg);
                        System.out.println("error3");
                        return;
                    }
                    //处理获取个人信息失败
                    if(personalInfo.isResult()==false){
                        Message msg=new Message();
                        switch (personalInfo.getErrCode()){
                            case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                                msg.what=ErrorCode.REQUEST_TOO_FRENQUENTLY;
                                break;
                            case ErrorCode.PARAMETER_ERROR:
                                msg.what=ErrorCode.PARAMETER_ERROR;
                                break;
                            case ErrorCode.AUTHORITY_NOT_ENOUGH:
                                msg.what=ErrorCode.AUTHORITY_NOT_ENOUGH;
                                break;
                            default:
                                msg.what=GET_USER_INFO_FAILURE;
                                break;
                        }
                        handler.sendMessage(msg);
                        return;
                    }
                    //将验证码信息保存到SharePreference里
                    SharedPreferences share=UserInfoActivity.this.getSharedPreferences(getString(R.string.sharepreference_user_info),MODE_PRIVATE);
                    SharedPreferences.Editor editor=share.edit();
                    editor.putBoolean("result",personalInfo.isResult());
                    editor.putInt("errCode",personalInfo.getErrCode());
                    if(personalInfo.getData()!=null) {
                        editor.putString("name", personalInfo.getData().getName());
                        editor.putString("nickname", personalInfo.getData().getNickname());
                        editor.putString("phone", personalInfo.getData().getPhone());
                        editor.putString("head", personalInfo.getData().getHead());
                        editor.putString("email", personalInfo.getData().getEmail());
                        editor.putString("token", personalInfo.getToken());
                        editor.putInt("is_red", personalInfo.getData().getIs_red());

                        if (personalInfo.getData().getRealInfo() != null) {
                            editor.putInt("readInfo_sex", personalInfo.getData().getRealInfo().getSex());
                            editor.putString("readInfo_realName", personalInfo.getData().getRealInfo().getRealName());
                            editor.putString("readInfo_birthPlace", personalInfo.getData().getRealInfo().getBirthPlace());

                            editor.putLong("readInfo_birthday", personalInfo.getData().getRealInfo().getBirthday());
                        }
                    }
                    editor.commit();
                    //获取信息成功
                    Message msg=new Message();
                    msg.what=GET_USER_INFO_SUCCESS;
                    handler.sendMessage(msg);
                }catch (IllegalStateException e){
                    Message msg=new Message();
                    msg.what=GET_USER_INFO_FAILURE;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }catch (JsonSyntaxException e){
                    Message msg=new Message();
                    msg.what=GET_USER_INFO_FAILURE;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }


            }
        });
    }




    //修改头像按钮响应函数
    private class ChangeHeadListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            View view_source=View.inflate(UserInfoActivity.this,R.layout.dialog_user_info_head_source,null);

            //从相机获取头像
            final TextView tv_user_info_head_from_camera=(TextView)view_source.findViewById(R.id.tv_user_info_head_from_camera);

            tv_user_info_head_from_camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //取消弹出框
                    if(dialog_user_head_source.isShowing()==true){
                        dialog_user_head_source.cancel();
                    }
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    // 判断存储卡是否可以用，可用进行存储
                    if (hasSdcard()) {
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(new File(Environment
                                        .getExternalStorageDirectory(), PHOTO_FILE_NAME)));
                    }
                    startActivityForResult(intent, PHOTO_REQUEST_CAMERA);

                }
            });

            //从相册获取头像
            TextView tv_user_info_head_from_photo=(TextView)view_source.findViewById(R.id.tv_user_info_head_from_photo);
            tv_user_info_head_from_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //取消弹出框
                    if(dialog_user_head_source.isShowing()==true){
                        dialog_user_head_source.cancel();
                    }
                    // 激活系统图库，选择一张图片
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/jpeg,image/jpg");
                    startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
                }
            });

            dialog_user_head_source=new AlertDialog.Builder(UserInfoActivity.this)
                    .setTitle("选择头像")
                    .setView(view_source)
                    .create();
            dialog_user_head_source.show();
        }
    }

    //判断呢是否有sd卡
    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 剪切图片
     * @param uri
     */
    private void crop(Uri uri,Uri newUri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        // 图片格式
        intent.putExtra(MediaStore.EXTRA_OUTPUT, newUri);
        intent.putExtra("outputFormat", "Bitmap.CompressFormat.JPEG.toString()");
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", false);// true:不返回uri，false：返回uri
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            if(isChange==true){
                setResult(RESULT_OK);
            }
        }
        return super.onKeyDown(keyCode,event);
    }

}
