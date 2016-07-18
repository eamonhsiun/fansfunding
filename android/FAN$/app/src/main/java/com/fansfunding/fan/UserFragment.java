package com.fansfunding.fan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fansfunding.internal.ErrorCode;
import com.fansfunding.internal.PersonalInfo;
import com.fansfunding.internal.UpLoadHead;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

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
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 *
 * 用来显示自己的个人主页
 */
public class UserFragment extends Fragment {



    //打开用户个人信息设置界面
    public static final int START_USER_INFO=301;

    //打开设置界面
    public static final int REQUEST_CODE_SETTING=302;





    //获取用户信息成功
    private static final int GET_USER_INFO_SUCCESS=100;

    //获取用户信息失败
    private static final int GET_USER_INFO_FAILURE=101;

    //是否正在进行用户信息获取,从而避免重复获取信息
    private boolean isGettingUserInfo=false;

    //用来判断是否加载过图片了
    private boolean isGetHead=false;

    //昵称
    private TextView tv_user_name;

    //判断是否需要重新改变
    private boolean isNeedChange=false;

    //头像
    private de.hdodenhof.circleimageview.CircleImageView iv_user_head;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case GET_USER_INFO_FAILURE:
                    if(UserFragment.this.getActivity()!=null)
                        Toast.makeText(UserFragment.this.getActivity(),"获取用户信息失败，请重新登录",Toast.LENGTH_LONG).show();
                    break;
                case GET_USER_INFO_SUCCESS:
                    changeUserInfo();
                    break;
                case ErrorCode.REQUEST_TOO_FRENQUENTLY:
                    if(UserFragment.this.isVisible()==true)
                        break;
                    Toast.makeText(UserFragment.this.getActivity(),"请求频繁",Toast.LENGTH_LONG).show();
                    break;
                case ErrorCode.PARAMETER_ERROR:
                    if(UserFragment.this.isVisible()==true)
                        break;
                    Toast.makeText(UserFragment.this.getActivity(),"参数错误",Toast.LENGTH_LONG).show();
                    break;
                case ErrorCode.AUTHORITY_NOT_ENOUGH:
                    if(UserFragment.this.isVisible()==true)
                        break;
                    Toast.makeText(UserFragment.this.getActivity(),"权限不足",Toast.LENGTH_LONG).show();
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }

        }
    };

    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance() {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        //getUserInfo();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView=inflater.inflate(R.layout.fragment_user, container, false);

        Toolbar toolbar=(Toolbar) rootView.findViewById(R.id.toolbar_user);
        toolbar.setTitle("更多");
        toolbar.setBackgroundColor(Color.RED);
        toolbar.setTitleTextColor(Color.WHITE);

        SharedPreferences share=getActivity().getSharedPreferences(getString(R.string.sharepreference_login_by_phone),Context.MODE_PRIVATE);

        //昵称
        tv_user_name=(TextView)rootView.findViewById(R.id.tv_user_name);
        tv_user_name.setText(share.getString("nickname","冰冻vita"));


        TextView tv_user_project=(TextView)rootView.findViewById(R.id.tv_user_project);
        tv_user_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(getResources().getString(R.string.activity_project));
                startActivity(intent);
            }
        });

        //打开设置界面
        LinearLayout ll_user_setting=(LinearLayout)rootView.findViewById(R.id.ll_user_setting);
        ll_user_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(getString(R.string.activity_setting));
                getActivity().startActivityForResult(intent,REQUEST_CODE_SETTING);
            }
        });


        //头像
        iv_user_head=(de.hdodenhof.circleimageview.CircleImageView)rootView.findViewById(R.id.iv_user_head);

        String url_head=share.getString("head","");
        //加载头像数据
        if(url_head.equals("")==false&&isGetHead==false){
            Picasso.with(getActivity()).load(getString(R.string.url_resources)+url_head).into(iv_user_head);
            //isGetHead=true;
        }


        //个人信息修改
        RelativeLayout rl_user_information=(RelativeLayout)rootView.findViewById(R.id.rl_user_information);

        rl_user_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(getString(R.string.activity_user_info));
                startActivityForResult(intent,START_USER_INFO);
            }
        });
        getUserInfo();
        return rootView;
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

    @Override
    public void onResume() {
        super.onResume();
        if(isNeedChange==true){
            getUserInfo();
            isNeedChange=false;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void getUserInfo(){
        OkHttpClient httpClient=new OkHttpClient();
        SharedPreferences share=getActivity().getSharedPreferences(getString(R.string.sharepreference_login_by_phone),Context.MODE_PRIVATE);
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

                Gson gson=new GsonBuilder().create();
                PersonalInfo personalInfo=new PersonalInfo();
                String str_response=response.body().string();
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
                    //将个人信息保存到SharePreference里
                    SharedPreferences share=UserFragment.this.getActivity().getSharedPreferences(getString(R.string.sharepreference_user_info),UserFragment.this.getActivity().MODE_PRIVATE);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case START_USER_INFO:
                if(resultCode==getActivity().RESULT_OK){
                    isNeedChange=true;
                    System.out.println("success");
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void changeUserInfo(){
        SharedPreferences share=getActivity().getSharedPreferences(getString(R.string.sharepreference_user_info),Context.MODE_PRIVATE);
        String user_info_url_head=share.getString("head","");
        String user_info_nickname=share.getString("nickname","");

        SharedPreferences share_login=getActivity().getSharedPreferences(getString(R.string.sharepreference_login_by_phone),Context.MODE_PRIVATE);
        String login_by_phone_url_head=share_login.getString("head","");
        String login_by_phone_nickname=share_login.getString("nickname","");
        SharedPreferences.Editor editor_login=share_login.edit();

        //如果修改过了昵称
        if(user_info_nickname.equals(login_by_phone_nickname)==false){
            tv_user_name.setText(user_info_nickname);
            editor_login.putString("nickname",user_info_nickname);
        }
        if(user_info_url_head.equals(login_by_phone_url_head)==false){
            //加载头像数据
            if(user_info_url_head.equals("")==false){
                Picasso.with(getActivity()).load(getString(R.string.url_resources)+user_info_url_head).memoryPolicy(MemoryPolicy.NO_CACHE).into(iv_user_head);
                isGetHead=true;
            }
            editor_login.putString("head",user_info_url_head);
        }


        editor_login.commit();
    }
}
