package com.fansfunding.fan;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.fansfunding.PullListView.XListView;
import com.fansfunding.internal.AllProjectInCategory;
import com.fansfunding.internal.ProjectDetailReward;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ProjectDetailActivity extends AppCompatActivity {


    private ViewPager vp_project_detail;
    private ProjectDetailAdapter adapter;
    private TabLayout tabLayout;

    private Fragment fragment_first;
    private Fragment fragment_second;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detial);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_project_detail);
        setSupportActionBar(toolbar);
        toolbar.setTitle("项目详情");

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(Color.WHITE);
        Intent intent=getIntent();
        AllProjectInCategory.ProjectDetail detail= (AllProjectInCategory.ProjectDetail) intent.getSerializableExtra("detail");
        /* adapter=new ProjectDetailAdapter(getSupportFragmentManager(),detail);

        vp_project_detail=(ViewPager)findViewById(R.id.vp_project_detail) ;
        vp_project_detail.setAdapter(adapter);

        tabLayout=(TabLayout)findViewById(R.id.tab_project_detail);

        tabLayout.setupWithViewPager(vp_project_detail);
        */
        fragment_first=ProjectDetailMainFragment.newInstance(detail);
        fragment_second= ProjectDetailViewPaperFragment.newInstance(detail);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_project_detail_first, fragment_first).add(R.id.frame_project_detail_second, fragment_second)
                .commit();

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
