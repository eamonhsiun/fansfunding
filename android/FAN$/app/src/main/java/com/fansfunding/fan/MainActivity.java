package com.fansfunding.fan;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.fansfunding.app.App;
import com.fansfunding.fan.message.BroadcastReceiver.NetWorkStatusReceiver;
import com.fansfunding.fan.message.service.PushService;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.jauker.widget.BadgeView;
import com.umeng.socialize.PlatformConfig;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 主界面
 */

public class MainActivity extends AppCompatActivity {
    private      App app;



    //选中Message Tab时发送消息
    private static final int SELECTED_MESSAGE = 1;

    //没有选中的时候
    private static final int UNSELECTED_MESSAGE = 2;



    //tablayout的tab没有被选中时的图标
    private final int[] tab_unselect = {R.drawable.dollar, R.drawable.pjimagetest, R.drawable.pjimagetest, R.drawable.more};

    //tablayout的tab被选中时的图标
    private final int[] tab_selected = {R.drawable.dollar_pressed, R.drawable.pjimagetest, R.drawable.pjimagetest, R.drawable.more_pressed};


    //启动设置界面的activity的请求码
    private ViewPager vp_Main;
    private MainPaperAdapter paperAdapter;
    private TabLayout tabLayout;

    //消息tablayout的item icon
    private ImageView imageView;

    //网络监听
    private NetWorkStatusReceiver netWorkStatusReceiver = new NetWorkStatusReceiver();

    private PushService.WebSocketBinder webSocketBinder;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            webSocketBinder = (PushService.WebSocketBinder) service;
            webSocketBinder.startConnection();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SELECTED_MESSAGE:
                    imageView.setImageResource(R.drawable.dollar_pressed);
                    break;
                case UNSELECTED_MESSAGE:
                    imageView.setImageResource(R.drawable.dollar);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PlatformConfig.setWeixin("wx73ef904839977e99", "3c62344c9b516cafa30f31a4b2bff001");
        //新浪微博 appkey appsecret
        PlatformConfig.setSinaWeibo("1040021508", "79315d14bec51895cad22aedc0cd3125");
        // QQ和Qzone appid appkey
        PlatformConfig.setQQZone("1105527311", "vZgb9lqVgZyIv98a");

        app = (App)getApplication();
        //初始化小圆点
        BadgeView b = new BadgeView(this);
        app.setBadgeView(b);

        String[] mPermissionList = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS};
        ActivityCompat.requestPermissions(MainActivity.this, mPermissionList, 100);

        paperAdapter = new MainPaperAdapter(getSupportFragmentManager(), this);

        // Set up the ViewPager with the sections adapter.
        vp_Main = (ViewPager) findViewById(R.id.vp_main);
        vp_Main.setAdapter(paperAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tab_main);

        tabLayout.setupWithViewPager(vp_Main);

        //初始化消息tab，将BadgeView与布局绑定
        initMessageTab(2);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                for (int i = 0; i < paperAdapter.getCount(); i++) {
                    if (tab == tabLayout.getTabAt(i)) {
                        if (i != 2) {
                            tabLayout.getTabAt(i).setIcon(getResources().getDrawable(tab_selected[i]));
                        } else {
                            handler.sendEmptyMessage(SELECTED_MESSAGE);
                        }
                        app.getBadgeView().setBadgeCount(i);
                        vp_Main.setCurrentItem(i);
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                for (int i = 0; i < paperAdapter.getCount(); i++) {
                    if (tab == tabLayout.getTabAt(i)) {
                        if (i != 2) {
                            tabLayout.getTabAt(i).setIcon(getResources().getDrawable(tab_unselect[i]));
                        } else {
                            handler.sendEmptyMessage(UNSELECTED_MESSAGE);
                        }

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

        //开启后台服务连接WebSocket
        Intent intent = new Intent(this, PushService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initMessageTab(int position) {
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        if (tab != null) {
            //MessageTab
            View view = LayoutInflater.from(this).inflate(R.layout.tab_title_layout, null);
            imageView = (ImageView) view.findViewById(R.id.iv_message_tab_item);
            tab.setCustomView(view);
//            app.getBadgeView() = new BadgeView(this);
            app.getBadgeView().setGravity(Gravity.TOP | Gravity.RIGHT);
            app.getBadgeView().setTargetView(imageView);
            imageView.setImageResource(R.drawable.dollar);
            app.getBadgeView().setBadgeCount(0);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //如果登陆状态改变的话
        if (paperAdapter.isNeedChange() == true) {
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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            //登录页返回
            case UnlogFragment.REQUEST_CODE_LOGIN:
                if (resultCode == RESULT_OK) {
                    paperAdapter.notifyDataSetChanged();
                }
                break;
            //登出页返回
            case UserFragment.REQUEST_CODE_SETTING:
                if (resultCode == SettingActivity.REQUEST_CODE_LOGOUT_SUCCESS) {
                    paperAdapter.notifyDataSetChanged();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //停止服务
//        Intent intent = new Intent(this, PushService.class);
//        stopService(intent);
        //解绑服务
        unbindService(serviceConnection);
    }

    /**
     * @author RJzz
     * create at 2016/8/27 10:35
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click(MainActivity.this);      //调用双击退出函数
        }
        return false;  //不会执行退出事件
    }

    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    public static void exitBy2Click(MainActivity activity) {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(activity, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            activity.finish();
            System.exit(0);
        }
    }



    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://host/path"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}

