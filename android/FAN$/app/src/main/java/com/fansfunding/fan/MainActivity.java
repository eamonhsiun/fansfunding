package com.fansfunding.fan;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.*;

/**
 * 主界面
 *
 * */

public class MainActivity extends AppCompatActivity {
    private ViewPager vp_Main;
    private MainPaperAdapter paperAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        paperAdapter=new MainPaperAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        vp_Main = (ViewPager) findViewById(R.id.vp_main);
        vp_Main.setAdapter(paperAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_main);
        tabLayout.setupWithViewPager(vp_Main);


    }


}
