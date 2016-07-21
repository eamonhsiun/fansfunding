package com.fansfunding.fan;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

/**
 *用来显示个人项目的界面，包括发起的项目，关注的项目，支持的项目
 *
 */

public class MyProjectActivity extends AppCompatActivity {

    private MyProjectPaperAdapter paperAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_project);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_myProject);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("我的项目");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.arrow);
        TabLayout tabLayout=(TabLayout) findViewById(R.id.tab_myProject);

        paperAdapter=new MyProjectPaperAdapter(getSupportFragmentManager());
        ViewPager viewPager=(ViewPager)findViewById(R.id.vp_myProject);
        viewPager.setAdapter(paperAdapter);
        tabLayout.setupWithViewPager(viewPager);

        int number=getIntent().getIntExtra("number",0);


        viewPager.setCurrentItem(number);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;

    }
}
