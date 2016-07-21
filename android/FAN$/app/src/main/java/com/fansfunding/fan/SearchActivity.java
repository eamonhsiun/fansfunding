package com.fansfunding.fan;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


/**
 * 搜索界面
 */
public class SearchActivity extends AppCompatActivity {

    SearchPaperAdapter paperAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar);
        //设置返回键
        ActionBar actionBar=this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.arrow);
        actionBar.setTitle(null);


        TabLayout tabLayout=(TabLayout)findViewById(R.id.tab_search);
        ViewPager viewPager=(ViewPager)findViewById(R.id.vp_search);
        paperAdapter=new SearchPaperAdapter(getSupportFragmentManager());
        viewPager.setAdapter(paperAdapter);
        tabLayout.setupWithViewPager(viewPager);








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
