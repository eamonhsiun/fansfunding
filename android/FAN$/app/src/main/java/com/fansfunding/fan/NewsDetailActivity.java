package com.fansfunding.fan;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 用来显示动态详情的界面
 */
public class NewsDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_news_detail);
        setSupportActionBar(toolbar);

        //设置返回键
        ActionBar actionBar=this.getSupportActionBar();
        actionBar.setTitle("动态");
        actionBar.setDisplayHomeAsUpEnabled(true);



        ListView listView=(ListView)findViewById(R.id.lv_news_detail_reply_list);
        View listViewHeader=getLayoutInflater().inflate(R.layout.activity_news_detail_header,null);

        //构建simpleadapter
        List<Map<String,Object>> listItems=new ArrayList<Map<String, Object>>();
        for(int i=0;i<2;i++){
            Map<String,Object> tempMap=new HashMap<String, Object>();

            listItems.add(tempMap);
        }
        SimpleAdapter simpleAdapter=new SimpleAdapter(this,listItems,R.layout.item_comment,
                new String[]{},
                new int[]{});

        listView.addHeaderView(listViewHeader);
        listView.setAdapter(simpleAdapter);


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
