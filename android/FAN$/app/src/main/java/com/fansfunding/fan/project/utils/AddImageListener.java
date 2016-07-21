package com.fansfunding.fan.project.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fansfunding.fan.R;

import java.io.File;

/**
 * Created by Eamon on 2016/7/19.
 */
public class AddImageListener implements View.OnClickListener {
    //设置相机所获取的照片的名字
    private static final String PHOTO_FILE_NAME = "temp_head_photo.jpg";
    private static final int PHOTO_REQUEST_CAMERA = 1001;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 1002;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 1003;// 结果

    private View view;
    private FragmentActivity context;

    //选择头像来源的弹出框
    private AlertDialog dialog_user_head_source;
    private View view_source;

    public AddImageListener(FragmentActivity context,String msg){
        this.context=context;
        view_source=View.inflate(context, R.layout.dialog_user_info_head_source,null);
        dialog_user_head_source=new AlertDialog.Builder(context)
                .setTitle(msg)
                .setView(view_source)
                .create();
        //避免多次初始化
        initSysPhotoCallBackViews(context);

    }

    private void initSysPhotoCallBackViews(final Activity context) {


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
                }else {
                    Toast.makeText(context, "未找到存储卡，无法存储照片！", Toast.LENGTH_LONG).show();
                }
                Log.e("TEST","SomeThingWrong101");
                startIntent(intent,PHOTO_REQUEST_CAMERA);
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

                Log.e("TEST","SomeThingWrong102");
                startIntent(intent, PHOTO_REQUEST_GALLERY);
            }
        });
    }


    private void startIntent(Intent intent,int id) {
        Log.e("TEST","SomeThingWrong103");
        context.startActivityForResult(intent, id);
        Log.e("TEST","SomeThingWrong104");
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

    @Override
    public void onClick(View v) {
        dialog_user_head_source.show();
    }

}
