package com.fansfunding.fan;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.fansfunding.fan.message.BroadcastReceiver.NetWorkStatusReceiver;
import com.umeng.socialize.PlatformConfig;

/**
 * 主界面
 *
 * */

public class MainActivity extends AppCompatActivity{


    //tablayout的tab没有被选中时的图标
    private final int[] tab_unselect={R.drawable.dollar,R.drawable.pjimagetest,R.drawable.pjimagetest,R.drawable.more};

    //tablayout的tab被选中时的图标
    private final int[] tab_selected={R.drawable.dollar_pressed,R.drawable.pjimagetest,R.drawable.pjimagetest,R.drawable.more_pressed};


    //启动设置界面的activity的请求码

    private ViewPager vp_Main;
    private MainPaperAdapter paperAdapter;
    private TabLayout tabLayout;
    private NetWorkStatusReceiver netWorkStatusReceiver = new NetWorkStatusReceiver();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PlatformConfig.setWeixin("wx73ef904839977e99", "3c62344c9b516cafa30f31a4b2bff001");
        //新浪微博 appkey appsecret
        PlatformConfig.setSinaWeibo("1040021508","79315d14bec51895cad22aedc0cd3125");
        // QQ和Qzone appid appkey
        PlatformConfig.setQQZone("1105527311", "vZgb9lqVgZyIv98a");



        String[] mPermissionList = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE,Manifest.permission.READ_LOGS,Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.SET_DEBUG_APP,Manifest.permission.SYSTEM_ALERT_WINDOW,Manifest.permission.GET_ACCOUNTS};
        ActivityCompat.requestPermissions(MainActivity.this,mPermissionList, 100);

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
        //将pviewpaper的缓存页设为4页
        vp_Main.setOffscreenPageLimit(4);

        //注册监听网络状态的广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkStatusReceiver, intentFilter);
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
            case UnlogFragment.REQUEST_CODE_LOGIN:
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(netWorkStatusReceiver);
    }
}
