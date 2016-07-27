package com.fansfunding.fan.utils;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.fansfunding.fan.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

//查看大图类

public class BigPictureActivity extends AppCompatActivity {

    private PhotoViewAttacher attacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_picture);

        Intent intent=getIntent();
        String url=intent.getStringExtra("url");
        PhotoView pv_big_picture=(PhotoView)findViewById(R.id.pv_big_picture);

        if(url!=null&&url.equals("")==false){
            Picasso.with(this).load(url).memoryPolicy(MemoryPolicy.NO_CACHE).into(pv_big_picture);
        }

    }
}
