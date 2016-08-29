package com.fansfunding.fan;

import android.content.Intent;
import android.nfc.Tag;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.*;
import android.util.Log;

import com.fansfunding.fan.login.LoginActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 主界面
 *
 * */

public class MainActivity extends AppCompatActivity{

    //启动登陆activity的请求码
    public static final int REQUEST_CODE_LOGIN=LoginActivity.REQUEST_LOGIN_BY_PHONE;

    //tablayout的tab没有被选中时的图标
    private final int[] tab_unselect={R.drawable.dollar,R.drawable.pjimagetest,R.drawable.more};

    //tablayout的tab被选中时的图标
    private final int[] tab_selected={R.drawable.dollar_pressed,R.drawable.pjimagetest,R.drawable.more_pressed};


    //启动设置界面的activity的请求码

    private ViewPager vp_Main;
    private MainPaperAdapter paperAdapter;
    private TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        paperAdapter=new MainPaperAdapter(getSupportFragmentManager(),this);

        // Set up the ViewPager with the sections adapter.
        vp_Main = (ViewPager) findViewById(R.id.vp_main);
        vp_Main.setAdapter(paperAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tab_main);

        tabLayout.setupWithViewPager(vp_Main);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                for(int i=0;i<paperAdapter.getCount();i++){
                    if(tab==tabLayout.getTabAt(i)){
                        tabLayout.getTabAt(i).setIcon(getResources().getDrawable(tab_selected[i]));
                        vp_Main.setCurrentItem(i);

                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                for(int i=0;i<paperAdapter.getCount();i++){
                    if(tab==tabLayout.getTabAt(i)){
                        tabLayout.getTabAt(i).setIcon(getResources().getDrawable(tab_unselect[i]));
                    }
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //将pviewpaper的缓存页设为3页
        vp_Main.setOffscreenPageLimit(3);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //如果登陆状态改变的话
        if(paperAdapter.isNeedChange()==true){
            paperAdapter.notifyDataSetChanged();
        }
        //设置tablayout的tab的图标
        for (int i = 0; i < paperAdapter.getCount(); i++) {
            if (i == vp_Main.getCurrentItem()) {
                tabLayout.getTabAt(i).setIcon(getResources().getDrawable(tab_selected[i]));
                continue;
            }

            tabLayout.getTabAt(i).setIcon(getResources().getDrawable(tab_unselect[i]));
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            //登录页返回
            case LoginActivity.REQUEST_LOGIN_BY_PHONE:
                if(resultCode==RESULT_OK){
                    paperAdapter.notifyDataSetChanged();
                }
                break;
            //登出页返回
            case UserFragment.REQUEST_CODE_SETTING:
                if(resultCode==SettingActivity.REQUEST_CODE_LOGOUT_SUCCESS){
                    paperAdapter.notifyDataSetChanged();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
