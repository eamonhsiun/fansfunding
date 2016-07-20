package com.fansfunding.fan;

import android.content.Intent;
import android.nfc.Tag;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.*;
import android.util.Log;

/**
 * 主界面
 *
 * */

public class MainActivity extends AppCompatActivity{


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
                if(tab==tabLayout.getTabAt(0)){
                    tabLayout.getTabAt(0).setIcon(getResources().getDrawable(R.drawable.dollar_pressed));
                    vp_Main.setCurrentItem(0);

                }
                else if(tab==tabLayout.getTabAt(1)){
                    tabLayout.getTabAt(1).setIcon(getResources().getDrawable(R.drawable.more_pressed));
                    vp_Main.setCurrentItem(1);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if(tab==tabLayout.getTabAt(0)){
                    tabLayout.getTabAt(0).setIcon(getResources().getDrawable(R.drawable.dollar));

                }
                else if(tab==tabLayout.getTabAt(1)){
                    tabLayout.getTabAt(1).setIcon(getResources().getDrawable(R.drawable.more));
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
        for (int i = 0; i < paperAdapter.getCount(); i++) {
            if (i == vp_Main.getCurrentItem()) {
                switch (i){
                    case 0:
                        tabLayout.getTabAt(i).setIcon(getResources().getDrawable(R.drawable.dollar_pressed));
                        break;
                    case 1:
                        tabLayout.getTabAt(i).setIcon(getResources().getDrawable(R.drawable.more_pressed));
                        break;
                }
                continue;
            }
            switch (i){
                case 0:
                    tabLayout.getTabAt(i).setIcon(getResources().getDrawable(R.drawable.dollar));
                    break;
                case 1:
                    tabLayout.getTabAt(i).setIcon(getResources().getDrawable(R.drawable.more));
                    break;
            }

        }


    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case UnlogFragment.REQUEST_CODE_LOGIN:
                if(resultCode==RESULT_OK){
                    paperAdapter.notifyDataSetChanged();
                }
                break;
            case UserFragment.REQUEST_CODE_SETTING:
                if(resultCode==SettingActivity.REQUEST_CODE_LOGOUT_SUCCESS){
                    paperAdapter.notifyDataSetChanged();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
