package com.fansfunding.fan;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.fansfunding.internal.AllProjectInCategory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectSupportActivity extends AppCompatActivity {

    //通过查找分类下所有项目所获取的数据
    private AllProjectInCategory.ProjectDetail detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_support);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_PJ_support);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("支持");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.arrow);
        toolbar.setTitleTextColor(Color.WHITE);
        Intent intent=getIntent();
        detail= (AllProjectInCategory.ProjectDetail) intent.getSerializableExtra("detail");

        ListView lv_PJ_support_reward=(ListView)findViewById(R.id.lv_PJ_support_reward);
        //构建simpleadapter
        List<Map<String,Object>> listItems=new ArrayList<Map<String, Object>>();
        for(int i=0;i<3;i++){
            Map<String,Object> tempMap=new HashMap<String, Object>();
            listItems.add(tempMap);
        }
        SimpleAdapter simpleAdapter=new SimpleAdapter(this,listItems,R.layout.item_project_detail_reward_select,
                new String[]{},
                new int[]{});


        View footer=View.inflate(this,R.layout.activity_project_support_footer,null);
        lv_PJ_support_reward.addFooterView(footer);
        lv_PJ_support_reward.setAdapter(simpleAdapter);

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
