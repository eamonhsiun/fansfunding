package com.fansfunding.fan;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用来创建他人或自己的动态的界面（自己的界面还可以通过NewsFragment查看）
 */

public class NewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_news);
        setSupportActionBar(toolbar);

        //设置返回键
        ActionBar actionbar=this.getSupportActionBar();
        actionbar.setTitle("动态");
        actionbar.setDisplayHomeAsUpEnabled(true);


        ListView listView=(ListView)findViewById(R.id.lv_news_list);

        //构建simpleadapter
        List<Map<String,Object>> listItems=new ArrayList<Map<String, Object>>();
        for(int i=0;i<2;i++){
            Map<String,Object> tempMap=new HashMap<String, Object>();
            tempMap.put("social_head",R.drawable.pjimagetest);
            tempMap.put("social_name","昵称");
            tempMap.put("social_time","刚刚");
            listItems.add(tempMap);
        }

        SimpleAdapter simpleAdapter=new SimpleAdapter(this,listItems,R.layout.item_social,
                new String[]{"social_head","social_name","social_time"},
                new int[]{R.id.iv_social_head,R.id.tv_social_name,R.id.tv_social_time});


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
