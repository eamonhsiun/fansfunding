package com.fansfunding.fan.project.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Eamon on 2016/7/22.
 */
public class PhotoUtils {
    private static final int PHOTO_REQUEST_CUT = 1003;// 结果

    public static Bitmap decodeUriAsBitmap(Context context,Uri uri){
        Bitmap bitmap = null;
        try {
            BitmapFactory.Options temp_opt = new BitmapFactory.Options();
            temp_opt.inJustDecodeBounds=true;
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri),null,temp_opt);
            Log.i("TAG","Height:"+temp_opt.outHeight);

            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inPreferredConfig = Bitmap.Config.RGB_565;
            opt.inJustDecodeBounds=false;

            if(temp_opt.outHeight>4000||temp_opt.outWidth>4000){
                Log.i("TAG","进入第一个");
                opt.inSampleSize=8;

            }else if(temp_opt.outHeight>2000||temp_opt.outWidth>2000){
                Log.i("TAG","进入第二个");
                opt.inSampleSize=4;
            }else if(temp_opt.outHeight>1000||temp_opt.outWidth>1000){
                opt.inSampleSize=2;
            }
            bitmap=BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri),null,opt);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    //将uri转化为bitmap
    public static Bitmap decodeUriAsBitmap(String path,FileInputStream in){
        Bitmap bitmap = null;
        try {
            BitmapFactory.Options temp_opt = new BitmapFactory.Options();
            temp_opt.inJustDecodeBounds=true;
            BitmapFactory.decodeFile(path,temp_opt);

            Log.i("TAG","Height:"+temp_opt.outHeight);
            if(temp_opt.outHeight>4000||temp_opt.outWidth>4000){
                Log.i("TAG","进入第一个");
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inSampleSize=8;
                opt.inPreferredConfig = Bitmap.Config.RGB_565;
                opt.inJustDecodeBounds=false;
                bitmap=BitmapFactory.decodeStream(in,null,opt);
                Log.i("TAG","HEIGHT2:"+bitmap.getByteCount());

            }else if(temp_opt.outHeight>2000||temp_opt.outWidth>2000){
                Log.i("TAG","进入第二个");
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inSampleSize=4;
                opt.inPreferredConfig = Bitmap.Config.RGB_565;
                opt.inJustDecodeBounds=false;
                bitmap=BitmapFactory.decodeStream(in,null,opt);
                Log.i("TAG","HEIGHT3:"+bitmap.getByteCount());
            }else if(temp_opt.outHeight>1000||temp_opt.outWidth>1000){
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inSampleSize=2;
                opt.inPreferredConfig = Bitmap.Config.RGB_565;
                opt.inJustDecodeBounds=false;
                bitmap=BitmapFactory.decodeStream(in,null,opt);
                Log.i("TAG","HEIGHT4:"+bitmap.getByteCount());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }
    private void crop(Activity context, Uri uri, Uri newUri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 4);
        intent.putExtra("aspectY", 3);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 400);
        intent.putExtra("outputY", 300);
        // 图片格式
        intent.putExtra(MediaStore.EXTRA_OUTPUT, newUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
        context.startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }



}
