package com.fansfunding.fan;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AT_Category extends AppCompatActivity {

    private String categoryName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Intent intent=getIntent();
        //设置标题
        categoryName=intent.getStringExtra(getResources().getString(R.string.category_actionbar_title));
        //设置返回键
        ActionBar actionbar=this.getSupportActionBar();
        actionbar.setTitle(categoryName);
        actionbar.setDisplayHomeAsUpEnabled(true);


        ListView listView=(ListView)findViewById(R.id.lv_PJ_SingleType);

        //构建simpleadapter
        List<Map<String,Object>> listItems=new ArrayList<Map<String, Object>>();
        for(int i=0;i<5;i++){
            Map<String,Object> tempMap=new HashMap<String, Object>();
            tempMap.put("tv_PJName","项目名称");
            tempMap.put("iv_PJImage",R.drawable.pjimagetest);
            tempMap.put("tv_PJIntro","这是一个简介");
            tempMap.put("tv_Finance",getResources().getString(R.string.finance)+"10000");
            tempMap.put("tv_SupportNum",getResources().getString(R.string.supportNum)+"10000");
            listItems.add(tempMap);
        }
        SimpleAdapter simpleAdapter=new SimpleAdapter(this,listItems,R.layout.item_project,
                new String[]{"tv_PJName","iv_PJImage","tv_PJIntro","tv_Finance","tv_SupportNum"},
                new int[]{R.id.tv_PJ_Name,R.id.iv_PJ_Image,R.id.tv_PJ_Intro,R.id.tv_PJ_Finance,R.id.tv_PJ_SupportNum});


        listView.setAdapter(simpleAdapter);
        listView.setDividerHeight(20);
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
