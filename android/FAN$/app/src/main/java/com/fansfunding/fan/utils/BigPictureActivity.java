package com.fansfunding.fan.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.fansfunding.fan.R;
import com.nostra13.universalimageloader.utils.L;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

//查看大图类

public class BigPictureActivity extends AppCompatActivity {

    public static final String URL="url";

    private PhotoViewAttacher attacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_picture);

        Intent intent=getIntent();
        String url=intent.getStringExtra(URL);
        PhotoView pv_big_picture=(PhotoView)findViewById(R.id.pv_big_picture);

        if(url!=null&&url.equals("")==false){
            Picasso.with(this).load(url).memoryPolicy(MemoryPolicy.NO_CACHE).into(pv_big_picture);
        }

    }

    public static void startThisActivity(Activity activity,String url){
        Intent intent=new Intent();
        intent.putExtra(URL,url);
        intent.setAction(activity.getString(R.string.activity_big_picture));
        activity.startActivity(intent);
    }

}
