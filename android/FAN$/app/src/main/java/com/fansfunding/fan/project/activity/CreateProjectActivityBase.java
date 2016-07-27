package com.fansfunding.fan.project.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.fansfunding.fan.R;
import com.fansfunding.fan.project.adapter.ProjectCreateAdapter;
import com.fansfunding.fan.project.utils.MotionLessViewPager;


/**
 * Created by Eamon on 2016/7/22.
 */
public class CreateProjectActivityBase extends AppCompatActivity {
    //Views
    public MenuItem item;
    public MotionLessViewPager viewPager;
    public ProjectCreateAdapter paperAdapter;
    private int pageState=0;
    public ActionBar actionBar;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_project_create, menu);
        item = menu.findItem(R.id.menu_create_next);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);


        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_createProject);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        actionBar=getSupportActionBar();
        actionBar.setTitle("发起项目");
        actionBar.setDisplayHomeAsUpEnabled(true);
        paperAdapter = new ProjectCreateAdapter(getSupportFragmentManager());

        viewPager=(MotionLessViewPager)findViewById(R.id.vp_project_create);
        viewPager.setAdapter(paperAdapter);
        viewPager.setCurrentItem(pageState);
    }

    public int getPageState(){
        return pageState;
    }

    public void setPageState(int pageState){
        this.pageState=pageState;
        changeFrag();
    }

    private void changeFrag(){
        switch (pageState){
            case -1:
                finish();
                break;
            case 0:
                item.setTitle("下一步");
                item.setIcon(null);
                break;
            case 1:
                item.setTitle("");
                item.setIcon(getResources().getDrawable(R.drawable.send));
                break;
            case 2:
                item.setTitle("");
                item.setIcon(getResources().getDrawable(R.drawable.correct));
                break;
        }
        actionBar.setTitle(paperAdapter.getPageTitle(pageState));
        viewPager.setCurrentItem(pageState);

        View view = getWindow().peekDecorView();
        //关闭软键盘
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        //CreateProjectRewordFragment.getInstance().refreshAdapter();
    }
}
