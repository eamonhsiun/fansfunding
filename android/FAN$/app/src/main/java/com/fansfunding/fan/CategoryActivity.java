package com.fansfunding.fan;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TooManyListenersException;
/*
* 用来显示单个目录下的所有项目，比如美妆下的所有项目
* */
public class CategoryActivity extends AppCompatActivity {

    private String categoryName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_category);
        setSupportActionBar(toolbar);
        Intent intent=getIntent();
        //设置标题
        categoryName=intent.getStringExtra(getResources().getString(R.string.actionbar_title));
        //设置返回键
        ActionBar actionBar=this.getSupportActionBar();
        actionBar.setTitle(categoryName);
        actionBar.setDisplayHomeAsUpEnabled(true);


        ListView listView=(ListView)findViewById(R.id.lv_PJ_SingleType);

        //构建simpleadapter
        List<Map<String,Object>> listItems=new ArrayList<Map<String, Object>>();
        for(int i=0;i<10;i++){
            Map<String,Object> tempMap=new HashMap<String, Object>();
            tempMap.put("tv_PJName","项目名称");
            tempMap.put("iv_PJImage",R.drawable.project_image_small_test);
            tempMap.put("tv_PJIntro","这是一个简介");
            tempMap.put("tv_Finance",getResources().getString(R.string.finance)+"10000");
            tempMap.put("tv_SupportNum",getResources().getString(R.string.supportNum)+"10000");
            listItems.add(tempMap);
        }
        SimpleAdapter simpleAdapter=new SimpleAdapter(this,listItems,R.layout.item_project,
                new String[]{"tv_PJName","iv_PJImage","tv_PJIntro","tv_Finance","tv_SupportNum"},
                new int[]{R.id.tv_PJ_Name,R.id.iv_PJ_Image,R.id.tv_PJ_Intro,R.id.tv_PJ_Finance,R.id.tv_PJ_SupportNum});


        listView.setAdapter(simpleAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setAction(getString(R.string.activity_project_detail));
                startActivity(intent);
            }
        });
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
