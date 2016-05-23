package com.fansfunding.fan;

import android.media.MediaRecorder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AT_Main extends AppCompatActivity {
    private ViewPager vp_Main;
    private MainPaperAdapter paperAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        paperAdapter=new MainPaperAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        vp_Main = (ViewPager) findViewById(R.id.vp_Main);
        vp_Main.setAdapter(paperAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_Main);
        tabLayout.setupWithViewPager(vp_Main);
    }


}
