package com.fansfunding.fan;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class ProjectDetailActivity extends AppCompatActivity {


    private ViewPager vp_project_detail;
    private ProjectDetailAdapter adapter;
    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detial);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_project_detail);
        setSupportActionBar(toolbar);
        toolbar.setTitle("项目详情");
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("项目详情");
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(Color.WHITE);
        adapter=new ProjectDetailAdapter(getSupportFragmentManager());

        vp_project_detail=(ViewPager)findViewById(R.id.vp_project_detail) ;
        vp_project_detail.setAdapter(adapter);

        tabLayout=(TabLayout)findViewById(R.id.tab_project_detail);

        tabLayout.setupWithViewPager(vp_project_detail);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_project_detail, menu);
        return true;
    }
}
